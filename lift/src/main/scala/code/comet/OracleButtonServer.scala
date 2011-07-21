package code.comet

import net.liftweb.actor.LiftActor
import net.liftweb.http.ListenerManager
import code.model.ButtonState

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 21-7-11
 * Time: 6:29
 * 
 */

object OracleButtonServer extends LiftActor with ListenerManager {
  var state : ButtonState = new ButtonState(0)

  protected def createUpdate = state

  override def lowPriority = {
    case newState: ButtonState => state = newState;

    updateListeners()
  }

}