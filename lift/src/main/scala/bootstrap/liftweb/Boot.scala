package bootstrap.liftweb

import net.liftweb._
import sitemap.LocPath._
import code.rest.OracleRest

//import sitemap.MenuSingleton._
import util._
import common._
import http._
import sitemap._
import Loc._
import mapper._

import code.model._


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
//    if (!DB.jndiJdbcConnAvailable_?) {
//      val vendor =
//	new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
//			     Props.get("db.url") openOr
//			     "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
//			     Props.get("db.user"), Props.get("db.password"))
//
//      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)
//
//      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
//    }

    println("Booting the Oracle?");
    //NeoInit.init();

//    // Use Lift's Mapper ORM to populate the database
//    // you don't need to use Mapper to use Lift... use
//    // any ORM you want
//    Schemifier.schemify(true, Schemifier.infoF _, User)

    // where to search snippet
    LiftRules.addToPackages("code")

    // Build SiteMap
    def sitemap = SiteMap(
      Menu.i("The Oracle") / "index",
      Menu(Loc("Edit", Link(List("edit"), true, "/edit/index"), "Edit Oracle")),
      Menu("DVMTest") / "test",
      Menu("UITest") / "uitest"
    )

    def sitemapMutators = User.sitemapMutator

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMapFunc(() => sitemap)

    // Use jQuery 1.4
    LiftRules.jsArtifacts = net.liftweb.http.js.jquery.JQuery14Artifacts

    LiftRules.dispatch.prepend(OracleRest.dispatch)

//    //Show the spinny image when an Ajax call starts
//    LiftRules.ajaxStart =
//      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)
//
//    // Make the spinny image go away when it ends
//    LiftRules.ajaxEnd =
//      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

//    // What is the function to test if a user is logged in?
//    LiftRules.loggedInTest = Full(() => User.loggedIn_?)

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))    

//    // Make a transaction span the whole HTTP request
//    S.addAround(DB.buildLoanWrapper)
  }
}
