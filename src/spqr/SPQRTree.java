package spqr;

import graph.model.IntEdge;
import graph.model.IntGraph;

import java.util.List;

public class SPQRTree {
    
    public abstract class Node {
        
        public IntGraph skeleton;
        
        public IntEdge associatedEdge;
        
        public List<Node> children;
        
    }
    
    public class SNode extends Node {
        
    }
    
    public class PNode extends Node {
        
    }
    
    public class QNode extends Node {
        
    }
    
    public class RNode extends Node {
        
    }
    
    public SPQRTree(IntGraph graph) {
        Node preTree = createPreTree(graph, graph.edges.get(0));
    }

    private Node createPreTree(IntGraph graph, IntEdge refEdge) {
        // Trivial case : graph is just 2 parallel edges
        new QNode();
        
        // Parallel case
        new PNode();
        
        // Series case
        new SNode();
        
        // Rigid case
        new RNode();
        
        return null;   // FIXME
    }

}
