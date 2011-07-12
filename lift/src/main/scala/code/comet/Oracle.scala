package code
package comet

import net.liftweb._
import http._
import actor._
import collection.immutable.HashMap
import comet.NeoInit

import model.{RelType, OracleNode, OracleModel}

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 4-6-11
 * Time: 2:35
 * 
 */

object Oracle {
  val model = OracleModel
  val menu = "Choices are: 1: Earth, 2:Water, 3:Fire, 4:Air & 5 Aether"
  val menuLookup = List(RelType.EARTH, RelType.WATER, RelType.FIRE, RelType.AIR, RelType.AETHER)
  var currentNode : OracleNode = OracleNode.findNode(0)

  println("Init Oracle?")

  def updateState(state: Int) : String = {
      val newOracleNode = currentNode.findReference(menuLookup(state - 1))
      if(newOracleNode != null) {
        currentNode = newOracleNode
      } else {
        return "No Valid choice"
      }

    currentNode.script
  }

  def reset() : String = {
    currentNode = OracleNode.findNode(0).findReference(RelType.ORACLE).findReference(RelType.CHALLENGE)
    println("References: " + currentNode.references)
    currentNode.script
  }
}