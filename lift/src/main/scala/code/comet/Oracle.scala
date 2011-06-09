package code
package comet

import net.liftweb._
import http._
import actor._
import collection.immutable.HashMap
import comet.NeoInit

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 4-6-11
 * Time: 2:35
 * 
 */

object Oracle {
  var state = 0
  val msgs = Array("Earth", "Water", "Fire", "Air", "Aether")
  val menu = "Choices are: 1: Earth, 2:Water, 3:Fire, 4:Air & 5 Aether";

  def updateState(state: Int) : String = {
    //NeoInit.addStateEvent()
    this.state = state
    msgs(state - 1)
  };
}