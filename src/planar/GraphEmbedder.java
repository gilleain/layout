package planar;

import graph.model.Block;
import graph.model.Edge;
import graph.model.IntGraph;
import graph.model.VertexGraph;
import graph.visitor.BlockFinder;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Embed a graph in the plane (if possible) by dividing the graph into parts -
 * blocks (biconnected components) and trees - and then embedding the parts separately.  
 * 
 * @author maclean
 *
 */
public class GraphEmbedder {
    
    public static GraphEmbedding embed(IntGraph graph) {
        
        // find all the blocks in the graph
        BlockFinder finder = new BlockFinder();
        VertexGraph graphObject = new VertexGraph(graph);
        graphObject.accept(finder);
        List<Block> blocks = finder.getBlocks();
        
        // split the blocks into tree-edges and cycle-blocks
        List<Edge> treeEdges = new ArrayList<Edge>();
        List<Block> cycleBlocks = new ArrayList<Block>();
        
        for (Block block : blocks) {
            if (block.getEdgeCount() == 1) {
                treeEdges.add(block.getEdges().get(0));
            } else {
                cycleBlocks.add(block);
            }
        }
        
        // connect up the blocks that are single edges into subgraph trees
        List<Block> treeBlocks = connectTrees(treeEdges);

        // make the graph embedding
        GraphEmbedding graphEmbedding = new GraphEmbedding(treeBlocks);
        for (Block cycleBlock : cycleBlocks) {
//            System.out.println("embedding " + cycleBlock);
            graphEmbedding.addBlockEmbedding(PlanarBlockEmbedder.embed(cycleBlock, graph));
        }
        
        return graphEmbedding;
    }
    
    private static List<Block> connectTrees(List<Edge> treeEdges) {
        List<Block> trees = new ArrayList<Block>();
        int n = treeEdges.size();
        BitSet visited = new BitSet(n);
        for (int index = 0; index < n; index++) {
            if (visited.get(index)) {
                continue;
            } else {
                Block tree = dfsEdge(index, treeEdges, new Block(), visited);
                if (!tree.isEmpty()) {
                    trees.add(tree);
                }
            }
        }
        return trees;
    }
    
    private static Block dfsEdge(int current, List<Edge> edges, Block tree, BitSet visited) {
        if (visited.get(current)) {
            return tree;
        } else {
            visited.set(current);
            Edge currentEdge = edges.get(current);
            tree.add(currentEdge.getA());
            tree.add(currentEdge.getB());
            tree.add(currentEdge.getA(), currentEdge.getB());
            for (int other = 0; other < edges.size(); other++) {
                if (other == current) continue;
                Edge otherEdge = edges.get(other);
                if (currentEdge.adjacent(otherEdge) && !tree.hasEdge(otherEdge)) {
                    dfsEdge(other, edges, tree, visited);
                }
            }
            return tree;
        }
    }
    
}
