package code.comet

import net.liftweb.actor.LiftActor
import net.liftweb.http.ListenerManager
import net.destinylounge.oracle.Oracle
import code.model.{RelType, ButtonState}
import net.destinylounge.serial.OracleProtocol

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 21-7-11
 * Time: 6:29
 * 
 */

object OracleButtonServer extends LiftActor with ListenerManager {
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
    println("Trigger " + state + " " + state.firstTrigger())
    state.firstTrigger() match {
      //class ButtonState(earth: Boolean,fire: Boolean,water: Boolean,air: Boolean,aether: Boolean,beam: Boolean)
      case "earth"  => {OracleServer ! Oracle.trigger(RelType.EARTH)}
      case "fire"   => {OracleServer ! Oracle.trigger(RelType.FIRE)}
      case "water"  => {OracleServer ! Oracle.trigger(RelType.WATER)}
      case "air"    => {OracleServer ! Oracle.trigger(RelType.AIR)}
      case "aether" => {OracleServer ! Oracle.trigger(RelType.AETHER)}
      case "beam"   => {OracleServer ! Oracle.reset(); animate()}
      case _ => println("Unknown trigger!")
    }
  }

  override def lowPriority = {
    case newState: ButtonState => state = newState;
    case trigger: String => state = ButtonState.create(trigger);

    triggerState()
    updateListeners()
  }

}