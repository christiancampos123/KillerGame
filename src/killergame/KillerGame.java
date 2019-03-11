package killergame;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.geom.Area;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JFrame;

public class KillerGame extends JFrame {

    //Attr
    private KillerServer s;
    private VisualHandler vhpm; //for previous screen
    private VisualHandler vhnm; //for next screen
    private ArrayList<KillerPad> pads; //for pads
    private Boolean serverCreated;
    private ArrayList<VisualObject> objects;
    private Viewer viewer;
    private ControllPanel cp;
    private Musica m;

    //Const
    public KillerGame() {
        //creates the array of pads
        this.pads = new ArrayList();

        //array
        this.objects = new ArrayList();

        //It starts on false
        this.serverCreated = false;

        //show window
        this.Window();

        //Canvas
        this.newViewer();

        //controll Window
        this.newControllPanel();

        // new server
        this.newKillerServer(30303);

        //new nexmod
        this.newNModule();

        //new premod
        this.newPModulr();

        //musica
    }

    //Meth
    public void newKillerBullet(int posX, int posY, Color color, int velX, int velY) {
        KillerBullet bullet = new KillerBullet(posX, posY, "bul", this, color, velX, velY);
        new Thread(bullet).start();
        objects.add(bullet);
        this.m.shot();
    }

    public void newKillerShip(int posX, int posY, String type, KillerGame kg, Color color, int velX, int velY, String id) {
        KillerShip ship = new KillerShip(posX, posY, "shi", this, color, velX, velY, id);
        new Thread(ship).start();
        objects.add(ship);
        //ship.shotDownLeft();
        //ship.shotRight();
        //ship.shotDown();
        //ship.shotLeft();
        //ship.shotUp();
        //ship.shotDownLeft();
        //ship.shotDownRight();
        //ship.shotUpLeft();
        //ship.shotUpRight();

    }

    public boolean checkCollision(VisualObject object) {

        return KillerRules.testCollision(object, this.objects);

    }

    public VisualHandler getVhpm() {
        return vhpm;
    }

    public VisualHandler getVhnm() {
        return vhnm;
    }

    public void Window() {
        this.setSize(1300, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new GridLayout());
        this.setResizable(false);
        this.setVisible(true);
    }

    public ArrayList<VisualObject> getObjects() {
        return this.objects;
    }

    public int getViewerW() {
        return this.viewer.getWidth();
    }

    public int getViewerH() {
        return this.viewer.getHeight();
    }

    //MethCreate
    public void newKillerServer(int port) {

        if (serverCreated == false) {
            this.s = new KillerServer(this, port);
            new Thread(this.s).start(); //create the thread and 
            serverCreated = true;
        }

    }

    public void newNModule() {
        this.vhnm = new VisualHandler("right", this);
        new Thread(this.vhnm).start();//thread
    }

    public void newPModulr() {
        this.vhpm = new VisualHandler("left", this);
        new Thread(this.vhpm).start();//thread
    }

    public void newKillerPad(Socket s, String colorsR) {

        KillerPad pad = new KillerPad("controller", this, s);
        this.pads.add(pad);
        new Thread(pad).start();
        int a;
        int b;
        int c;
        Color col = Color.WHITE;

        try {
            a = Integer.parseInt(colorsR);

            if (a == 1) {
                col = Color.RED;
            }
            if (a == 2) {
                col = Color.BLUE;
            }
            if (a == 3) {
                col = Color.GREEN;
            }
            if (a == 4) {
                col = Color.LIGHT_GRAY;
            }
            if (a == 5) {
                col = Color.DARK_GRAY;
            }
            if (a == 6) {
                col = Color.WHITE;
            }

        } catch (Exception e) {
            col = Color.WHITE;
        }

        //kg.newKillerShip(500, 150,"shi",kg,Color.WHITE, -1, 0,"1")
        this.newKillerShip(this.viewer.getWidth() / 4, this.viewer.getHeight() / 4, "shi", this, col, 0, 0, pad.getHost());//new killerShip

    }

    public void newViewer() {
        this.viewer = new Viewer(this);
        new Thread(this.viewer).start();
        this.add(this.viewer, 0, 0);
    }

    public Viewer getViewer() {
        return viewer;
    }

    public ArrayList<KillerPad> getPads() {
        return pads;
    }

    public KillerServer getServer() {
        return s;
    }

    public VisualHandler getPrevModule() {
        return this.vhpm;
    }

    public VisualHandler getNextModule() {
        return this.vhnm;
    }

    public ControllPanel getControllPanel() {
        return cp;
    }

    public void newControllPanel() {
        this.cp = new ControllPanel(this);
    }

    public String sendObject(VisualObject o) {
        String info = null;

        if (o.getType() == "bul") {
            info = Integer.toString(o.getPosY()) + "%"//0
                    + Integer.toString(o.getColor().getRGB()) + "%"; //parse color to string //1
        }
        if (o instanceof KillerBullet) {
            KillerBullet s = (KillerBullet) o;
            info = info + s.getVelX();//2
            info = info + "%" + s.getVelY();//3
            info = info + "%bullet%r%r%r";//4567
            System.out.println("me la cargueeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            //this.objects.remove(o);

        }

        if (o.getType() == "shi") {

            info = null;
            info = Integer.toString(o.getPosX()) + "%"//0
                    + Integer.toString(o.getPosY()) + "%"//1
                    + o.getType() + "%"//2
                    + Integer.toString(o.getColor().getRGB()) + "%";//3
            //return true;
        }
        if (o instanceof KillerShip) {
            KillerShip s = (KillerShip) o;
            info = info + s.getID();//4
            info = info + "%" + s.getVelX();//5
            info = info + "%" + s.getVelY();//6
            info = info + "%ship";//7
            //this.objects.remove(o);

        }

        return info;

    }

    public Boolean sendObjectToN(VisualObject o) {
        String info = this.sendObject(o);
        //System.out.println(this.vhnm.connected());

        if (this.vhnm.connected() == true) {
            System.out.println("entro ObjToN");

            if (o.getType() == "bul") {
                //info = this.getColor() + Integer.toString(this.getPosX())+ Integer.toString(this.getPosY()) + this.getType();
                System.out.println(info);
                this.vhnm.sendCommand(info);
                //this.vhnm.processBullet(info);
                //todos atributos 
                System.out.println("entro3");
                return true;
            }

            if (o.getType() == "shi") {
                //info = this.getColor() + Integer.toString(this.getPosX())+ Integer.toString(this.getPosY()) + this.getType();
                System.out.println(info);
                this.vhnm.sendCommand(info);
                //this.vhnm.processShip(info);
                return true;
            }
        }

        return false;

    }

    public Boolean sendObjectToP(VisualObject o) {
        String info = this.sendObject(o);
        if (this.vhpm.connected() == true) {
            System.out.println("entro ObjToP");

            if (o.getType() == "bul") {
                //info = this.getColor() + Integer.toString(this.getPosX())+ Integer.toString(this.getPosY()) + this.getType();
                System.out.println(info);
                this.vhpm.sendCommand(info);
                //this.vhnm.processBullet(info);
                //todos atributos 
                return true;
            }

            if (o.getType() == "shi") {
                System.out.println("entro ObjToP apartado ship");
                //info = this.getColor() + Integer.toString(this.getPosX())+ Integer.toString(this.getPosY()) + this.getType();
                System.out.println(info);
                this.vhpm.sendCommand(info);
                //this.vhpm.processShip(info);
                return true;

            }
        }

        return false;
    }

    //Main
    public static void main(String[] args) {
        KillerGame kg = new KillerGame();

        //kg.getVhnm().setHost("25.83.227.191");
        //kg.getVhnm().setPort(30303);
        //kg.getVhpm().setHost("25.83.227.191");
        //kg.getVhpm().setPort(30303);
        //KillerBullet bullet1 = new KillerBullet(10, 10, "BUL", kg, Color.WHITE, 0, 0);
        //KillerBullet bullet2 = new KillerBullet(12, 12, "BUL", kg, Color.WHITE, 0, 0);
        //Area area1 = new Area(bullet1.getShape());
        //Area area2 = new Area(bullet2.getShape());
        //area1.intersect(area2);
        /**
         * if (!area1.isEmpty()) { System.out.println("Se tocan."); } else {
         * System.out.println("No se tocan"); }
         *
         */
        //Color c = new Color(50, 10, 150);
        //System.out.println(c);
        //String m = Integer.toString(c.getRGB());
        //Color c2 = new Color((Integer.parseInt(m)));
        //kg.newKillerShip(1050, 50, "shi", kg, Color.RED, -1, 0, "1");
        //kg.newKillerBullet(500, 500, Color.WHITE, +1, -1);
        //kg.newKillerShip(500, 150,"shi",kg,Color.WHITE, 1, -2,"1");
    }
    //h

}//KillerGame acabado en principio Christian Campos ahora si
