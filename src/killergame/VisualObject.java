package killergame;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;

class VisualObject {
    
    //attr
    private int posX;
    private int posY;
    private String type;
    private KillerGame kg;
    private Color color;
    private Shape shape;
    
    //const

    public VisualObject(int posX, int posY, String type, KillerGame kg, Color color) {
        this.posX = posX;
        this.posY = posY;
        this.type = type;
        this.kg = kg;
        this.color = color;
        System.out.println("se prendio" + this.color);
        System.out.println("se prendio2" +color);

    }
    
    
    //meth
    
    public void paint (BufferedImage image) {
        
    }
    
    
    
    public KillerGame getKillerGame(){
        return this.kg;
    }
    
    public Shape getShape(){
        return this.shape;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }
    
    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public Color getColor() {
        return color;
    }    

    public String getType() {
        return type;
    }
    
    public  Area getArea(){
        return new Area(this.shape);
    }
    
    public void collision(){
    }
    
}
