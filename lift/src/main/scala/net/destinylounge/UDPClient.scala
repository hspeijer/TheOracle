package net.destinylounge

import java.net.{DatagramPacket, InetAddress, DatagramSocket}

/**
 * Created by IntelliJ IDEA.
 * User: markhalldev
 * Date: 7/7/11
 * Time: 9:13 PM
 * To change this template use File | Settings | File Templates.
 */

object UDPClient {
//  val server = "localhost"
  val server = "192.168.0.14"
  val port = 2638

  def sendMessage(message : String) {
     var clientSocket: DatagramSocket = new DatagramSocket
     var IPAddress: InetAddress = InetAddress.getByName(server)
     var sendData: Array[Byte] = new Array[Byte](256)
     var receiveData: Array[Byte] = new Array[Byte](256)

     //System.out.println("Sending message:" + message)
     sendData = message.getBytes
     var sendPacket: DatagramPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port)
     clientSocket.send(sendPacket)
     var receivePacket: DatagramPacket = new DatagramPacket(receiveData, receiveData.length)
     clientSocket.receive(receivePacket)
     var modifiedSentence: String = new String(receivePacket.getData)
     System.out.println("FROM SERVER:" + modifiedSentence)
     clientSocket.close
   }

}