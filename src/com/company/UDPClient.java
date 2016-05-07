package com.company;

import java.io.*;
import java.net.*;
import java.util.*;

class UDPClient
{
    private InetAddress IPAddress;
    boolean done;
    boolean keepGoing;

    public UDPClient(String sHostName)
    {
        try {
            IPAddress = InetAddress.getByName(sHostName);
            System.out.println ("Attemping to connect to " + IPAddress +") via UDP port 9876");
        }
        catch (UnknownHostException ex)
        {
            System.err.println(ex);
            System.exit(1);
        }
        try
        {
            done = false;

            DatagramSocket clientSocket = new DatagramSocket();
            byte[] sendData = {1};
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
            clientSocket.send(sendPacket);

            done = true;
            byte[] receiveData = new byte[1024];
            keepGoing = true;

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            System.out.println ("Waiting for return packet");

            clientSocket.setSoTimeout(10000);

            // decoding video
            while (keepGoing)
            {
                try
                {
                    clientSocket.receive(receivePacket);
                    String modifiedSentence = new String(receivePacket.getData());
                    System.out.println("Message: " + modifiedSentence);

                }
                catch (SocketTimeoutException ste)
                {
                    System.out.println ("Timeout Occurred: Packet assumed lost");
                    if (done)
                        keepGoing = false;
                }
            }
            clientSocket.close();
        }
        catch (IOException ex)
        {
            System.err.println(ex);
        }
    }

    public static void main(String args[]) throws Exception
    {
        String serverHostname = new String ("127.0.0.1");

        if (args.length > 0)
            serverHostname = args[0];

        new UDPClient (serverHostname);

    }
}
