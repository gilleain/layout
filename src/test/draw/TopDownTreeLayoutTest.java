package test.draw;

import graph.model.Graph;
import graph.model.GraphFileReader;
import graph.model.Vertex;
import graph.tree.TreeCenterFinder;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
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

import layout.TopDownTreeLayout;

import org.junit.Test;

import draw.ParameterSet;
import draw.Representation;

public class TopDownTreeLayoutTest {
    
    public static final String IN_DIR = "output/trees/prufer";
    
    public static final String OUT_DIR = "output/trees/img/tree/topdown";
    
    public ParameterSet getParams() {
        ParameterSet params = new ParameterSet();
        params.set("pointRadius", 3);
        params.set("lineWidth", 3);
        return params;
    }
    
    public void singleTree(Graph tree, String filename) throws IOException {
        File dir = new File(OUT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        int w = 800;
        int h = 400;
        
        TopDownTreeLayout layout = new TopDownTreeLayout();
        Representation r = layout.layout(tree, new Rectangle2D.Double(0, 0, w, h));
        System.out.println(r);
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = (Graphics2D)image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.BLACK);
        r.draw(g, getParams());
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
       
        TopDownTreeLayout layout = new TopDownTreeLayout();
        int w = 400;
        int h = 100;
        Rectangle2D canvas = new Rectangle2D.Double(0, 0, w, h);
        int count = 0;
        String prefix = inputFilename.substring(0, inputFilename.length() - 4);
        for (Graph tree : file) {
            Representation repr = layout.layout(tree, canvas);
            File outFile = new File(dir, prefix + "_tree" + count + ".png");
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
    
}
