package test.macrocycle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;

import macrocycle.Lattice;

public class LatticeTest {
    
    public File getFileHandle(String dirString, String filename) {
        File dir = new File(dirString);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, filename);
    }
    
    public void draw(Lattice lattice, double r, double w, Graphics2D g) {
        Stroke s = g.getStroke();
        g.setStroke(new BasicStroke((float) w));
        for (Line2D l : lattice.getLineList()) {
            g.draw(l);
        }
        g.setStroke(s);
        for (Point2D p : lattice.getPointList()) {
            double x = p.getX();
            double y = p.getY();
            g.fill(new Ellipse2D.Double(x - r, y - r, 2  * r, 2  * r));
        }
    }
    
    public void drawWithDual(Lattice lattice, 
                             double pointRadius, double dualPointRadius,
                             double lineWidth, double dualLineWidth,
                             Color color, Color dualColor,
                             Graphics2D g) {
        g.setColor(color);
        draw(lattice, pointRadius, lineWidth, g);
        
        g.setColor(dualColor);
        draw(lattice.getDual(), dualPointRadius, dualLineWidth, g);
    }
    
}
