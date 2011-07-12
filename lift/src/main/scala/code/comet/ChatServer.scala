package code
package comet

import net.liftweb._
import http._
import actor._
import scala.Int
import collection.immutable.Vector._
import comet.NeoInit
import net.destinylounge.UDPClient
import net.destinylounge.media.DVMVideoPlayer

/**
 * A singleton that provides chat features to all clients.
 * It's an Actor so it's thread-safe because only one
 * message will be processed at once.
 */
object ChatServer extends LiftActor with ListenerManager {
  private var msgs = Vector("Welcome to the Oracle") // private state

  println("Works yeah?")
  msgs :+= Oracle.reset()
  /**
   * When we update the listeners, what message do we send?
   * We send the msgs, which is an immutable data structure,
   * so it can be shared with lots of threads without any
   * danger or locking.
   */
  def createUpdate = msgs

  def mediaList() : String = {
      """
      1) flower    <br>
      2) spirit    <br>
      """
  }
  /**
   * process messages that are sent to the Actor.  In
   * this case, we're looking for Strings that are sent
   * to the ChatServer.  We append them to our Vector of
   * messages, and then update all the listeners.
   */
  override def lowPriority = {
    case s: String => {
      try {
        val i : Int = s.toInt

        msgs :+= Oracle.updateState(i)
        //msgs :+= NeoInit.addStateEvent()
      } catch {

        case e: Exception => {
          s match {
            case "menu"   => msgs :+= Oracle.menu
            case "state"  => msgs :+= Oracle.currentNode.script
            case "reset"  => msgs :+= Oracle.reset()
            case "help"   => msgs :+= "Try 'menu' instead"
            case _        => msgs :+= "Unknown command :" + s
          }
        }
      }

    updateListeners()}
  }

}
