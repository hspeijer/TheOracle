package code.rest

import net.liftweb.common.Logger
import actors.Actor

/**
 * Created by IntelliJ IDEA.
 * User: hspeijer
 * Date: 8/24/11
 * Time: 1:41 AM
 * To change this template use File | Settings | File Templates.
 */

object LightServer extends Logger {


}

class LightActor extends Actor {

}

case class DMXBuffer(universeId : Int) {
  var buffer : Array[Byte] = Array[Byte](512)
}

case class DMXGenerator(channel: Int) {
  def getFrame(frameId : Int) : Int
}

case class SineGenerator(center : Byte, amplitude: Byte, period: Int)

case class RampGenerator(start: Byte, end: Byte, startFrame: Int, endFrame: Int)
