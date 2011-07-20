package code.model

import org.neo4j.graphdb.RelationshipType
import scala.collection.mutable.HashMap
import scala.collection.mutable.MutableList
import xml.NodeSeq
import net.liftweb.common.Empty
import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 14-6-11
 * Time: 21:04
 * 
 */

object RelType extends Enumeration {
  type RelType = Value
  val EARTH, WATER, FIRE, AIR, AETHER, ORACLE, CHALLENGE = Value

  implicit def conv(rt: RelType) = new RelationshipType() {def name = rt.toString}
}
import code.model.RelType._

object OracleNode {
  def apply(i: Int, s: String, s1: String, trigger: MovieTrigger) = {new OracleNode(i,s,s1,trigger).toString()}


  var currentId : Long = 500;
  val nodes = new HashMap[Long, OracleNode]();


  def findAll() : List[OracleNode] = {
    return nodes.toSeq.sortBy(_._1).map(_._2).toList
  }

  def nextId: Long = {
    currentId = currentId + 1
    currentId
  }

  def findNode(id: Long) : OracleNode = {
    nodes.get(id).get
  }

  def save(node: OracleNode) {
    nodes.put(node.id, node)
  }

  def htmlHeaders() : NodeSeq = {
    <th>Id</th><th>Description</th>
  }
}

class OracleNode(_id: Long, _script : String, _description: String, _clip: MovieTrigger) {

  val references = new MutableList[Reference]
  val id = _id
  var script = _script
  var description = _description
  var clip = _clip

  def this() = this(OracleNode.nextId, "", "", new MovieTrigger(0,""))

  def addReference(ref : Reference) {
    references += ref
  }

  def findReference(relType : RelType) : OracleNode = {
    references.foreach((ref : Reference) => if(ref.relType == relType) return OracleNode.findNode(ref.destId))

    null
  }

  def findReference(index: Int, relType : RelType) : OracleNode = {
    var foundIndex : Int = 0
    references.foreach((ref : Reference) =>
      if(ref.relType == relType) {
        if (index == foundIndex)
          return OracleNode.findNode(ref.destId)
        else
          foundIndex += 1
      })

    null
  }

  def countReferences(relType : RelType) : Int = {
    var numFound : Int = 0
    references.foreach((ref : Reference) => if(ref.relType == relType) numFound +=1)

    numFound
  }

  def save() = {
    OracleNode.save(this)
  }

  def htmlLine() : NodeSeq = {
    <td>{id}</td><td>{description}</td>
  }

  //todo: figure out implicit type conversion
  def setDuration(s: String) = {
    clip.duration = Integer.parseInt(s)
  }

  def toForm(empty: Empty.type, function: (OracleNode) => Any) : NodeSeq = {
    val result = {<span>Description:</span><node:description></node:description> <br/>
    <span>Clip: </span><node:name></node:name>
    <span>Duration</span><node:duration></node:duration><br/>
    <span>Script: </span><node:script></node:script>
    <node:save></node:save>}

    def doSave() {
      save
    }

    bind("node", result,
      "script" -> SHtml.textarea(script, script = _),
      "description" -> SHtml.text(description, description = _),
      "duration" -> SHtml.text(clip.durationStr, setDuration(_)),
      "name" -> SHtml.text(clip.name, clip.name = _),
      "save" -> SHtml.submit("Save", doSave)
    )

  }

  override def toString() : String = "" + id + ":" + description + " " +  references
}

class Reference(_srcId : Long, _destId: Long, _relType : RelType) {
  val srcId = _srcId
  val destId = _destId
  val relType = _relType

  override def toString() = "(" + srcId + ", " + destId + " " + relType + ")"
}

class MediaTrigger(_duration: Int) {
  var duration = _duration
  def durationStr = duration.toString
}

class MovieTrigger(_duration: Int, _name: String) extends MediaTrigger(_duration){
  var name = _name
}

object OracleModel {

  val testNodes = List(
    new OracleNode(0, "This is the root node", "root", new MovieTrigger(15, "1234")),
    new OracleNode(1, "I am kinda stupid but I know stuff", "Stupid-Intro", new MovieTrigger(15, "1234")),
    new OracleNode(2, "Pose your question!", "Stupid-Challenge", new MovieTrigger(15, "1234")),
    new OracleNode(3, "Fire burns ya know", "Stupid-Fire1", new MovieTrigger(15, "worst-anti-drugs")),
    new OracleNode(4, "all you need on the playa", "Stupid-Water1", new MovieTrigger(15, "1234")),
    new OracleNode(14, "More Water! Please!!!!!!!!!!!", "Stupid-Water2", new MovieTrigger(15, "worst-anti-drugs")),
    new OracleNode(5, "Plenty of dust, go roll around", "Stupid-Earth1", new MovieTrigger(15, "1234")),
    new OracleNode(6, "I like butterflies", "Stupid-Air1", new MovieTrigger(15, "flower")),
    new OracleNode(7, "Go fairy!", "Stupid-Aether1", new MovieTrigger(15, "spirit"))
    new OracleNode(21, "I am 2 stupid but I know stuff", "Stupid-Intro", new MovieTrigger(15, "1234")),
    new OracleNode(22, "Pose your question!-2", "Stupid-Challenge", new MovieTrigger(15, "1234")),
    new OracleNode(31, "I am 3 stupid but I know stuff", "Stupid-Intro", new MovieTrigger(15, "1234")),
    new OracleNode(32, "Pose your question!-3", "Stupid-Challenge", new MovieTrigger(15, "1234")),
    new OracleNode(41, "I am 4 stupid but I know stuff", "Stupid-Intro", new MovieTrigger(15, "1234")),
    new OracleNode(42, "Pose your question!-4", "Stupid-Challenge", new MovieTrigger(15, "1234")),
    new OracleNode(51, "I am 5 stupid but I know stuff", "Stupid-Intro", new MovieTrigger(15, "1234")),
    new OracleNode(52, "Pose your question!-5", "Stupid-Challenge", new MovieTrigger(15, "1234")),
    new OracleNode(61, "I am 6 stupid but I know stuff", "Stupid-Intro", new MovieTrigger(15, "1234")),
    new OracleNode(62, "Pose your question!-6", "Stupid-Challenge", new MovieTrigger(15, "1234"))
  )

  val testReferences = List(
    // Oracle 1
    new Reference(0, 1, RelType.ORACLE),
    new Reference(1, 2, RelType.CHALLENGE),
    new Reference(2, 3, RelType.FIRE),
    new Reference(2, 4, RelType.WATER),
    new Reference(4, 14, RelType.WATER),
    new Reference(2, 5, RelType.EARTH),
    new Reference(2, 6, RelType.AIR),
    new Reference(2, 7, RelType.AETHER),

    // Oracle 2
    new Reference(0,  21, RelType.ORACLE),
    new Reference(21, 22, RelType.CHALLENGE),
    new Reference(22,  3, RelType.FIRE),
    new Reference(22,  4, RelType.WATER),
    new Reference(22,  5, RelType.EARTH),
    new Reference(22,  6, RelType.AIR),
    new Reference(22,  7, RelType.AETHER),

    // Oracle 3
    new Reference(0,  31, RelType.ORACLE),
    new Reference(31, 32, RelType.CHALLENGE),
    new Reference(32,  3, RelType.FIRE),
    new Reference(32,  4, RelType.WATER),
    new Reference(32,  5, RelType.EARTH),
    new Reference(32,  6, RelType.AIR),
    new Reference(32,  7, RelType.AETHER),

    // Oracle 4
    new Reference(0,  41, RelType.ORACLE),
    new Reference(41, 42, RelType.CHALLENGE),
    new Reference(42,  3, RelType.FIRE),
    new Reference(42,  4, RelType.WATER),
    new Reference(42,  5, RelType.EARTH),
    new Reference(42,  6, RelType.AIR),
    new Reference(42,  7, RelType.AETHER),

    // Oracle 5
    new Reference(0,  51, RelType.ORACLE),
    new Reference(51, 52, RelType.CHALLENGE),
    new Reference(52,  3, RelType.FIRE),
    new Reference(52,  4, RelType.WATER),
    new Reference(52,  5, RelType.EARTH),
    new Reference(52,  6, RelType.AIR),
    new Reference(52,  7, RelType.AETHER),

    // Oracle 6
    new Reference(0,  61, RelType.ORACLE),
    new Reference(61, 62, RelType.CHALLENGE),
    new Reference(62,  3, RelType.FIRE),
    new Reference(62,  4, RelType.WATER),
    new Reference(62,  5, RelType.EARTH),
    new Reference(62,  6, RelType.AIR),
    new Reference(62,  7, RelType.AETHER)
  )

  testNodes.foreach((node : OracleNode) => OracleNode.nodes.put(node.id, node));
  testReferences.foreach((ref : Reference) => OracleNode.findNode(ref.srcId).addReference(ref))
}