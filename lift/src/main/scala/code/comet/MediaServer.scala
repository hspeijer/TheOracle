package code.comet

import code.lib.ConfigurationManager
import net.liftweb.actor.LiftActor
import net.destinylounge.media.DVMVideoPlayer
import net.liftweb.http._
import net.liftweb.common.{Box, Full, Logger}
import code.model.{MediaFile, MediaContainer}
import io.Source
import net.liftweb.json._
import net.liftweb.common.Logger._
import collection.SortedMap

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 14-8-11
 * Time: 11:53
 * 
 */

class MediaPlayer
class AudioPlayer  extends MediaPlayer
class CLIMP3Player extends AudioPlayer
class VideoPlayer  extends MediaPlayer

class DVMPLayer    extends VideoPlayer {
  val player : DVMVideoPlayer = new DVMVideoPlayer()

  def play(file : MediaFile) = {
     player.play(file.name+ConfigurationManager.getSetting("media.dvm.suffix").toString, false)
  }

  def stop(file: MediaFile) = {
     player.stop
  }

  def stopAll() = {
     player.stop
  }
}

class WebPlayer    extends VideoPlayer {

  def play(file : MediaFile) = {
    var movieURL : String = "";
    val prefix = ConfigurationManager.getSetting("media.path").toString
    val mediaType = ConfigurationManager.getSetting("media.web.suffix").toString

    movieURL = prefix + file.name + mediaType;

    // ?.play(movieURL)
  }

  def stop(file: MediaFile) = {
  }

  def stopAll() = {
  }

}

object MediaServer extends LiftActor with ListenerManager with Logger {

  var mediaContainer = new MediaContainer(ConfigurationManager.getSetting("media.path").toString)

  readMetaData()

  def readMetaData() = {
    implicit val formats = DefaultFormats // Brings in default date formats etc.

    var json = Source.fromFile(ConfigurationManager.getSetting("media.path").toString + "/metadata.json").mkString
    val parsed = parse(json)
    val extracted = parsed \\ "files"

    extracted.children.toList.foreach(value => {
      val tags = (value \ "tags").extract[List[String]]
      val name = (value \ "name").extract[String]
      val duration = (value \ "duration").extract[Double]
      val file = mediaContainer.files(name);
      file.duration = (duration * 1000).toInt
      file.tags = tags
    })

    //println(MediaServer.getMediaList(List("Fire")))
  }


  def getMedialList(tags: List[String]) : SortedMap[String, MediaFile] = {
    mediaContainer.files.filter(file => tags.forall(s => file._2.tags.exists(tag => tag == s)))
  }

  def registerPlayer(player : MediaPlayer) = {}

  val player : DVMVideoPlayer = new DVMVideoPlayer()

  def play(file : MediaFile) = {
    val fileName = file.name.substring(0, file.name.indexOf("."))

    MessageServer ! "Playing " + file.name
    WebMovieServer ! fileName
    player.play(fileName + ".mpg", false)
  }

  def stop(file: MediaFile) = {}

  def stopAll() = {}

  protected def createUpdate = null
}
