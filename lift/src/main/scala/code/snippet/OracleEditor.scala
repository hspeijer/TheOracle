package code.snippet

/*
 * Copyright 2007-2010 WorldWide Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import _root_.net.liftweb._
import http._
import mapper._
import S._
import SHtml._

import common._
import util._
import Helpers._

import _root_.scala.xml.{NodeSeq, Text, Group}
import _root_.java.util.Locale
import scala.collection.immutable.List
import code.model.OracleNode
import javax.management.remote.rmi._RMIConnection_Stub

object OracleEditor {
  private object selectedNode extends RequestVar[Box[OracleNode]](Empty)

  /**
   * Get the XHTML containing a list of users
   */
  def nodes: NodeSeq = {
    // the header
    <tr>{OracleNode.htmlHeaders()}<th>Edit</th><th>Delete</th></tr> ::
      OracleNode.findAll().flatMap(node => <tr>{node.htmlLine}
        <td>{link("/edit/edit", () => selectedNode(Full(node)), Text("Edit"))}</td>
        <td>{link("/edit/delete", () => selectedNode(Full(node)), Text("Delete"))}</td>
                                                           </tr>)
    // get and display each of the users

  }
  /*
  /**
   * Confirm deleting a user
   */
  def confirmDelete(xhtml: Group): NodeSeq = {
    (for (user <- selectedUser.is) // find the user
     yield {
        def deleteUser() {
          notice("User "+(user.firstName+" "+user.lastName)+" deleted")
          user.delete_!
          redirectTo("/simple/index.html")
        }

        // bind the incoming XHTML to a "delete" button.
        // when the delete button is pressed, call the "deleteUser"
        // function (which is a closure and bound the "user" object
        // in the current content)
        bind("xmp", xhtml, "username" -> (user.firstName.is+" "+user.lastName.is),
             "delete" -> submit("Delete", deleteUser _))

        // if the was no ID or the user couldn't be found,
        // display an error and redirect
      }) openOr {error("User not found"); redirectTo("/simple/index.html")}
  }
*/
  // called when the form is submitted
  private def saveNode(node: OracleNode) = {
    node.save()
  }


  /**
   * Add a user
   */
  def add(xhtml: Group): NodeSeq =
  selectedNode.is.openOr(new OracleNode()).toForm(Empty, saveNode _) ++ <tr>
    <td><a href="/edit/index.html">Cancel</a></td>
                                                                </tr>
  /**
   * Edit a user
   */
  def edit(xhtml: Group): NodeSeq =
  selectedNode.map(_.
                   // get the form data for the user and when the form
                   // is submitted, call the passed function.
                   // That means, when the user submits the form,
                   // the fields that were typed into will be populated into
                   // "user" and "saveUser" will be called.  The
                   // form fields are bound to the model's fields by this
                   // call.
                   toForm(Empty, saveNode _) ++ <tr>
      <td><a href="/edit/index.html">Cancel</a></td>
                                                </tr>

                   // bail out if the ID is not supplied or the user's not found
  ) openOr {error("Node not found"); redirectTo("/edit/index.html")}

}


