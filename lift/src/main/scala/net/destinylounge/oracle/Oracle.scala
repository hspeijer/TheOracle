package net.destinylounge.oracle

import code.model.{RelType, OracleNode, OracleModel}
import code.comet.WebMovieServer

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
  var currentNode : OracleNode = OracleNode.findNode(0)
  var currentOracleIndex : Int = -1
  val randomNumberGen = new scala.util.Random

  def setCurrentNode(node : OracleNode) = {
    WebMovieServer ! node.clip.name
    currentNode = node
  }

  def trigger(refType: RelType.RelType) : String = {
      val newOracleNode = currentNode.findReference(refType)
      if(newOracleNode != null) {
         setCurrentNode(newOracleNode)
      } else {
        return "No Valid choice"
      }

    currentNode.script
  }

  def triggerBeam(): String = {
    reset();
  }

  def reset() : String = {
    // Count the number of Oracles connected to the root node
    val numOracles = OracleNode.findNode(0).countReferences(RelType.ORACLE)
    var newOracleIndex = -1

    // If there is more than one Oracle then make sure you get one other than the current one
    do {
      // Get a random number from 0 to numOracles-1
      oracleIndex = randomNumberGen.nextInt(numOracles)
    } while (numOracles > 1 && newOracleIndex == currentOracleIndex)

    setCurrentNode(OracleNode.findNode(0).findReference(newOracleIndex, RelType.ORACLE).findReference(RelType.CHALLENGE))
    currentOracleIndex = newOracleIndex

    println("References: " + currentNode.references)
    currentNode.script
  }
}