package planar.visitor;

import planar.GraphObject;
import planar.Vertex;

public interface DFSVisitor {
	
	public void visit(GraphObject g, Vertex v);

	public boolean seen(Vertex v);
	
	public void reset();

}
