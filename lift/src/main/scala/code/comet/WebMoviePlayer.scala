package code.comet

import net.liftweb.http.{CometListener, CometActor}
import net.liftweb.http.js.JsCmds.Alert
import net.liftweb.http.js.JE.{JsRaw, Call}
import net.liftweb.common.Logger


/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 13-7-11
 * Time: 10:04
 * 
 */

class WebMoviePlayer extends CometActor with CometListener with Logger{
  private var url = ""
  protected def registerWith = WebMovieServer

  override def lowPriority = {
    case s: String => url = s; reRender()
  }
  def render = {
    debug("WebMoviePlayer " + url)
    JsRaw("var player = $('#video_playback')[0]; player.src = '" + url + "'; player.play()").cmd
  }
}