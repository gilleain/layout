package test.draw;

import graph.model.IntGraph;
import graph.model.GraphFileReader;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

import layout.CircleLayout;

import org.junit.Test;

import draw.ParameterSet;
import draw.Representation;

public class CircleLayoutTest extends BaseDrawTest {
	
	public void draw(IntGraph graph, int w, int h, String filename) throws IOException {
		ParameterSet params = new ParameterSet();
		params.set("border", 10);
		params.set("pointRadius", 3);
		CircleLayout layout = new CircleLayout(params);
		Representation repr = layout.layout(graph, new Rectangle2D.Double(0, 0, w, h));
		Image image = makeBlankImage(w, h);
		repr.draw((Graphics2D)image.getGraphics(), params);
		ImageIO.write((RenderedImage)image, "PNG", new File(filename));
	}
	
	@Test
	public void circleLayoutTest() throws IOException {
		int w = 300;
		int h = 300;
//		Graph graph = new Graph("[0:1, 0:2, 0:3, 10:11, 1:2, 1:4, 2:8, 3:5, 3:7, 4:10, 4:6, 5:6, 5:9, 6:7, 7:11, 8:11, 8:9, 9:10]");
		IntGraph graph = new IntGraph("[0:1, 0:2, 0:4, 10:11, 1:10, 1:3, 2:11, 2:3, 3:5, 4:5, 4:6, 5:7, 6:8, 6:9, 7:8, 7:9, 8:10, 9:11]");
		draw(graph, w, h, "tmp.png");
	}
	
	@Test
	public void drawFile() throws IOException {
		int w = 200;
		int h = 200;
		String filename = "output/degree_threes/ten_threes.txt";
		int index = 0;
		for (IntGraph g : new GraphFileReader(new FileReader(filename))) {
			draw(g, w, h, "output/img/g_" + index + ".png");
			index++;
		}
	}
}
