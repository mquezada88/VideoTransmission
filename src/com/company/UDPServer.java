package com.company;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

class UDPServer {
    public static void main(String args[]) throws Exception
    {
        try
        {
            DatagramSocket serverSocket = new DatagramSocket(9876);

            byte[] receiveData;
            byte[] sendData;

            while(true)
            {
                receiveData = new byte[1024];

                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                System.out.println ("Waiting for datagram packet");

                serverSocket.receive(receivePacket);

                // start transmit video
                byte[] msg = receivePacket.getData();
                if (msg[0] == 1)
                {
                    InetAddress IPAddress = receivePacket.getAddress();
                    int port = receivePacket.getPort();

                    System.out.println ("From: " + IPAddress + ":" + port);
                    System.out.println ("Command: " + msg[0]);

                    // encode video
                    sendData = "VIDEO TRANSMISSON".getBytes();
                    //send video
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                    serverSocket.send(sendPacket);
                }
            }
        }
        catch (SocketException ex)
        {
            System.out.println("UDP Port 9876 is occupied.");
            System.exit(1);
        }
    }
}

