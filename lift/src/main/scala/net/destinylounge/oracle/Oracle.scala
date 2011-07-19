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
    setCurrentNode(OracleNode.findNode(0).findReference(RelType.ORACLE).findReference(RelType.CHALLENGE))
    println("References: " + currentNode.references)
    currentNode.script
  }
}