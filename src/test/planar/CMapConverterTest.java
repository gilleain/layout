package test.planar;

import graph.model.Graph;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import planar.CMapConverter;
import planar.Vertex;

public class CMapConverterTest {
    
    public void addToMap(Map<Vertex, List<Vertex>> cm, Vertex key, Vertex... partners) {
        cm.put(key, Arrays.asList(partners));
    }
    
    @Test
    public void testK4_EmbedA() {
        Map<Vertex, List<Vertex>> cm = new HashMap<Vertex, List<Vertex>>();
        addToMap(cm, new Vertex(0), new Vertex(1), new Vertex(3), new Vertex(2));
        addToMap(cm, new Vertex(1), new Vertex(0), new Vertex(2), new Vertex(3));
        addToMap(cm, new Vertex(2), new Vertex(0), new Vertex(3), new Vertex(1));
        addToMap(cm, new Vertex(3), new Vertex(0), new Vertex(1), new Vertex(2));
        Graph g = CMapConverter.toGraph(cm);
        System.out.println(g);
    }
    
    @Test
    public void testK4_EmbedB() {
        Map<Vertex, List<Vertex>> cm = new HashMap<Vertex, List<Vertex>>();
        addToMap(cm, new Vertex(0), new Vertex(2), new Vertex(3), new Vertex(1));
        addToMap(cm, new Vertex(1), new Vertex(0), new Vertex(3), new Vertex(2));
        addToMap(cm, new Vertex(2), new Vertex(0), new Vertex(1), new Vertex(3));
        addToMap(cm, new Vertex(3), new Vertex(0), new Vertex(2), new Vertex(1));
        Graph g = CMapConverter.toGraph(cm);
        System.out.println(g);
    }
    
}
