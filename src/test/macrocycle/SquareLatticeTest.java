package test.macrocycle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import macrocycle.SquareLattice;

import org.junit.Test;

public class SquareLatticeTest extends LatticeTest {
    
    public static final String OUT_DIR = "output/lattice/square";
    
    @Test
    public void drawLattice() throws IOException {
        SquareLattice lattice = new SquareLattice(12, 9, 50);
        int w = 600;
        int h = 500;
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        g.translate(20, 30);
        g.setColor(Color.BLACK);
        draw(lattice, 6, 2, g);
       
        ImageIO.write(image, "PNG", getFileHandle(OUT_DIR, "square.png"));
    }
    
    @Test
    public void drawLatticePlusDual() throws IOException {
        SquareLattice lattice = new SquareLattice(12, 9, 50);
        int w = 600;
        int h = 500;
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        g.translate(20, 30);
        
        double pointRadius = 6;
        double dualPointRadius = 6;
        double lineWidth = 2;
        double dualLineWidth = 1;
        Color color = Color.BLACK;
        Color dualColor = Color.GRAY;
        drawWithDual(lattice, pointRadius, dualPointRadius, 
                     lineWidth, dualLineWidth, color, dualColor, g);
        ImageIO.write(image, "PNG", getFileHandle(OUT_DIR, "square_plus_dual.png"));
    }
    
}
