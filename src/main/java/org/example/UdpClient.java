package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import org.json.JSONObject;

public class UdpClient {
        private static Scanner input = new Scanner(System.in);
        private String groupIp;
        private int port;
        private String username;
        private String message;

        public UdpClient(String username, int port, String groupIp) {
            this.username = username;
            this.port = port;
            this.groupIp = groupIp;
        }

        public void multicastChat(){
            try {
                MulticastSocket socket = new MulticastSocket(port);
                UdpServer server = new UdpServer(socket);
                server.start();
                InetAddress group = InetAddress.getByName(groupIp);
                socket.joinGroup(group);
                System.out.println(username+" joined the group!");

                while (!Thread.interrupted()) {
                    System.out.print("Type a message: ");
                    message = input.nextLine();

                    JSONObject jsonObject = createJson(message);
                    byte[] buf = jsonObject.toString().getBytes();
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, group, port);
                    socket.send(packet);
                    System.out.println("Message send!");


                    if ("<exit>".equals(message)) {
                        System.out.println(username + " is leaving group...");
                        socket.leaveGroup(group);
                        socket.close();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                e.getMessage();
            }
        }
        private JSONObject createJson(String message){
            JSONObject jsonObj = new JSONObject();
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
            jsonObj.put("date", dateFormat.format(LocalDateTime.now()));
            jsonObj.put("time", timeFormat.format(LocalDateTime.now()));
            jsonObj.put("username", username);
            jsonObj.put("message", message);

            return jsonObj;
        }
}
