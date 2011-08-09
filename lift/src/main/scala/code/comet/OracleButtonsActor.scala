package code.comet

import net.liftweb.http.{CometListener, CometActor}
import net.liftweb.http.js.JE.JsRaw._
import code.model.ButtonState
import net.liftweb.http.js.JE.JsRaw
import net.liftweb.common.Logger

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 21-7-11
 * Time: 5:42
 * 
 */

class OracleButtonsActor extends CometActor with CometListener with Logger {
  private var state: ButtonState= new ButtonState(0x1f) // private state

  protected def registerWith = OracleButtonServer

  override def lowPriority = {
    case buttonState: ButtonState => state = buttonState; reRender()
  }
  def render = {
    debug("ButtonState Change " + state);

    JsRaw("updateButtonState(" + state.toJS() + ")").cmd
  }

}