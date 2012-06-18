package planar;

import java.util.List;

public class Path extends GraphObject {
	
	public Path() {
		super();
	}
	
	public Path(int vcount) {
		super(vcount);
	}
	
	public Path(List<Vertex> vertices) {
		super(vertices);
	}
	
	public Path(Edge edge) {
	    super();
	    Vertex a = edge.getA();
	    Vertex b = edge.getB();
	    add(a);
	    add(b);
	    add(a, b);
    }

    public boolean contains(List<Vertex> endPoints) {
		for (int i = 1; i < vertices.size() - 1; i++) {
			if (endPoints.contains(vertices.get(i))) {
				return true;
			}
		}
		return false;
	}

    public Path reverse() {
        Path rev = new Path();
        for (int i = vertices.size() - 1; i >= 0; i--) {
            rev.add(vertices.get(i));
        }
        for (int e = edges.size() - 1; e >= 0; e--) {
            Edge edge = edges.get(e);
            rev.add(edge.getB(), edge.getA());
        }
        return rev;
    }

}
