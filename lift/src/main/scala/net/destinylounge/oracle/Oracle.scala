package net.destinylounge.oracle

import net.liftweb._
import http._
import actor._
import collection.immutable.HashMap
import code.comet.NeoInit
import code.model.{RelType, OracleNode, OracleModel}

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

  def trigger(refType: RelType.RelType) : String = {
      val newOracleNode = currentNode.findReference(refType)
      if(newOracleNode != null) {
        currentNode = newOracleNode
      } else {
        return "No Valid choice"
      }

    currentNode.script
  }

  def triggerBeam(): String = {
    reset();
  }

  def reset() : String = {
    currentNode = OracleNode.findNode(0).findReference(RelType.ORACLE).findReference(RelType.CHALLENGE)
    println("References: " + currentNode.references)
    currentNode.script
  }
}