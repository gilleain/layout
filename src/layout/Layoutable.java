package layout;

import java.util.List;
import java.util.Map;

/**
 * A layoutable object is a graph-like object that can be laid out.
 * 
 * @author maclean
 *
 */
public interface Layoutable {

    /**
     * Get the indices of vertices connected to this vertex.
     * 
     * @param root
     * @return
     */
    List<Integer> getConnected(int vertex);

    /**
     * Get the number of edges.
     * 
     * @return
     */
    int esize();

    /**
     * Get the number of vertices.
     * @return
     */
    int vsize();

    /**
     * Get all the connections between vertices.
     * 
     * @return
     */
    Map<Integer, List<Integer>> getConnectionTable();

}
