package planar.visitor;

import java.util.ArrayList;
import java.util.List;

import planar.Block;
import planar.GraphObject;
import planar.Vertex;

public class ConnectedComponentFinder implements DFSVisitor {
	
	private List<Vertex> visited;
	
	private List<Block> components;
	
	private int currentComponentIndex;
	
	private boolean shouldSetup;
	
	public ConnectedComponentFinder() {
		setup();
		shouldSetup = false;
	}
	
	private void setup() {
	    visited = new ArrayList<Vertex>();
        components = new ArrayList<Block>();
        currentComponentIndex = 0;
	}

	public void visit(GraphObject g, Vertex v) {
	    if (shouldSetup) setup();
	    
		Block currentComponent = new Block(); 
		dfs(g, v, currentComponent);
		if (currentComponent.isEmpty()) {
			return;
		} else {
			components.add(currentComponent);
			currentComponentIndex++;
		}
	}
	
	private void dfs(GraphObject g, Vertex v, Block currentComponent) {
		if (visited.contains(v)) {
			return;
		} else {
			visited.add(v);
			currentComponent.add(v);
			for (Vertex w : g.getConnected(v)) {
				if (!currentComponent.hasEdge(v, w)) {
					currentComponent.add(v, w);
				}
				dfs(g, w, currentComponent);
			}
		}
	}

	public boolean seen(Vertex v) {
		return visited.contains(v);
	}
	
	public List<Block> getComponents() {
		return components;
	}
	
	public void reset() {
	    shouldSetup = true;
	}

}
