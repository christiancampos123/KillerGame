package killergame;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VisualHandler implements Runnable {

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
    public VisualHandler(String type, KillerGame kg) {
        this.client = new KillerClient(this, 2000);
        this.type = type;
        this.kg = kg;
        this.isConnected = false;
        System.out.println("visual h false");
        //programa tiene (servidor mirando si conectan) si conectan pasa al cHandler y recive mensaje
        //si el msj es correcto crea un visual module. Este crea un Client handler que se conecta con la ip y puerto pasados

        new Thread(this.client).start();//thread client
    }

    //When init without ip and port
    public VisualHandler(String type) {
        this.client = new KillerClient(this, 2000);
        new Thread(this.client).start();
        this.type = type;
    }

    //Meth
    public void contact() {

        //borramos el socket si hay
        try {
            this.s.close(); //if there is shocket it closes first
        } catch (IOException error) {
            //no shocket
        } catch (NullPointerException e) {
            //no socket
        }

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

    public void processShip(String s) {
        //int posX, int posY, String type, KillerGame kg, Color color, int velX, int velY, String id
        int x = 0;

        if (this.type == "left") {
            x = 0;

        }
        if (this.type == "right") {
            x = this.kg.getViewerW();
        }

        String[] parts = s.split("%");
        Color c = new Color(Integer.parseInt(parts[3]));
        this.kg.newKillerShip(x,
                Integer.parseInt(parts[1]),
                parts[2],
                this.kg,
                c,
                Integer.parseInt(parts[5]),
                Integer.parseInt(parts[6]),
                parts[4]);

        //System.out.println(Integer.parseInt(parts[5]));
    }

    public void processBullet(String s) {
        //int posX, int posY, int velX, int velY, Color color
        int x = 0;

        if (this.type == "left") {
            x = 0;

        }
        if (this.type == "right") {
            x = this.kg.getViewerW();
        }

        String[] parts = s.split("%");
        Color c = new Color(Integer.parseInt(parts[1]));
        this.kg.newKillerBullet(x, Integer.parseInt(parts[0]), c, Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        System.out.println("creo bola");

        //System.out.println(Integer.parseInt(parts[5]));
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

    private void processCommand(String text) {//proces the text to know if it is valid.
        //System.out.println(text);
        //TODO 
        boolean end = false;
        if (text == null) {
            text = "";
        }

        try {
            String[] parts = text.split("%");
            System.out.println(parts[0]);

            //recibo ship
            if (parts[7].equals("ship")) {
                System.out.println("ship recived");
                this.processShip(text);
            }
            //recibe bullet
            if (parts[4].equals("bullet")) {
                System.out.println("bullet recived");
                this.processBullet(text);
            }

            if (parts[0].equals("pmv")) {
                System.out.println(parts[1]);
                System.out.println(parts[2]);

                this.processPadM(parts[1], parts[2]);

            }

            if (parts[0].equals("pst")) {
                System.out.println("disparo en otra pantalla");
                System.out.println(parts[1]);
                System.out.println(parts[2]);
                System.out.println("disparo en otra pantalla");

                this.processPadS(parts[1], parts[2]);
            }
            
            if (parts[0].equals("no")) {

                this.processStop(parts[1], parts[2]);
            }
            
            if (parts[0].equals("alive")) {

                this.processAliveness(parts[1]);
            }
            

            if (parts[0].equals("bye")) {
                System.out.println("bye profundo");
                try {
                    this.s.close();//close when I recived a bye
                    this.s = null;
                    this.isConnected = false;
                    System.out.println("processComand false");
                    System.out.println("bye recived");
                } catch (IOException ex) {

                }
                System.out.println("");
            }
        } catch (Exception e) {

        }

    }
    
    private void processAliveness(String text){
        
        boolean succes = false;
        for (int i = 0; i < this.kg.getPads().size(); i++) {
            KillerPad pad = this.kg.getPads().get(i);
            if (pad.getHost().equals(text)) {
                pad.sendCommand("bye");
                succes = true;
            }
        }

        if (!succes) {
            this.sendCommand("alive%" + text + "%0%0%0%0%0%0%0");
      
        }
    }

    private void processPadM(String text1, String text2) {

        try {
            text1 = text1.toLowerCase();
            text2 = text2.toLowerCase();
        } catch (Exception error) {
            System.out.println("Ignoring command from [" + this.host + "].");

        }

        if (text1.equals("uu")) {
            System.out.println("Precesing command MOV from [" + this.host + "]...");
            this.processMove(text1, text2);
        }

        if (text1.equals("dd")) {
            System.out.println("Precesing command MOV from [" + this.host + "]...");
            this.processMove(text1, text2);
        }

        if (text1.equals("rr")) {
            System.out.println("Precesing command MOV from [" + this.host + "]...");
            this.processMove(text1, text2);
        }

        if (text1.equals("ll")) {
            System.out.println("Precesing command MOV from [" + this.host + "]...");
            this.processMove(text1, text2);
        }

        if (text1.equals("ur")) {
            System.out.println("Precesing command MOV from [" + this.host + "]...");
            this.processMove(text1, text2);
        }

        if (text1.equals("ul")) {
            System.out.println("Precesing command MOV from [" + this.host + "]...");
            this.processMove(text1, text2);
        }

        if (text1.equals("dr")) {
            System.out.println("Precesing command MOV from [" + this.host + "]...");
            this.processMove(text1, text2);
        }

        if (text1.equals("dl")) {
            System.out.println("Precesing command MOV from [" + this.host + "]...");
            this.processMove(text1, text2);
        }

    }

    private void processPadS(String text1, String text2) {

        try {
            text1 = text1.toLowerCase();
        } catch (Exception error) {
            System.out.println("Ignoring command from [" + this.host + "].");

        }

        if (text1.equals("uu")) {
            System.out.println("Precesing command MOV from [" + this.host + "]...");
            this.processShot(text1, text2);
        }

        if (text1.equals("dd")) {
            System.out.println("Precesing command MOV from [" + this.host + "]...");
            this.processShot(text1, text2);
        }

        if (text1.equals("rr")) {
            System.out.println("Precesing command MOV from [" + this.host + "]...");
            this.processShot(text1, text2);
        }

        if (text1.equals("ll")) {
            System.out.println("Precesing command MOV from [" + this.host + "]...");
            this.processShot(text1, text2);
        }

        if (text1.equals("ur")) {
            System.out.println("Precesing command MOV from [" + this.host + "]...");
            this.processShot(text1, text2);
        }

        if (text1.equals("ul")) {
            System.out.println("Precesing command MOV from [" + this.host + "]...");
            this.processShot(text1, text2);
        }

        if (text1.equals("dr")) {
            System.out.println("Precesing command MOV from [" + this.host + "]...");
            this.processShot(text1, text2);
        }

        if (text1.equals("dl")) {
            System.out.println("Precesing command MOV from [" + this.host + "]...");
            this.processShot(text1, text2);
        }

    }

    private void processStop(String text1, String text2) {
        boolean entered = false;
        String command = "00";
        
        try {
            command = text1.toLowerCase();
        } catch (Exception error) {
            System.out.println("Failed command");
            entered = true;
        }

        if (!entered && command.equals("nope")) {
            
            System.out.println("Precesing command uu from [" + this.host + "]...");
            if (!KillerAction.mNope(text2, this.kg.getObjects())) {
                this.sendCommand("pmv%" + command + "%" + text2 + "%0%0%0%0%0");

            }
            //KillerAction.mU(this.host, this.kg.getObjects());
            entered = true;
        }

    }

    private void processMove(String text1, String text2) {
        System.out.println(text1);
        System.out.println(text2);

        boolean entered = false;
        String command = "00";

        try {
            command = text1.toLowerCase();
        } catch (Exception error) {
            System.out.println("Failed command");
            entered = true;
        }

        if (!entered && command.equals("uu")) {
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + "pmv%" + command + "%" + text2);
            System.out.println("Precesing command uu from [" + this.host + "]...");
            if (!KillerAction.mU(text2, this.kg.getObjects())) {
                this.sendCommand("pmv%" + command + "%" + text2 + "%0%0%0%0%0");

            }
            //KillerAction.mU(this.host, this.kg.getObjects());
            entered = true;
        }

        if (!entered && command.equals("dd")) {
            System.out.println("Precesing command dd from [" + this.host + "]...");
            if (!KillerAction.mD(text2, this.kg.getObjects())) {
                this.sendCommand("pmv%" + command + "%" + text2 + "%0%0%0%0%0");

            }
            //KillerAction.mD(this.host, this.kg.getObjects());
            entered = true;
        }

        if (!entered && command.equals("rr")) {
            System.out.println("Precesing command rr from [" + this.host + "]...");
            if (!KillerAction.mR(text2, this.kg.getObjects())) {
                this.sendCommand("pmv%" + command + "%" + text2 + "%0%0%0%0%0");
            }
            //KillerAction.mR(this.host, this.kg.getObjects());
            entered = true;
        }

        if (!entered && command.equals("ll")) {
            System.out.println("Precesing command ll from [" + this.host + "]...");
            if (!KillerAction.mL(text2, this.kg.getObjects())) {
                this.sendCommand("pmv%" + command + "%" + text2 + "%0%0%0%0%0");
            }
            //KillerAction.mL(this.host, this.kg.getObjects());
            entered = true;
        }

        if (!entered && command.equals("ur")) {
            System.out.println("Precesing command ur from [" + this.host + "]...");
            if (!KillerAction.mUR(text2, this.kg.getObjects())) {
                this.sendCommand("pmv%" + command + "%" + text2 + "%0%0%0%0%0");
            }
            //KillerAction.mUR(this.host, this.kg.getObjects());
            entered = true;
        }

        if (!entered && command.equals("ul")) {
            System.out.println("Precesing command ul from [" + this.host + "]...");
            if (!KillerAction.mUL(text2, this.kg.getObjects())) {
                this.sendCommand("pmv%" + command + "%" + text2 + "%0%0%0%0%0");
            }
            //KillerAction.mUL(this.host, this.kg.getObjects());
            entered = true;
        }

        if (!entered && command.equals("dr")) {
            System.out.println("Precesing command dr from [" + this.host + "]...");
            if (!KillerAction.mDR(text2, this.kg.getObjects())) {
                this.sendCommand("pmv%" + command + "%" + text2 + "%0%0%0%0%0");
            }
            //KillerAction.mDR(this.host, this.kg.getObjects());
            entered = true;
        }

        if (!entered && command.equals("dl")) {
            System.out.println("Precesing command dl from [" + this.host + "]...");
            if (!KillerAction.mDL(text2, this.kg.getObjects())) {
                this.sendCommand("pmv%" + command + "%" + text2 + "%0%0%0%0%0");
            }
            //KillerAction.mDL(this.host, this.kg.getObjects());
            entered = true;
        }

        if (!entered) {
            System.out.println("Incompatible");
        }

    }

    private void processShot(String text1, String text2) {

        boolean entered = false;
        String command = "00";

        try {
            command = text1.toLowerCase();
        } catch (Exception error) {
            System.out.println("Failed command");
            entered = true;
        }

        if (!entered && command.equals("uu")) {
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + "pmv%" + command + "%" + text2);
            System.out.println("Precesing command uu from [" + this.host + "]...");
            if (!KillerAction.sU(text2, this.kg.getObjects())) {
                this.sendCommand("pst%" + command + "%" + text2 + "%0%0%0%0%0");

            }
            //KillerAction.mU(this.host, this.kg.getObjects());
            entered = true;
        }

        if (!entered && command.equals("dd")) {
            System.out.println("Precesing command dd from [" + this.host + "]...");
            if (!KillerAction.sD(text2, this.kg.getObjects())) {
                this.sendCommand("pst%" + command + "%" + text2 + "%0%0%0%0%0");

            }
            //KillerAction.mD(this.host, this.kg.getObjects());
            entered = true;
        }

        if (!entered && command.equals("rr")) {
            System.out.println("Precesing command rr from [" + this.host + "]...");
            if (!KillerAction.sR(text2, this.kg.getObjects())) {
                this.sendCommand("pst%" + command + "%" + text2 + "%0%0%0%0%0");

            }
            //KillerAction.mR(this.host, this.kg.getObjects());
            entered = true;
        }

        if (!entered && command.equals("ll")) {
            System.out.println("Precesing command ll from [" + this.host + "]...");
            if (!KillerAction.sL(text2, this.kg.getObjects())) {
                this.sendCommand("pst%" + command + "%" + text2 + "%0%0%0%0%0");

            }
            //KillerAction.mL(this.host, this.kg.getObjects());
            entered = true;
        }

        if (!entered && command.equals("ur")) {
            System.out.println("Precesing command ur from [" + this.host + "]...");
            if (!KillerAction.sUR(text2, this.kg.getObjects())) {
                this.sendCommand("pst%" + command + "%" + text2 + "%0%0%0%0%0");

            }
            //KillerAction.mUR(this.host, this.kg.getObjects());
            entered = true;
        }

        if (!entered && command.equals("ul")) {
            System.out.println("Precesing command ul from [" + this.host + "]...");
            if (!KillerAction.sUL(text2, this.kg.getObjects())) {
                this.sendCommand("pst%" + command + "%" + text2 + "%0%0%0%0%0");

            }
            //KillerAction.mUL(this.host, this.kg.getObjects());
            entered = true;
        }

        if (!entered && command.equals("dr")) {
            System.out.println("Precesing command dr from [" + this.host + "]...");
            if (!KillerAction.sDR(text2, this.kg.getObjects())) {
                this.sendCommand("pst%" + command + "%" + text2 + "%0%0%0%0%0");

            }
            //KillerAction.mDR(this.host, this.kg.getObjects());
            entered = true;
        }

        if (!entered && command.equals("dl")) {
            System.out.println("Precesing command dl from [" + this.host + "]...");
            if (!KillerAction.sDL(text2, this.kg.getObjects())) {
                this.sendCommand("pst%" + command + "%" + text2 + "%0%0%0%0%0");

            }
            //KillerAction.mDL(this.host, this.kg.getObjects());
            entered = true;
        }

        if (!entered) {
            System.out.println("Incompatible");
        }

    }

    @Override
    public void run() {
        while (true) {
            this.processCommand(this.getText());
        }
    }

}
