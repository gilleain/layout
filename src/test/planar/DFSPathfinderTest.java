package test.planar;

import graph.model.VertexGraph;
import graph.model.Path;
import graph.visitor.DFSPathFinder;

import org.junit.Test;

public class DFSPathfinderTest {
	
	@Test
	public void linearTest() {
		VertexGraph g = new VertexGraph(5);
		g.add(0, 1);
		g.add(1, 2);
		g.add(2, 3);
		g.add(3, 4);
		DFSPathFinder finder = new DFSPathFinder();
		g.accept(finder);
		for (Path path : finder.getPaths()) {
			System.out.println(path);
		}
	}
	
	@Test
	public void cycleTest() {
		VertexGraph g = new VertexGraph(5);
		g.add(0, 1, 4);
		g.add(1, 2);
		g.add(2, 3);
		g.add(3, 4);
		DFSPathFinder finder = new DFSPathFinder();
		g.accept(finder);
		for (Path path : finder.getPaths()) {
			System.out.println(path);
		}
	}
	
	@Test
	public void cycleWithTwoOutsTest() {
		VertexGraph g = new VertexGraph(7);
		g.add(0, 1, 4);
		g.add(1, 2, 5);
		g.add(2, 3);
		g.add(3, 4, 6);
		DFSPathFinder finder = new DFSPathFinder();
		g.accept(finder);
		for (Path path : finder.getPaths()) {
			System.out.println(path);
		}
	}


}
