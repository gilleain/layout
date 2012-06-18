package planar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Stack;

public class SpanningTree extends GraphObject {
    
    private GraphObject g;
    
    private List<Edge> cycleEdges;

    private GraphObject tree;
    
    public SpanningTree(GraphObject g) {
        this.g = g;
        this.cycleEdges = new ArrayList<Edge>();
        this.tree = makeSpanningTree(cycleEdges);
    }
    
    public List<Edge> getCycleEdges() {
        return cycleEdges;
    }
    
    public GraphObject getTree() {
        return tree;
    }
    
    private GraphObject makeSpanningTree(List<Edge> cycleEdges) {
        // make the tree
        int depth = 1;
        int n = g.vsize();
        int[] depthList = new int[n];
        GraphObject tree = new GraphObject();
        int index = 0;
        while (index < n) {
            depth = dfs(index, depthList, depth, tree, cycleEdges);
            // XXX if there are multiple components (ie: graph disconnected)
            // then each one is a separate call to dfs?
            for (; index < n; index++) {
                if (depthList[index] == 0)
                    break;
            }
        }
        return tree;
    }
    
    private int dfs(int index, int[] depthList, int depth, GraphObject  tree, List<Edge> cycleEdges) {
        depthList[index] = depth;
        Vertex vertex = g.getVertex(index);
        if (!tree.hasVertex(vertex)) {
            tree.add(vertex);
        }
        int j = depth;
        for (int otherIndex : g.getConnectedIndices(vertex)) {
            Vertex other = g.getVertex(otherIndex);
            if (depthList[otherIndex] == 0) {
                tree.add(other);
                tree.edges.add(new Edge(vertex, other));
                j = dfs(otherIndex, depthList, depth + 1, tree, cycleEdges);
            } else {
                Edge edge = new Edge(vertex, other);
                if (tree.hasEdge(vertex, other) || cycleEdges.contains(edge)) {
                    continue;
                } else {
                    cycleEdges.add(edge);
                }
            }
        }
        return j;
    }
    
    public Path getPath(Vertex vi, Vertex vj) {
        return getPath(tree.indexOf(vi), tree.indexOf(vj));
    }
    
    public Path getPath(int vIdxI, int vIdxJ) {
        Path path = new Path();
        Vertex vI = tree.getVertex(vIdxI);
        Vertex vJ = tree.getVertex(vIdxJ);
        
        // label each vertex with its depth from vi
        int[] labels = new int[tree.vsize()];
        labels[vIdxI] = 1;
        Stack<Vertex> toVisit = new Stack<Vertex>();
        toVisit.push(vI);
        while (!toVisit.isEmpty()) {
            Vertex v = toVisit.pop();
            int l = labels[tree.indexOf(v)];
            for (int nIndex : tree.getConnectedIndices(v)) {
                Vertex n = tree.getVertex(nIndex);
                if (labels[nIndex] == 0) {
                    labels[nIndex] = l + 1;
                    toVisit.push(n);
                }
            }
        }
        
        // construct the path by iterating from vj->vi, following only decreasing labels
        path.add(vJ);
        Vertex prev = vJ;
        BitSet visited = new BitSet(tree.vsize());
        visited.set(vIdxJ);
        int prevLabel = labels[tree.indexOf(prev)];
        while (!prev.equals(vI)) {
            Vertex current = null;
            for (int nIdx : tree.getConnectedIndices(prev)) {
                Vertex neighbour = tree.getVertex(nIdx);
                if (!visited.get(nIdx) && labels[nIdx] < prevLabel) {
                    prevLabel = labels[nIdx]; 
                    visited.set(nIdx);
                    current = neighbour;
                    break;
                }
            }
            path.add(current);
            path.add(prev, current);
            if (current == null) { 
                System.out.println("ERROR " + path + " " + vI + " " + vJ + " " + tree + " " + Arrays.toString(labels));
            }
            prev = current;
        }
        
        return path;
    }
    
    public String toString() {
        return "T = " + tree + " C =" + cycleEdges;  
    }
    
}
