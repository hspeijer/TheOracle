package code.snippet

import xml.NodeSeq
import _root_.net.liftweb._
import http._
import mapper._
import S._
import SHtml._

import common._
import util._
import net.liftweb.util.Helpers._

/**
 * Created by IntelliJ IDEA.
 * User: markhalldev
 * Date: 7/9/11
 * Time: 7:05 PM
 * To change this template use File | Settings | File Templates.
 */

class dvmtest {

  def test() = {
    System.out.println("test")
  }

  def dvm (xhtml : NodeSeq) : NodeSeq = {
    var filename = ""

    def processPlay() = {
      println("Play? " + filename)
    }

    def processStop() = {
      println("Stop?")
    }

    bind("dvm", xhtml,
       "filename" -> SHtml.text(filename, filename = _),
       "play" -> SHtml.submit("Play", processPlay),
       "stop" -> SHtml.submit("Stop", processStop)
    )

    //<h1>Testing</h1>
  }

   def play (xhtml : NodeSeq) : NodeSeq = {
    var filename = "init"

    def processPlay() = {
      println("Play? " + filename)
    }

    bind("dvm", xhtml,
        "play" -> SHtml.submit("Play", processPlay),
       "filename" -> SHtml.text(filename, filename = _),
       "filename1" -> SHtml.text(filename, filename = _),
            "filename2" -> SHtml.text(filename, filename = _)
    )


    //<h1>Testing</h1>
  }
  def login(xhtml : NodeSeq) : NodeSeq = {
  var user = ""; var pass = "";
  def auth () = {println("Auth")}
  bind("login", xhtml,
       "user" -> SHtml.text(user, user = _, "maxlength" -> "40"),
       "pass" -> SHtml.password(pass, pass = _),
       "submit" -> SHtml.submit("Login", auth))
}
}