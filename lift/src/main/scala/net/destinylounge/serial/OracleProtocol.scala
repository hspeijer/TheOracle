package net.destinylounge.serial

import code.model.ButtonState
import gnu.io.{SerialPort, CommPort, CommPortIdentifier}
import java.io.{OutputStream, IOException, InputStream}
import code.comet.OracleButtonServer
import java.lang.String
import net.liftweb.common.Logger
import code.lib.ConfigurationManager
import com.sun.xml.internal.ws.developer.MemberSubmissionAddressing.Validation
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result

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
  def getSerialWriter(out: OutputStream) : Runnable
}

class StampProtocol extends  ProtocolHandler {

  def getSerialReader(in: InputStream) : Runnable = return new SerialReader(in)
  def getSerialWriter(out: OutputStream) : Runnable = return new SerialWriter(out)

  /***/
  class SerialReader(in: InputStream) extends Runnable {
    def run: Unit = {
      val readBuffer = new Array[Byte](1024)
      var len = -1
      try {
        while (({len = in.read(readBuffer); len}) > -1) {
            try {
              val string = new String(readBuffer, 0, len)
              val result = string.substring(string.indexOf("B>") + 2, string.indexOf("<B"));
              OracleProtocol.receivedButtonState(new ButtonState(java.lang.Byte.parseByte(result, 16)))
            } catch {
                case e: Exception =>
            }

          for( i <- 0 until 1023) {readBuffer(i) = 0.toByte}

          Thread.sleep(200);
        }
      } catch {
        case e: IOException => error("IO Exception " + e.getMessage)
      }
    }
  }

  /***/
  class SerialWriter(out: OutputStream) extends Runnable {
    def run: Unit = {
      try {
        while (true) {
          val bytes = lightState.toByte().toHexString
          out.write(bytes.getBytes)
          out.write('\n')
          Thread.sleep(100);
        }
      } catch {
        case e: IOException => error("IO Exception " + e.getMessage)
      }
    }
  }
}

class BinaryProtocol extends ProtocolHandler {
  def getSerialReader(in: InputStream) : Runnable = return new SerialReader(in)
  def getSerialWriter(out: OutputStream) : Runnable = return new SerialWriter(out)

  /***/
  class SerialReader(in: InputStream) extends Runnable {
    def run: Unit = {
      val readBuffer = new Array[Byte](1024)
      var len = -1
      try {
        while (({len = in.read(readBuffer); len}) > -1) {
          for (i <- 0 until len) {
            OracleProtocol.receivedButtonState(new ButtonState(readBuffer(i)))
          }
          Thread.sleep(100);
        }
      } catch {
        case e: IOException => error("IO Exception " + e.getMessage)
      }
    }
  }

  /***/
  class SerialWriter(out: OutputStream) extends Runnable {
    def run: Unit = {
      try {
        while (true) {
          val byte = lightState.toByte()
          out.write(byte)
          Thread.sleep(100);
        }
      } catch {
        case e: IOException => error("IO Exception " + e.getMessage)
      }
    }
  }
}

object OracleProtocol extends Logger {
  val portId = ConfigurationManager.getSetting("serial.port");
  val oracleHandlerType = ConfigurationManager.getSetting("serial.protocol.handler")
  var handler : ProtocolHandler = null

  oracleHandlerType match {
    case "net.destinylounge.serial.StampProtocol" => handler = new StampProtocol
    case "net.destinylounge.serial.BinaryProtocol" => handler = new BinaryProtocol
    case x => handler = new BinaryProtocol
  }

  info("Initializing Oracle Serial Protocol")

  connect()

  def main(args : Array[String]) {
    ConfigurationManager.loadSettings("oracle");
    sendState(new ButtonState(63));
    Thread.sleep(10000);
  }

  def sendState(state: ButtonState) = {
    handler.lightState = state
    debug("Sending to serial: " + handler.lightState)
  }

  def receivedButtonState(buttonState : ButtonState) {
    //debug("Received:" + buttonState)
    if (handler.buttonState.toByte() != buttonState.toByte()) {
      handler.buttonState = buttonState
      debug("Received from serial: " + buttonState)
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

          (new Thread(handler.getSerialReader(serialPort.getInputStream))).start
          (new Thread(handler.getSerialWriter(serialPort.getOutputStream))).start
        } else {
          error("Only serial ports are handled.")
        }
      }
    } catch {
      case e => error("Could not initialise serial port" + e.getMessage)
    }
  }
}