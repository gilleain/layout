package planar;

import graph.model.IntGraph;

import java.util.List;

/**
 * Simplistic method to find the (inner) dual of a planar graph.
 * 
 * @author maclean
 *
 */
public class DualFinder {
    
    public static IntGraph getDual(BlockEmbedding e) {
        IntGraph dual = new IntGraph();
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
