package test.tree;

import graph.model.IntGraph;
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

import layout.CircleLayout;

import org.junit.Test;

import draw.ParameterSet;
import draw.Representation;

public class DrawTrees {
	
	public static final String IN_DIR = "output/trees/prufer";
	
	public static final String OUT_DIR = "output/trees/img/circle";
	
	public void drawAsCircle(String inputFilename) throws IOException {
		File dir = new File(OUT_DIR);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File inFile = new File(IN_DIR, inputFilename);
		GraphFileReader file = new GraphFileReader(new FileReader(inFile));
		ParameterSet params = new ParameterSet();
		params.set("pointRadius", 3);
		CircleLayout layout = new CircleLayout(params);
		int w = 100;
		int h = 100;
		Rectangle2D canvas = new Rectangle2D.Double(0, 0, w, h);
		int count = 0;
		String prefix = inputFilename.substring(0, inputFilename.length() - 4);
		for (IntGraph tree : file) {
			Representation repr = layout.layout(tree, canvas);
			File outFile = new File(dir, prefix + "_tree" + count + ".png");
			Image image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			image.getGraphics().setColor(Color.WHITE);
			image.getGraphics().fillRect(0, 0, w, h);
			Map<Vertex, Color> colorMap = new HashMap<Vertex, Color>();
			List<Integer> center = TreeCenterFinder.findCenter(tree);
			for (int i = 0; i < tree.getVertexCount(); i++) {
				if (center.contains(i)) {
					colorMap.put(new Vertex(i), Color.RED);
				} else {
					colorMap.put(new Vertex(i), Color.BLACK);
				}
			}
			repr.draw((Graphics2D)image.getGraphics(), params, colorMap);
			ImageIO.write((RenderedImage)image, "PNG", outFile);
			count++;
		}
	}
	
	@Test
	public void drawFourTreesAsCircle() throws IOException {
		drawAsCircle("fours.txt");
	}
	
	@Test
	public void drawFiveTreesAsCircle() throws IOException {
		drawAsCircle("fives.txt");
	}
	
	@Test
	public void drawSixTreesAsCircle() throws IOException {
		drawAsCircle("sixes.txt");
	}
	
	@Test
	public void drawSevenTreesAsCircle() throws IOException {
		drawAsCircle("sevens.txt");
	}
	
	@Test
	public void drawEightTreesAsCircle() throws IOException {
		drawAsCircle("eights.txt");
	}

}
