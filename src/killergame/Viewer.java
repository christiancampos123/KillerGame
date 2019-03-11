package killergame;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Viewer extends Canvas implements Runnable {

    //attr
    private final double refresh = 0.016683 * 1000; //perf time
    private BufferedImage image;
    private KillerGame kg;

    //const
    //meth
    public Viewer(KillerGame kg) {
        this.kg = kg;
    }

    @Override
    public Dimension getPreferredSize() { //Dimensions of the canvas
        return new Dimension(this.kg.getWidth(), this.kg.getHeight());
    }

    @Override
    public void run() {
        while (true) {

            //cerates image with the canvas size
            //this.image = new BufferedImage(this.kg.getWidth(), this.kg.getHeight(), 1); //image type

            try {
                this.image = ImageIO.read(new File("src/img/fondo3.jpg"));
            } catch (IOException error) {
                this.image = new BufferedImage(this.kg.getWidth() - 1, this.kg.getHeight() - 1, 1);
            }

            //goes all the array and tells objects to paint
            for (int i = 0; i < this.kg.getObjects().size(); i++) {
                this.kg.getObjects().get(i).paint(this.image);
            }

            //graphics
            try {
                Thread.sleep((long) (this.refresh)); //refresh time

            } catch (InterruptedException ex) {
                Logger.getLogger(Viewer.class.getName()).log(Level.SEVERE, null, ex);
            }

            this.getGraphics().drawImage(this.image, 0, 0, this);

        }

    }

}
