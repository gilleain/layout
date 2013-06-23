package test.draw;

import graph.model.Graph;
import graph.model.GraphFileReader;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

import layout.GraphLayout;

import org.junit.Test;

import planar.GraphEmbedder;
import planar.GraphEmbedding;
import draw.ParameterSet;
import draw.Representation;

public class GraphLayoutTest extends BaseDrawTest {
    
    public static final String IN_DIR = "output/mckay/";
    
    public static final String OUT_DIR = "output/planar/graph";
    
    public void testGraph(Graph g, int w, int h, String filename) throws IOException {
        testGraph(g, w, h, 30, false, "misc", filename);
    }
    
    public void testGraph(Graph g, int w, int h, int edgeLen, boolean drawNumbers, String subdir, String filename) throws IOException {
        GraphEmbedding ge = GraphEmbedder.embed(g);
        ParameterSet params = new ParameterSet();
        params.set("edgeLength", edgeLen);
        params.set("pointRadius", 5);
        params.set("lineWidth", 2);
        if (drawNumbers) {
            params.set("drawNumberLabels", 1);
        } else { 
            params.set("drawNumberLabels", 0);
        }
        
        GraphLayout layout = new GraphLayout(params);
        Representation rep = layout.layout(ge, new Rectangle2D.Double(0, 0, w, h));
        rep.centerOn(w / 2, h / 2);
        Image image = makeBlankImage(w, h);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.BLACK);
        rep.draw(graphics, params);
        ImageIO.write((RenderedImage) image, "PNG", getFile(new File(OUT_DIR, subdir), filename));
    }
    
    public void testFile(String inputFilename, int w, int h, String prefix) throws IOException {
        testFile(inputFilename, w, h, prefix, -1);
    }
    
    public void testFile(String inputFilename, int w, int h, String prefix, int limit) throws IOException {
        GraphFileReader graphs = new GraphFileReader(new FileReader(new File(IN_DIR, inputFilename)));
        int counter = 0;
        for (Graph g : graphs) {
            try {
                testGraph(g, w, h, 30, false, prefix, prefix + "_" + counter + ".png");
                System.out.println("done " + counter + "\t" + g);
            } catch (Exception e) {
                StackTraceElement[] st = e.getStackTrace();
                if (st.length > 0) {
                    System.out.println("errr "  + counter + "\t" + e.getStackTrace()[0] + "\t" + g);
                } else {
                    System.out.println("errr "  + counter + "\t" + g);
                }
            }
            counter++;
            if (counter == limit) break;
        }
    }
    
    @Test
    public void treeOn5() throws IOException {
        testGraph(new Graph("0:1,0:2,1:3,2:4"), 300, 300, "treeOn5.png");
    }
    
    @Test
    public void pawGraph() throws IOException {
        testGraph(new Graph("0:1,0:2,1:2,2:3"), 300, 300, "paw.png");
    }
    
    @Test
    public void spiraTriangles() throws IOException {
        testGraph(new Graph("0:1,0:2,1:2,2:3,2:4,3:4"), 300, 300, 50, true, "misc", "spiraTri.png");
    }
    
    @Test
    public void dumbellTriangles() throws IOException {
        testGraph(new Graph("0:1,0:2,1:2,2:3,3:4,3:5,4:5"), 300, 300, "dumbellTri.png");
    }
    
    @Test
    public void hornedTriangle() throws IOException {
        testGraph(new Graph("0:1,0:2,1:2,2:3,2:4"), 400, 400, 50, true, "misc", "hornedTri.png");
    }
    
    @Test
    public void spikedTriangle() throws IOException {
        testGraph(new Graph("0:1,0:2,0:3,1:2,1:4"), 400, 400, 50, true, "misc", "spikedTri.png");
    }
    
    @Test
    public void hornedSquare() throws IOException {
        testGraph(new Graph("0:2, 0:3, 1:2, 1:3, 2:4, 2:5"), 400, 400, 50, true, "misc", "hornedSq.png");
    }
    
    @Test
    public void bugOn6() throws IOException {
        testGraph(new Graph("0:1, 0:2, 0:3, 0:4, 0:5, 1:2, 3:4"), 400, 400, 50, true, "misc", "bugOn6.png");
    }
    
    @Test
    public void bugOn6_105() throws IOException {
        testGraph(new Graph("0:1, 0:2, 0:3, 0:4, 0:5, 1:2, 1:3, 1:4, 2:3, 2:4, 3:5, 4:5"), 400, 400, 50, true, "misc", "bugOn6_105.png");
    }
    
    @Test
    public void missingEight() throws IOException {
        testGraph(new Graph("0:1, 0:2, 0:3, 0:5, 1:2, 1:3, 1:6, 2:4, 2:7, 3:4, 4:5, 4:6, 5:6, 5:7, 6:7"),
                400, 400, 50, true, "misc", "missingEight.png");
    }
    
    @Test
    public void testFours() throws IOException {
        testFile("four_x.txt", 300, 300, "g4");
    }
    
    @Test
    public void testFives() throws IOException {
        testFile("five_x.txt", 300, 300, "g5");
    }
    
    @Test
    public void testSixes() throws IOException {
        testFile("six_x.txt", 200, 200, "g6");
    }
    
    @Test
    public void testEights() throws IOException {
        testFile("eight_x.txt", 300, 300, "g8");
    }
    
    @Test
    public void testFirst1000Twelve_d3() throws IOException {
        testFile("twelve_3.txt", 300, 300, "g12", 1000);
    }
    
}
