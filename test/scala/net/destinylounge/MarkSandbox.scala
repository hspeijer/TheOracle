package net.destinylounge

/**
 * Created by IntelliJ IDEA.
 * User: markhalldev
 * Date: 7/28/11
 * Time: 11:50 PM
 * To change this template use File | Settings | File Templates.
 */

object MarkSandbox {


  class Whatever {
    def doSomething(): Unit = {
      print("Doing something")
    }
  }

  def main(args: Array[String]): Unit = {
    new Whatever().doSomething()
  }


}