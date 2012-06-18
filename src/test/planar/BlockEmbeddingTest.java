package test.planar;

import model.Graph;

import org.junit.Test;

import planar.Block;
import planar.BlockEmbedding;
import planar.Face;
import planar.Path;
import planar.Vertex;

public class BlockEmbeddingTest {
    
    @Test
    public void largeCycleTest() {
        Graph graph = new Graph("0:1, 2:3, 4:5, 0:6, 6:7, 7:8, 8:9, 9:1, 2:10, 10:11, " +
        		                "11:12, 12:13, 13:3, 4:14, 14:15, 15:16, 16:17, 17:5");
        Block b = new Block(graph);
        BlockEmbedding em = new BlockEmbedding(graph, b);
        System.out.println(em.getFaces());
    }
	
	@Test
	public void initial5CycleTest() {
		Graph graph = new Graph(); // don't really need this
		Block initialCycle = new Block(5);
		initialCycle.add(0, 1, 4);
		initialCycle.add(1, 2);
		initialCycle.add(2, 3);
		initialCycle.add(3, 4);
		BlockEmbedding em = new BlockEmbedding(graph, initialCycle);
		System.out.println(initialCycle);
		System.out.println(em.getCM());
	}
	
	@Test
	public void initial6CycleTest() {
		Graph graph = new Graph(); // don't really need this
		Block initialCycle = new Block(6);
		initialCycle.add(0, 1, 5);
		initialCycle.add(1, 2);
		initialCycle.add(2, 3);
		initialCycle.add(3, 4);
		initialCycle.add(4, 5);
		BlockEmbedding em = new BlockEmbedding(graph, initialCycle);
		System.out.println(initialCycle);
		System.out.println(em.getCM());
	}
	
	@Test
	public void fourCycleFaceTest() {
		Graph graph = new Graph(); // don't really need this
		Block initialCycle = new Block(4);
		initialCycle.add(0, 2, 3);
		initialCycle.add(1, 2);
		initialCycle.add(1, 3);
		BlockEmbedding em = new BlockEmbedding(graph, initialCycle);
		System.out.println(em.getExternalFace());
		System.out.println(em.getFaces());
	}
	
	@Test
	public void fourCycleFaceSplitTest() {
		Graph graph = new Graph(); // don't really need this
		
		// a four cycle
		Block initialCycle = new Block(4);
		initialCycle.add(0, 1, 3);
		initialCycle.add(1, 2);
		initialCycle.add(2, 3);
		BlockEmbedding em = new BlockEmbedding(graph, initialCycle);
		System.out.println(initialCycle);
		Face face = em.getFaces().get(0);
		System.out.println(face);
		
		// make a path to embed in the cycle
		Path path = new Path();
		path.add(initialCycle.getVertex(0));
		path.add(new Vertex(4));
		path.add(initialCycle.getVertex(2));
		path.add(0, 1);
		path.add(1, 2);
		em.add(path, face);
		System.out.println(em.getFaces());
	}
	
}
