package test.draw;

import graph.model.Graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import layout.ConcentricCircularLayout;

import org.junit.Test;

import planar.BlockEmbedding;
import planar.PlanarBlockEmbedder;
import draw.ParameterSet;
import draw.Representation;

public class ConcentricCircularLayoutTest extends BaseDrawTest {
    
    public static final String OUT_DIR = "output/planar/concentric";
    
    public void test(Graph g, int w, int h, String filename) throws IOException {
        BlockEmbedding be = PlanarBlockEmbedder.embed(g);
        ConcentricCircularLayout layout = new ConcentricCircularLayout();
        Representation rep = layout.layout(be, new Rectangle2D.Double(0, 0, w, h));
        ParameterSet params = new ParameterSet();
        params.set("lineWidth", 2);
        params.set("pointRadius", 5);
        Image image = makeBlankImage(w, h);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.BLACK);
        rep.draw(graphics, params);
        ImageIO.write((RenderedImage) image, "PNG", getFile(OUT_DIR, filename));
    }
    
    @Test
    public void fourCycle() throws IOException {
        test(new Graph("0:1, 0:2, 1:3, 2:3"), 400, 400, "fourCycle.png");
    }
    
}
