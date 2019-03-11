package killergame;

import java.awt.Color;
import java.awt.Shape;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Alive extends VisualObject implements Runnable {

    private int velX;
    private int velY;
    private boolean alive;

    public Alive(int posX, int posY, String type, KillerGame kg, Color color, int velX, int velY) {
        super(posX, posY, type, kg, color);
        this.velX = velX;
        this.velY = velY;
        this.alive = true;
    }

    public void move() {
        this.setPosX(this.getPosX() + this.velX);
        this.setPosY(this.getPosY() + this.velY);
        
    }

    public boolean Alive() {
        return this.alive;
    }

    public void dead() {
        this.alive = false;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public int getVelX() {
        return this.velX;
    }

    public int getVelY() {
        return this.velY;
    }

    @Override
    public void collision() {
        this.dead();
        this.getKillerGame().getObjects().remove(this);
    }

    @Override
    public void run() {

        while (alive) { //while Alive the obj moves
            this.move();
            if (this.getKillerGame().checkCollision(this)) {
                this.collision();
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Alive.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
