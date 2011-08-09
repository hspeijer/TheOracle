package code.snippet

import xml.NodeSeq
import _root_.net.liftweb._
import http._
import common._
import net.liftweb.util.Helpers._
import net.destinylounge.media.DVMVideoPlayer
import net.liftweb.common.Logger

/**
 * Created by IntelliJ IDEA.
 * User: markhalldev
 * Date: 7/9/11
 * Time: 7:05 PM
 * To change this template use File | Settings | File Templates.
 */

object DVMTest extends Logger {

  val player : DVMVideoPlayer = new DVMVideoPlayer()

  def stop (xhtml : NodeSeq) : NodeSeq = {
    def processStop() = {
      debug("Stop?")
      player.stop
    }

    bind("dvm", xhtml,
       "stop" -> SHtml.submit("Stop", processStop)
    )
  }

   def play (xhtml : NodeSeq) : NodeSeq = {
      var filename = ""
      var loop = false

      debug("Play");

      def processPlay() = {
        debug("Play? " + filename + " loop? " + loop)
        player.play(filename, loop)
      }

     def setLoop() = {
       debug("Loop: " + loop)
       loop = true
     }
      bind("dvm", xhtml,
         "filename" -> SHtml.text(filename, filename = _),
         "loop" -> SHtml.checkbox_id(loop, if (_) setLoop(), Full("snazzy")),
         "play" -> SHtml.submit("Play", processPlay)
      )
   }
}