package net.destinylounge

import gnu.io.{SerialPort, CommPort, CommPortIdentifier}
import java.io.{OutputStream, IOException, InputStream}

object ScalaTestSerial {

  def main(args: Array[String]): Unit = {
    try {
      (new SerialTest).connect("COM3")
    } catch {
      case e: Exception => e.printStackTrace
    }
  }

  /***/
  class SerialReader(in: InputStream) extends Runnable {
    def run: Unit = {
      val buffer = new Array[Byte](1024)
      var len: Int = -1
      try {
        while (({len = in.read(buffer); len}) > -1) {
          System.out.print(new String(buffer, 0, len))
        }
      } catch {
        case e: IOException => e.printStackTrace
      }
    }
  }

  /***/
  class SerialWriter(out: OutputStream) extends Runnable {
    def run: Unit = {
      try {
        var c: Int = 0
        while (({ c = System.in.read; c }) > -1) {
          this.out.write(c)
        }
      } catch {
        case e: IOException => e.printStackTrace
      }
    }
  }

  class SerialTest {
    def connect(portName: String): Unit = {
      val portIdentifier = CommPortIdentifier.getPortIdentifier(portName)
      if (portIdentifier.isCurrentlyOwned) {
        System.out.println("Error: Port is currently in use")
      } else {
        val commPort = portIdentifier.open(this.getClass.getName, 2000)
        if (commPort.isInstanceOf[SerialPort]) {
          val serialPort = commPort.asInstanceOf[SerialPort]
          serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE)

          (new Thread(new ScalaTestSerial.SerialReader(serialPort.getInputStream))).start
          (new Thread(new ScalaTestSerial.SerialWriter(serialPort.getOutputStream))).start
        } else {
          System.out.println("Error: Only serial ports are handled by this example.")
        }
      }
    }
  }
}