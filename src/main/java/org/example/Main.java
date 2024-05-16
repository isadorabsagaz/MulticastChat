package org.example;

import java.util.Scanner;

public class Main {
    static Scanner input = new Scanner(System.in);
    public static void main(String[] args) {

        System.out.println("\nWelcome to the multicast chat!");
        System.out.println("To leave the chat, type <exit>");
        System.out.println("----------------------------------");
        System.out.print("\nType your username: ");
        String username = input.nextLine();

        System.out.print("Type the communication port: ");
        int port = Integer.parseInt(input.nextLine());

        System.out.print("Type the group IP \n(number in the range 224.0.0.1 to 239.255.255.255): ");
        String groupIp = input.nextLine();

        UdpClient client = new UdpClient(username, port, groupIp);
        client.multicastChat();
    }
}