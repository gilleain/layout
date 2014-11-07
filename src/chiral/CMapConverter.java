package chiral;

import graph.model.Graph;
import graph.model.Vertex;

/**
 * Converts combinatorial maps to graphs.
 * 
 * @author maclean
 *
 */
public class CMapConverter {
    
    public static Graph toGraph(CombinatorialMap combinatorialMap) {
        Graph g = new Graph();
        for (Vertex v : combinatorialMap) {
            int vi = v.getIndex();
            for (Vertex w : combinatorialMap.getNeighbours(v)) {
                int wi = w.getIndex();
                if (!g.hasEdge(vi, wi)) {
                    g.makeEdge(vi, wi);
                }
            }
        }
        return g;
    }
    
}
