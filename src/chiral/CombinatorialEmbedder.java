package chiral;

import graph.model.Graph;
import graph.tree.TreeCenterFinder;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Map;

import planar.GraphEmbedder;
import planar.GraphEmbedding;
import planar.Vertex;
import draw.Representation;

/**
 * Temporary hack class to get embedding working for combinatorial
 * maps, rather than graphs.
 * 
 * In other words, a CM is provided (as a map from vertices to lists
 * of vertices) specifying the orientation of the neighbours of each
 * vertex.
 * 
 * @author maclean
 *
 */
public class CombinatorialEmbedder {
    
    public static Representation layoutCM(CombinatorialMap cm, Rectangle2D canvas) {
        Representation rep = new Representation();
        
        // convert the map to a graph so that we can analyse the parts
        Graph g = CMapConverter.toGraph(cm);
        
        // we still need to embed this graph, in order to get the blocks 
        GraphEmbedding embedding = GraphEmbedder.embed(g);
        
        Graph partTree = embedding.getPartGraph();
        int centerIndex;
        if (partTree.esize() == 0) {
            centerIndex = 0;
        } else {
            centerIndex = TreeCenterFinder.findUniqueCenter(partTree);
            if (centerIndex == -1) {
                // XXX = the part graph is a cycle! - arbitrary choice!
                centerIndex = 0;
            }
        }
        
        Point2D center = new Point2D.Double(canvas.getCenterX(), canvas.getCenterY());
        
        return rep;
    }
    
}
