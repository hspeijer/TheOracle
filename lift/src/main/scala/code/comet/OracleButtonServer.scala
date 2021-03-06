package code.comet

import net.liftweb.http.ListenerManager
import net.destinylounge.oracle.Oracle
import net.destinylounge.serial.OracleProtocol
import net.liftweb.actor.LiftActor
import code.model._
import net.liftweb.common.Logger

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 21-7-11
 * Time: 6:29
 * 
 */

object OracleButtonServer extends LiftActor with ListenerManager with Logger {
  var state = new ButtonState(0)

  OracleProtocol.sendState(state)

  protected def createUpdate = state

  abstract class Animator() {
    var pulseCount : Int = 0

    def pulse() : Unit = pulseCount += 1
    def finished() : Boolean
    def value() : Byte
  }

  class Flash(pulseOn: Int, pulseOf: Int) extends Animator{
    def value() : Byte = if(pulseCount <= pulseOn) 0x1f else 0x00
    def finished() : Boolean = pulseCount > pulseOn + pulseOf
  }

  class Walker(pulseHold: Int, leftToRight: Boolean) extends Animator {
    var state = if(leftToRight) 16 else 1

    override def pulse() : Unit = {
      if (pulseCount % pulseHold == 0 && pulseCount > 0) {
        if(leftToRight) {
          state = state >> 1;
        } else {
          state = state << 1;
        }
        //println("animator" + state.toBinaryString)
      }
      pulseCount += 1
    }
    def value() : Byte = state.toByte
    def finished() : Boolean = pulseCount >= pulseHold * 5
  }

  def animate() = {
    val animation = List(
      new Flash(12, 6),
      new Flash(12, 6),
      new Walker(4, true),
      new Walker(4, false),
      new Walker(2, true),
      new Walker(2, false),
      new Flash(4, 0)
    );

    animation.foreach(animator => {
      while(!animator.finished()) {
        animator.pulse()
        val oldState = state
        state = new ButtonState(animator.value())
        if (oldState.toByte() != state.toByte()) {
          updateListeners()
        }
        Thread.sleep(100);
      }
    })
  }

  override def updateListeners() = {
    super.updateListeners()
    OracleProtocol.sendState(state)
  }

  def triggerState() = {
    if (state.firstTrigger() != null) {
      debug("Trigger " + state + " " + state.firstTrigger())
      state.firstTrigger() match {
        //class ButtonState(earth: Boolean,fire: Boolean,water: Boolean,air: Boolean,aether: Boolean,beam: Boolean)
        case Beam   => {Oracle ! Beam}
        case _ =>
          Oracle ! state.firstTrigger()
      }
    }
  }

  override def lowPriority = {
    case trigger: String => debug("String: trigger")
    case trigger: Trigger => {state = new ButtonState(List(trigger)); debug("Trigger: " + trigger.toString); triggerState()}
    case newState: ButtonState => {state = newState; triggerState()}
    case command : LightCommand => { state = command.state }

    updateListeners()
  }

}

case class LightCommand(state : ButtonState)
