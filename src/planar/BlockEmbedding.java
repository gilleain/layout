package planar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Graph;

/**
 * The embedding for a single block (biconnected subgraph) of a graph, as a set of faces and a
 * combinatorial map of the vertices.
 * 
 * @author maclean
 *
 */
public class BlockEmbedding {
	
	/**
	 * The underlying graph.
	 */
	private Graph graph;
	
	/**
	 * The block that this embedding is of...
	 * XXX : should this be passed to the constructor?
	 */
	private Block block;
	
	/**
	 * The faces of the embedding.
	 */
	private List<Face> faces;
	
	/**
	 * The embedding, as an ordering of the neighbours of each vertex.
	 */
	private Map<Vertex, List<Vertex>> combinatorialMap;
	
	/**
	 * The vertices on the outside of the embedding.
	 */
	private Face externalFace;
	
	/**
	 * The edges of the dual graph whose vertices are faces.
	 */
	private List<DualEdge> dualEdges;
	
	public BlockEmbedding(Graph graph) {
		this.graph = graph;
		combinatorialMap = new HashMap<Vertex, List<Vertex>>();
		faces = new ArrayList<Face>();
		dualEdges = new ArrayList<DualEdge>();
	}
	
	public BlockEmbedding(Graph graph, Block circuit) {
		this(graph);
		
		// this is wrong : if bridges are embedded in external face, this changes 
		externalFace = embedCircuit(circuit); 
		faces.add(externalFace);
	}
	
	public void addDualEdge(Face faceA, Face faceB) {
	    dualEdges.add(new DualEdge(faceA, faceB));
	}
		
	/**
	 * TODO : remove or change this!
	 * @param block
	 */
	public void setBlock(Block block) {
		this.block = block;		//XXX!
	}
	
	public int size() {
		return combinatorialMap.size();
	}
	
	/**
	 * Embed a circuit, returning it as a face. The edges of the face are in cyclic order. 
	 * The vertices are added to the combinatorial map.
	 * 
	 * @param circuit
	 * @return
	 */
	private Face embedCircuit(Block circuit) {
		Vertex start = circuit.getVertex(0);
		Face face = new Face(circuit.vertices);
		embedCircuit(circuit, null, start, start, face);
		return face;
	}
	
	// XXX seems pointless - cycles are always degree-2 : why not just 
	// add each neighbour of each vertex...
	private void embedCircuit(
	        Block circuit, Vertex previous, Vertex current, Vertex start, Face face) {
//		System.out.println("embedding " + previous + " " + current);
		List<Vertex> neigbours = new ArrayList<Vertex>();
		if (previous != null) {
			neigbours.add(previous);
			face.add(previous, current);
		}
		combinatorialMap.put(current, neigbours);
		for (Vertex next : circuit.getConnected(current)) {
//			System.out.print("next " + next);
			if (next == previous || combinatorialMap.containsKey(next)) {
				if (next == start && next != previous) {
//					System.out.println(" == start");
					face.add(current, start);
					neigbours.add(next);
					
					// add to the front, to preserve orientation
					combinatorialMap.get(start).add(0, current);
				} else {
//					System.out.println(" seen");
					continue;
				}
			} else {
//				System.out.println(" adding");
				neigbours.add(next);
				embedCircuit(circuit, current, next, start, face);
			}
		}
	}
	
	public Face getExternalFace() {
		return externalFace;
	}
	
	public List<Face> getFaces() {
		return faces;
	}
	
	public Map<Vertex, List<Vertex>> getCM() {
		return combinatorialMap;
	}
	
	public Graph getGraph() {
		return graph;
	}

	public void embedInOuterFace(Path path) {
	    Path rev = path.reverse();
        Vertex start = rev.getVertex(0);
        Vertex end   = rev.getVertex(rev.vsize() - 1);
        
        Face faceL = externalFace.getStartToEndFace(start, end, path);
        Face faceR = externalFace.getEndToStartFace(start, end, path);
        
        embed(rev, start, end);
        
        externalFace = faceL;
        faces.add(faceR);
    }

    /**
	 * Add a path to the embedding, putting it in the specified face.
	 * 
	 * @param path
	 * @param face
	 */
	public void add(Path path, Face face) {
//		System.out.println("cm was" + combinatorialMap);
		faces.remove(face);
		
		// split the face in two by adding the path
		Vertex start = path.getVertex(0);
		Vertex end   = path.getVertex(path.vsize() - 1);
		
		Face faceL = face.getStartToEndFace(start, end, path);
//		System.out.println("face L = " + faceL);
		Face faceR = face.getEndToStartFace(start, end, path);
//		System.out.println("face R = " + faceR);
		
		// now add the path vertices to the CM
		embed(path, start, end);
		
		faces.add(faceL);
		faces.add(faceR);
//		System.out.println("cm now" + combinatorialMap);
	}
	
	private void embed(Path path, Vertex start, Vertex end) {
		Vertex startPartner = path.edges.get(0).getB();
		Vertex endPartner   = path.edges.get(path.esize() - 1).getA();
		
		// start vertex
		if (!combinatorialMap.containsKey(start)) {
			combinatorialMap.put(start, new ArrayList<Vertex>());
		}
		combinatorialMap.get(start).add(startPartner);
		
		// internal vertices of path
		Vertex previous = start;
		for (int vIndex = 1; vIndex < path.vsize() - 1; vIndex++) {
			Vertex vertex = path.getVertex(vIndex); 
			if (!combinatorialMap.containsKey(vertex)) {
				combinatorialMap.put(vertex, new ArrayList<Vertex>());
			}
			combinatorialMap.get(vertex).add(previous);
			if (vIndex < path.vsize() - 1) {
				Vertex next = path.getVertex(vIndex + 1);
				combinatorialMap.get(vertex).add(next);
			}
			previous = vertex;
		}
		
		// end vertex
		if (!combinatorialMap.containsKey(end)) {
			combinatorialMap.put(end, new ArrayList<Vertex>());
		}
		combinatorialMap.get(end).add(endPartner);
	}

	public void add(BlockEmbedding blockEmbedding) {
		faces.addAll(blockEmbedding.getFaces());
		for (Vertex v : blockEmbedding.combinatorialMap.keySet()) {
			List<Vertex> vertices = blockEmbedding.combinatorialMap.get(v); 
			combinatorialMap.put(v, vertices);
		}
		
		// TODO : FIXME!
		externalFace = blockEmbedding.externalFace;
		block = blockEmbedding.block;	// UGH
	}
	
	/**
	 * For each bridge, get a list of the faces that is can be drawn in.
	 * 
	 * @param currentEmbedding
	 * @param bridges
	 * @return
	 */
	public Map<Bridge, List<Face>> getBridgeFaceCounts(List<Bridge> bridges) {
		Map<Bridge, List<Face>> drawableFaceMap = new HashMap<Bridge, List<Face>>();
		for (Bridge bridge : bridges) {
			drawableFaceMap.put(bridge, getDrawableFaces(bridge));
		}
		return drawableFaceMap;
	}
	
	public List<Face> getDrawableFaces(Bridge bridge) {
		List<Face> drawableFaces = new ArrayList<Face>();
		for (Face face : faces) {
			// basically, a bridge is drawable in a face only if 
			// all its endpoints are in vertex list of that face 
//		    System.out.println("checking face " + face + " and " + bridge.getEndpoints());
			if (face.containsAllVertices(bridge.getEndpoints())) {
				drawableFaces.add(face);
			}
		}
		return drawableFaces;
	}
	
	public String toString() {
		return "F : " + faces + " CM : " + combinatorialMap + " EF : " + externalFace; 
	}

	public Vertex getVertexWithIndex(int index) {
		return block.getVertexWithIndex(index);
	}

    public Block getBlock() {
        return block;
    }

}
