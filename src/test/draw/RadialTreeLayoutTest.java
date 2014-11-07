package test.draw;

import graph.model.Graph;
import graph.model.GraphFileReader;
import graph.model.Vertex;
import graph.tree.TreeCenterFinder;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import layout.RadialTreeLayout;

import org.junit.Test;

import draw.ParameterSet;
import draw.Representation;

public class RadialTreeLayoutTest {
    
    public static final String IN_DIR = "output/trees/prufer";
    
    public static final String OUT_DIR = "output/trees/img/tree/radial";
    
    public ParameterSet getParams() {
        ParameterSet params = new ParameterSet();
        params.set("pointRadius", 4);
        params.set("lineWidth", 3);
        params.set("edgeLength", 40);
        params.set("drawNumberLabels", 0);
        return params;
    }
    
    public void singleTree(Graph tree, String filename) throws IOException {
        File dir = new File(OUT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        int w = 800;
        int h = 400;
        ParameterSet params = getParams();
        RadialTreeLayout layout = new RadialTreeLayout(params);
        Representation r = layout.layout(tree, new Rectangle2D.Double(0, 0, w, h));
        System.out.println(r);
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = (Graphics2D)image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.BLACK);
        r.draw(g, params);
        ImageIO.write(image, "PNG", new File(dir, filename));
    }
    
    @Test
    public void singleTestA() throws IOException {
        singleTree(new Graph("0:1,0:2,1:3,2:4,3:5"), "1_2_3_1_2.png");
    }
    
    @Test
    public void singleTestB() throws IOException {
        singleTree(new Graph("0:2,0:4,0:5,0:6,1:2,1:3"), "1_2_2_2_1_2.png");
    }
    
    public void draw(String inputFilename) throws IOException {
        File dir = new File(OUT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File inFile = new File(IN_DIR, inputFilename);
        GraphFileReader file = new GraphFileReader(new FileReader(inFile));
       
        RadialTreeLayout layout = new RadialTreeLayout(getParams());
        int w = 200;
        int h = 200;
        Rectangle2D canvas = new Rectangle2D.Double(0, 0, w, h);
        int count = 0;
        String prefix = inputFilename.substring(0, inputFilename.length() - 4);
        for (Graph tree : file) {
            Representation repr = layout.layout(tree, canvas);
            String name = prefix + "_tree" + count;
            System.out.println("drawing " + tree + " to " + name);
            File outFile = new File(dir, name + ".png");
            Image image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = (Graphics2D)image.getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, w, h);
            g.setColor(Color.BLACK);
            Map<Vertex, Color> colorMap = new HashMap<Vertex, Color>();
            List<Integer> center = TreeCenterFinder.findCenter(tree);
            for (int i = 0; i < tree.getVertexCount(); i++) {
                if (center.contains(i)) {
                    colorMap.put(new Vertex(i), Color.RED);
                } else {
                    colorMap.put(new Vertex(i), Color.BLACK);
                }
            }
            repr.draw(g, getParams(), colorMap);
            ImageIO.write((RenderedImage)image, "PNG", outFile);
            count++;
        }
    }
    
    @Test
    public void drawFourTrees() throws IOException {
        draw("fours.txt");
    }
    
    @Test
    public void drawFiveTrees() throws IOException {
        draw("fives.txt");
    }
    
    @Test
    public void drawSixTrees() throws IOException {
        draw("sixes.txt");
    }
    
    @Test
    public void drawSevenTrees() throws IOException {
        draw("sevens.txt");
    }
    
    @Test
    public void drawEightTrees() throws IOException {
        draw("eights.txt");
    }
    
    @Test
    public void tmp() {
        Point2D pP = new Point2D.Double(10, 10);
        int n = 10;
        double alpha = Math.toRadians(360.0 / n);
        double cx = pP.getX();
        double cy = pP.getY();
        double r = 10;
        double currentAngle = 0.0;
        for (int i = 0; i < n; i++) {
            double x = cx + (r * Math.cos(currentAngle));
            double y = cy + (r * Math.sin(currentAngle));
            Point2D pV = new Point2D.Double(x, y);
            double a = angle(pP, pV);
            System.out.println(String.format("%2.0f %2.0f %2.0f %2.0f",
                                pV.getX(), pV.getY(), 
                                Math.toDegrees(a), Math.toDegrees(currentAngle)));
            currentAngle += alpha;
        }
    }
    
    private double angle(Point2D pA, Point2D pB) {
        double a = Math.atan2((pB.getY() - pA.getY()), 
                               pB.getX() - pA.getX());
        if (a < 0) {
            return 2 * Math.PI + a;
        } else {
            return a;
        }
    }
    
}
