package killergame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler implements Runnable {

    //attr
    private KillerGame kg;
    private Socket s;
    private String ipClient;
    private PrintWriter out;
    //recive data from user
    private BufferedReader in;
    private boolean succes;

    //cons
    public ConnectionHandler(KillerGame kg, Socket s) {
        this.kg = kg;
        this.s = s;

        //returns the ip
        //socket has all the info for the conection (link of devices)
        this.ipClient = this.s.getInetAddress().getHostAddress();
        this.succes = false;
    }

    private void contact() {

        try {
            this.in = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
            this.out = new PrintWriter(this.s.getOutputStream(), true);
            System.out.println("Connected to [" + this.ipClient + "].");
        } catch (IOException error) {
            System.out.println("Fail to connected to [" + this.ipClient + "].");
        }

    }

    private String getText() { //gets the in
        String text = null;
        try {
            text = this.in.readLine();//readLine reads the las line
            if (text != null) {
                System.out.println(text);
                this.succes = true;
            }
        } catch (IOException e) {
            //System.out.println("Failed to get getText");
        }
        return text;
    }

    private boolean processCommand(String text) {//proces the text to know if it is valid.
        //System.out.println(text);
        //TODO 
        boolean treated = false;
        String[] parts = text.split("%");

        if (parts[0].equals("connection")) {
            System.out.println("he connection");
            if (parts[1].equals("right")) {
                this.kg.getVhpm().setSocket(s);
                treated = true;
                System.out.println("he recibido right");

            } else if (parts[1].equals("left")) {
                this.kg.getVhnm().setSocket(s);
                treated = true;
                System.out.println("he recibido left");

            } else if (parts[1].equals("controller")) {
                treated = true;
                
                this.kg.newKillerPad(this.s,parts[2]);
            }
        }
        return treated;
    }

    private void sendCommand(String command) {
        try {
            System.out.println("Sending comand: " + command + " to " + this.ipClient + "");
            out.println(command);
        } catch (Exception error) {

        }
    }

    private void closeConnection() {
        try {
            this.sendCommand("bye");
            this.s.close();
            System.out.println("Disconnected from " + this.ipClient + ".");
        } catch (IOException error) {

        }
    }

    @Override
    public void run() {
        this.contact();
        
        String line = "";

        while (!succes) {
            line = this.getText();
        }
        
        if(!this.processCommand(line)){
            this.closeConnection();
        }

    }
}
