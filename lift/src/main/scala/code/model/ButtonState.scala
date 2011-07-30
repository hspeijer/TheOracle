package code.model

import net.liftweb.http.js._
import com.sun.corba.se.impl.copyobject.JavaStreamObjectCopierImpl

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 21-7-11
 * Time: 7:28
 * 
 */

object ButtonState {
  val EARTH_MASK  = 0x01;
  val FIRE_MASK   = 0x02;
  val WATER_MASK  = 0x04;
  val AIR_MASK    = 0x08;
  val AETHER_MASK = 0x10;
  val BEAM_MASK   = 0x20;
}

class ButtonState(
  earth: Boolean,
  fire: Boolean,
  water: Boolean,
  air: Boolean,
  aether: Boolean,
  beam: Boolean) {

  def this(byte : Byte) {
    this(
      (byte & ButtonState.EARTH_MASK)  > 0,
      (byte & ButtonState.FIRE_MASK)   > 0,
      (byte & ButtonState.WATER_MASK)  > 0,
      (byte & ButtonState.AIR_MASK)    > 0,
      (byte & ButtonState.AETHER_MASK) > 0,
      (byte & ButtonState.BEAM_MASK)   > 0
    )
  }

  def toByte() : Byte = {
    val result : Int = (
      (if(earth)  ButtonState.EARTH_MASK  else 0)
    | (if(fire)   ButtonState.FIRE_MASK   else 0)
    | (if(water)  ButtonState.WATER_MASK  else 0)
    | (if(air)    ButtonState.AIR_MASK    else 0)
    | (if(aether) ButtonState.AETHER_MASK else 0)
    | (if(beam)   ButtonState.BEAM_MASK   else 0)
    )

    result.toByte
  }

  def toJS() = {
    "{earth:" + earth + ", fire:" + fire + ", water:" +
      water + ", air:" + air + ", aether:" + aether + ", beam:" + beam + "}"
  }

  override def toString() = {
    "[ButtonState earth:" + earth + " fire:" + fire + " water:" +
      water + " air:" + air + " aether:" + aether + " beam:" + beam + "]"
  }
}
