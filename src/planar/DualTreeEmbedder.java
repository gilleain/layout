package planar;

import graph.model.Block;
import graph.model.CycleFinder;
import graph.model.Edge;
import graph.model.IntGraph;
import graph.model.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * A specialized embedder for graphs whose dual is a tree, which implies it has an 
 * outerplanar drawing without crossing edges. This applies to most graphs that are 
 * composed of a few fused cycles.
 * 
 * @author maclean
 *
 */
public class DualTreeEmbedder {
	
    public static BlockEmbedding embed(IntGraph graph) {
        return embed(graph, CycleFinder.getMaxCycle(new Block(graph)));
    }
    
	public static BlockEmbedding embed(IntGraph graph, Block outerCycle) {
//	    System.out.println("Outer cycle " + outerCycle);
		BlockEmbedding embedding = new BlockEmbedding(graph, outerCycle);
		Block b = new Block(graph);    // ugh! re-creating block-graph here
		
		// the list of faces
		Face initialFace = new Face(outerCycle);  // XXX unused?
		
		// isn't this just b.difference(outerCycle)?
		List<Edge> bridgeEdges = getBridgeEdges(b, outerCycle);
		for (Edge bridge : bridgeEdges) {
//		    System.out.println("adding bridge edge " + bridge);
		    Face face = getFace(bridge, embedding.getFaces());
		    Path path = new Path(bridge);
		    if (face != null) {
		        embedding.add(path, face);
		    } else {
		        System.out.println("face is null");
		    }
		}
		embedding.setBlock(b);    // XXX FIXME
		return embedding;
	}
	
	private static Face getFace(Edge bridge, List<Face> faces) {
	    for (Face face : faces) {
	        if (face.contains(bridge.getA()) && face.contains(bridge.getB())) {
	            return face;
	        }
	    }
	    return null;
	}
	
	private static List<Edge> getBridgeEdges(Block b, Block outerCycle) {
	    List<Edge> bridgeEdges = new ArrayList<Edge>();
	    for (Edge e : b.getEdges()) {
	        if (outerCycle.hasEdge(e)) {
	            continue;
	        } else {
	            bridgeEdges.add(e);
	        }
	    }
	    return bridgeEdges;
	}

}
