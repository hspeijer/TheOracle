package code.model

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 21-7-11
 * Time: 7:28
 *
 */

//Todo Hsp 26/07/2011: There has to be a better way to do this kind of class. Maybe an enumeration with bit twidling capabilities
object ButtonState {
  val EARTH_MASK  = 0x01;
  val FIRE_MASK   = 0x02;
  val WATER_MASK  = 0x04;
  val AIR_MASK    = 0x08;
  val AETHER_MASK = 0x10;
  val BEAM_MASK   = 0x20;

  def create(trigger: String) : ButtonState = {
    trigger match {
      //class ButtonState(earth: Boolean,fire: Boolean,water: Boolean,air: Boolean,aether: Boolean,beam: Boolean)
      case "earth"  => {new ButtonState(true, false, false, false, false, false)}
      case "fire"   => {new ButtonState(false, true, false, false, false, false)}
      case "water"  => {new ButtonState(false, false, true, false, false, false)}
      case "air"    => {new ButtonState(false, false, false, true, false, false)}
      case "aether" => {new ButtonState(false, false, false, false, true, false)}
      case "beam"   => {new ButtonState(false, false, false, false, false, true)}
    }
  }
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

  def firstTrigger() : String = {
    if (earth)  return "earth"
    if (fire)   return "fire"
    if (water)  return "water"
    if (air)    return "air"
    if (aether) return "aether"
    if (beam)   return "beam"

    return ""
  }

  override def toString() = {
    "[ButtonState earth:" + earth + " fire:" + fire + " water:" +
      water + " air:" + air + " aether:" + aether + " beam:" + beam + "]"
  }
}
