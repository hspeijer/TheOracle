package code.snippet

import xml.NodeSeq
import _root_.net.liftweb._
import http._
import mapper._
import S._
import SHtml._

import common._
import util._
import Helpers._
/**
 * Created by IntelliJ IDEA.
 * User: markhalldev
 * Date: 7/9/11
 * Time: 7:05 PM
 * To change this template use File | Settings | File Templates.
 */

object DVMTest {

  def test() = {
    System.out.println("test")
  }

  def add (xhtml : NodeSeq) : NodeSeq = {
    println("Add")

    def processAdd() = {
      println("Added?")
    }

    bind("entry", xhtml,
       "submit" -> SHtml.submit("Add", processAdd))
  }
}