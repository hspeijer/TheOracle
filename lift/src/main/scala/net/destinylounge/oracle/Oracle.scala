package net.destinylounge.oracle

import scala.actors.Actor._
import code.model._
import net.liftweb.common.Logger
import actors.{TIMEOUT, Actor}
import code.lib.ConfigurationManager
import code.comet._

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 4-6-11
 * Time: 2:35
 * 
 */

object Oracle extends Actor with Logger {

  var currentOracle = ""
  val randomAnswerGen = new scala.util.Random
  var currentTimeout = 1000

//  def setCurrentNode(file : OracleNode) = {
//    WebMovieServer ! node.clip.name
//	debug("Gonna play: " + node.clip.name);
//    currentNode = node
//  }

  def initialize() {
    ConfigurationManager.loadSettings("oracle")
  }

  val state: ButtonState = new ButtonState()

  def chooseAnswer(trigger : Trigger) {

    OracleButtonServer ! LightCommand(new ButtonState(List(trigger)))
    var tags = List(currentOracle, "Answer")
    trigger match {
      case Earth =>
        tags ::= "Earth"
      case Fire =>
        tags ::= "Fire"
      case Air =>
        tags ::= "Air"
      case Water =>
        tags ::= "Water"
      case Aether =>
        tags ::= "Aether"
    }

    val mediaList = MediaServer.getMedialList(tags)
    MessageServer ! "Question state answer : " + trigger
    val newMediaIndex = if(mediaList.size > 0) randomAnswerGen.nextInt(mediaList.size) else {0}
    val file = (mediaList.toList)(newMediaIndex)._2

     //   println("Duration: " + file.duration)
        // select a random one
      Oracle.currentTimeout = file.duration
        MediaServer.play(file)
  }


  def reset() : String = {

    this ! IdleState

    "Reset"
  }

  // State Machine ...
  var currentState : State = IdleState;
  //this ! IdleState
  def act() {
    loop {
      receive {
        // ToDo MEH: Handle all cases in the same way?
        case IdleState =>
          MessageServer ! "Entering Idle State"
          debug("state message - IdleState")
          currentState = IdleState
          if (IdleState.getState == State.New)
            IdleState.start()
          else
            IdleState.restart()  // Probably State.Terminated

        case ChallengeState =>
          MessageServer ! "Entering Challenge State"
          debug("state message - ChallengeState")
          currentState = ChallengeState

          if (ChallengeState.getState == State.New)
            ChallengeState.start()
          else
            ChallengeState.restart()  // Probably State.Terminated

        case QuestionState =>
          debug("state message - QuestionState")
          MessageServer ! "Entering Question State"
          currentState = QuestionState
          if (QuestionState.getState == State.New)
            QuestionState.start()
          else
            QuestionState.restart()  // Probably State.Terminated

        case AnswerState =>
          MessageServer ! "Entering Answer State"
          debug("state message - AnswerState")
          currentState = AnswerState
          if (AnswerState.getState == State.New)
            AnswerState.start()
          else
            AnswerState.restart()  // Probably State.Terminated

        case BeGoneState =>
          MessageServer ! "Entering Begone State"
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
          currentState ! Aether

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

    def act() {
      debug("IdleState.act")

      blockInput = false
//    setLighting(sideWalls, deepBlue)
//    setAllStones(glow)

      loop {
        // if we have a queue, play the next one.
          //query media files for IdleState clips
        val mediaList = MediaServer.getMedialList(List("Intro"))
        val newMediaIndex = if(mediaList.size > 0) randomAnswerGen.nextInt(mediaList.size) else {0}
        val file = (mediaList.toList)(newMediaIndex)._2

//        println("Duration: " + file.duration + " size: " +mediaList.size +" index: " + newMediaIndex)
        // select a random one

        MediaServer.play(file)

//        reactWithin(5000) {
        reactWithin(file.duration + 5000) {

          case Beam =>
            Oracle ! ChallengeState
            exit()

          case TIMEOUT =>
            println("IdleState.act TIMEOUT reached")
        }
      }
    }

  }

  case object ChallengeState  extends State("challenge")
  {
    def act() {
      debug("ChallengeState.act")

      OracleButtonServer ! LightCommand(new ButtonState(31))
      blockInput = true

//      setLighting(sideWalls, colorCycle)
//      setAllStones(dim)
      //select an oracle that will do the challenge
      val mediaList = MediaServer.getMedialList(List("Challenge"))
      val newMediaIndex = if(mediaList.size > 0) randomAnswerGen.nextInt(mediaList.size) else {0}
      val file = (mediaList.toList)(newMediaIndex)._2
      currentOracle = file.tags(0)

      MessageServer ! "Current Oracle selected: " + currentOracle

      MediaServer.play(file)
      Oracle.currentTimeout = file.duration

//      reactWithin(file.duration) {
      reactWithin(5000) {

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
      OracleButtonServer ! LightCommand(new ButtonState(0))

      blockInput = false

      loop{
      // ToDo MEH: Get timeout from config?
      reactWithin(Oracle.currentTimeout) {

        // ToDo MEH: Is there anything specific to do or can all these cases be handled the same way?

        case Earth =>
          debug("QuestionState.act Earth received")
          chooseAnswer(Earth)
          Oracle ! AnswerState
          exit()

        case Fire =>
          debug("QuestionState.act Fire received")
          chooseAnswer(Fire)
          Oracle ! AnswerState
          exit()

        case Water =>
          debug("QuestionState.act Water received")
          chooseAnswer(Water)
          Oracle ! AnswerState
          exit()

        case Air =>
          debug("QuestionState.act Air received")
          chooseAnswer(Air)
          Oracle ! AnswerState
          exit()

        case Aether =>
          debug("QuestionState.act Aether received")
          chooseAnswer(Aether)
          Oracle ! AnswerState
          exit()

        case TIMEOUT =>
          debug("QuestionState.act TIMEOUT reached")
          if((Beam.mask & OracleButtonServer.state.toByte()) == 0) {
            Oracle ! BeGoneState
            exit()
          }
      }
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
          reactWithin(Oracle.currentTimeout) {

          case TIMEOUT =>
            MessageServer ! " Answer finished play"
            debug("AnswerState.act TIMEOUT reached")
            OracleButtonServer ! LightCommand(new ButtonState(0))
            Oracle ! QuestionState
      }
    }
  }

  case object BeGoneState  extends State("beGone")
  {
    def act() {
      debug("BeGoneState.act")
      currentOracle = ""
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