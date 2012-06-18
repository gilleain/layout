package planar.visitor;

import java.util.ArrayList;
import java.util.List;

import planar.Block;
import planar.GraphObject;
import planar.Vertex;

/**
 * Inefficiently find circuits possible from a DFS.
 * 
 * NOTE : may return incorrect cycles!
 * 
 * @author maclean
 *
 */
public class SimpleCircuitFinder implements DFSVisitor {
	
	private List<Vertex> visited;
	
	private List<Block> circuits;
	
	private Block currentCircuit;
	
	public SimpleCircuitFinder() {
		reset();
	}
	
	public void reset() {
	    visited = new ArrayList<Vertex>();
        circuits = new ArrayList<Block>();
        currentCircuit = new Block();
	}

	public void visit(GraphObject g, Vertex root) {
		if (visited.contains(root)) {
			return;
		}
//		System.out.println("root : " + root);
		visited.add(root);
		for (Vertex o : g.getConnected(root)) {
//			System.out.println(root + " -> " + o);
			if (visited.contains(o)) {
				if (currentCircuit.hasEdge(root, o)) {
					continue;
				} else { 
//					System.out.println("adding " + o + " to circuit : " + currentCircuit);
					currentCircuit.add(root, o);	// close the loop
					if (!currentCircuit.hasVertex(o)) {
						currentCircuit.add(o);
					}
					circuits.add(currentCircuit);
					currentCircuit = new Block();
					return;					// return at the first cycle
				}
			} else {
//				System.out.println("traversing : " + root + " -> " + o);
				traverseEdge(g, root, o);
			}
		}
//		System.out.println("visited " + visited);
	}
	
	private void traverseEdge(GraphObject g, Vertex start, Vertex end) {
		// TODO : all in one method?
		currentCircuit.add(start);
		currentCircuit.add(end);
		currentCircuit.add(start, end);
		visit(g, end);
	}
	
	public List<Block> getCircuits() {
		return circuits;
	}

	public boolean seen(Vertex v) {
		return visited.contains(v);
	}

}
