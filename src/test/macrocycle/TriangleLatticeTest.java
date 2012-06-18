package test.macrocycle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import macrocycle.TriangleLattice;

import org.junit.Test;

public class TriangleLatticeTest extends LatticeTest {
    
    public static final String OUT_DIR = "output/lattice/triangle";
    
    @Test
    public void makeDualTest() {
        TriangleLattice lattice = new TriangleLattice(11, 4, 50);
        lattice.getDual();
    }
    
    @Test
    public void drawLattice() throws IOException {
        TriangleLattice lattice = new TriangleLattice(19, 8, 50);
        int w = 600;
        int h = 500;
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        g.translate(10, 30);
        g.setColor(Color.BLACK);
        draw(lattice, 6, 2, g);
       
        ImageIO.write(image, "PNG", getFileHandle(OUT_DIR, "tri.png"));
    }
    
    @Test
    public void drawLatticePlusDual() throws IOException {
        TriangleLattice lattice = new TriangleLattice(19, 8, 50);
        int w = 600;
        int h = 500;
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        g.translate(10, 30);
        
        double pointRadius = 6;
        double dualPointRadius = 6;
        double lineWidth = 2;
        double dualLineWidth = 1;
        Color color = Color.BLACK;
        Color dualColor = Color.GRAY;
        drawWithDual(lattice, pointRadius, dualPointRadius, 
                     lineWidth, dualLineWidth, color, dualColor, g);
        ImageIO.write(image, "PNG", getFileHandle(OUT_DIR, "tri_plus_dual.png"));
    }
    
}
