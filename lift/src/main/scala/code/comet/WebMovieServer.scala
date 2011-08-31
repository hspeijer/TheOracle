package code.comet

import net.liftweb.actor.LiftActor
import java.io.IOException
import net.liftweb.http.ListenerManager
import net.destinylounge.oracle.Oracle
import code.lib.ConfigurationManager
import net.destinylounge.media.DVMVideoPlayer
import net.liftweb.common.Logger

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 13-7-11
 * Time: 10:07
 * 
 */

object WebMovieServer extends LiftActor with ListenerManager with Logger {
  var movieURL : String = "";
  val prefix = "./api/media/get?file="
  val mediaType = ""
  val player : DVMVideoPlayer = new DVMVideoPlayer()

  protected def createUpdate = movieURL

  def play(s: String) = {
    debug("Playing?" + s)
    //player.play(s+ConfigurationManager.getSetting("media.dvm.suffix").toString, false)
    ""
  }

  override def lowPriority = {
    case s: String => {movieURL = prefix + s}

    updateListeners()
  }
}