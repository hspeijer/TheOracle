package net.destinylounge;

import java.io.*;
import java.net.*;

class UDPServer
{
    public static String getHexString(byte[] b) throws Exception {
  String result = "";
  for (int i=0; i < b.length; i++) {
    result +=
          Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
  }
  return result;
}

   public static void main(String args[]) throws Exception
      {
          System.out.println("Starting Server...");
         DatagramSocket serverSocket = new DatagramSocket(2638);

            while(true)
               {
                   byte[] receiveData = new byte[256];
                   byte[] sendData = new byte[1024];
                  DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                  serverSocket.receive(receivePacket);
                  String sentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
                  System.out.println("RECEIVED packet" + getHexString(receivePacket.getData()) + '\n' + sentence);
                  InetAddress IPAddress = receivePacket.getAddress();
                  int port = receivePacket.getPort();
                  String capitalizedSentence = sentence.toUpperCase();
                  sendData = capitalizedSentence.getBytes();
                  DatagramPacket sendPacket =
                  new DatagramPacket(sendData, sendData.length, IPAddress, port);
                  serverSocket.send(sendPacket);
               }
      }
}
