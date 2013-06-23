package test.planar;

import graph.model.Graph;

import java.io.IOException;

import org.junit.Test;

import planar.Block;
import planar.BlockEmbedding;
import planar.PlanarBlockEmbedder;
import draw.SignatureColorer;

public class DrawingTest extends AbstractDrawingTest {
	
	public static final String OUTPUT_DIR = "output/planar";
	
	@Override
	public String getOutputDir() {
		return OUTPUT_DIR;
	}

	@Test
	public void sixCycleTest() throws IOException {
		Graph graph = new Graph(); 
		graph.makeMultipleEdges(0, 1, 5);
		graph.makeMultipleEdges(1, 2);
		graph.makeMultipleEdges(2, 3);
		graph.makeMultipleEdges(3, 4);
		graph.makeMultipleEdges(4, 5);
		BlockEmbedding em = new BlockEmbedding(graph);
		
		draw(em, "six.png");
	}
	
	@Test
	public void sixPrismTest() throws IOException {
		Graph graph = new Graph(); // don't really need this
		Block block = new Block(12);
		graph.makeMultipleEdges(0, 1, 5, 6);
		graph.makeMultipleEdges(1, 2, 7);
		graph.makeMultipleEdges(2, 3, 8);
		graph.makeMultipleEdges(3, 4, 9);
		graph.makeMultipleEdges(4, 5, 10);
		graph.makeMultipleEdges(5, 11);
		graph.makeMultipleEdges(6, 7, 11);
		graph.makeMultipleEdges(7, 8);
		graph.makeMultipleEdges(8, 9);
		graph.makeMultipleEdges(9, 10);
		graph.makeMultipleEdges(10, 11);
		
		BlockEmbedding em = PlanarBlockEmbedder.embed(block, graph);
		if (em != null) {
			draw(em, "six_prism.png");
		}
		System.out.println(block.esize());
	}
	
	@Test
	public void edgeBridgeTest() throws IOException {
		Graph graph = new Graph(); // don't really need this
		Block block = new Block(7);
		graph.makeMultipleEdges(0, 1, 4);
		graph.makeMultipleEdges(1, 2, 4, 6);
//		graph.makeMultipleEdges(1, 2, 6);
		graph.makeMultipleEdges(2, 3, 6);
		graph.makeMultipleEdges(3, 4, 5);
		graph.makeMultipleEdges(4, 5);
		graph.makeMultipleEdges(5, 6);
		
		BlockEmbedding em = PlanarBlockEmbedder.embed(block, graph);
		if (em != null) {
			draw(em, 1000, 1000, "edge_bridge.png");
		}
	}
	
	@Test
	public void edgeBridge2Test() throws IOException {
		Graph graph = new Graph(); // don't really need this
		Block block = new Block(7);
		graph.makeMultipleEdges(0, 1, 4);
		graph.makeMultipleEdges(1, 2, 4, 5);
		graph.makeMultipleEdges(2, 3, 5);
		graph.makeMultipleEdges(3, 4, 5);
		graph.makeMultipleEdges(4, 5);
		
		BlockEmbedding em = PlanarBlockEmbedder.embed(block, graph);
		if (em != null) {
			draw(em, 1000, 1000, "edge_bridge2.png");
		}
	}
	
	@Test
	public void sixDoublePrismTest() throws IOException {
		Graph graph = new Graph(); 
		graph.makeMultipleEdges(0, 1, 5, 6);
		graph.makeMultipleEdges(1, 2, 7);
		graph.makeMultipleEdges(2, 3, 8);
		graph.makeMultipleEdges(3, 4, 9);
		graph.makeMultipleEdges(4, 5, 10);
		graph.makeMultipleEdges(5, 11);
		graph.makeMultipleEdges(6, 7, 11, 12);
		graph.makeMultipleEdges(7, 8, 13);
		graph.makeMultipleEdges(8, 9, 14);
		graph.makeMultipleEdges(9, 10, 15);
		graph.makeMultipleEdges(10, 11, 16);
		graph.makeMultipleEdges(11, 17);
		graph.makeMultipleEdges(12, 13, 17);
		graph.makeMultipleEdges(13, 14);
		graph.makeMultipleEdges(14, 15);
		graph.makeMultipleEdges(15, 16);
		graph.makeMultipleEdges(16, 17);
		
		BlockEmbedding em = PlanarBlockEmbedder.embed(graph);
		if (em != null) {
			draw(em, 1000, 1000, "six_double_prism.png");
		}
	}
	
	@Test
	public void gcg_pp98_2345_3_7_1() throws IOException {
		Graph graph = new Graph();
		graph.makeMultipleEdges(0, 1, 7);
		graph.makeMultipleEdges(1, 2, 9);
		graph.makeMultipleEdges(2, 3, 13);
		graph.makeMultipleEdges(3, 4, 13);
		graph.makeMultipleEdges(4, 5, 11);
		graph.makeMultipleEdges(5, 6, 12);
		graph.makeMultipleEdges(6, 7);
		graph.makeMultipleEdges(7, 8);
		graph.makeMultipleEdges(8, 9, 12);
		graph.makeMultipleEdges(9, 10);
		graph.makeMultipleEdges(10, 11, 13);
		graph.makeMultipleEdges(11, 12);
		
		BlockEmbedding em = PlanarBlockEmbedder.embed(graph);
		if (em != null) {
			draw(em, WIDTH, HEIGHT, "gcg_pp98_2345_3_7_1.png", new SignatureColorer());
		}
	}
	
	@Test
	public void gcg_pp98_2345_3_7_2() throws IOException {
		Graph graph = new Graph(); 
		graph.makeMultipleEdges(0, 1, 7, 9);
		graph.makeMultipleEdges(1, 2);
		graph.makeMultipleEdges(2, 3, 10);
		graph.makeMultipleEdges(3, 4, 11);
		graph.makeMultipleEdges(4, 5, 12);
		graph.makeMultipleEdges(5, 6, 8);
		graph.makeMultipleEdges(6, 7);
		graph.makeMultipleEdges(7, 8);
		graph.makeMultipleEdges(8, 13);
		graph.makeMultipleEdges(9, 10, 13);
		graph.makeMultipleEdges(10, 11);
		graph.makeMultipleEdges(11, 12);
		graph.makeMultipleEdges(12, 13);
		
		BlockEmbedding em = PlanarBlockEmbedder.embed(graph);
		if (em != null) {
			draw(em, WIDTH, HEIGHT, "gcg_pp98_2345_3_7_2.png", new SignatureColorer());
		}
	}
	
	@Test
	public void gcg_pp98_2345_3_7_3() throws IOException {
		Graph graph = new Graph(); 
		graph.makeMultipleEdges(0, 1, 8, 9);
		graph.makeMultipleEdges(1, 2);
		graph.makeMultipleEdges(2, 3, 10);
		graph.makeMultipleEdges(3, 4, 11);
		graph.makeMultipleEdges(4, 5, 12);
		graph.makeMultipleEdges(5, 6, 14);
		graph.makeMultipleEdges(6, 7);
		graph.makeMultipleEdges(7, 8);
		graph.makeMultipleEdges(8, 14);
		graph.makeMultipleEdges(9, 10, 13);
		graph.makeMultipleEdges(10, 11);
		graph.makeMultipleEdges(11, 12);
		graph.makeMultipleEdges(12, 13);
		graph.makeMultipleEdges(13, 14);
		
		BlockEmbedding em = PlanarBlockEmbedder.embed(graph);
		if (em != null) {
			draw(em, WIDTH, HEIGHT, "gcg_pp98_2345_3_7_3.png", new SignatureColorer());
		}
	}

	@Test
	public void gcg_pp98_2345_3_7_4() throws IOException {
		Graph graph = new Graph();
		graph.makeMultipleEdges(0, 1, 8);
		graph.makeMultipleEdges(1, 2, 9);
		graph.makeMultipleEdges(2, 3, 10);
		graph.makeMultipleEdges(3, 4, 11);
		graph.makeMultipleEdges(4, 5);
		graph.makeMultipleEdges(5, 6, 12);
		graph.makeMultipleEdges(6, 7);
		graph.makeMultipleEdges(7, 8, 13);
		graph.makeMultipleEdges(8, 9);
		graph.makeMultipleEdges(9, 14);
		graph.makeMultipleEdges(10, 11, 14);
		graph.makeMultipleEdges(11, 12);
		graph.makeMultipleEdges(12, 13);
		graph.makeMultipleEdges(13, 14);
		
		BlockEmbedding em = PlanarBlockEmbedder.embed(graph);
		if (em != null) {
			draw(em, WIDTH, HEIGHT, "gcg_pp98_2345_3_7_4.png", new SignatureColorer());
		}
	}

	@Test
	public void gcg_pp98_2345_3_7_5() throws IOException {
		Graph graph = new Graph();
		graph.makeMultipleEdges(0, 1, 9, 10);
		graph.makeMultipleEdges(1, 2);
		graph.makeMultipleEdges(2, 3, 11);
		graph.makeMultipleEdges(3, 4);
		graph.makeMultipleEdges(4, 5, 12);
		graph.makeMultipleEdges(5, 6, 13);
		graph.makeMultipleEdges(6, 7, 15);
		graph.makeMultipleEdges(7, 8);
		graph.makeMultipleEdges(8, 9);
		graph.makeMultipleEdges(9, 15);
		graph.makeMultipleEdges(10, 11, 14);
		graph.makeMultipleEdges(11, 12);
		graph.makeMultipleEdges(12, 13);
		graph.makeMultipleEdges(13, 14);
		graph.makeMultipleEdges(14, 15);
		
		BlockEmbedding em = PlanarBlockEmbedder.embed(graph);
		if (em != null) {
			draw(em, WIDTH, HEIGHT, "gcg_pp98_2345_3_7_5.png", new SignatureColorer());
		}
	}
	
	@Test
	public void gcg_pp98_2345_3_7_6() throws IOException {
		Graph graph = new Graph();
		graph.makeMultipleEdges(0, 1, 6);
		graph.makeMultipleEdges(1, 2, 8);
		graph.makeMultipleEdges(2, 3, 9);
		graph.makeMultipleEdges(3, 4, 11);
		graph.makeMultipleEdges(4, 5);
		graph.makeMultipleEdges(5, 6, 12);
		graph.makeMultipleEdges(6, 7);
		graph.makeMultipleEdges(7, 8, 13);
		graph.makeMultipleEdges(8, 9);
		graph.makeMultipleEdges(9, 10);
		graph.makeMultipleEdges(10, 11, 13);
		graph.makeMultipleEdges(11, 12);
		graph.makeMultipleEdges(12, 13);
		
		BlockEmbedding em = PlanarBlockEmbedder.embed(graph);
		if (em != null) {
			draw(em, WIDTH, HEIGHT, "gcg_pp98_2345_3_7_6.png", new SignatureColorer());
		}
	}
	
	@Test
	public void gcg_pp98_2345_3_7_9() throws IOException {
		Graph graph = new Graph();
		graph.makeMultipleEdges(0, 1, 7, 8);
		graph.makeMultipleEdges(1, 2);
		graph.makeMultipleEdges(2, 3, 9);
		graph.makeMultipleEdges(3, 4, 10);
		graph.makeMultipleEdges(4, 5);
		graph.makeMultipleEdges(5, 6, 11);
		graph.makeMultipleEdges(6, 7, 12);
		graph.makeMultipleEdges(7, 12);
		graph.makeMultipleEdges(8, 9, 13);
		graph.makeMultipleEdges(9, 10);
		graph.makeMultipleEdges(10, 11);
		graph.makeMultipleEdges(11, 13);
		graph.makeMultipleEdges(12, 13);
		
		BlockEmbedding em = PlanarBlockEmbedder.embed(graph);
		if (em != null) {
			draw(em, WIDTH, HEIGHT, "gcg_pp98_2345_3_7_9.png", new SignatureColorer());
		}
	}
	
	@Test
	public void gcg_pp101_2345_3_10_7() throws IOException {
		Graph graph = new Graph();
		graph.makeMultipleEdges(0, 1, 5, 6);
		graph.makeMultipleEdges(1, 2, 7);
		graph.makeMultipleEdges(2, 3, 8);
		graph.makeMultipleEdges(3, 4, 9);
		graph.makeMultipleEdges(4, 5, 10);
		graph.makeMultipleEdges(5, 11);
		graph.makeMultipleEdges(6, 7, 15);
		graph.makeMultipleEdges(7, 12);
		graph.makeMultipleEdges(8, 12, 13);
		graph.makeMultipleEdges(9, 10, 13);
		graph.makeMultipleEdges(10, 14);
		graph.makeMultipleEdges(11, 14, 15);
		graph.makeMultipleEdges(12, 16);
		graph.makeMultipleEdges(13, 17);
		graph.makeMultipleEdges(14, 17);
		graph.makeMultipleEdges(15, 16);
		graph.makeMultipleEdges(16, 17);
		
		BlockEmbedding em = PlanarBlockEmbedder.embed(graph);
		if (em != null) {
			draw(em, WIDTH, HEIGHT, "gcg_pp101_2345_3_10_7.png", new SignatureColorer());
		}
	}
	
	@Test
	public void herschelGraph() throws IOException {
		Graph graph = new Graph();
		graph.makeMultipleEdges(0, 1, 3, 4);
		graph.makeMultipleEdges(1, 2, 5);
		graph.makeMultipleEdges(2, 3, 6, 7);
		graph.makeMultipleEdges(3, 8);
		graph.makeMultipleEdges(4, 5, 8);
		graph.makeMultipleEdges(5, 6, 9);
		graph.makeMultipleEdges(6, 10);
		graph.makeMultipleEdges(7, 8, 10);
		graph.makeMultipleEdges(8, 9);
		graph.makeMultipleEdges(9, 10);
		
		BlockEmbedding em = PlanarBlockEmbedder.embed(graph);
		if (em != null) {
			draw(em, WIDTH, HEIGHT, "herschel.png");
		}
	}
	
	@Test
	public void icosohedralGraph() throws IOException {
		Graph graph = new Graph();
		graph.makeMultipleEdges(0, 1, 4, 5);
		graph.makeMultipleEdges(1, 2, 6);
		graph.makeMultipleEdges(2, 3, 7);
		graph.makeMultipleEdges(3, 4, 8);
		graph.makeMultipleEdges(4, 9);
		graph.makeMultipleEdges(5, 10, 14);
		graph.makeMultipleEdges(6, 10, 11);
		graph.makeMultipleEdges(7, 11, 12);
		graph.makeMultipleEdges(8, 12, 13);
		graph.makeMultipleEdges(9, 13, 14);
		graph.makeMultipleEdges(10, 15);
		graph.makeMultipleEdges(11, 16);
		graph.makeMultipleEdges(12, 17);
		graph.makeMultipleEdges(13, 18);
		graph.makeMultipleEdges(14, 19);
		graph.makeMultipleEdges(15, 16, 19);
		graph.makeMultipleEdges(16, 17);
		graph.makeMultipleEdges(17, 18);
		graph.makeMultipleEdges(18, 19);
		
		BlockEmbedding em = PlanarBlockEmbedder.embed(graph);
		if (em != null) {
			draw(em, WIDTH, HEIGHT, "icosohedral.png");
		}
	}

}
