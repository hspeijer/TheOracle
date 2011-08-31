package code.rest

import actors.Actor
import net.liftweb.common.Full._
import net.liftweb.common.{Full, Box, Logger}
import net.liftweb.http.JsonResponse._
import net.liftweb.json.Extraction._
import code.rest.OracleRest.JQGridResonse._
import code.model.OracleModel
import net.liftweb.http._
import net.liftweb.common.Logger._

/**
 * Created by IntelliJ IDEA.
 * User: hspeijer
 * Date: 8/24/11
 * Time: 1:41 AM
 * To change this template use File | Settings | File Templates.
 */

object LightServer extends Logger {
  val framesPerSecond = 4;
  var frame = new DMXBuffer(1)

  def dispatch: LiftRules.DispatchPF = {
    // Req(url_pattern_list, suffix, request_type)
    case request@Req("api" :: "light" :: "set" :: Nil, _, GetRequest) => () => Box(setChannelValue(request.param("channel"), request.param("value")))
    case request@Req("api" :: "light" :: "getRGB" :: Nil, _, GetRequest) => () => Box(getRGB(request.param("startIndex")))
    case Req("api" :: "light" :: "get" :: Nil, _, GetRequest) => () => Full(getBuffer())
    case request@Req("api" :: "light" :: "gotoFrame" :: Nil, _, GetRequest) => () => Box(gotoFrame(request.param("frameId")))
  }

  implicit val formats = net.liftweb.json.DefaultFormats

  def setChannelValue(channel : Box[String],value : Box[String])= JsonResponse(decompose(frame))
  def getRGB(startIndex : Box[String]) = PlainTextResponse(RGB.getHex(frame, Integer.parseInt(startIndex.openOr("0"))))
  def getBuffer() = PlainTextResponse(frame.toString())
 
  def start() {}
  def stop() {}
  def gotoFrame(frameId : Box[String]) = {LightActor.currentFrame = Integer.parseInt(frameId.openOr("-1")); PlainTextResponse("OK") }

  LightActor.start()
}

object LightActor extends Actor with Logger {
  var generators : List[DMXGenerator] = List[DMXGenerator]()
  var currentFrame : Int = 0;
  var startTime : Long = System.currentTimeMillis();

  generators ::= SineGenerator(1, 128.toShort, 127.toShort, 50).asInstanceOf[DMXGenerator]
  generators ::= SineGenerator(2, 128.toByte, 127.toByte, 40).asInstanceOf[DMXGenerator]
  generators ::= SineGenerator(3, 128.toByte, 127.toByte, 30).asInstanceOf[DMXGenerator]
  generators ::= RampGenerator(0, 0, 255.toShort , 0, 60).asInstanceOf[DMXGenerator]

  def act() {
    debug("Starting light rendering")

    while(true) {
      //make each generator do it's work
      generators.foreach(generator => LightServer.frame.buffer(generator.getChannel) = generator.getFrame(currentFrame))

      //println("Current frame :" + currentFrame);
      Thread.sleep(1000/LightServer.framesPerSecond)

      currentFrame = ((System.currentTimeMillis() - startTime) / (1000/LightServer.framesPerSecond)).asInstanceOf[Int]
    }
  }
}

case class DMXBuffer(universeId : Int) {
  var buffer = new Array[Short](512)

  override def toString() = {
    val builder : StringBuilder = new StringBuilder()

    builder.append("[");

    for(i <- 0 until 512) {
      builder.append(buffer(i))
      if(i < 511) builder.append(",")
    }

    builder.append("]");

    builder.toString
  }
}

trait DMXGenerator {
    def getChannel : Int = 0
  	def getFrame(frameId : Int) : Short = 0

}

object RGB {
	def getHex(buffer: DMXBuffer, startIndex : Int) = {
		"#ffffff"
	}
}

case class SineGenerator(channel: Int, center : Short, amplitude: Short, period: Int) extends DMXGenerator {
  	override def getFrame(frameId : Int) : Short = {
      val offset =((frameId % period) - (period / 2))
      val radial = offset / (period/2.0)

//      println("Offset: " + offset + " radial:" + radial + " sin:" + (center + Math.sin(radial) * (amplitude/2)))

      Math.abs(center + Math.sin(radial) * (amplitude/2)).toShort
	  }
  override def getChannel : Int = channel
}

case class RampGenerator(channel: Int, start: Short, end: Short, startFrame: Int, endFrame: Int) extends DMXGenerator {
  override def getChannel : Int = channel
  override def getFrame(frameId : Int) : Short = {
    var result : Short = 0

    if(frameId > startFrame || frameId < endFrame) {
      val duration : Int = endFrame - startFrame
      val delta : Double = (end - start) / duration //delta per frame
      var now = frameId - startFrame
//      println("Ramp: " + now + " frame:" + start + (delta * now))

      result = (start + (delta * now)).toShort
    }

    if (frameId < startFrame)  {
      result = start
    }

    if (frameId > endFrame) {
      result = end
    }

    result
	}
}
