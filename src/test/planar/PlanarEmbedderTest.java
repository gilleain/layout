package test.planar;

import graph.model.Graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import layout.ConcentricCircularLayout;

import org.junit.Test;

import planar.BlockEmbedding;
import planar.PlanarBlockEmbedder;
import draw.Drawing;

public class PlanarEmbedderTest {
	
	public final static String OUT_DIR = "Output/misc"; 
	
	public final static int W = 300;
	
	public final static int H = 300;
	
	public void draw(BlockEmbedding em, String name) throws IOException {
		if (em != null) {
			System.out.println(em);
			File outDir = new File(OUT_DIR);
			if (!outDir.exists()) { outDir.mkdir(); }
			
			Drawing drawing = new Drawing(em, new ConcentricCircularLayout());
			BufferedImage image = new BufferedImage(W, H, BufferedImage.TYPE_BYTE_GRAY);
			Graphics2D g = (Graphics2D) image.getGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, W, H);
			g.setColor(Color.BLACK);
			drawing.draw(g, W, H);
			g.dispose();
			
			// write
			String filename = name + ".png";
			ImageIO.write(image, "PNG", new File(outDir, filename));
		}
	}
	
	public void embedAndDraw(Graph graph, String name) throws IOException {
	    BlockEmbedding em = PlanarBlockEmbedder.embed(graph);
        if (em != null) {
            draw(em, name);
            System.out.println(em.getCM());
            System.out.println(em.getFaces());
        }
	}
	
	@Test
	public void cuneaneTest() throws IOException {
	    embedAndDraw(new Graph("0:1,0:3,0:5,1:2,1:7,2:3,2:7,3:4,4:5,4:6,5:6,6:7"), "cuneane");
	}
	
	@Test
	public void k4Test() throws IOException {
	    embedAndDraw(new Graph("0:1,0:2,0:3,1:2,1:3,2:3"), "k4");
	}
	
	@Test
	public void squareHubWheelTest() throws IOException {
	    embedAndDraw(new Graph("0:1,0:3,0:4,1:2,1:4,2:3,2:4,3:4"), "squareHubWheel");
	}
	
	@Test
	public void cycle456TreeTest() throws IOException {
	    embedAndDraw(new Graph("0:1,0:10,1:2,1:7,2:3,3:4,3:6,4:5,5:6,6:7,7:8,8:9,9:10"), "cycle456Tree");
	}
	
	@Test
	public void twoFusedHexagons() throws IOException {
	    embedAndDraw(new Graph("0:1,0:9,1:2,1:6,2:3,3:4,4:5,5:6,6:7,7:8,8:9"), "twoFusedHexagons");
	}
	
	@Test
	public void threeFusedSquares() throws IOException {
	    embedAndDraw(new Graph("0:1,0:7,1:2,1:6,2:3,2:5,3:4,4:5,5:6,6:7"), "threeFusedSquares");
	}

}
