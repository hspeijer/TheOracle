package code.snippet

import net.liftweb.http.SHtml
import code.comet.MessageServer
import net.liftweb.http.js.JsCmds.SetValById._
import net.liftweb.http.js.JsExp._
import net.liftweb.http.js.JE._
import xml.NodeSeq


/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 17-7-11
 * Time: 23:15
 * 
 */

object OracleData {
  import net.liftweb.json.JsonAST
  import net.liftweb.json.JsonDSL._

  case class Winner(id: Long, numbers: List[Int])
  case class Lotto(id: Long, winningNumbers: List[Int], winners: List[Winner], drawDate: Option[java.util.Date])

  val winners = List(Winner(23, List(2, 45, 34, 23, 3, 5)), Winner(54, List(52, 3, 12, 11, 18, 22)))
  val lotto = Lotto(5, List(2, 45, 34, 23, 7, 5, 3), winners, None)

  val json =
    ("lotto" ->
      ("lotto-id" -> lotto.id) ~
      ("winning-numbers" -> lotto.winningNumbers) ~
      ("draw-date" -> lotto.drawDate.map(_.toString)) ~
      ("winners" ->
        lotto.winners.map { w =>
          (("winner-id" -> w.id) ~
           ("numbers" -> w.numbers))}))

  def render(in: NodeSeq) = {
    JsRaw(json.toString()).cmd
  }

  def oracle(in: NodeSeq) : NodeSeq = {
    <div>{JsonAST.render(json)}</div>
  }
}