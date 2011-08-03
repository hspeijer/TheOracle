package code.model

import xml.NodeSeq
import collection.mutable.{MutableList, HashMap}
import net.liftweb.common.Empty
import net.liftweb.util.BindHelpers._
import net.liftweb.http.SHtml

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 21-7-11
 * Time: 7:27
 * 
 */

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

case class OracleNode(id: Long, script : String, description: String, clip: MovieTrigger) {

  val references = new MutableList[Reference]
  var scriptVar = script
  var descriptionVar = description

  def this() = this(OracleNode.nextId, "", "", new MovieTrigger(0,""))

  def addReference(ref : Reference) {
    references += ref
  }

  def findReference(triggerType : Trigger) : OracleNode = {
    references.foreach((ref : Reference) => if(ref.relType == triggerType) return OracleNode.findNode(ref.destId))

    null
  }

  def findReference(index: Int, relType : Trigger) : OracleNode = {
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

  def countReferences(relType : Trigger) : Int = {
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
    clip.durationVar = Integer.parseInt(s)
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
      "script" -> SHtml.textarea(scriptVar, scriptVar = _),
      "description" -> SHtml.text(descriptionVar, descriptionVar = _),
      "duration" -> SHtml.text(clip.durationStr, setDuration(_)),
      "name" -> SHtml.text(clip.nameVar, clip.nameVar = _),
      "save" -> SHtml.submit("Save", doSave)
    )
  }

  override def toString() : String = "" + id + ":" + description + " " +  references
}
