package code.comet

import net.liftweb.actor.LiftActor
import net.liftweb.http.ListenerManager
import net.destinylounge.oracle.Oracle
import code.lib.ConfigurationManager
import net.destinylounge.media.DVMVideoPlayer

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 13-7-11
 * Time: 10:07
 * 
 */

object WebMovieServer extends LiftActor with ListenerManager {
  var movieURL : String = "";
  val prefix = "./media/"
  val mediaType = ".mp4"
  val player : DVMVideoPlayer = new DVMVideoPlayer()

  protected def createUpdate = movieURL

  override def lowPriority = {
    case s: String => {

      player.play(s+ConfigurationManager.getSetting("media.dvm.suffix").toString, false)

      movieURL = prefix + s + mediaType
    };

    updateListeners()
  }
}