package test.planar;

import graph.model.IntGraph;
import graph.model.GraphObject;
import graph.model.SpanningTree;

import org.junit.Test;

public class SpanningTreeTest {
    
    @Test
    public void testSimpleCycle() {
        GraphObject cycle = new GraphObject(new IntGraph("0:1,0:5,1:2,2:3,3:4,4:5"));
        SpanningTree tree = new SpanningTree(cycle);
        System.out.println(tree);
    }
    
}
