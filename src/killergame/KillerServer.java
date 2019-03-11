package killergame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

//the kg will create puertoCita to listen clients, and when it is finished.
//the puertoCita is destroyed.
public class KillerServer implements Runnable {

    //Attr
    private KillerGame kg;
    private int port; //final to not modify
    private ServerSocket serverSocket;
    private boolean exit;

    //const
    public KillerServer(KillerGame kg, int port) {
        this.kg = kg;
        this.port = port;
        this.exit = false;
    }

    public void newConnectionHandler(Socket s) {

        ConnectionHandler ch = new ConnectionHandler(kg, s);

        //creates the thread
        new Thread(ch).start();

    }

    public void setPort(int port) {
        this.port = port;
        this.kg.getControllPanel().refreshPort(port);
        try {
            this.serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(KillerServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void waitForClient() {

        boolean portOpen = false;

        for (int i = 0; !portOpen; i++) { //if the port is not avaiable, it tries the next one
            try {
                this.serverSocket = new ServerSocket(this.port);
                
                this.kg.getControllPanel().refreshPort(this.port);
                
                //JOptionPane.showMessageDialog(null, "The opened port is "+ this.port);
                portOpen = true;
            } catch (IOException e) {
                this.port ++;
            }
            if(this.port>65000){
                this.port = 30303;
            }
        }

        System.out.println("Waiting for client...");

        try {
            Socket client = this.serverSocket.accept();
            this.newConnectionHandler(client);
        } catch (IOException error) {
        }
    }

    //method boolean
    @Override
    public void run() {
        while (exit == false) {
            this.waitForClient();

            try {
                this.serverSocket.close();
            } catch (IOException e) {

            }
        }

    }

}
