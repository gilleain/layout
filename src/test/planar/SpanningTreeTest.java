package test.planar;

import model.Graph;

import org.junit.Test;

import planar.GraphObject;
import planar.SpanningTree;

public class SpanningTreeTest {
    
    @Test
    public void testSimpleCycle() {
        GraphObject cycle = new GraphObject(new Graph("0:1,0:5,1:2,2:3,3:4,4:5"));
        SpanningTree tree = new SpanningTree(cycle);
        System.out.println(tree);
    }
    
}
