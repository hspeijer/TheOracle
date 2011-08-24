package code.rest

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 17-7-11
 * Time: 23:39
 * 
 */
import net.liftweb.common.{Full, Box}
import net.liftweb.json.Extraction._
import code.comet.OracleButtonServer
import code.comet.MediaServer
import net.liftweb.json.JsonAST.{JObject, JField, JInt}
import java.lang.{StringBuilder, Long}
import net.liftweb.http._
import net.liftweb.common.Logger
import code.model._
import net.destinylounge.serial.OracleProtocol

object OracleRest extends Logger {

  def dispatch: LiftRules.DispatchPF = {
    // Req(url_pattern_list, suffix, request_type)
    case Req("api" :: "oracle" :: "nodes" :: Nil, _, GetRequest) => () => Full(oracleNodes)
    case Req("api" :: "oracle" :: "references" :: Nil, _, GetRequest) => () => Full(references)
    case request@Req("api" :: "graph" :: "center" :: Nil, _, GetRequest) => () => Full(queryGraph(request.param("nodeId"), request.param("depth")))
    case request@Req("api" :: "trigger" :: Nil, _, PostRequest) => () => Full(trigger(request.param("field")))
    case request@Req("api" :: "media" :: "get" :: Nil, _, GetRequest) => () => Box(getMediaFile(request.param("file")))
    case request@Req("api" :: "media" :: "list" :: Nil, _, GetRequest) => () => Full(getMediaList(request.param("filter")))
  }

  //"page":"1","total":2,"records":"13","rows":
  case class JQGridResonse(page: Int, total: Int, records: Int, rows: List[Product] )

  implicit val formats = net.liftweb.json.DefaultFormats

  def oracleNodes = JsonResponse(decompose(JQGridResonse(1,1,OracleModel.testNodes.length,OracleModel.testNodes)))
  def references = JsonResponse(decompose(JQGridResonse(1,1,OracleModel.testReferences.length,OracleModel.testReferences)))

  case class GraphResponse(id : String, depth : String, result: String)
  case class Node(id: String, var label: String, var references: Map[Any, Node]) {
    def addChild(child : Node) {
      references += child.id -> child
    }
  }

  def getMediaList(filter: Box[String]) = {
    JsonResponse(decompose(MediaServer.mediaContainer))
  }

  def getMediaFile(name: Box[String]) : Box[LiftResponse] = {
    val file = MediaServer.mediaContainer.getFile(name.openTheBox)
    val stream = file.inputStream
    Box(StreamingResponse(stream,
                        () => { stream.close },
                        file.size,
                        ("Content-Type" -> "video/mp4") :: Nil,
                        Nil,
                        200))
  }

  def queryGraph(nodeId: Box[String], depth: Box[String]) = {
    val root = Node("0", "root", Map())
    val happy = Node("1", "happy", Map())
    root addChild happy
    happy addChild  Node("1.1", "smiles", Map(1 -> Node("1.1.1", "glad", Map()), 2 -> Node("1.1.2", "joy", Map())))

    root addChild Node("2", "great", Map())
    root addChild Node("3", "work", Map())
    root addChild Node("4", "for", Map())
    root addChild Node("5", "today", Map(15 -> Node("1.0", "sleep", Map())))
    import net.liftweb.json._
    import net.liftweb.json.JsonDSL._

    def nodeRecurse(node: Node): String = {
      //println("recurse: " + node.id + " -> " + node.label)
      var result = "\"" + node.label.toString + "\":{"

      if (node.references.toList.length > 0) {
        node.references.foreach(entry => {

          if (entry._2.references.toList.length > 0) {
            result += nodeRecurse(entry._2)
          } else {
            //println("Leaf!\n")
            result += "\"" + entry._2.label + "\":\"" + entry._2.id + "\""
          }
          if (entry != node.references.last) {
            result += ","
          } else {
            result += "}"
          }
        })
      }

      result
    }

    val result = "{" + nodeRecurse(root) + "}"

    debug(compact(render(parse(result))))

    JsonResponse(parse(result))
    //JsObj(Js("foo", 4), ("bar", "baz")).toJsCmd
  }

  def trigger(field: Box[String]) = PlainTextResponse(field match {
    case Full(state_) => {
      debug("Received trigger : " + field.elements.next())
      val state = new ButtonState(field.elements.next())
      OracleButtonServer ! state
      OracleProtocol.sendState(state)

      "thanks"
    }
    case _ => "missing parameter field"
  })

}