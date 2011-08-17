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

    new OracleNode(101, "I am the Earth Oracle", "Earth-Intro", new MovieTrigger(12, "EARTH001")),
    new OracleNode(102, "Welcome seeker. Speak your question and choose a stone!", "Earth-Challenge", new MovieTrigger(29, "EARTH001")),
    new OracleNode(103, "Do you seek more of my council? Speak your mind or leave my presence.", "Earth-Challenge1", new MovieTrigger(14, "EARTH002")),

    new OracleNode(160, "Veins of mineral draw a path through my mind. The path leads to the answer yes.", "Earth-yes1", new MovieTrigger(10, "EARTH003")),
    new OracleNode(161, "The answer is yes.", "Earth-yes2", new MovieTrigger(10, "EARTH004")),
    new OracleNode(162, "The answer is yes.", "Earth-yes3", new MovieTrigger(8, "EARTH005")),
    new OracleNode(163, "The answer is yes.", "Earth-yes4", new MovieTrigger(7, "EARTH006")),
    new OracleNode(164, "The answer is yes.", "Earth-yes5", new MovieTrigger(10, "EARTH007")),
    new OracleNode(165, "The answer is no.", "Earth-no1", new MovieTrigger(11, "EARTH008")),
    new OracleNode(166, "The answer is no.", "Earth-no2", new MovieTrigger(11, "EARTH009")),
    new OracleNode(167, "The answer is no.", "Earth-no3", new MovieTrigger(11, "EARTH010")),
    new OracleNode(168, "The answer is no.", "Earth-no4", new MovieTrigger(9, "EARTH011")),
    new OracleNode(169, "The answer is no.", "Earth-no5", new MovieTrigger(12, "EARTH012")),
    new OracleNode(170, "The answer is uncertain.", "Earth-uncertain1", new MovieTrigger(11, "EARTH013")),
    new OracleNode(171, "The answer is uncertain.", "Earth-uncertain2", new MovieTrigger(14, "EARTH014")),
    new OracleNode(172, "The answer is uncertain.", "Earth-uncertain3", new MovieTrigger(13, "EARTH015")),
    new OracleNode(173, "There answer is unknown.", "Earth-none1", new MovieTrigger(11, "EARTH016")),
    new OracleNode(174, "There answer is unknown.", "Earth-none2", new MovieTrigger(10, "EARTH017")),
    new OracleNode(175, "There answer is unknown.", "Earth-none3", new MovieTrigger(10, "EARTH018")),

    new OracleNode(201, "I am the Fire Oracle", "Fire-Intro", new MovieTrigger(8, "FIRE0004")),
    new OracleNode(202, "Welcome seeker. Speak your question and choose a stone!", "Fire-Challenge", new MovieTrigger(9, "FIRE0004")),

    new OracleNode(301, "I am the Hill Oracle", "Hill-Intro", new MovieTrigger(13, "HILLB001")),
    new OracleNode(302, "I am the Hill Oracle", "Hill-Intro", new MovieTrigger(13, "HILLB021")),
    new OracleNode(305, "Welcome seeker. Speak your question and choose a stone!", "Hill-Challenge", new MovieTrigger(13, "HILLB001")),

    new OracleNode(401, "I am the Time Oracle", "Time-Intro", new MovieTrigger(17, "TIME0002")),
    new OracleNode(402, "I am the Time Oracle", "Time-Intro", new MovieTrigger(10, "TIME0015")),
    new OracleNode(403, "I am the Time Oracle", "Time-Intro", new MovieTrigger(37, "TIME0052")),
    new OracleNode(405, "Welcome seeker. Speak your question and choose a stone!", "Time-Challenge", new MovieTrigger(19, "TIME0001")),

    new OracleNode(501, "I am the Water Oracle", "Water-Intro", new MovieTrigger(10, "WATER001")),
    new OracleNode(502, "Welcome seeker. Speak your question and choose a stone!", "Water-Challenge", new MovieTrigger(10, "WATER001"))
  )

  val testReferences = List(
    // Oracle 1
    new Reference(0, 101, OracleSelect),
    new Reference(101, 102, Challenge),
    new Reference(102, 103, Challenge),

    new Reference(102, 160, Earth),
    new Reference(102, 165, Earth),
    new Reference(102, 170, Earth),
    new Reference(102, 173, Earth),
    new Reference(103, 160, Earth),
    new Reference(103, 165, Earth),
    new Reference(103, 170, Earth),
    new Reference(103, 173, Earth),

    new Reference(102, 161, Fire),
    new Reference(102, 166, Fire),
    new Reference(102, 171, Fire),
    new Reference(102, 174, Fire),
    new Reference(103, 161, Fire),
    new Reference(103, 166, Fire),
    new Reference(103, 171, Fire),
    new Reference(103, 174, Fire),

    new Reference(102, 162, Water),
    new Reference(102, 167, Water),
    new Reference(102, 172, Water),
    new Reference(102, 175, Water),
    new Reference(103, 162, Water),
    new Reference(103, 167, Water),
    new Reference(103, 172, Water),
    new Reference(103, 175, Water),

    new Reference(102, 163, Air),
    new Reference(102, 168, Air),
    new Reference(102, 170, Air),
    new Reference(102, 173, Air),
    new Reference(103, 163, Air),
    new Reference(103, 168, Air),
    new Reference(103, 170, Air),
    new Reference(103, 173, Air),

    new Reference(102, 164, Aether),
    new Reference(102, 169, Aether),
    new Reference(102, 171, Aether),
    new Reference(102, 174, Aether),
    new Reference(103, 164, Aether),
    new Reference(103, 169, Aether),
    new Reference(103, 171, Aether),
    new Reference(103, 174, Aether),

    // Oracle 2
    new Reference(0, 201, OracleSelect),
    new Reference(201, 102, Challenge),

    // Oracle 3
    new Reference(0, 301, OracleSelect),
    new Reference(0, 302, OracleSelect),
    new Reference(301, 102, Challenge),
    new Reference(302, 102, Challenge),

    // Oracle 4
    new Reference(0, 401, OracleSelect),
    new Reference(0, 402, OracleSelect),
    new Reference(0, 403, OracleSelect),
    new Reference(401, 102, Challenge),
    new Reference(402, 102, Challenge),
    new Reference(403, 102, Challenge),

    // Oracle 5
    new Reference(0, 501, OracleSelect),
    new Reference(501, 102, Challenge)
  )

  testNodes.foreach((node : OracleNode) => OracleNode.nodes.put(node.id, node));
  testReferences.foreach((ref : Reference) => OracleNode.findNode(ref.srcId).addReference(ref))
}