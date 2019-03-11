package killergame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KillerShip extends Controllable {

    //attr
    private final String ID; //sera la ip del mando
    private static final int RAD = 25; //radio nave
    private int counter = 0;
    private final int cd = 60;
    private int cdActual;

    public KillerShip(int posX, int posY, String type, KillerGame kg, Color color, int velX, int velY, String id) {
        super(posX, posY, type, kg, color, velX, velY);
        this.ID = id;
        this.setShape(this.createShape());
        this.counter = 0;
        this.cdActual = 0;
    }

    //meth
    public String getID() {
        return ID;
    }

    public void moveStop() {
        this.setVelY(0);
        this.setVelX(0);
    }

    public void moveUp() {
        this.setVelY(-2);
        this.setVelX(0);
    }

    public void moveDown() {
        this.setVelY(2);
        this.setVelX(0);
    }

    public void moveLeft() {
        this.setVelX(-2);
        this.setVelY(0);

    }

    public void moveRight() {
        this.setVelX(2);
        this.setVelY(0);
    }

    public void moveUpRight() {
        this.setVelY(-2);
        this.setVelX(2);
    }

    public void moveUpLeft() {
        this.setVelY(-2);
        this.setVelX(-2);
    }

    public void moveDownRight() {
        this.setVelY(2);
        this.setVelX(2);
    }

    public void moveDownLeft() {
        this.setVelY(2);
        this.setVelX(-2);
    }

    //shot
    /*
     public void shot() {
     int inc = 0;

     if (this.getVelY() == 0 && this.getVelX() == 0) {
     inc = 2;
     }

     this.getKillerGame().newKillerBullet(
     this.getPosX() + (this.RAD * (inc + this.getPosX())),
     this.getPosY() + (this.RAD * (inc + this.getVelY())),
     this.getColor(),
     (this.getVelX() + inc) * 2,
     (this.getVelY() + inc) * 2);
     }
     */
    
    public synchronized void shotDown() {
        if (cdActual == 0) {
            this.getKillerGame().newKillerBullet(this.getPosX(), this.getPosY()
                    + this.RAD + 50, this.getColor(), 0, +4);
            cdActual = cd;
        }
    }

    public synchronized void shotUp() {
        if (cdActual == 0) {
            this.getKillerGame().newKillerBullet(this.getPosX(), this.getPosY()
                    - this.RAD - 50, this.getColor(), 0, -4);
            cdActual = cd;
        }
    }

    public synchronized void shotLeft() {
        if (cdActual == 0) {

            this.getKillerGame().newKillerBullet(this.getPosX() - this.RAD - 50,
                    this.getPosY(), this.getColor(), -4, 0);
            cdActual = cd;
        }
    }

    public synchronized void shotRight() {
        if (cdActual == 0) {

            this.getKillerGame().newKillerBullet(this.getPosX() + this.RAD + 50,
                    this.getPosY(), this.getColor(), +4, 0);
            cdActual = cd;
        }
    }

    public synchronized void shotDownRight() {
        if (cdActual == 0) {

            this.getKillerGame().newKillerBullet(this.getPosX() + this.RAD + 50,
                    this.getPosY() + this.RAD + 50, this.getColor(), 4, 4);
            cdActual = cd;
        }
    }

    public synchronized void shotDownLeft() {
        if (cdActual == 0) {

            this.getKillerGame().newKillerBullet(this.getPosX() - this.RAD - 50,
                    this.getPosY() + this.RAD + 50, this.getColor(), -4, +4);
            cdActual = cd;
        }
    }

    public synchronized void shotUpRight() {
        if (cdActual == 0) {

            this.getKillerGame().newKillerBullet(this.getPosX() + this.RAD + 50,
                    this.getPosY() - this.RAD - 50, this.getColor(), +4, -4);
            cdActual = cd;
        }
    }

    public synchronized void shotUpLeft() {
        if (cdActual == 0) {

            this.getKillerGame().newKillerBullet(this.getPosX() - this.RAD,
                    this.getPosY() - this.RAD, this.getColor(), -4, -4);
            cdActual = cd;
        }
    }

    public Shape createShape() {
        Ellipse2D.Double shape = null;

        shape = new Ellipse2D.Double(this.getPosX() - RAD, this.getPosY() - RAD, RAD * 2, RAD * 2);

        return shape;
    }

    @Override
    public void paint(BufferedImage image) {
        Graphics g = image.getGraphics();
        //more
        g.setColor(this.getColor());
        g.fillOval(this.getPosX() - RAD, this.getPosY() - RAD, RAD * 2, RAD * 2);
    }

    @Override
    public void move() {
        // Detectores horizontales
        if (this.getPosX() - RAD <= 0 && this.getVelX() < 0 && counter == 0) {
            if (!this.getKillerGame().sendObjectToP(this)) {
                System.out.println("entro");
                this.setVelX(-this.getVelX());

            } else {
                counter++;
            }
        }

        if (this.getPosX() + this.RAD >= this.getKillerGame().getViewerW() && this.getVelX() > 0 && counter == 0) {
            if (!this.getKillerGame().sendObjectToN(this)) {//arror solucionado
                System.out.println("entrooooooooo guay");
                this.setVelX(-this.getVelX());

            } else {
                counter++;
            }
        }

        // Detectores verticales
        if (this.getPosY() - this.RAD <= 0 && this.getVelY() < 0) {
            this.setPosY(40);
        }

        if (this.getPosY() + this.RAD >= this.getKillerGame().getViewerH() && this.getVelY() > 0) {
            this.setPosY(this.getKillerGame().getViewerH()-40); //rebound 
        }

        // Matar thread
        if (this.getPosX() + this.RAD == 0 && this.getVelX() < 0) {
            this.getKillerGame().getObjects().remove(this);
        }

        if (this.getPosX() - this.RAD == this.getKillerGame().getViewerW() && this.getVelX() > 0) {
            this.getKillerGame().getObjects().remove(this);
        }

        super.move();
        this.setShape(this.createShape());
    }

    @Override
    public void collision() {

        super.collision();

        boolean succes = false;
        for (int i = 0; i < this.getKillerGame().getPads().size(); i++) {
            KillerPad pad = this.getKillerGame().getPads().get(i);
            if (pad.getHost().equals(ID)) {
                System.out.println("bye va");
                pad.sendCommand("bye");
                succes = true;
            }
        }

        if (!succes) {
            System.out.println("Soy un no succes!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

            this.getKillerGame().getNextModule().sendCommand("alive%" + this.ID + "%0%0%0%0%0%0%0");
            this.getKillerGame().getPrevModule().sendCommand("alive%" + this.ID + "%0%0%0%0%0%0%0");
        }

    }

    @Override
    public void run() {

        while (this.Alive()) { //while Alive the obj moves
            this.move();
            if (this.getKillerGame().checkCollision(this)) {
                this.collision();
            }
            try {
                if(cdActual>0){
                    cdActual--;
                }
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Alive.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
