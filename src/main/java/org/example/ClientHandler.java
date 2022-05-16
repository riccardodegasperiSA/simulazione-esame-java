package org.example;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class ClientHandler extends Thread{

    private ServerSocket serverSocket;

    private Socket clientSocket = null;

    private BufferedReader in = null;

    private PrintWriter out = null;

    private String userName;

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public String getUserName() {
        return userName;
    }

    public ClientHandler(ServerSocket serverSocket, Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.serverSocket = serverSocket;

        creaOut();

        creaIn();

    }

    public void run(){

            comunica();

    }

    private void comunica() {

        String s;

        try {

            while ((s = in.readLine()) != null) {
                out.println(process(s));

            }
        } catch (IOException e) {
            System.err.println("Connection failed " + e);
        }
        finally {
            App.connected.remove(this);
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void creaOut() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void creaIn() {
        try {
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.err.println("accept failed "+ e);
        }
    }

    private String process(String s) {


//        String result;

        switch (s) {
            case "hottest": {
                return hottest(App.cities);
            }
            case "all": {
                return every(App.cities);
            }
            case "sorted_by_name": {
                return sortByName(App.cities);
            }
            case "sorted_by_temp": {
                return sortByTemp(App.cities);
            }
            default: {
                return "not a command";
            }
        }

    }

    private String sortByName(ArrayList<City> cities) {
        ArrayList<City> sortedCities = new ArrayList<>();

        String [] nameArray = new String[cities.size()];
        int i = 0;

        for (City c : cities) {
            nameArray[i] = c.name;
            i++;
        }
        Arrays.sort(nameArray);

        for (i = 0; i < nameArray.length; i++) {
            for (City c : cities) {
                if (c.name == nameArray[i]){
                    sortedCities.add(c);
                    break;
                }
            }
//            cities.remove(sortedCities.get(i));
        }

        Gson gson = new Gson();
        String jsonString = new String();

        jsonString = gson.toJson(sortedCities);

        return jsonString;
    }

    private String sortByTemp(ArrayList<City> cities) {

        ArrayList<City> sortedCities = new ArrayList<>();
        double [] tempArray = new double[cities.size()];
        int i = 0;

        for (City c : cities) {
            tempArray[i] = c.temp;
            i++;
        }
        Arrays.sort(tempArray);

        for (i = 0; i < tempArray.length; i++) {
            for (City c : cities) {
                if (c.temp == tempArray[i]){
                    sortedCities.add(c);
                    break;
                }
            }
//            cities.remove(cities.get(i));
        }

        Gson gson = new Gson();
        String jsonString = new String();

        jsonString = gson.toJson(sortedCities);

        return jsonString;
    }

    private String every(ArrayList<City> cities) {
        Gson gson = new Gson();
        String jsonString = new String();

        jsonString = gson.toJson(cities);

        return jsonString;

    }

    private String hottest(ArrayList<City> cities) {
        double maxtemp = -100;
        for (City c : cities) {
            if (c.temp > maxtemp) {
                maxtemp = c.temp;
            }
        }

        Gson gson = new Gson();
        String jsonString = new String();

        for (City c : cities){
            if (c.temp == maxtemp){
                jsonString = gson.toJson(c);
                break;
            }
        }

        return jsonString;
    }

}
