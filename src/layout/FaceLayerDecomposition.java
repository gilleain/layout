package layout;

import graph.model.Block;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;

import planar.BlockEmbedding;
import planar.Face;

/**
 * 'Decompose' the faces of an embedding into concentric layers (if possible).
 * 
 * @author maclean
 *
 */
public class FaceLayerDecomposition {
    
    private List<List<Integer>> faceLayers;
    
    public FaceLayerDecomposition(BlockEmbedding embedding) {
        Block dual = embedding.calculateInnerDual();
        int count = dual.getVertexCount();
        faceLayers = new ArrayList<List<Integer>>();
        
        Face outerFace = embedding.getExternalFace();
        List<Face> faces = embedding.getFaces();
        BitSet seen = new BitSet(count);
        
        // get the outer edge of faces
        List<Integer> currentLayer = new ArrayList<Integer>();
        for (int faceIndex = 0; faceIndex < count; faceIndex++) {
            Face face = faces.get(faceIndex);
            if (face.sharesEdge(outerFace)) {
                currentLayer.add(faceIndex);
                seen.set(faceIndex);
            }
        }
        faceLayers.add(currentLayer);
        
        // go inwards
        while (seen.cardinality() < count) {
            List<Integer> nextLayer = new ArrayList<Integer>();
            for (int faceIndex : currentLayer) {
                for (int connectedFace : dual.getConnected(faceIndex)) {
                    if (!seen.get(connectedFace)) {
                        nextLayer.add(connectedFace);
                        seen.set(connectedFace);
                    }
                }
            }
            faceLayers.add(nextLayer);
            currentLayer = nextLayer;
        }
    }
    
    /**
     * Iterate through the face layers in reverse order of discovery - in other words,
     * from the inside-out.
     * 
     * @return
     */
    public Iterator<List<Integer>> getInnerLayers() {
        return new Iterator<List<Integer>>() {
            
            private int index = faceLayers.size() - 1;

            @Override
            public boolean hasNext() {
                return index > 0;
            }

            @Override
            public List<Integer> next() {
                index--;
                return faceLayers.get(index);
            }

            @Override
            public void remove() {}
            
        };
        
    }
    
    /**
     * Get the innermost layer.
     * 
     * @return
     */
    public List<Integer> getCore() {
        return faceLayers.get(faceLayers.size() - 1);
    }
    
    public List<List<Integer>> getFaceLayers() {
        return faceLayers;
    }

}
