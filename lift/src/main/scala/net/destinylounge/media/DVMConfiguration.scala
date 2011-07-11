package net.destinylounge.media

import java.net.{InetAddress}

/**
 * Created by IntelliJ IDEA.
 * User: markhalldev
 * Date: 7/8/11
 * Time: 12:21 PM
 * To change this template use File | Settings | File Templates.
 */

class DVMConfiguration extends DVMController {

  def setColorspace (n : Int) {
    /*
      This command only applies to DVM’s with either the Professional Video(/P)
      or Component Video(/R) options installed.

      0=RGB
      1=YUV
     */

    // Check for Professional Video(/P) or Component Video(/R) options?    MEH? Todo

    sendMessage(n, 0, 1, 0, "VC")
  }

  def setSyncOnGreen (n : Int) {
    /*
      This command only applies to DVM’s with either the Professional Video(/P)
      or Component Video(/R) options installed. This command also only applies
      when the RGB colorspace format is being used.

      0=OFF (RGB-HV)
      1=ON (RGsB)
     */

    // Check for Professional Video(/P) or Component Video(/R) options?    MEH? Todo

    sendMessage(n, 0, 1, 0, "SG")
  }

  def setBrightness (n : Int) {
    sendMessage(n, 0, 100, 50, "BN")
  }

  def setContrast (n : Int) {
    sendMessage(n, 0, 100, 50, "CO")
  }

  def setSaturation (n : Int) {
    sendMessage(n, 0, 100, 50, "SA")
  }

  def setAspectRatio (n : Int) {
    /*
      0 = 4:3 Normal
      1 = 4:3 Pan-and-Scan
      2 = 4:3 Letterbox
      3 = 16:9 Normal
      4 = 16:9 Letterbox
      5 = 16:9 Pan-and-Scan
     */

    // Default value? MEH? Todo
    sendMessage(n, 0, 5, 0, "AP")
  }

  def setTestPattern (n : Int) {
    /*
      0 = Test Pattern Off (Normal video output displayed)
      1 = ITU Combination Test Signal
      2 = Pluge Test Pattern
      3 = SMPTE Bars
      4 = Step and Pulse Test Pattern
      5 = EIA Color Bars
      6 = EBU Color Bars
      7 = EBU Color Bars (100%)
     */
    sendMessage(n, 0, 7, 0, "TP")
  }

  // Ethernet Configuration Methods


  def sendMessage (ipAddress: InetAddress, commandBytes: String) {
    // Extract everything beyond the last forward slash
    var addressStr = ipAddress.toString
    addressStr = addressStr.substring(1 +
      addressStr.lastIndexOf("/", addressStr.length))

    sendMessage(addressStr, commandBytes)
  }

  def setIPAddress (ipAddress: InetAddress) {
    // xxx.xxx.xxx.xxxIP

    sendMessage(ipAddress, "IP")
  }

  def setIPAddress (s : String) /* throws
UnknownHostException */ {
    // xxx.xxx.xxx.xxxIP

    // MEH? Todo - Need to save IP address and or pass it to the UDPClient?

    //Any checking needed on IP addr integrity? Throw exception. MEH? Todo
    var ipAddress = InetAddress.getByName(s);

    setIPAddress(ipAddress)
  }

  def setSubnetMask (ipAddress: InetAddress) {
    // xxx.xxx.xxx.xxxSM

    sendMessage(ipAddress, "SM")
  }

  def setSubnetMask (s : String) {
    // xxx.xxx.xxx.xxxSM

    //Any checking needed on IP addr integrity? Throw exception. MEH? Todo
    var ipAddress = InetAddress.getByName(s);

    setSubnetMask(ipAddress)
  }

 def setGateway (ipAddress: InetAddress) {
    // xxx.xxx.xxx.xxxSM

    sendMessage(ipAddress, "GW")
  }

  def setGateway (s : String) {
    // xxx.xxx.xxx.xxxSM

    //Any checking needed on IP addr integrity? Throw exception. MEH? Todo
    var ipAddress = InetAddress.getByName(s);

    setGateway(ipAddress)
  }


  // FTP Methods

  def setFTPUserName (s : String) {
    /*
      This string of characters is case-sensitive,
      and must be between 4 and 32 characters in length.
     */

    // Check and/or limit length. MEH? Todo
    var shouldFail = false
    if (s.length < 4 || s.length > 32)
      shouldFail = true

    sendMessage(s, "US")
  }

  def setFTPPassword (s : String) {
    /*
      This string of characters is case-sensitive,
      and must be between 4 and 32 characters in length.
     */

    // Check and/or limit length. MEH? Todo

    sendMessage(s, "PW")
  }


}