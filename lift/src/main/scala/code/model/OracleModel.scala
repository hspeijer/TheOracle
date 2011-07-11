package scala.code.model

import org.neo4j.graphdb.RelationshipType

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 14-6-11
 * Time: 21:04
 * 
 */

object RelTypes extends Enumeration {
  type RelTypes = Value
  val EARTH, WATER, FIRE, AIR, AETHER = Value

  implicit def conv(rt: RelTypes) = new RelationshipType() {def name = rt.toString}
}


class StoryNode(id: Int, description : String)

class SoundEffect(path: String, format : String)

class MovieClip(path: String, format: String)

object OracleModel {

}