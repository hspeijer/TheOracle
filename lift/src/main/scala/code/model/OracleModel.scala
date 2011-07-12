package code.model

import org.neo4j.graphdb.RelationshipType
import scala.collection.mutable.HashMap
import scala.collection.mutable.MutableList
import code.lib.DependencyFactory
import code.model.OracleModel
import xml.NodeSeq
import net.liftweb.mapper.{OrderBy, Mapper}
import net.liftweb.common.Empty

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
  val nodes = new HashMap[Long, OracleNode]();

  def findAll() : List[OracleNode] = {
    return nodes.toSeq.sortBy(_._1).map(_._2).toList
  }

  def findNode(id: Long) : OracleNode = {
    nodes.get(id).get
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

  def addReference(ref : Reference) {
    references += ref
  }

  def findReference(relType : RelType) : OracleNode = {
    references.foreach((ref : Reference) => if(ref.relType == relType) return OracleNode.findNode(ref.destId))

    null
  }

  def htmlLine() : NodeSeq = {
    <td>{id}</td><td>{description}</td>
  }

  def toForm(empty: Empty.type, value: Any) : NodeSeq = {
    <form></form>
  }

  override def toString() : String = "" + id + ":" + description + " " +  references
}

class Reference(_srcId : Long, _destId: Long, _relType : RelType) {
  val srcId = _srcId
  val destId = _destId
  val relType = _relType

  override def toString() = "(" + srcId + ", " + destId + " " + relType + ")"
}

class MediaTrigger(duration: Int)

class MovieTrigger(duration: Int, name: String) extends MediaTrigger(duration)

object OracleModel {

  val testNodes = List(
    new OracleNode(0, "This is the root node", "root", null),
    new OracleNode(1, "I am kinda stupid but I know stuff", "Stupid-Intro", new MovieTrigger(15, "1234")),
    new OracleNode(2, "Pose your question!", "Stupid-Challenge", new MovieTrigger(15, "1234")),
    new OracleNode(3, "Fire burns ya know", "Stupid-Fire1", new MovieTrigger(15, "1234")),
    new OracleNode(4, "all you need on the playa", "Stupid-Water1", new MovieTrigger(15, "1234")),
    new OracleNode(5, "Plenty of dust, go roll around", "Stupid-Earth1", new MovieTrigger(15, "1234")),
    new OracleNode(6, "I like butterflies", "Stupid-Air1", new MovieTrigger(15, "1234")),
    new OracleNode(7, "Go fairy!", "Stupid-Aether1", new MovieTrigger(15, "1234"))
  )

  val testReferences = List(
    new Reference(0, 1, RelType.ORACLE),
    new Reference(1, 2, RelType.CHALLENGE),
    new Reference(2, 3, RelType.FIRE),
    new Reference(2, 4, RelType.WATER),
    new Reference(2, 5, RelType.EARTH),
    new Reference(2, 6, RelType.AIR),
    new Reference(2, 7, RelType.AETHER)
  )

  testNodes.foreach((node : OracleNode) => OracleNode.nodes.put(node.id, node));
  testReferences.foreach((ref : Reference) => OracleNode.findNode(ref.srcId).addReference(ref))
}