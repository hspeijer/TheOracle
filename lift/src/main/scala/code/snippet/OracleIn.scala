package code
package snippet

import net.liftweb._
import builtin.snippet.Msg
import http._
import js._
import JsCmds._
import JE._

import util.BindHelpers._
import xml.NodeSeq
import net.destinylounge.oracle.Oracle
import comet.{OracleServer}
import model.RelType

/**
 * A snippet transforms input to output... it transforms
 * templates to dynamic content.  Lift's templates can invoke
 * snippets and the snippets are resolved in many different
 * ways including "by convention".  The snippet package
 * has named snippets and those snippets can be classes
 * that are instantiated when invoked or they can be
 * objects, singletons.  Singletons are useful if there's
 * no explicit state managed in the snippet.
 */
object OracleIn {

  def triggerEarth = {
    SHtml.onSubmit(msg => {
      OracleServer ! Oracle.trigger(RelType.EARTH)
    })
  }

  def triggerFire = {
    SHtml.onSubmit(msg => {
      OracleServer ! Oracle.trigger(RelType.FIRE)
    })
  }

  def triggerWater = {
    SHtml.onSubmit(msg => {
      OracleServer ! Oracle.trigger(RelType.WATER)
    })
  }

  def triggerAir = {
    SHtml.onSubmit(msg => {
      OracleServer ! Oracle.trigger(RelType.AIR)
    })
  }

  def triggerAether = {
    SHtml.onSubmit(msg => {
      OracleServer ! Oracle.trigger(RelType.AETHER)
    })
  }

  def triggerBeam = {
    SHtml.onSubmit(msg => {
      OracleServer ! Oracle.reset()
    })
  }

  /**
   * The render method in this case returns a function
   * that transforms NodeSeq => NodeSeq.  In this case,
   * the function transforms a form input element by attaching
   * behavior to the input.  The behavior is to send a message
   * to the ChatServer and then returns JavaScript which
   * clears the input.
   */
  def render = SHtml.onSubmit(s => {
    println("Onsubmit " + s)
    OracleServer ! s
    SetValById("chat_in", "")
  })
}
