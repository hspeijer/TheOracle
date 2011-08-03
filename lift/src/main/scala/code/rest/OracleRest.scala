package code.rest

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 17-7-11
 * Time: 23:39
 * 
 */
import net.liftweb.http.{Req, GetRequest, PostRequest,
  LiftRules, JsonResponse, PlainTextResponse}
import net.liftweb.common.{Full, Box}
import net.liftweb.json.Extraction._
import net.liftweb.json.JsonAST
import net.liftweb.json.Printer._
import code.comet.OracleButtonServer
import code.model.{OracleNode, ButtonState, MovieTrigger, OracleModel}

object OracleRest {

  def dispatch: LiftRules.DispatchPF = {
    // Req(url_pattern_list, suffix, request_type)
    case Req("api" :: "get" :: Nil, _, GetRequest) => () => Full(get)
    case r@Req("api" :: "put" :: Nil, _, PostRequest) => () => Full(post(r.param("name")))
    case r@Req("api" :: "trigger" :: Nil, _, PostRequest) => () => Full(trigger(r.param("field")))
  }

  //"page":"1","total":2,"records":"13","rows":
  case class JQGridResonse(page: Int, total: Int, records: Int, rows: List[Product] )

  implicit val formats = net.liftweb.json.DefaultFormats
  def get = JsonResponse(decompose(JQGridResonse(1,1,OracleModel.testNodes.length,OracleModel.testNodes)))

  def trigger(field: Box[String]) = PlainTextResponse(field match {
    case Full(state_) => {
      System.out.println("Received trigger : " + field)
      OracleButtonServer ! field.iterator.next()

      "thanks"
    }
    case _ => "missing parameter field"
  })

  def post(name: Box[String]) = PlainTextResponse(name match {
    case Full(name_) => "thanks"
    case _ => "missing parameter name"
  })
}