package test.planar;

import graph.model.IntGraph;
import graph.model.GraphFactory;

import java.io.IOException;

import org.junit.Test;

import planar.BlockEmbedding;
import planar.PlanarBlockEmbedder;
import draw.SignatureColorer;

public class RegularPolyhedraTests extends AbstractDrawingTest {

	public static final String OUTPUT_DIR = "output/planar/regular";
	
	@Override
	public String getOutputDir() {
		return OUTPUT_DIR;
	}
	
	public void embed(IntGraph g, String outfileName) throws IOException {
	    BlockEmbedding em = PlanarBlockEmbedder.embed(g);
        if (em != null) {
            draw(em, WIDTH, HEIGHT, outfileName + ".png");
        }
	}
	
	@Test
    public void triPrism() throws IOException {
        embed(GraphFactory.nPrism(3), "triPrism");
    }
	
	@Test
	public void cube() throws IOException {
	    embed(GraphFactory.nPrism(4), "cube");
	}
	
	@Test
    public void pentaPrism() throws IOException {
	    embed(GraphFactory.nPrism(5), "pentaprism");
    }
	
	@Test
    public void hexaPrism() throws IOException {
        embed(GraphFactory.nPrism(6), "hexaprism");
    }
	
	@Test
	public void a3_6_6_v12() throws IOException {
		IntGraph graph = new IntGraph();
		graph.makeMultipleEdges(0, 1, 5, 6);
		graph.makeMultipleEdges(1, 2, 7);
		graph.makeMultipleEdges(2, 3, 7);
		graph.makeMultipleEdges(3, 4, 8);
		graph.makeMultipleEdges(4, 5, 8);
		graph.makeMultipleEdges(5, 6);
		graph.makeMultipleEdges(6, 9);
		graph.makeMultipleEdges(7, 10);
		graph.makeMultipleEdges(8, 11);
		graph.makeMultipleEdges(9, 10, 11);
		graph.makeMultipleEdges(10, 11);
		
		BlockEmbedding em = PlanarBlockEmbedder.embed(graph);
		if (em != null) {
			draw(em, WIDTH, HEIGHT, "a3_6_6_v12.png", new SignatureColorer());
		}
	}
	
	@Test
	public void a3_4_3_4_v12() throws IOException {
		IntGraph graph = new IntGraph();
		graph.makeMultipleEdges(0, 1, 3, 4, 7);
		graph.makeMultipleEdges(1, 2, 4, 5);
		graph.makeMultipleEdges(2, 3, 5, 6);
		graph.makeMultipleEdges(3, 6, 7);
		graph.makeMultipleEdges(4, 8, 11);
		graph.makeMultipleEdges(5, 8, 9);
		graph.makeMultipleEdges(6, 9, 10);
		graph.makeMultipleEdges(7, 10, 11);
		graph.makeMultipleEdges(8, 9, 11);
		graph.makeMultipleEdges(9, 8, 10);
		graph.makeMultipleEdges(10, 9, 11);
		
		BlockEmbedding em = PlanarBlockEmbedder.embed(graph);
		if (em != null) {
			draw(em, WIDTH, HEIGHT, "a3_4_3_4_v12.png", new SignatureColorer());
		}
	}
	
	@Test
	public void a3_6_6_v16() throws IOException {
		// XXX THIS IS NOT A REGULAR POLYHEDRA!
		IntGraph graph = new IntGraph();
		graph.makeMultipleEdges(0, 1, 5, 6);
		graph.makeMultipleEdges(1, 2, 6);
		graph.makeMultipleEdges(2, 3, 7);
		graph.makeMultipleEdges(3, 4, 8);
		graph.makeMultipleEdges(4, 5, 8);
		graph.makeMultipleEdges(5, 9);
		graph.makeMultipleEdges(6, 10);
		graph.makeMultipleEdges(7, 12, 13);
		graph.makeMultipleEdges(8, 11);
		graph.makeMultipleEdges(9, 14, 15);
		graph.makeMultipleEdges(10, 12, 15);
		graph.makeMultipleEdges(11, 13, 14);
		graph.makeMultipleEdges(12, 15);
		graph.makeMultipleEdges(13, 14);
		
		BlockEmbedding em = PlanarBlockEmbedder.embed(graph);
		if (em != null) {
			draw(em, WIDTH, HEIGHT, "f3_6_6_v16.png", new SignatureColorer());
		}
	}
	

}
