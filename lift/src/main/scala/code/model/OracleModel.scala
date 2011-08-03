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

case class Reference(_srcId : Long, _destId: Long, _relType: Trigger) {
  val srcId = _srcId
  val destId = _destId
  val relType = _relType

  override def toString() = "(" + srcId + ", " + destId + " " + relType + ")"
}



object OracleModel {

  val testNodes = List(
    new OracleNode(0, "This is the root node", "root", new MovieTrigger(15, "1234")),
    new OracleNode(1, "I am kinda stupid but I know stuff", "Stupid-Intro", new MovieTrigger(15, "1234")),
    new OracleNode(2, "Pose your question!", "Stupid-Challenge", new MovieTrigger(15, "earth-challenge")),
    new OracleNode(3, "Fire burns ya know", "Stupid-Fire1", new MovieTrigger(15, "worst-anti-drugs")),
    new OracleNode(4, "all you need on the playa", "Stupid-Water1", new MovieTrigger(15, "1234")),
    new OracleNode(14, "More Water! Please!!!!!!!!!!!", "Stupid-Water2", new MovieTrigger(15, "worst-anti-drugs")),
    new OracleNode(5, "Plenty of dust, go roll around", "Stupid-Earth1", new MovieTrigger(15, "1234")),
    new OracleNode(6, "I like butterflies", "Stupid-Air1", new MovieTrigger(15, "flower")),
    new OracleNode(7, "Go fairy!", "Stupid-Aether1", new MovieTrigger(15, "spirit")),
    new OracleNode(21, "I am 2 stupid but I know stuff", "Stupid-Intro", new MovieTrigger(15, "1234")),
    new OracleNode(22, "Pose your question!-2", "Stupid-Challenge", new MovieTrigger(15, "earth-challenge")),
    new OracleNode(31, "I am 3 stupid but I know stuff", "Stupid-Intro", new MovieTrigger(15, "1234")),
    new OracleNode(32, "Pose your question!-3", "Stupid-Challenge", new MovieTrigger(15, "earth-challenge")),
    new OracleNode(41, "I am 4 stupid but I know stuff", "Stupid-Intro", new MovieTrigger(15, "1234")),
    new OracleNode(42, "Pose your question!-4", "Stupid-Challenge", new MovieTrigger(15, "1234")),
    new OracleNode(51, "I am 5 stupid but I know stuff", "Stupid-Intro", new MovieTrigger(15, "1234")),
    new OracleNode(52, "Pose your question!-5", "Stupid-Challenge", new MovieTrigger(15, "earth-challenge")),
    new OracleNode(61, "I am 6 stupid but I know stuff", "Stupid-Intro", new MovieTrigger(15, "1234")),
    new OracleNode(62, "Pose your question!-6", "Stupid-Challenge", new MovieTrigger(15, "earth-challenge"))
  )

  val testReferences = List(
    // Oracle 1
    new Reference(0, 1, OracleSelect),
    new Reference(1, 2, Challenge),
    new Reference(2, 3, Fire),
    new Reference(2, 4, Water),
    new Reference(4, 14, Water),
    new Reference(2, 5, Earth),
    new Reference(2, 6, Air),
    new Reference(2, 7, Aether),

    // Oracle 2
    new Reference(0,  21, OracleSelect),
    new Reference(21, 22, Challenge),
    new Reference(22, 3, Fire),
    new Reference(22, 4, Water),
    new Reference(22, 5, Earth),
    new Reference(22, 6, Air),
    new Reference(22, 7, Aether),

    // Oracle 3
    new Reference(0,  31, OracleSelect),
    new Reference(31, 32, Challenge),
    new Reference(32, 3, Fire),
    new Reference(32, 4, Water),
    new Reference(32, 5, Earth),
    new Reference(32, 6, Air),
    new Reference(32, 7, Aether)
  )

  testNodes.foreach((node : OracleNode) => OracleNode.nodes.put(node.id, node));
  testReferences.foreach((ref : Reference) => OracleNode.findNode(ref.srcId).addReference(ref))
}