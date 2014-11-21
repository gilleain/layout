package test.draw;

import graph.model.IntGraph;
import graph.model.GraphFileReader;
import graph.model.Vertex;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import layout.GraphGridLayout;

import org.junit.Test;

import draw.ParameterSet;
import draw.Representation;

public class MassLayoutTest extends BaseDrawTest {
    
    public static final String IN_DIR = "../generate/output/mckay/";
    
    public static final String OUT_DIR = "output/mass";
    
    @Test
    public void layout4s() throws IOException {
        layout("four_x.txt", "g4_test");
    }
    
    @Test
    public void layout5s() throws IOException {
        layout("five_x.txt", "g5_test");
    }
    
    @Test
    public void layout6s() throws IOException {
        layout("six_x.txt", "g6_test");
    }
    
    public void layout(String inputFilename, String outputFilename) throws IOException {
        GraphFileReader graphs = new GraphFileReader(new FileReader(new File(IN_DIR, inputFilename)));
       
        int w = 100;
        int h = 100;
        boolean drawNumbers = false;
        int edgeLen = 30;
        
        ParameterSet params = new ParameterSet();
        params.set("edgeLength", edgeLen);
        params.set("pointRadius", 5);
        params.set("lineWidth", 2);
        if (drawNumbers) {
            params.set("drawNumberLabels", 1);
        } else { 
            params.set("drawNumberLabels", 0);
        }
        params.set("padding", 10);
        
        GraphGridLayout layout = new GraphGridLayout(w, h, params);
        Map<IntGraph, Representation> reps = layout.layout(graphs); 
        
        Image image = makeBlankImage(layout.getTotalWidth(), layout.getTotalHeight());
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.BLACK);
        for (IntGraph g : reps.keySet()) {
            Representation rep = reps.get(g);
            if (rep != null) {
                Map<Vertex, Color> colorMap = getColorMap(g);
                rep.draw(graphics, params, colorMap);
            }
        }
        ImageIO.write((RenderedImage) image, "PNG", getFile(OUT_DIR, outputFilename + ".png"));
    }
    
    public Map<Vertex, Color> getColorMap(IntGraph g) {
        Map<Vertex, Color> degreeMap = new HashMap<Vertex, Color>();
        for (int i = 0; i < g.vsize(); i++) {
            switch (g.degree(i)) {
                case 1 : degreeMap.put(new Vertex(i), Color.GREEN); break;
                case 2 : degreeMap.put(new Vertex(i), Color.MAGENTA); break;
                case 3 : degreeMap.put(new Vertex(i), Color.BLUE); break;
                case 4 : degreeMap.put(new Vertex(i), Color.RED); break;
                case 5 : degreeMap.put(new Vertex(i), Color.ORANGE); break;
                default: degreeMap.put(new Vertex(i), Color.BLACK);
            }
        }
        return degreeMap;
    }
    
}
