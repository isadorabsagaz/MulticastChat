package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import org.json.JSONObject;

public class UdpServer extends Thread{
    private DatagramSocket socket;
    private byte[] buf;

    public UdpServer(MulticastSocket socket) {
        this.socket = socket;
        this.buf = new byte[65507];
    }

    public void run(){
        while (!Thread.interrupted()) {
            try{
                DatagramPacket received = new DatagramPacket(buf, buf.length);
                socket.receive(received);
                String msg = new String(buf, "UTF-8");
                JSONObject jsonObject = new JSONObject(msg);

                if(jsonObject.getString("message").equals("<exit>")){
                    socket.close();
                    break;
                }
                else System.out.println("Message received: "+ jsonObject.getString("message") + "\n{date: "+jsonObject.getString("date")+"," +
                        " time: "+jsonObject.getString("time")+", username: "+jsonObject.get("username")+"}");
            }
            catch (IOException e){
                e.printStackTrace();
                e.getMessage();
            }
        }
    }
}
