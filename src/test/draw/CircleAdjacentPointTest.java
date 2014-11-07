package test.draw;

import graph.model.Graph;
import graph.model.Vertex;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import layout.ConcentricCircularLayout;

import org.junit.Test;

import planar.BlockEmbedding;
import planar.PlanarBlockEmbedder;
import draw.ParameterSet;
import draw.Representation;

public class CircleAdjacentPointTest extends BaseDrawTest {
    
    public static final String OUT_DIR = "output/planar/circle";
    
    @Test
    public void test() throws IOException {
        int w = 400;
        int h = 400;
        int cw = 200;
        int ch = 200;
        Graph circle = new Graph("0:1,1:2,2:3,3:4,4:5,5:6,6:0");
        ConcentricCircularLayout layout = new ConcentricCircularLayout();
        BlockEmbedding em = PlanarBlockEmbedder.embed(circle);
        Representation rep = layout.layout(em, new Rectangle2D.Double(cw / 2, ch / 2, cw, ch));
        int count = rep.getVertices().size();
        for (Vertex v : rep.getVertices()) {
            List<Vertex> conn = em.getExternalFace().getConnected(v);
            assert conn.size() == 2;
            Point2D p = makePoint(v, conn, rep);
            rep.addPoint(new Vertex(count), p);
//            System.out.println(p);
            count++;
        }
        ParameterSet params = new ParameterSet();
        params.set("pointRadius", 5);
        params.set("lineWidth", 2);
        Image image = makeBlankImage(w, h);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.BLACK);
        rep.draw(g, params);
        ImageIO.write((RenderedImage) image, "PNG", getFile(OUT_DIR, "test.png"));
    }

    private Point2D makePoint(Vertex v, List<Vertex> conn, Representation rep) {
        double l = 20;
        Point2D pV = rep.getPoint(v);
        Point2D pA = rep.getPoint(conn.get(0));
        Point2D pB = rep.getPoint(conn.get(1));
        double dX = (pA.getX() - pV.getX()) + (pB.getX() - pV.getX());
        double dY = (pA.getY() - pV.getY()) + (pB.getY() - pV.getY());
        double m = Math.sqrt((dX * dX) + (dY * dY));
        double x = pV.getX() + (l * (-dX / m));
        double y = pV.getY() + (l * (-dY / m));
        Point2D p = new Point2D.Double(x, y);
        System.out.println(String.format("(%2.0f %2.0f) %2.0f %2.0f (%2.0f %2.0f)", 
                                         pV.getX(), pV.getY(), dX, dY, p.getX(), p.getY()));
        return p;
    }
    
}
