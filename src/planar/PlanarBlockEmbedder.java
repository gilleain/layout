package planar;

import graph.model.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import planar.visitor.ConnectedComponentFinder;

/**
 * Implementation of algorithm due to Demoucron et al from page 88 of Alan Gibbon's book 
 * 'Algorithmic Graph Theory'.
 * 
 * @author maclean
 *
 */
public class PlanarBlockEmbedder {
	
	/**
	 * Embed a graph, assuming that the graph is a single biconnected component (a block).
	 * 
	 * @param graph
	 * @return
	 */
	public static BlockEmbedding embed(Graph graph) {
	    return embed(new Block(graph), graph);
	}

	public static BlockEmbedding embed(Block blockToEmbed, Graph graph) {
//		System.out.println("embedding block");
		// find a circuit of G
		List<Block> cycles = CycleFinder.findAll(blockToEmbed);
//		System.out.println(cycles.size() + " cycles");
		cycles = CycleFinder.getSortedConnectedSimpleCycles(cycles);
//		for (Block cycle : cycles) { System.out.println(cycle); }
		int cycleIndex = 0;
		Block circuit = cycles.get(cycleIndex);
		BlockEmbedding embedding = null;
		// XXX this is not a great idea. We are trying all cycles here to embed
		// in, until we find one that works. If we allowed embedding of paths in 
		// the the outer face, this would not be necessary...
		int numberOfCycles = cycles.size();
		while (embedding == null) {
//			System.out.println("trying " + circuit);
			embedding = embedInCycle(circuit, blockToEmbed, graph);
			cycleIndex++;
			if (cycleIndex == numberOfCycles) {
			    break;
			} else {
			    circuit = cycles.get(cycleIndex);
			}
		}
		return embedding;
	}
	
	public static BlockEmbedding embedInCycle(Block circuit, Block blockToEmbed, Graph graph) {
		Block currentBlock = circuit;
		
		// make an embedding of the circuit
		BlockEmbedding embedding = new BlockEmbedding(graph, circuit);
		
		int faceCount = 1;
		int eulerNumber = blockToEmbed.esize() - blockToEmbed.vsize() + 2;
		boolean embeddable = true;
		
		while (faceCount != eulerNumber && embeddable) {
//			System.out.println("Current block " + currentBlock);
			embedding.setBlock(currentBlock);	// XXX
			List<Bridge> bridges = findBridges(currentBlock, blockToEmbed);
			if (bridges.isEmpty()) {
				return embedding;
			}
//			System.out.println("Bridges " + bridges);
			Map<Bridge, List<Face>> faceMap = embedding.getBridgeFaceCounts(bridges);
			Bridge chosenBridge = chooseBridge(faceMap);
//			System.out.println("Chosen bridge " + bridge + " from " + faceMap);
			if (chosenBridge == null) {
			    Face outerFace = embedding.getExternalFace();
			    boolean embeddedInOuterFace = false;
			    for (Bridge bridge : bridges) {
    			    if (outerFace.containsAllVertices(bridge.getEndpoints())) {
    			        Path path = choosePath(bridge);
    			        embedding.embedInOuterFace(path);
    			        addPathToGraph(currentBlock, path);
    			        faceCount++;
    			        break;
    			    }
			    }
			    if (!embeddedInOuterFace) {
			        return null; // not embeddable
			    }
			} else {
				// choose a face to embed the bridge in
				List<Face> embeddableFaces = faceMap.get(chosenBridge);
				Face face = embeddableFaces.get(0);
//				System.out.println("Chosen face " + face);
				
				// choose a path in the bridge that connects points in the embedding
				Path path = choosePath(chosenBridge);
//				System.out.println("Chosen path " + path);
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
//		System.out.println("Diff " + diff);
		List<Bridge> bridges = new ArrayList<Bridge>();
		
		// first, find isolated edges, and remove them from diff
		for (int i = 0; i < subgraph.vsize(); i++) {
			Vertex vI = subgraph.getVertex(i);
			for (int j = i + 1; j < subgraph.vsize(); j++) {
				Vertex vJ = subgraph.getVertex(j);
				Edge diffEdge = diff.getEdge(vI, vJ);
				if (diffEdge != null && !subgraph.hasEdge(vI, vJ)) {
					diff.edges.remove(diffEdge);
					diff.removeIfDisconnected(vI);
					diff.removeIfDisconnected(vJ);
					Bridge edgeBridge = new Bridge();
					edgeBridge.add(vI);
					edgeBridge.add(vJ);
					edgeBridge.edges.add(diffEdge);
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
			Bridge bridge = new Bridge(component.vertices, component.edges);
			
			// somewhat inefficient
			for (Vertex v : bridge.vertices) {
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
//		System.out.println("endpoints = " + endpoints);
		SpanningTree spanningTree = bridge.getTree(); 
		GraphObject tree = spanningTree.getTree();    // ugh
		for (int i = 0; i < endpoints.size(); i++) {
			Vertex vi = endpoints.get(i);
			int vIdxI = tree.indexOf(vi);
			for (int j = i + 1; j < endpoints.size(); j++) {
				Vertex vj = endpoints.get(j);
				int vIdxJ = tree.indexOf(vj);
//				Path path = bridge.getPath(vi, vj);
				Path path = spanningTree.getPath(vIdxI, vIdxJ);
//				List<Path> paths = bridge.getAllPaths(endpoints, vi, vj);
//				for (Path path : paths) {
//					System.out.println("(" + vi + ", " + vj + ") checking path = " + path);
					
					// yeah, this is stupid, but it works for now
					if (path != null 
							&& !path.contains(endpoints) 
							&& path.hasVertex(vi) && path.hasVertex(vj)) {
						return path;
					}
//				}
			}
		}
		return null;
	}

	private static void addPathToGraph(Block block, Path path) {
		for (Edge edge : path.edges) {
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
			} else if (faces.size() > 1){
				bridge = possibleBridge;
			}
		}
		return bridge;
	}

}
