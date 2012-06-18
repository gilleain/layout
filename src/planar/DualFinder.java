package planar;

import java.util.List;

import model.Graph;

/**
 * Simplistic method to find the (inner) dual of a planar graph.
 * 
 * @author maclean
 *
 */
public class DualFinder {
    
    public static Graph getDual(BlockEmbedding e) {
        Graph dual = new Graph();
        List<Face> faces = e.getFaces();
        for (int i = 0; i < faces.size(); i++) {
            Face faceI = faces.get(i);
            for (int j = i + 1; j < faces.size(); j++) {
                Face faceJ = faces.get(j);
                if (faceI.sharesEdge(faceJ)) {
                    dual.makeEdge(i, j);
                }
            }
        }
        return dual;
    }
    
}
