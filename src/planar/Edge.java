package planar;

public class Edge {
	
	private Vertex a;
	
	private Vertex b;
	
	public Edge(Vertex a, Vertex b) {
		this.a = a;
		this.b = b;
	}
	
	public Vertex other(Vertex v) {
		if (v.equals(a)) {
			return b;
		} else if (v.equals(b)) {
			return a;
		} else {
			return null;
		}
	}
	
	public Vertex getA() {
		return a;
	}
	
	public Vertex getB() {
		return b;
	}
	
	public boolean adjacent(Edge o) {
	    return a.equals(o.a) || a.equals(o.b) || b.equals(o.a) || b.equals(o.b); 
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof Edge) {
			Edge o = (Edge) obj;
			return (o.a.equals(a) && o.b.equals(b)) || (o.a.equals(b) && o.b.equals(a));
		}
		return false;
	}
	
	public int hashCode() {
	    return a.hashCode() * b.hashCode();
	}
	
	public String toString() {
		return a + ":" + b;
	}

	public boolean contains(Vertex o) {
		return o.equals(a) || o.equals(b);
	}
	
	public Vertex getSharedVertex(Edge o) {
        if (a.equals(o.a) || a.equals(o.b)) return a;
        if (b.equals(o.b) || b.equals(o.a)) return b;
        return null;
    }

}
