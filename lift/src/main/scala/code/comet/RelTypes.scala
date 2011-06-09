package code
package comet

import org.neo4j.graphdb.RelationshipType

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 9-6-11
 * Time: 4:32
 * 
 */
object RelTypes extends Enumeration {
  type RelTypes = Value
  val USERS_REFERENCE, USER, FRIEND = Value

  implicit def conv(rt: RelTypes) = new RelationshipType() {def name = rt.toString}
}

