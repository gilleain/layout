package test.chiral;

import graph.model.Graph;
import graph.model.Vertex;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import chiral.CMapConverter;
import chiral.CombinatorialMap;

public class CMapConverterTest {
    
    @Test
    public void testK4_EmbedA() {
        CombinatorialMap cm = new CombinatorialMap(new HashMap<Vertex, List<Vertex>>());
        cm.addToMap(new Vertex(0), new Vertex(1), new Vertex(3), new Vertex(2));
        cm.addToMap(new Vertex(1), new Vertex(0), new Vertex(2), new Vertex(3));
        cm.addToMap(new Vertex(2), new Vertex(0), new Vertex(3), new Vertex(1));
        cm.addToMap(new Vertex(3), new Vertex(0), new Vertex(1), new Vertex(2));
        Graph g = CMapConverter.toGraph(cm);
        System.out.println(g);
    }
    
    @Test
    public void testK4_EmbedB() {
        CombinatorialMap cm = new CombinatorialMap(new HashMap<Vertex, List<Vertex>>());
        cm.addToMap(new Vertex(0), new Vertex(2), new Vertex(3), new Vertex(1));
        cm.addToMap(new Vertex(1), new Vertex(0), new Vertex(3), new Vertex(2));
        cm.addToMap(new Vertex(2), new Vertex(0), new Vertex(1), new Vertex(3));
        cm.addToMap(new Vertex(3), new Vertex(0), new Vertex(2), new Vertex(1));
        Graph g = CMapConverter.toGraph(cm);
        System.out.println(g);
    }
    
}
