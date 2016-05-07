package com.company;

import org.jcodec.api.awt.FrameGrab;
import org.jcodec.codecs.h264.H264Encoder;
import org.jcodec.common.FileChannelWrapper;
import org.jcodec.common.NIOUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class UDPServer {
    public static void main(String args[]) throws Exception
    {
        try
        {
            DatagramSocket serverSocket = new DatagramSocket(9876);

            //Encode encode = new Encode();
            //encode.Encode();

            byte[] receiveData;
            byte[] sendData;

            File file = new File("video.mp4");
            FileInputStream fis = new FileInputStream(file);
            DatagramPacket sendPacket;

            int size = 0;
            byte[] buffer = new byte[((int) file.length())];
            ByteBuffer bb = ByteBuffer.allocate(4);
            bb.order(ByteOrder.BIG_ENDIAN);

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

                    while (true)
                    {
                        fis.read(buffer);
                        sendPacket = new DatagramPacket(buffer, buffer.length, IPAddress, port);
                        serverSocket.send(sendPacket);
                    }
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

