/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killergame;

import static com.sun.javafx.css.SizeUnits.RAD;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christian
 */
public class KillerBullet extends Autonomous {

    //attr
    private static final int RAD = 5; //radius
    private int counter;

    //const
    public KillerBullet(int posX, int posY, String type, KillerGame kg, Color color, int velX, int velY) {
        super(posX, posY, type, kg, color, velX, velY);
        this.setShape(this.createShape());
        this.counter = 0;
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
        if (this.getPosX() - RAD < 0 && this.getVelX() < 0 && counter == 0) {
            if (!this.getKillerGame().sendObjectToP(this)) {
                System.out.println(this.getVelX());
                System.out.println("entro1");
                this.setVelX(-this.getVelX());
                System.out.println(this.getVelX());

            } else {
                counter++;
            }
        }

        if (this.getPosX() + this.RAD > this.getKillerGame().getViewerW() && this.getVelX() > 0 && counter == 0) {
            if (!this.getKillerGame().sendObjectToN(this)) {
                System.out.println(this.getVelX());
                System.out.println("entro2");
                System.out.println(this.getVelX());
                this.setVelX(-this.getVelX());
                System.out.println(this.getVelX());

            } else {
                counter++;
            }
        }

        // Detectores verticales
        if (this.getPosY() - this.RAD <= 0 && this.getVelY() < 0) {
            this.setVelY(-this.getVelY());
        }

        if (this.getPosY() + this.RAD >= this.getKillerGame().getViewerH() && this.getVelY() > 0) {
            this.setVelY(-this.getVelY()); //rebound
        }

        // Matar thread
        if (this.getPosX() + this.RAD == 0 && this.getVelX() < 0) {
            this.getKillerGame().getObjects().remove(this);
            System.out.println("removeo1");
        }

        if (this.getPosX() - this.RAD == this.getKillerGame().getViewerW() && this.getVelX() > 0) {
            this.getKillerGame().getObjects().remove(this);
            System.out.println("removeo2");
        }

        super.move();
        this.setShape(this.createShape());
    }

}
