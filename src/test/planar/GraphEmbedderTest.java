package test.planar;

import graph.model.Graph;

import java.util.List;

import org.junit.Test;

import planar.Block;
import planar.GraphEmbedder;
import planar.GraphEmbedding;

public class GraphEmbedderTest {
    
    public void test(Graph graph) {
        GraphEmbedding embedding = GraphEmbedder.embed(graph);
        Graph partGraph = embedding.getPartGraph();
        List<Block> blocks = embedding.getBlockParts();
        System.out.println("B = " + blocks);
        
        List<Block> trees = embedding.getTreeParts();
        System.out.println("T = " + trees);
        System.out.println(partGraph);
    }
    
    @Test
    public void triangularDumbellGraph() {
        test(new Graph("0:1,0:2,1:2,2:3,3:4,4:5,5:6,5:7,6:7"));
    }
    
    @Test
    public void disconnectedTrees() {
        test(new Graph("0:1,0:2,1:2,2:3,3:4,3:7,4:5,4:6,5:6,7:8,7:9,8:9," +
        		       "9:10,10:11,10:14,11:12,11:13,12:13,14:15,14:16,15:16"));
    }
    
}
