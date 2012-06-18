package planar.visitor;

import java.util.ArrayList;
import java.util.List;

import planar.GraphObject;
import planar.Path;
import planar.Vertex;

/**
 * BAD IMPLEMENTATION! Does not find paths properly, but does at least find A path.
 * 
 * It is possible that you can't find paths with DFS.
 * 
 * @author maclean
 *
 */
public class DFSPathFinder implements DFSVisitor {
	
	private List<Path> paths;
	
	private List<Vertex> startingPoints;
	
	public DFSPathFinder() {
		reset(); 
	}
	
	public DFSPathFinder(List<Vertex> startingPoints) {
		this();
		this.startingPoints = startingPoints; 
	}
	
	public void reset() {
	    paths = new ArrayList<Path>();
	}

	public void visit(GraphObject g, Vertex v) {
		Path path = new Path();
		visit(g, v, path);
		paths.add(path);
	}
	
	private void visit(GraphObject g, Vertex v, Path path) {
		path.add(v);
		for (Vertex w : g.getConnected(v)) {
			if (path.hasVertex(w)) {
				continue;
			} else {
				path.add(v, w);
				visit(g, w, path);
				return;	// ugh - this is wrong...
			}
		}
	}

	public boolean seen(Vertex v) {
//		return false;	// we want to visit every vertex
		if (startingPoints != null && startingPoints.contains(v)) {
			return true;
		} else {
			return false;
		}
	}

	public List<Path> getPaths() {
		return paths;
	}

}
