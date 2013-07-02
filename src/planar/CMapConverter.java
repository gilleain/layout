package planar;

import graph.model.Graph;

import java.util.List;
import java.util.Map;

/**
 * Converts combinatorial maps to graphs.
 * 
 * @author maclean
 *
 */
public class CMapConverter {
    
    public static Graph toGraph(Map<Vertex, List<Vertex>> combinatorialMap) {
        Graph g = new Graph();
        for (Vertex v : combinatorialMap.keySet()) {
            int vi = v.getIndex();
            for (Vertex w : combinatorialMap.get(v)) {
                int wi = w.getIndex();
                if (!g.hasEdge(vi, wi)) {
                    g.makeEdge(vi, wi);
                }
            }
        }
        return g;
    }
    
}
