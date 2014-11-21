package test.planar;

import graph.model.Block;
import graph.model.IntGraph;
import graph.model.GraphObject;
import graph.model.Vertex;
import graph.visitor.ConnectedComponentFinder;

import java.util.Random;

import org.junit.Test;

public class ConnectedComponentTest {
	
	public void check(GraphObject g) {
		System.out.println("G = " + g);
		ConnectedComponentFinder ccF = new ConnectedComponentFinder(); 
		g.accept(ccF);
		System.out.println("C = " + ccF.getComponents());
		GraphObject union = new GraphObject(g.vsize());
		int count = 0;
		for (Block b : ccF.getComponents()) {
			System.out.println("b" + count + " = " + b);
			for (int i = 0; i < g.vsize(); i++) {
				Vertex v = g.getVertex(i);
				for (Vertex w : b.getConnected(v)) {
					if (union.hasEdge(v, w)) {
						continue;
					} else {
						union.add(v, w);
					}
				}
			}
			count++;
		}
		System.out.println("U = " + union);
	}
	
	@Test
	public void bugTest() {
	    IntGraph g = new IntGraph("0:1, 0:3, 1:7, 2:3, 2:7, 4:5, 4:6, 5:6");
	    GraphObject go = new GraphObject(g);
	    check(go);
	}
	
	@Test
	public void bugTest2() {
	    IntGraph g = new IntGraph("0:1, 1:2, 0:7, 7:8, 8:9, 2:9, 10:11, 11:12, 10:17, 17:18, 18:19, 12:19");
//	    Graph g = new Graph("0:1, 1:2, 0:5, 4:5, 3:4, 2:3, 6:7, 7:8, 6:11, 10:11, 9:10, 8:9");
	    GraphObject go = new GraphObject(g);
	    check(go);
	}
	
	@Test
    public void bugTest3() {
//        Graph g = new Graph("7:5, 6:7, 5:6");
	    IntGraph g = new IntGraph("5:6, 5:7, 6:7");
        GraphObject go = new GraphObject(g);
        check(go);
    }
	
	@Test
	public void twoCycleTest() {
		GraphObject g = new GraphObject(8);
		// cc one
		g.add(0, 1, 3);
		g.add(1, 2);
		g.add(2, 3);
		
		// cc two
		g.add(4, 5, 7);
		g.add(5, 6);
		g.add(6, 7);
		
		check(g);
	}
	
	@Test
	public void randomGraph() {
		int vN = 10;
		int eN = 7;
		int maxAttempts = 100;	// just to be sure
		GraphObject g = new GraphObject(vN);
		Random r = new Random();
		int e = 0;
		int attempts = 0;
		while (e < eN || attempts > maxAttempts) {
			int vi = r.nextInt(vN);
			int vj = r.nextInt(vN);
			if (vi == vj || g.hasEdge(vi, vj)) {
				continue;
			} else {
				g.add(vi, vj);
				e++;
			}
		}
		check(g);
	}

}
