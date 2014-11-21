package planar;

import graph.model.Block;
import graph.model.CycleFinder;
import graph.model.Edge;
import graph.model.IntGraph;
import graph.model.GraphObject;
import graph.model.Path;
import graph.model.SpanningTree;
import graph.model.Vertex;
import graph.visitor.ConnectedComponentFinder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Implementation of algorithm due to Demoucron et al from page 88 of Alan
 * Gibbon's book 'Algorithmic Graph Theory'.
 * 
 * @author maclean
 * 
 */
public class PlanarBlockEmbedder {

    /**
     * Embed a graph, assuming that the graph is a single biconnected component
     * (a block).
     * 
     * @param atomContainer
     * @return
     */
    public static BlockEmbedding embed(IntGraph g) {
        return embed(new Block(g), g);
    }

    /**
     * Embed the specified block of the atom container.
     *  
     * @param blockToEmbed
     * @param atomContainer
     * @return
     */
    public static BlockEmbedding embed(Block blockToEmbed, IntGraph g) {
//         System.out.println("embedding block");
        // find a circuit of G
//        List<Block> cycles = CycleFinder.findAll(blockToEmbed);
//        
//        cycles = CycleFinder.getSortedConnectedSimpleCycles(cycles);
        
        // XXX this is not a great idea. We are trying all cycles here to embed
        // in, until we find one that works. If we allowed embedding of paths in
        // the the outer face, this would not be necessary...
        Iterator<Block> cycleStream = CycleFinder.getCycleStream(blockToEmbed, true); 
        while (cycleStream.hasNext()) {
            Block cycle = cycleStream.next();
            BlockEmbedding embedding = embedInCycle(cycle, blockToEmbed, g);
            if (embedding != null) {
                return embedding;
            }
        }
        return null;
    }

    /**
     * Try to embed the atom container using the supplied circuit. Returns
     * null if it is not possible to embed in this circuit.
     *  
     * @param circuit
     * @param blockToEmbed
     * @param atomContainer
     * @return
     */
    public static BlockEmbedding embedInCycle(
            Block circuit, Block blockToEmbed, IntGraph g) {
        Block currentBlock = circuit;

        // make an embedding of the circuit
        BlockEmbedding embedding = new BlockEmbedding(g, circuit);

        int faceCount = 1;
        int eulerNumber = blockToEmbed.esize() - blockToEmbed.vsize() + 2;
        boolean embeddable = true;

        while (faceCount != eulerNumber && embeddable) {
//             System.out.println("Current block " + currentBlock);
            embedding.setBlock(currentBlock); // XXX
            List<Bridge> bridges = findBridges(currentBlock, blockToEmbed);
            if (bridges.isEmpty()) {
                return embedding;
            }
            // System.out.println("Bridges " + bridges);
            Map<Bridge, List<Face>> faceMap = 
                    embedding.getBridgeFaceCounts(bridges);
            Bridge chosenBridge = chooseBridge(faceMap);
//            System.out.println("Chosen bridge " + chosenBridge + " from " + faceMap);
            if (chosenBridge == null) {
                Face outerFace = embedding.getExternalFace();
//                System.out.println("embedding in outer face " + outerFace);
                boolean embeddedInOuterFace = false;
                for (Bridge bridge : bridges) {
                    if (outerFace.containsAllVertices(bridge.getEndpoints())) {
                        Path path = choosePath(bridge);
                        embedding.embedInOuterFace(path);
                        addPathToGraph(currentBlock, path);
                        faceCount++;
                        embeddedInOuterFace = true;
                        break;
                    }
                }
                if (!embeddedInOuterFace) {
//                    System.out.println("not embeddable");
                    return null; // not embeddable
                }
            } else {
                // choose a face to embed the bridge in
                List<Face> embeddableFaces = faceMap.get(chosenBridge);
                Face face = embeddableFaces.get(0);
                // System.out.println("Chosen face " + face);

                // choose a path in the bridge that connects points in the
                // embedding
                Path path = choosePath(chosenBridge);
                // System.out.println("Chosen path " + path);
                embedding.add(path, face);
                addPathToGraph(currentBlock, path);
                faceCount++;
            }
        }
        return embedding;
    }

    /**
     * Find all the bridges of a graph relative to a subgraph
     * 
     * @param subgraph
     * @param graph
     * @return
     */
    private static List<Bridge> findBridges(Block subgraph, Block graph) {
        // get the graph minus the subgraph
        GraphObject diff = graph.difference(subgraph);
        // System.out.println("Diff " + diff);
        List<Bridge> bridges = new ArrayList<Bridge>();

        // first, find isolated edges, and remove them from diff
        for (int i = 0; i < subgraph.vsize(); i++) {
            Vertex vI = subgraph.getVertex(i);
            for (int j = i + 1; j < subgraph.vsize(); j++) {
                Vertex vJ = subgraph.getVertex(j);
                Edge diffEdge = diff.getEdge(vI, vJ);
                if (diffEdge != null && !subgraph.hasEdge(vI, vJ)) {
                    diff.getEdges().remove(diffEdge);
                    diff.removeIfDisconnected(vI);
                    diff.removeIfDisconnected(vJ);
                    Bridge edgeBridge = new Bridge();
                    edgeBridge.add(vI);
                    edgeBridge.add(vJ);
                    edgeBridge.getEdges().add(diffEdge);
                    edgeBridge.addEndpoint(vI);
                    edgeBridge.addEndpoint(vJ);
                    bridges.add(edgeBridge);
                }
            }
        }

        // find the connected components
        ConnectedComponentFinder finder = new ConnectedComponentFinder();
        diff.accept(finder);

        // convert each component to a bridge
        for (Block component : finder.getComponents()) {
            Bridge bridge = new Bridge(component);

            // somewhat inefficient
            for (Vertex v : bridge) {
                if (subgraph.hasVertex(v)) {
                    bridge.addEndpoint(v);
                }
            }
            bridges.add(bridge);
        }
        return bridges;
    }

    private static Path choosePath(Bridge bridge) {
        List<Vertex> endpoints = bridge.getEndpoints();
        // System.out.println("endpoints = " + endpoints);
        SpanningTree spanningTree = bridge.getTree();
        GraphObject tree = spanningTree.getTree(); // ugh
        for (int i = 0; i < endpoints.size(); i++) {
            Vertex vi = endpoints.get(i);
            int vIdxI = tree.indexOf(vi);
            for (int j = i + 1; j < endpoints.size(); j++) {
                Vertex vj = endpoints.get(j);
                int vIdxJ = tree.indexOf(vj);
                // Path path = bridge.getPath(vi, vj);
                Path path = spanningTree.getPath(vIdxI, vIdxJ);
                // List<Path> paths = bridge.getAllPaths(endpoints, vi, vj);
                // for (Path path : paths) {
                // System.out.println("(" + vi + ", " + vj +
                // ") checking path = " + path);

                // yeah, this is stupid, but it works for now
                if (path != null && !path.contains(endpoints)
                        && path.hasVertex(vi) && path.hasVertex(vj)) {
                    return path;
                }
                // }
            }
        }
        return null;
    }

    private static void addPathToGraph(Block block, Path path) {
        for (Edge edge : path.getEdges()) {
            if (!block.hasVertex(edge.getA())) {
                block.add(edge.getA());
            }
            if (!block.hasVertex(edge.getB())) {
                block.add(edge.getB());
            }
            block.add(edge.getA(), edge.getB());
        }
    }

    private static Bridge chooseBridge(Map<Bridge, List<Face>> faceMap) {
        Bridge bridge = null;
        for (Bridge possibleBridge : faceMap.keySet()) {
            List<Face> faces = faceMap.get(possibleBridge);
            if (faces.size() == 1) {
                return possibleBridge;
            } else if (faces.size() > 1) {
                bridge = possibleBridge;
            }
        }
        return bridge;
    }

}
