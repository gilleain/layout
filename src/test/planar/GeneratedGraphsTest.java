package test.planar;

import graph.model.IntGraph;
import graph.model.GraphFileReader;

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

public class GeneratedGraphsTest {
	
	public void draw(String inputDir, String outputDir, int w, int h) throws IOException {
		File outDir = new File(outputDir);
		if (!outDir.exists()) { outDir.mkdir(); }
		
		int i = 0;
		for (IntGraph graph : GraphFileReader.readAll(inputDir)) {
			BlockEmbedding em = null;
			try {
				em = PlanarBlockEmbedder.embed(graph);
			} catch (Exception e) {
				System.out.println("ERROR for " + i + " = " + graph);
			}
			if (em == null) {
				System.out.println("no embedding for graph " + i + " = " + graph);
			} else {
				System.out.println("embedding for graph " + i + " = " + graph);
				Drawing drawing = new Drawing(em, new ConcentricCircularLayout());
				BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
				Graphics2D g = (Graphics2D) image.getGraphics();
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, w, h);
				drawing.draw(g, w, h);
				g.dispose();
				
				// write
				String filename = "g" + i + ".png";
				ImageIO.write(image, "PNG", new File(outDir, filename));
			}
			i++;
		}
	}
	
	@Test
	public void testSixThrees() throws IOException {
		draw("output/degree_threes/six_threes.txt", "gen_planar_6_3/", 300, 300);
	}
	
	@Test
	public void testTenThrees() throws IOException {
		draw("output/degree_threes/ten_threes.txt", "gen_planar_10_3/", 300, 300);
	}
	
	@Test
	public void testTwelveThrees() throws IOException {
		draw("output/degree_threes/twelve_threes.txt", "gen_planar_12_3/", 300, 300);
	}


}
