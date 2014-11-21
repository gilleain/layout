package test.planar;

import graph.model.Block;
import graph.model.VertexGraph;
import graph.model.Vertex;
import graph.visitor.SimpleCircuitFinder;

import org.junit.Test;

public class GraphObjectTests {
	
	public VertexGraph testGraph() {
		VertexGraph g = new VertexGraph(8);
		g.add(0, 1, 3, 4);
		g.add(1, 2, 5);
		g.add(2, 3, 5, 6);
		g.add(3, 7);
		g.add(6, 7);
		return g;
	}
	
	@Test
	public void indexOfTest() {
	    VertexGraph g = testGraph();
	    int index = g.indexOf(new Vertex(5));
	    System.out.println(index);
	}
	
	@Test
	public void diffTest() {
		VertexGraph g = testGraph();
		
		VertexGraph sg = new VertexGraph(4);
		sg.add(0, 1, 3);
		sg.add(1, 2);
		sg.add(2, 3);
		
		System.out.println(g);
		System.out.println(sg);
		System.out.println(g.difference(sg));
	}
	
	@Test
	public void cycleWithEdgeBridgeDiffTest() {
		VertexGraph g = new VertexGraph(4);
		g.add(0, 1, 2, 3);
		g.add(1, 2);
		g.add(2, 3);
		
		VertexGraph sg = new VertexGraph(4);
		sg.add(0, 1, 3);
		sg.add(1, 2);
		sg.add(2, 3);
		
		System.out.println(g);
		System.out.println(sg);
		System.out.println(g.difference(sg));
	}
	
	@Test
	public void cycleWithVertexBridgeDiffTest() {
		VertexGraph g = new VertexGraph(5);
		g.add(0, 1, 4, 3);
		g.add(1, 2);
		g.add(2, 3);
		g.add(4, 2);
		
		VertexGraph sg = new VertexGraph(4);
		sg.add(0, 1, 3);
		sg.add(1, 2);
		sg.add(2, 3);
		
		System.out.println(g);
		System.out.println(sg);
		System.out.println(g.difference(sg));
	}
	
	@Test
	public void cycleTest() {
		VertexGraph g = testGraph();
		SimpleCircuitFinder finder = new SimpleCircuitFinder();
		g.accept(finder);
		for (Block circuit : finder.getCircuits()) {
			System.out.println(circuit);
		}
	}

}
