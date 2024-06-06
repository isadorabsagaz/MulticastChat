package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import org.json.JSONObject;

import javax.swing.*;

public class UdpServer extends Thread{
    private DatagramSocket socket;
    private JTextArea msgTextArea;
    private byte[] buf;

    public UdpServer(MulticastSocket socket, JTextArea msgTextArea) {
        this.socket = socket;
        this.msgTextArea = msgTextArea;
        this.buf = new byte[65507];
    }

    public void run(){
        while (!Thread.interrupted()) {
            try{
                DatagramPacket received = new DatagramPacket(buf, buf.length);
                socket.receive(received);
                String msg = new String(buf, "UTF-8");
                JSONObject jsonObj = new JSONObject(msg);

                msgTextArea.append("("+jsonObj.get("username")+")\n"+ jsonObj.getString("message") +
                        "\n{date: "+jsonObj.getString("date")+"," + " time: "+jsonObj.getString("time")+"}\n"+"\n");

                System.out.println("Message received: "+ jsonObj.getString("message") + "\n{date: "+jsonObj.getString("date")+"," +
                        " time: "+jsonObj.getString("time")+", username: "+jsonObj.get("username")+"}\n");
            }
            catch (IOException e){
                e.printStackTrace();
                e.getMessage();
            }
        }
    }
}
