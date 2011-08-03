package code.model

import java.lang.{StringBuilder}

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 21-7-11
 * Time: 7:28
 *
 */
abstract class Trigger(_mask : Int, _id : String) {
  val mask = _mask
  val id = _id
}
case object Earth  extends Trigger(0x01, "earth")
case object Fire   extends Trigger(0x02, "fire")
case object Water  extends Trigger(0x04, "water")
case object Air    extends Trigger(0x08, "air")
case object Aether extends Trigger(0x10, "aether")
case object Beam   extends Trigger(0x20, "beam")

case object Challenge extends Trigger(0x20, "challenge")
case object OracleSelect extends Trigger(0x20, "oracle")

case class ButtonState() {
  var triggers : List[Trigger] = List()

  def this(number : Int) {
    this()
    if ((number & Earth.mask)  > 0) triggers ::= Earth
    if ((number & Fire.mask)   > 0) triggers ::= Fire
    if ((number & Water.mask)  > 0) triggers ::= Water
    if ((number & Air.mask)    > 0) triggers ::= Air
    if ((number & Aether.mask) > 0) triggers ::= Aether
    if ((number & Beam.mask)   > 0) triggers ::= Beam
  }

  def this(list : List[Trigger]) {
    this();
    triggers = list ::: triggers
  }

  def this(id : String) {
    this(List[Trigger](id match {
      case Earth.id => Earth
      case Fire.id => Fire
      case Water.id => Water
      case Air.id => Air
      case Aether.id => Aether
      case Beam.id => Beam
    }
    ))
  }

  def toByte() : Byte = {
    var result = 0
    triggers.foreach(trigger => result = result | trigger.mask)

    result.toByte;
  }

  def firstTrigger(): Trigger = {
    triggers.head
  }

  override def toString = {
    val builder : StringBuilder = new StringBuilder("[ButtonState ")
    if(triggers != null) triggers.foreach(trigger => builder.append(trigger.id).append(":true "))
    builder.append("]").toString
  }

  def toJS() : String = {
    val builder : StringBuilder = new StringBuilder("{")
    triggers.foreach(trigger => {
      builder.append(trigger.id).append(":true")
      if (trigger != triggers.last) builder.append(", ")
    })
    builder.append("}").toString
  }
}

object ButtonTest {
  def main(args : Array[String]) {
    val triggers = List(Earth, Water)
    println(new ButtonState(triggers))
    println(new ButtonState(triggers).toJS)
    println(new ButtonState("fire"))
    println(new ButtonState(0x3f).toJS + " " + new ButtonState(0x3f).toByte())
  }
}

