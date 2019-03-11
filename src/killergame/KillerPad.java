package killergame;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KillerPad implements Runnable {

    //Attr
    private String host;
    private int port;
    private PrintWriter out;
    //recive data from user
    private BufferedReader in;
    private Socket s;
    private KillerClient client;
    //control right or left
    private String type;
    private boolean isConnected;
    private KillerGame kg;

    //Const
    //with ip and port when someone joins
    public KillerPad(String type, KillerGame kg, Socket s) {
        this.client = new KillerClient(this, 2000);
        this.type = type;
        this.kg = kg;
        this.isConnected = false;
        System.out.println("visual h false");
        this.setSocket(s);
        //programa tiene (servidor mirando si conectan) si conectan pasa al cHandler y recive mensaje
        //si el msj es correcto crea un visual module. Este crea un Client handler que se conecta con la ip y puerto pasados
        new Thread(this.client).start();//thread client
    }

    //When init without ip and port
    public KillerPad(String type) {
        this.client = new KillerClient(this, 2000);
        new Thread(this.client).start();
        this.type = type;
    }

    //Meth
    public void contact() {

        //borramos el socket si hay
        try {
            this.s.close(); //if there is shocket it closes first
        } catch (IOException | NullPointerException error) {
            //no shocket
        }
        //no socket

        this.s = null; //aseguramos que se quede en null

        try {
            this.s = new Socket(host, port);
            this.in = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
            this.out = new PrintWriter(this.s.getOutputStream(), true);
            System.out.println("Connected to [" + this.host + "].");
            this.isConnected = true; //si se conecta es true
            System.out.println("contact true");
        } catch (IOException error) {
            System.out.println("Fail to connected to [" + this.host + "].");
            this.isConnected = false;
            System.out.println("contact false");
        }

        this.sendCommand("connection%" + type); //sends connection&left or connection&right
    }

    public void processFCommand(String s) {
        boolean entered = false;
        String command = "000";

        try {
            command = s.toLowerCase().substring(0, 2);
        } catch (Exception error) {
            System.out.println("Failed command");
            entered = true;
        }

        if (!entered && command.equals("mv")) {//move
            System.out.println("Procesing command[" + this.host + "]...");
            this.processMovement(s.substring(2));
            entered = true;
        }

        if (!entered && command.equals("no")) {//stop
            System.out.println("Procesing command[" + this.host + "]...");
            this.processNope(s.substring(2));
            entered = true;
        }

        if (!entered && command.equals("st")) {//shot
            System.out.println("Procesing command DOR[" + this.host + "]...");
            this.processShot(s.substring(2));
            entered = true;
        }

        if (!entered && command.equals("by")) {
            System.out.println("Procesing command DOR[" + this.host + "]...");
            this.processBye();
            entered = true;
        }

        if (!entered) {
            System.out.println("The movement direction is uncompatible with the game.");
        }

    }

    public void processNope(String s) {
        boolean entered = false;
        String command = "00";

        try {
            command = s.toLowerCase();
        } catch (Exception error) {
            System.out.println("Failed command");
            entered = true;
        }

        if (!entered && command.equals("nope")) {
            System.out.println("Precesing command uu from [" + this.host + "]...");
            if (!KillerAction.mNope(this.host, this.kg.getObjects())) {
                this.kg.getNextModule().sendCommand("no%" + command + "%" + this.host + "%0%0%0%0%0");
                this.kg.getPrevModule().sendCommand("no%" + command + "%" + this.host + "%0%0%0%0%0");
            }
            //KillerAction.mU(this.host, this.kg.getObjects());
            entered = true;
        }
    }

    public void processBye() {

        try {
            this.s.close();
            this.s = null;
            System.out.println("Disconnected from [" + this.host + "].");
            this.isConnected = false;
        } catch (IOException error) {

        }

    }

    public void processMovement(String s) {
        boolean entered = false;
        String command = "00";

        try {
            command = s.toLowerCase();
        } catch (Exception error) {
            System.out.println("Failed command");
            entered = true;
        }

        if (!entered && command.equals("uu")) {
            System.out.println("Precesing command uu from [" + this.host + "]...");
            System.out.println("sending command");
            if (KillerAction.mU(this.host, this.kg.getObjects()) == false) {
                System.out.println("pmv%" + command + "%" + this.host);
                this.kg.getNextModule().sendCommand("pmv%" + command + "%" + this.host + "%0%0%0%0%0");
                this.kg.getPrevModule().sendCommand("pmv%" + command + "%" + this.host + "%0%0%0%0%0");
            }
            //KillerAction.mU(this.host, this.kg.getObjects());
            entered = true;
        }

        if (!entered && command.equals("dd")) {
            System.out.println("Precesing command dd from [" + this.host + "]...");
            if (!KillerAction.mD(this.host, this.kg.getObjects())) {
                this.kg.getNextModule().sendCommand("pmv%" + command + "%" + this.host + "%0%0%0%0%0");
                this.kg.getPrevModule().sendCommand("pmv%" + command + "%" + this.host + "%0%0%0%0%0");
            }
            //KillerAction.mD(this.host, this.kg.getObjects());
            entered = true;
        }

        if (!entered && command.equals("rr")) {
            System.out.println("Precesing command rr from [" + this.host + "]...");
            if (!KillerAction.mR(this.host, this.kg.getObjects())) {
                this.kg.getNextModule().sendCommand("pmv%" + command + "%" + this.host + "%0%0%0%0%0");
                this.kg.getPrevModule().sendCommand("pmv%" + command + "%" + this.host + "%0%0%0%0%0");
            }
            //KillerAction.mR(this.host, this.kg.getObjects());
            entered = true;
        }

        if (!entered && command.equals("ll")) {
            System.out.println("Precesing command ll from [" + this.host + "]...");
            if (!KillerAction.mL(this.host, this.kg.getObjects())) {
                this.kg.getNextModule().sendCommand("pmv%" + command + "%" + this.host + "%0%0%0%0%0");
                this.kg.getPrevModule().sendCommand("pmv%" + command + "%" + this.host + "%0%0%0%0%0");
            }
            //KillerAction.mL(this.host, this.kg.getObjects());
            entered = true;
        }

        if (!entered && command.equals("ur")) {
            System.out.println("Precesing command ur from [" + this.host + "]...");
            if (!KillerAction.mUR(this.host, this.kg.getObjects())) {
                this.kg.getNextModule().sendCommand("pmv%" + command + "%" + this.host + "%0%0%0%0%0");
                this.kg.getPrevModule().sendCommand("pmv%" + command + "%" + this.host + "%0%0%0%0%0");
            }
            //KillerAction.mUR(this.host, this.kg.getObjects());
            entered = true;
        }

        if (!entered && command.equals("ul")) {
            System.out.println("Precesing command ul from [" + this.host + "]...");
            if (!KillerAction.mUL(this.host, this.kg.getObjects())) {
                this.kg.getNextModule().sendCommand("pmv%" + command + "%" + this.host + "%0%0%0%0%0");
                this.kg.getPrevModule().sendCommand("pmv%" + command + "%" + this.host + "%0%0%0%0%0");
            }
            //KillerAction.mUL(this.host, this.kg.getObjects());
            entered = true;
        }

        if (!entered && command.equals("dr")) {
            System.out.println("Precesing command dr from [" + this.host + "]...");
            if (!KillerAction.mDR(this.host, this.kg.getObjects())) {
                this.kg.getNextModule().sendCommand("pmv%" + command + "%" + this.host + "%0%0%0%0%0");
                this.kg.getPrevModule().sendCommand("pmv%" + command + "%" + this.host + "%0%0%0%0%0");
            }
            //KillerAction.mDR(this.host, this.kg.getObjects());
            entered = true;
        }

        if (!entered && command.equals("dl")) {
            System.out.println("Precesing command dl from [" + this.host + "]...");
            if (!KillerAction.mDL(this.host, this.kg.getObjects())) {
                this.kg.getNextModule().sendCommand("pmv%" + command + "%" + this.host + "%0%0%0%0%0");
                this.kg.getPrevModule().sendCommand("pmv%" + command + "%" + this.host + "%0%0%0%0%0");
            }
            //KillerAction.mDL(this.host, this.kg.getObjects());
            entered = true;
        }

        if (!entered) {
            System.out.println("Incompatible");
        }

    }

    public void processShot(String s) {
        boolean entered = false;
        String command = "00";

        try {
            command = s.toLowerCase();
        } catch (Exception error) {
            System.out.println("Failed command");
            entered = true;
        }

        if (!entered && command.equals("uu")) {
            System.out.println("Precesing command UPP from [" + this.host + "]...");
            if (!KillerAction.sU(this.host, this.kg.getObjects())) {
                this.kg.getNextModule().sendCommand("pst%" + command + "%" + this.host + "%0%0%0%0%0");
                this.kg.getPrevModule().sendCommand("pst%" + command + "%" + this.host + "%0%0%0%0%0");
            }
            entered = true;
        }

        if (!entered && command.equals("dd")) {
            System.out.println("Precesing command UPP from [" + this.host + "]...");
            if (!KillerAction.sD(this.host, this.kg.getObjects())) {
                this.kg.getNextModule().sendCommand("pst%" + command + "%" + this.host + "%0%0%0%0%0");
                this.kg.getPrevModule().sendCommand("pst%" + command + "%" + this.host + "%0%0%0%0%0");
            }
            entered = true;
        }

        if (!entered && command.equals("rr")) {
            System.out.println("Precesing command UPP from [" + this.host + "]...");
            if (!KillerAction.sR(this.host, this.kg.getObjects())) {
                this.kg.getNextModule().sendCommand("pst%" + command + "%" + this.host + "%0%0%0%0%0");
                this.kg.getPrevModule().sendCommand("pst%" + command + "%" + this.host + "%0%0%0%0%0");
            }
            entered = true;
        }

        if (!entered && command.equals("ll")) {
            System.out.println("Precesing command UPP from [" + this.host + "]...");
            if (!KillerAction.sL(this.host, this.kg.getObjects())) {
                this.kg.getNextModule().sendCommand("pst%" + command + "%" + this.host + "%0%0%0%0%0");
                this.kg.getPrevModule().sendCommand("pst%" + command + "%" + this.host + "%0%0%0%0%0");
            }
            entered = true;
        }

        if (!entered && command.equals("ur")) {
            System.out.println("Precesing command UPP from [" + this.host + "]...");
            if (!KillerAction.sUR(this.host, this.kg.getObjects())) {
                this.kg.getNextModule().sendCommand("pst%" + command + "%" + this.host + "%0%0%0%0%0");
                this.kg.getPrevModule().sendCommand("pst%" + command + "%" + this.host + "%0%0%0%0%0");
            }
            entered = true;
        }

        if (!entered && command.equals("ul")) {
            System.out.println("Precesing command UPP from [" + this.host + "]...");
            if (!KillerAction.sUL(this.host, this.kg.getObjects())) {
                this.kg.getNextModule().sendCommand("pst%" + command + "%" + this.host + "%0%0%0%0%0");
                this.kg.getPrevModule().sendCommand("pst%" + command + "%" + this.host + "%0%0%0%0%0");
            }
            entered = true;
        }

        if (!entered && command.equals("dr")) {
            System.out.println("Precesing command UPP from [" + this.host + "]...");
            if (!KillerAction.sDR(this.host, this.kg.getObjects())) {
                this.kg.getNextModule().sendCommand("pst%" + command + "%" + this.host + "%0%0%0%0%0");
                this.kg.getPrevModule().sendCommand("pst%" + command + "%" + this.host + "%0%0%0%0%0");
            }
            entered = true;
        }

        if (!entered && command.equals("dl")) {
            System.out.println("Precesing command UPP from [" + this.host + "]...");
            if (!KillerAction.sDL(this.host, this.kg.getObjects())) {
                this.kg.getNextModule().sendCommand("pst%" + command + "%" + this.host + "%0%0%0%0%0");
                this.kg.getPrevModule().sendCommand("pst%" + command + "%" + this.host + "%0%0%0%0%0");
            }
            entered = true;
        }

        if (!entered) {
            System.out.println("Uncompatible");
        }

    }

    //Methods new
    public void newKillerClient() {
        //with ip and port it creates the connection

    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host; //ip
    }

    public void sendCommand(String command) {
        try {
            out.println(command);
        } catch (Exception e) {

        }
    }

    public void setSocket(Socket s) {
        this.s = s;
        this.host = s.getInetAddress().getHostAddress();
        this.port = s.getPort();

        try {
            this.in = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
            this.out = new PrintWriter(this.s.getOutputStream(), true);
            System.out.println("Connected to [" + this.host + "].");
            this.isConnected = true;
            System.out.println("shocket true");
        } catch (IOException error) {
            System.out.println("Fail to connected to [" + this.host + "].");
            this.isConnected = false;
            System.out.println("shocket false");
        }

    }

    public boolean connected() {
        System.out.println(this.isConnected);
        return this.isConnected;

    }

    public void listenClient() {

    }

    private String getText() { //gets the in
        String text = "";

        try {
            text = this.in.readLine();//readLine reads the las line
            if (text != null) {
                System.out.println("Line recived");
            }
        } catch (IOException e) {
            this.isConnected = false;
            System.out.println("getText false");
        } catch (NullPointerException e) {

        }
        return text;
    }

    @Override
    public void run() {
        while (true) {
            this.processFCommand(this.getText());
        }
    }

}
