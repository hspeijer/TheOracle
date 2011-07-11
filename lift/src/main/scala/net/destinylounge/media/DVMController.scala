package net.destinylounge.media

import scala.net.destinylounge.UDPClient

/**
 * Created by IntelliJ IDEA.
 * User: markhalldev
 * Date: 7/8/11
 * Time: 2:48 AM
 * To change this template use File | Settings | File Templates.
 */

class DVMController {
  // initialize value... MEH? Todo
  var deviceIDStr: Array[Byte] = new Array[Byte](4)

  def formatMedia () {
    sendMessage("FO")
  }

  def setBaudRate (n : Int) {
    /*
        0 = 300 baud
        1 = 600 baud
        2 = 1200 baud
        3 = 2400 baud
        4 = 4800 baud
        5 = 9600 baud (default)
        6 = 19200 baud
        7 = 38400 baud
        8 = 57600 baud
        9 = 115200 baud
     */

    sendMessage(n, 0, 9, 5, "BR")
  }

  def setDeviceID (n : Int) {
    // Device ID (‘0’-‘126’), '127' acts as wildcard and all units in the control line execute a command

    var value = n

    // Check 0 <= n <= 127
    if (value > 0 || value < 127)
      value = 0     // Does anyone need to be informed that the value was out of range? MEH? todo

    //Convert the ID to ASCII and store it
    deviceIDStr = value.toString.getBytes()

    sendMessage(value, "ID")
  }

  def softwareReset () {
    sendMessage("XX")
  }

  def sendSerialString (message : String, appendCR : Boolean = false) {
    var command = "\"" + message + "\""
    if (appendCR)
      command += "h0d"
    sendMessage(command, "SS")
  }

  def sendSerialString (n : Int) {
    /*
     Converts the Int to a hex string and then formats it such that:
        0x123F
     is formatted as:
        h01h02h03h0f

     Example:
        sendSerialString(0x123F)

       Sends:
        h01h02h03h0fSS

     */

    // Not sure if DVM accepts upper and lower case hex values MEH? todo

    // Loving the compact nature of Scala, Hans! Todo
    var command = n.toHexString.toList.mkString("h0", "h0", "")

    sendMessage(command, "SS")
  }

  def setRelayOutputs (playRelay : Boolean, state : Boolean) {
    // params don't make for readable code   MEH? - ToDo
    // example:
    //     setRelayOutputs (false, true)

    /*
      1P = Turns ON Play Relay
      0P = Turns OFF Play Relay
      1F = Turns ON Fault Relay
      0F = Turns OFF Fault Relay
      X = Returns Relays to default Play/Fault operation
     */

    // Convert Boolean to String of the integer
    var command = "0"
    if (state)
      command = "1"

    if (playRelay)
      command += "P"
    else
      command += "F"

    sendMessage(command, "RL")
  }

  def setRelayOutputsToDefaults () {
    // Could call  sendMessage("XRL")   MEH? - ToDo
    sendMessage("X", "RL")
  }

  def setOnScreenDisplay (n : Int) {
    /*
       0 = OSD OFF (Normal Operation)
       1 = Display System Configuration Screen
       2 = Display Video Information Screen
       3 = Display Audio Information Screen
       4 = Display Activity Log
     */
    sendMessage(n, 0, 4, 0, "OS")
  }


  def sendMessage (commandBytes : String) {
    var command = deviceIDStr + "@" + commandBytes
    // Separate UDPClient objects in each method? Overhead of new free on sockets, etc? abstract for serial/UDP stream MEH? todo
    UDPClient.sendMessage(command)
  }


  def sendMessage (n : Int, commandBytes : String) {

    // Convert n to ASCII and concat strings
    var command = deviceIDStr + "@" + n.toString + commandBytes

    UDPClient.sendMessage(command)
  }

  def sendMessage (messageStr : String, commandBytes : String, addQuotes : Boolean = false) {

    var command = deviceIDStr + "@"
    if (addQuotes)
      command += "\"" + messageStr + "\""
    else
      command += messageStr
    command += commandBytes

    UDPClient.sendMessage(command)
  }

  def sendMessage (n : Int, min : Int, max : Int, default : Int, commandBytes : String) {
    var value = n

    // Check min <= n <= max
    if (value > max || value < min)
      value = default          // Does anyone need to be informed that the value was out of range? MEH? todo

    sendMessage(value, commandBytes)
  }
}