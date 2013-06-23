package planar;

import graph.model.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The embedding of an entire graph, composed of a set of blocks - or biconnected
 * components - and trees connecting these blocks, or connected to them.
 * 
 * @author maclean
 *
 */
public class GraphEmbedding {
    
    private List<BlockEmbedding> blockEmbeddings;
    
    private List<Block> trees;
    
    private Map<Vertex, List<Vertex>> cm;
    
    public GraphEmbedding() {
        blockEmbeddings = new ArrayList<BlockEmbedding>();
        trees = new ArrayList<Block>();
    }
    
    public GraphEmbedding(List<Block> trees) {
        blockEmbeddings = new ArrayList<BlockEmbedding>();
        this.trees = trees;
        cm = new HashMap<Vertex, List<Vertex>>();
    }
    
    public void addBlockEmbedding(BlockEmbedding blockEmbedding) {
        if (blockEmbedding == null) {
            //??
        } else {
            blockEmbeddings.add(blockEmbedding);
            cm.putAll(blockEmbedding.getCM());
        }
    }
    
    public Map<Vertex, List<Vertex>> getCombinatorialMap() {
        return cm;
    }
    
    public Graph getPartGraph() {
        Graph partGraph = new Graph();
        int b = blockEmbeddings.size();
        int t = trees.size(); 
        for (int blockIndexI = 0; blockIndexI < b; blockIndexI++) {
            Block blockI = blockEmbeddings.get(blockIndexI).getBlock();
            
            // check block-block edges
            for (int blockIndexJ = blockIndexI + 1; blockIndexJ < b; blockIndexJ++) {
                Block blockJ = blockEmbeddings.get(blockIndexJ).getBlock();
                if (blockI.connectedTo(blockJ)) {
                    partGraph.makeEdge(blockIndexI, blockIndexJ);
                }
            }
            // check block-tree edges
            for (int treeIndex = 0; treeIndex < t; treeIndex++) {
                Block tree = trees.get(treeIndex);
                if (blockI.connectedTo(tree)) {
                    // offset the tree index to account for the non-tree parts
                    partGraph.makeEdge(blockIndexI, treeIndex + b);
                }
            }
        }
        return partGraph;
    }

    public List<Block> getBlockParts() {
        List<Block> blocks = new ArrayList<Block>();
        for (BlockEmbedding embedding : blockEmbeddings) {
            blocks.add(embedding.getBlock());
        }
        return blocks;
    }
    
    public List<Block> getTreeParts() {
        return trees;
    }

    public Block getPart(int partIndex) {
        int bSize = blockEmbeddings.size();
        if (partIndex < bSize) {
            Block b = blockEmbeddings.get(partIndex).getBlock(); 
            return b;
        } else {
            return trees.get(partIndex - bSize);
        }
    }

    public boolean isTreePart(int partIndex) {
        return partIndex >= blockEmbeddings.size();
    }

    public BlockEmbedding getBlockPart(int partIndex) {
        if (partIndex <= blockEmbeddings.size()) {
            return blockEmbeddings.get(partIndex);
        } else {
            return null;
        }
    }

    public List<Vertex> getConnectedInOuterCycle(int partIndex, Vertex vertex) {
        BlockEmbedding embedding = blockEmbeddings.get(partIndex);
        Face face = embedding.getExternalFace(); 
        return face.getConnected(vertex);
    }

    public BlockEmbedding getBlockEmbedding(int blockIndex) {
        return blockEmbeddings.get(blockIndex);
    }
}
