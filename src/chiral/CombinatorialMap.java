package chiral;

import graph.model.Vertex;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A thin wrapper around a map between a vertex and an ordered list 
 * of its neighbours.
 * 
 * @author maclean
 *
 */
public class CombinatorialMap implements Iterable<Vertex> {
    
    private Map<Vertex, List<Vertex>> cm;
    
    public CombinatorialMap(Map<Vertex, List<Vertex>> cm) {
        this.cm = cm;
    }
    
    public List<Vertex> getNeighbours(Vertex v) {
        return this.cm.get(v);
    }
    
    public void addToMap(Vertex key, Vertex... partners) {
        cm.put(key, Arrays.asList(partners));
    }

    @Override
    public Iterator<Vertex> iterator() {
        return this.cm.keySet().iterator();
    }
    
}
