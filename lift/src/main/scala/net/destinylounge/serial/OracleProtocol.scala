package net.destinylounge.serial

import code.model.ButtonState
import gnu.io.{SerialPort, CommPort, CommPortIdentifier}
import java.io.{OutputStream, IOException, InputStream}
import java.lang.String
import net.liftweb.common.Logger
import code.lib.ConfigurationManager
import code.comet.{MessageServer, OracleButtonServer}

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 26-7-11
 * Time: 3:07
 * 
 */

abstract class ProtocolHandler extends Logger {
  var lightState = new ButtonState(0)
  var buttonState = new ButtonState(0);

  def getSerialReader(in: InputStream) : Runnable
  def getSerialWriter(out: OutputStream, in: InputStream) : Runnable
}

//class StampProtocol extends  ProtocolHandler {
//
//  def getSerialReader(in: InputStream) : Runnable = return new SerialReader(in)
//  def getSerialWriter(out: OutputStream) : Runnable = return new SerialWriter(out)
//
//  /***/
//  class SerialReader(in: InputStream) extends Runnable {
//    def run: Unit = {
//      val readBuffer = new Array[Byte](1024)
//      var len = -1
//      try {
//        while (({len = in.read(readBuffer); len}) > -1) {
//            try {
//              val string = new String(readBuffer, 0, len)
//              val result = string.substring(string.indexOf("B>") + 2, string.indexOf("<B"));
//              OracleProtocol.receivedButtonState(new ButtonState(java.lang.Byte.parseByte(result, 16)))
//            } catch {
//                case e: Exception =>
//            }
//
//          for( i <- 0 until 1023) {readBuffer(i) = 0.toByte}
//
//          Thread.sleep(200);
//        }
//      } catch {
//        case e: IOException => error("IO Exception " + e.getMessage)
//      }
//    }
//  }
//
//  /***/
//  class SerialWriter(out: OutputStream) extends Runnable {
//    def run: Unit = {
//      try {
//        while (true) {
//          val bytes = lightState.toByte().toHexString
//          out.write(bytes.getBytes)
//          out.write('\n')
//          Thread.sleep(100);
//        }
//      } catch {
//        case e: IOException => error("IO Exception " + e.getMessage)
//      }
//    }
//  }
//}

class BinaryProtocol extends ProtocolHandler with Logger {
  def getSerialReader(in: InputStream) : Runnable = return new SerialReader(in)
  def getSerialWriter(out: OutputStream, in: InputStream) : Runnable = return new SerialWriter(out, in)

  debug("Init Binary Protocol")

  /***/
  class SerialReader(in: InputStream) extends Runnable {
    def run: Unit = {
      val readBuffer = new Array[Byte](1024)
      var len = -1
      try {
        while (({len = in.read(readBuffer); len}) > -1) {
          val string = new String(readBuffer, 0, len).split(" ")(1)
          //debug("Received: " + string);
          try {
            OracleProtocol.receivedButtonState(new ButtonState(Integer.parseInt(string)))
          } catch {case _ => {}}

          Thread.sleep(500);
        }
      } catch {
        case e: IOException => error("IO Exception " + e.getMessage)
      }
    }
  }

  /***/
  class SerialWriter(out: OutputStream, in :InputStream) extends Runnable {
    def run: Unit = {
      try {
        while (true) {
          val byte = lightState.toByte()
          out.write(byte)
          Thread.sleep(100)

          val readBuffer = new Array[Byte](1024)
          var len = in.read(readBuffer)
          val string = new String(readBuffer, 0, len)
          //debug("Received: " + string);

          try {
            OracleProtocol.receivedButtonState(new ButtonState(Integer.parseInt(string.split(" ")(0))))
          } catch {case _ => {}}

          Thread.sleep(500)
        }
      } catch {
        case e: IOException => error("IO Exception " + e.getMessage)
      }
    }
  }
}

object ProtocolTester extends Logger {
  def main(args : Array[String]) {
    ConfigurationManager.loadSettings("oracle");
for(i <- 0 to 100) {
    OracleProtocol.sendState(new ButtonState(1));
    Thread.sleep(1000);
    OracleProtocol.sendState(new ButtonState(2));
    Thread.sleep(1000);
    OracleProtocol.sendState(new ButtonState(4));
    Thread.sleep(1000);
    OracleProtocol.sendState(new ButtonState(8));
    Thread.sleep(1000);
    OracleProtocol.sendState(new ButtonState(16));
    Thread.sleep(1000);
}
  }
}

object OracleProtocol extends Logger {
  val portId = ConfigurationManager.getSetting("serial.port");
  val oracleHandlerType = ConfigurationManager.getSetting("serial.protocol.handler")
  var handler : ProtocolHandler = null

  oracleHandlerType match {
    //case "net.destinylounge.serial.StampProtocol" => handler = new StampProtocol
    case "net.destinylounge.serial.BinaryProtocol" => handler = new BinaryProtocol
    case x => handler = new BinaryProtocol
  }

  info("Initializing Oracle Serial Protocol")

  connect()

  def sendState(state: ButtonState) = {
    handler.lightState = state
    //debug("Sending to serial: " + handler.lightState)
  }

  def receivedButtonState(buttonState : ButtonState) {
    //debug("Received:" + buttonState)
    if (handler.buttonState.toByte() != buttonState.toByte()) {
      handler.buttonState = buttonState
      //debug("Received from serial: " + buttonState)
      MessageServer ! "Received from serial: " + buttonState
      OracleButtonServer ! buttonState
    }
  }

  def connect() = {
    try {
      info("Opening port at " + portId)
      val portIdentifier = CommPortIdentifier.getPortIdentifier(portId.toString)
      if (portIdentifier.isCurrentlyOwned) {
        error("Port is currently in use.")
      } else {
        val commPort = portIdentifier.open(this.getClass.getName, 2000)
        if (commPort.isInstanceOf[SerialPort]) {
          val serialPort = commPort.asInstanceOf[SerialPort]
          serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE)

          //(new Thread(handler.getSerialReader(serialPort.getInputStream))).start
          (new Thread(handler.getSerialWriter(serialPort.getOutputStream, serialPort.getInputStream))).start
        } else {
          error("Only serial ports are handled.")
        }
      }
    } catch {
      case e => error("Could not initialise serial port" + e.getMessage)
    }
  }
}