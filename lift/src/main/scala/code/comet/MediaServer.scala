package code.comet

import code.lib.ConfigurationManager
import net.liftweb.actor.LiftActor
import net.destinylounge.media.DVMVideoPlayer
import net.liftweb.http._
import net.liftweb.common.{Box, Full, Logger}
import code.model.{MediaFile, MediaContainer}

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


object MediaServer extends LiftActor with ListenerManager {

  val mediaContainer = new MediaContainer(ConfigurationManager.getSetting("media.path").toString)

//  def getMedialList(tagName: String) : MediaContainer = {
//  }

  def registerPlayer(player : MediaPlayer) = {}

  def play(file : MediaFile) = {}

  def stop(file: MediaFile) = {}

  def stopAll() = {}

  protected def createUpdate = null
}
