package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.ArrayList;

public class App {

    static ArrayList<City> cities = new ArrayList<City>();

    private static ServerSocket serverSocket = null;

    private static Socket clientSocket = null;

    public static ArrayList<ClientHandler> connected = new ArrayList<>();

    public static void main(String[] args) {

        String hostname = "127.0.0.1";
        int portNumber = 1234;

        if (args.length > 0) {
            hostName = args[0];
            portNumber = Integer.parseInt(args[1]);
        }




        creaServerSocket(portNumber);

        System.out.println("Server Started Hostname: " + hostName + " " + Instant.now());

        buildCitiesList();

        while (true){
            ciclo(portNumber, hostName);
        }

    }

    private static void ciclo(int portNumber, String hostName) {

        connettiClient();

        connected.add(new ClientHandler(serverSocket, clientSocket));

        connected.get(connected.size()-1).start();

        int count = connected.size();

    }

    private static void connettiClient() {
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("accept failed " + e);
        }
    }

    private static void creaServerSocket(int portNumber) {

        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.err.println("reader failed "+ e);
        }

    }

    static void buildCitiesList() {
        cities.add(new City(3,"Toronto",15.9));
        cities.add(new City(33,"Milan",25.94));
        cities.add(new City(55,"Rome",35.4));
        System.out.println(cities);
    }

}