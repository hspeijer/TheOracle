package net.destinylounge.serial

import code.model.ButtonState
import gnu.io.{SerialPort, CommPort, CommPortIdentifier}
import java.io.{OutputStream, IOException, InputStream}
import code.comet.OracleButtonServer
import java.lang.String
import net.liftweb.common.Logger
import code.lib.ConfigurationManager

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 26-7-11
 * Time: 3:07
 * 
 */

class ProtocolHandler
class StampProtocol extends  ProtocolHandler
class BinaryProtocol extends ProtocolHandler

object OracleProtocol extends Logger {
  val portId = ConfigurationManager.getSetting("serial.port");
  var lightState = new ButtonState(0);
  var buttonState = new ButtonState(0);

  info("Initializing Oracle Serial Protocol")

  connect()

  info("Still here?")

  def sendState(state: ButtonState) = {
    lightState = state
    debug("Sending to serial: " + lightState)
  }

  def receivedButtonState(string : String) {
    try {
      debug("Received:" + string)
      val byte = java.lang.Byte.parseByte(string, 16)
      if (byte != buttonState.toByte()) {
        buttonState = new ButtonState(byte);
        debug("Received from serial: " + buttonState)
        OracleButtonServer ! buttonState
      }
    } catch {
        case e: NumberFormatException => buttonState = new ButtonState(0)
    }
  }

  /***/
  class SerialReader(in: InputStream) extends Runnable {
    def run: Unit = {
      val readBuffer = new Array[Byte](1024)
      var len = -1
      try {
        while (({len = in.read(readBuffer); len}) > -1) {
          if(readBuffer.contains('\r')) {
            val firstNewLine = readBuffer.indexOf('\n')
            try {
              receivedButtonState(new String(readBuffer, firstNewLine + 1, 2))
            } catch {
                case e: Exception =>  error("Error receiving serial " + e.getMessage)
            }
          }
          Thread.sleep(1000);
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

          (new Thread(new SerialReader(serialPort.getInputStream))).start
          (new Thread(new SerialWriter(serialPort.getOutputStream))).start
        } else {
          error("Only serial ports are handled.")
        }
      }
    } catch {
      case e => error("Could not initialise serial port" + e.getMessage)
    }
  }
}