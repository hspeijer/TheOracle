package code.comet

import code.lib.ConfigurationManager
import java.util.HashMap
import java.io.File
import javax.print.attribute.standard.Media
import net.liftweb.actor.LiftActor
import net.liftweb.http.ListenerManager
import sun.org.mozilla.javascript.internal.debug.DebugFrame
import net.liftweb.common.Logger
import net.destinylounge.media.DVMVideoPlayer


/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 14-8-11
 * Time: 11:53
 * 
 */

case class MediaContainer(val path : String) extends MediaFile(path) with Logger {
  var files = new HashMap[String, MediaFile]()
  def addFile(file : MediaFile) {
    files.put(file.name, file)
  }

  def scanFolder() {
    var folder = new File(path)
    folder.listFiles().foreach(f  => addFile(new MediaFile(f.getName())))
    debug("Files:" + files)
  }

  def metaData : String = {
    var tmp = new File(path + ".meta")

    tmp.getAbsolutePath
  }
}

trait FileReference{
//  var file : File
}

case class MediaFile(name: String) extends FileReference {
  val tags : List[String] = List("*")
  var duration : Long = 0

  def MediaFile(name : String, duration : Long) = {
    this.duration = duration
    var file = new File(name)
  }

  override def toString() = name + ":" + duration
//  var file = _
}

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


object MediaServer extends LiftActor with ListenerManager{
  val mediaContainer = new MediaContainer(ConfigurationManager.getSetting("media.path").toString)

//  def getMedialList(tagName: String) : MediaContainer = {
//  }

  def registerPlayer(player : MediaPlayer) = {}

  def play(file : MediaFile) = {}

  def stop(file: MediaFile) = {}

  def stopAll() = {}

  protected def createUpdate = null
}
