package net.destinylounge.oracle

import scala.actors.Actor._
import code.model._
import code.comet.WebMovieServer
import actors.{TIMEOUT, Actor}
import net.liftweb.common.Logger

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 4-6-11
 * Time: 2:35
 * 
 */

object Oracle extends Actor with Logger {

  val model = OracleModel
  var currentNode : OracleNode = OracleNode.findNode(0)
  var currentOracleIndex : Int = -1
  val randomNumberGen = new scala.util.Random
  var blockInputUntil = -1 // <0 is Not blocking; >0 is Blocking until a unix time to allow input again

  def setCurrentNode(node : OracleNode) = {
//    blockInputUntil = System.currentTimeMillis() + (node.clip.duration  * 1000)
    WebMovieServer ! node.clip.name
    currentNode = node
  }

  val state: ButtonState = new ButtonState()

  def trigger(trigger : Trigger) : String = {
    var allowInput : Boolean = true
//    if (blockInputUntil > 0) {
//      if (blockInputUntil < System.currentTimeMillis())
//        blockInputUntil = -1 // Stop blocking
//      else
//        allowInput = false  // block input, since we hav not reached blockInputUntil
//    }

    if (allowInput) {
      val newOracleNode = currentNode.findReference(trigger)
      if(newOracleNode != null) {
         setCurrentNode(newOracleNode)
      } else {
        return "No Valid choice"
      }
    }
    else
      info("Input ignored")

    currentNode.script
  }

  def triggerBeam(): String = {
    reset();
  }

  def reset() : String = {
    // Count the number of Oracles connected to the root node
    val numOracles = OracleNode.findNode(0).countReferences(OracleSelect)
    var newOracleIndex = -1

    // If there is more than one Oracle then make sure you get one other than the current one
    do {
      // Get a random number from 0 to numOracles-1
      newOracleIndex = randomNumberGen.nextInt(numOracles)
    } while (numOracles > 1 && newOracleIndex == currentOracleIndex)

    setCurrentNode(OracleNode.findNode(0).findReference(newOracleIndex, OracleSelect).findReference(Challenge))
    currentOracleIndex = newOracleIndex

    debug("References: " + currentNode.references)
    currentNode.script
  }


  // State Machine ...
  def act() {
    println("act")
    for (i <- 1 to 10) {
        println("### Number: "+i)
        Thread.sleep(1000)
    }
/*
    blockInput = false
    setLighting(sideWalls, deepBlue)
    setAllStones(glow)
    loop
         if (not oracle.currentNode.clip.isPlaying())
             setCurrentNode(random oracle)
    until beam
         setState(Challenge)
*/
    println("About to loop")
    loop {
      reactWithin(3000) {

        case TIMEOUT =>
          println("TIMEOUT reached")
          exit()
      }
    }
  }
}