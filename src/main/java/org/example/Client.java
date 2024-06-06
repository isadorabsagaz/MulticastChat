package org.example;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Client extends JFrame{

    private JPanel clientPanel;
    private JPanel loginPanel;
    private JTextField usernameTextField;
    private JTextField portTextField;
    private JTextField groupIpTextField;
    private JButton leaveButton;
    private JButton joinButton;
    private JLabel usernameLabel;
    private JLabel portLabel;
    private JLabel groupIPLabel;
    private JScrollPane SrollTextArea;
    private JTextArea msgTextArea;
    private JTextField msgTextField;
    private JButton sendButton;
    private JPanel msgPanel;
    private String username;
    private int port;
    private String groupIp;
    private static MulticastSocket socket;
    private static InetAddress group;
    private static UdpServer server;
    private Random random;


    public Client() {

        joinButton.addActionListener(new ActionListener() { //JOIN BUTTON
            @Override
            public void actionPerformed(ActionEvent e) {
                random = new Random();
                if (usernameTextField.getText().isEmpty()) {
                    username = "user"+random.nextInt(100);
                } else username = usernameTextField.getText();

                if (portTextField.getText().isEmpty()) {
                    port = 5000;
                } else port = Integer.parseInt(portTextField.getText());

                if (groupIpTextField.getText().isEmpty()){
                    groupIp = "225.0.0.0";
                } else groupIp = groupIpTextField.getText();


                try {
                    socket = new MulticastSocket(port);
                    server = new UdpServer(socket, msgTextArea);
                    server.start();
                    msgTextArea.append("Starting server...\n");
                    group = InetAddress.getByName(groupIp);
                    socket.joinGroup(group);

                    sendJsonToServer("'"+username+"'"+" joined the group!");

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    ioException.getMessage();
                }
            }
        });
        leaveButton.addActionListener(new ActionListener() { //LEAVE BUTTON
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendJsonToServer("'"+username+"'"+" leaved the group...");
                    server.interrupt();
                    socket.leaveGroup(group);
                    socket.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        sendButton.addActionListener(new ActionListener() { //SEND BUTTON
            @Override
            public void actionPerformed(ActionEvent e) {
                   sendJsonToServer(msgTextField.getText());
                   msgTextField.setText("");
            }
        });
    }
    private JSONObject createJson(String message){
        JSONObject jsonObj = new JSONObject();
        DateTimeFormatter date = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");
        jsonObj.put("date", date.format(LocalDateTime.now()));
        jsonObj.put("time", time.format(LocalDateTime.now()));
        jsonObj.put("username", username);
        jsonObj.put("message", message);

        return jsonObj;
    }

    private void sendJsonToServer(String message){
        try {
            JSONObject jsonObj = createJson(message);
            byte[] buf = jsonObj.toString().getBytes("UTF-8");
            DatagramPacket packet = new DatagramPacket(buf, buf.length, group, port);
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("- Multicast Chat -");
        frame.setContentPane(new Client().clientPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setBounds(450, 175, 600, 500);
        frame.setVisible(true);
    }
}
