package net.destinylounge;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by IntelliJ IDEA.
 * User: markhalldev
 * Date: 7/7/11
 * Time: 7:09 PM
 * To change this template use File | Settings | File Templates.
 */
class JavaUDPClient
{
   public static void main(String args[]) throws Exception
   {
       BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
      DatagramSocket clientSocket = new DatagramSocket();
      InetAddress IPAddress = InetAddress.getByName("192.168.1.254");
      byte[] sendData = new byte[1024];
      byte[] receiveData = new byte[1024];
      try {
          while(true) {
              String sentence = inFromUser.readLine();
              System.out.println("Sending: " + sentence);
              sendData = sentence.getBytes();
              DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 2638);
              clientSocket.send(sendPacket);
              DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
              clientSocket.receive(receivePacket);
              String modifiedSentence = new String(receivePacket.getData());
              System.out.println("FROM SERVER:" + modifiedSentence);
          }
      } finally {
        clientSocket.close();
      }
   }
}
