package test.macrocycle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import macrocycle.HexLattice;

import org.junit.Test;

public class HexLatticeTest extends LatticeTest {
    
    public static final String OUT_DIR = "output/lattice/hexagon";
    
    @Test
    public void threeByFourTest() {
        double r = 40;
        HexLattice lattice = new HexLattice(3, 4, r);
        for (Point2D p : lattice.getPointList()) {
            System.out.println(p);
        }
    }
    
    @Test
    public void drawLattice() throws IOException {
        double hexagonRadius = 40;
        HexLattice lattice = new HexLattice(7, 6, hexagonRadius);
        int w = 600;
        int h = 600;
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        g.translate(30, 30);
        g.setColor(Color.BLACK);
        double pointRadius = 5;
        double lineWidth = 1;
        draw(lattice, pointRadius, lineWidth, g);
        ImageIO.write(image, "PNG", getFileHandle(OUT_DIR, "hex.png"));
    }
    
}
