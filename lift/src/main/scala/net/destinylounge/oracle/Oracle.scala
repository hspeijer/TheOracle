package net.destinylounge.oracle

import scala.actors.Actor._
import code.model._
import code.comet.WebMovieServer
import net.liftweb.common.Logger
import actors.{TIMEOUT, Actor}
import code.lib.ConfigurationManager

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
  val randomAnswerGen = new scala.util.Random

  def setCurrentNode(node : OracleNode) = {
    WebMovieServer ! node.clip.name
    currentNode = node
  }

  def initialize() {
    ConfigurationManager.loadSettings("oracle")
  }

  val state: ButtonState = new ButtonState()

  def trigger(trigger : Trigger) : String = {

//    val newOracleNode = currentNode.findReference(trigger)

    // Count the number of nodes of the given type that are connected to the current node
    val numNodes = currentNode.countReferences(trigger)
    // Get a random number from 0 to numNodes-1
    val newNodeIndex = randomAnswerGen.nextInt(numNodes)
    val newOracleNode = currentNode.findReference(newNodeIndex, trigger)
    debug("trigger - newNodeIndex: " + newNodeIndex + " numNodes: " + numNodes)

    if(newOracleNode != null) {
       setCurrentNode(newOracleNode)
    } else {
      return "No Valid choice"
    }

    currentNode.script
  }

  def reset() : String = {

    this ! IdleState

    currentNode.script
  }

  // State Machine ...
  var currentState : State = IdleState;
  def act() {
    loop {
      receive {
        // ToDo MEH: Handle all cases in the same way?
        case IdleState =>
          debug("state message - IdleState")
          currentState = IdleState
          if (IdleState.getState == State.New)
            IdleState.start()
          else
            IdleState.restart()  // Probably State.Terminated

        case ChallengeState =>
          debug("state message - ChallengeState")
          currentState = ChallengeState
          if (ChallengeState.getState == State.New)
            ChallengeState.start()
          else
            ChallengeState.restart()  // Probably State.Terminated

        case QuestionState =>
          debug("state message - QuestionState")
          currentState = QuestionState
          if (QuestionState.getState == State.New)
            QuestionState.start()
          else
            QuestionState.restart()  // Probably State.Terminated

        case AnswerState =>
          debug("state message - AnswerState")
          currentState = AnswerState
          if (AnswerState.getState == State.New)
            AnswerState.start()
          else
            AnswerState.restart()  // Probably State.Terminated

        case BeGoneState =>
          debug("state message - BeGoneState")
          currentState = BeGoneState
          if (BeGoneState.getState == State.New)
            BeGoneState.start()
          else
            BeGoneState.restart()  // Probably State.Terminated

        // Buttons
        case Earth =>
          debug("state message - Earth received")
          currentState ! Earth

        case Fire =>
          debug("state message - Fire received")
          currentState ! Fire

        case Water =>
          debug("state message - Water received")
          currentState ! Water

        case Air =>
          debug("state message - Air received")
          currentState ! Air

        case Aether =>
          debug("state message - Aether received")
          currentState ! Earth

        case Beam =>
          debug("state message - Beam received")
          currentState ! Beam
      }
    }
  }

  abstract class State(_id : String) extends Actor {
    val id = _id
    var blockInput = false
  }

  case object IdleState  extends State("idle") {
    val randomNumberGen = new scala.util.Random

    def playRandomOracle() {
      // Count the number of Oracles connected to the root node
      val numOracles = OracleNode.findNode(0).countReferences(OracleSelect)
      var newOracleIndex = -1

      // If there is more than one Oracle then make sure you get one other than the current one
      do {
        // Get a random number from 0 to numOracles-1
        newOracleIndex = randomNumberGen.nextInt(numOracles)
      } while (numOracles > 1 && newOracleIndex == currentOracleIndex)

      setCurrentNode(OracleNode.findNode(0).findReference(newOracleIndex, OracleSelect))
      currentOracleIndex = newOracleIndex

      debug("playRandomOracle - Oracle Index: " + currentOracleIndex + ", References: " + currentNode.references)
    }

    def act() {
      debug("IdleState.act")

      blockInput = false
//    setLighting(sideWalls, deepBlue)
//    setAllStones(glow)

      loop {
        playRandomOracle()
//        reactWithin(5000) {
        reactWithin(currentNode.clip.duration*1000) {

          case Beam =>
            Oracle ! ChallengeState
            exit()

          case TIMEOUT =>
            debug("IdleState.act TIMEOUT reached")
        }
      }
    }

  }

  case object ChallengeState  extends State("challenge")
  {
    def act() {
      debug("ChallengeState.act")

      blockInput = true
//      setLighting(sideWalls, colorCycle)
//      setAllStones(dim)

      // ToDo MEH - Pose second challenge after the first run though
      setCurrentNode(OracleNode.findNode(0).findReference(currentOracleIndex, OracleSelect).findReference(Challenge))
//      reactWithin(5000) {
      reactWithin(currentNode.clip.duration*1000) {

        case TIMEOUT =>
          debug("ChallengeState.act TIMEOUT reached")
          Oracle ! QuestionState
      }
    }

  }

  case object QuestionState  extends State("question")
  {
    def act() {
      debug("QuestionState.act")

      blockInput = false

      // ToDo MEH: Get timeout from config?
      reactWithin(10000) {

        // ToDo MEH: Is there anything specific to do or can all these cases be handled the same way?

        case Earth =>
          debug("QuestionState.act Earth received")
          Oracle ! AnswerState
          exit()

        case Fire =>
          debug("QuestionState.act Fire received")
          Oracle ! AnswerState
          exit()

        case Water =>
          debug("QuestionState.act Water received")
          Oracle ! AnswerState
          exit()

        case Air =>
          debug("QuestionState.act Air received")
          Oracle ! AnswerState
          exit()

        case Aether =>
          debug("QuestionState.act Aether received")
          Oracle ! AnswerState
          exit()

        case TIMEOUT =>
          debug("QuestionState.act TIMEOUT reached")
          Oracle ! BeGoneState
      }
    }

  }

  case object AnswerState  extends State("answer")
  {
    def act() {
      debug("AnswerState.act")

      blockInput = true
//      flashStone(stoneMsg)
//      setLighting(sideWalls, stoneMsg.color)
//      setCurrentNode(oracle.currentNode[stoneMsg]

//      reactWithin(5000) {
      reactWithin(currentNode.clip.duration*1000) {

        case TIMEOUT =>
          debug("AnswerState.act TIMEOUT reached")
          Oracle ! ChallengeState
      }

    }
  }

  case object BeGoneState  extends State("beGone")
  {
    def act() {
      debug("BeGoneState.act")

      blockInput = true
//      setCurrentNode(oracle.findNode[beGone]

      reactWithin(5000) {

        case TIMEOUT =>
          debug("BeGoneState.act TIMEOUT reached")
          Oracle ! IdleState
      }
    }

  }
}