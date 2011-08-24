package net.destinylounge

import org.neo4j.kernel.EmbeddedGraphDatabase
//import org.neo4j.index.IndexService
//import org.neo4j.index.lucene.LuceneIndexService
import collection.JavaConversions
import org.neo4j.graphdb.{Direction, RelationshipType}
import org.h2.expression.Variable

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 25-5-11
 * Time: 6:01
 * 
 */
//
//object RelTypes2 extends Enumeration {
//  type RelTypes = Value
//  val USERS_REFERENCE, USER, FRIEND = Value
//
//  implicit def conv(rt: RelTypes) = new RelationshipType() {def name = rt.toString}
//}

import RelTypes2._
import org.neo4j.graphdb.GraphDatabaseService

object EmbeddedNeo4jWithIndexingS {
  private val DB_PATH = "neo4j-store3"
  private val USERNAME_KEY = "username"
  private val graphDb: GraphDatabaseService = new EmbeddedGraphDatabase(DB_PATH)
//  private val indexService: IndexService = new LuceneIndexService(graphDb)

  private def registerShutdownHook =
    Runtime.getRuntime.addShutdownHook(new Thread() {override def run = shutdown})

  private def shutdown = {
//    indexService.shutdown
    graphDb.shutdown
  }

  private def idToUserName(id: Int) = {
    "user_macho" + id + "@neo4j.org"
  }

  private def createAndIndexUser(username: String) = {
    val node = graphDb.createNode
    node.setProperty(USERNAME_KEY, username)
//    indexService.index(node, USERNAME_KEY, username)
    node
  }

  private def doTx(f: GraphDatabaseService => Unit) = {
    val tx = graphDb.beginTx
    try
    {
      f(graphDb)
      tx.success
    }
    finally {
      tx.finish
    }
  }

  def main(args: Array[String]): Unit = {
    registerShutdownHook
    doTx {
      db =>
        val usersReferenceNode = db.createNode

        db.getReferenceNode.createRelationshipTo(usersReferenceNode, USERS_REFERENCE)

        var friendNode = usersReferenceNode
        for (id <- 0 to 1000)
        {
          val userNode = createAndIndexUser(idToUserName(id))
          usersReferenceNode.createRelationshipTo(userNode, USER);
          if (id % 23 == 0) {userNode.createRelationshipTo(friendNode, FRIEND); friendNode = userNode; println(id);}
        }
        println("Users created")

        val idToFind = 655
//        val foundUser = indexService.getSingleNode(USERNAME_KEY, idToUserName(idToFind))
 //       println("The username of user " + idToFind + " is " + foundUser.getProperty(USERNAME_KEY))
    }
  }
}