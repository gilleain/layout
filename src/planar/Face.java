package planar;

import graph.model.Block;
import graph.model.Edge;
import graph.model.GraphObject;
import graph.model.Path;
import graph.model.Vertex;

import java.util.List;

/**
 * A face is just a cycle, with additional methods.
 * 
 * @author maclean
 *
 */
public class Face extends GraphObject {

	public Face() {
		super();
	}
	
	public Face(int vsize) {
		super(vsize);
	}
	
	public Face(List<Vertex> vertices) {
	    super(vertices);
	}
	
	public Face(Block outerCycle) {
        // TODO Auto-generated constructor stub
    }

    /**
	 * Get the vertices of the cycle from start to end.
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public Face getStartToEndFace(Vertex start, Vertex end, Path path) {
//		System.out.println("splitting face " + start + " " + end);
		Face face = new Face();
		
		// look for the edge containing the start (assumes edges are cyclically ordered)
		Edge currentEdge = null;
		int edgeIndex;
		for (edgeIndex = 0; edgeIndex < esize(); edgeIndex++) {
			Edge e = edges.get(edgeIndex);
			if (e.getA().equals(start)) {	// implicitly assuming a directed edge from A->B
				currentEdge = e;
				break;
			}
		}
		
		// start the face, not including the first vertex
		Vertex currentVertex = currentEdge.getB();
		face.add(currentVertex);
		face.add(start, currentVertex);
		
		// now run through the edges in order
		while (!currentVertex.equals(end) && face.esize() < esize()) {
//			System.out.println("currentV = " + currentVertex);
			if (edgeIndex == esize() - 1) {
				edgeIndex = 0;
			} else {
				edgeIndex++;
			}
			currentEdge = edges.get(edgeIndex);
//			System.out.println("currentEdgeIndex=" + edgeIndex + " currentE = " + currentEdge);
			currentVertex = currentEdge.getB();
			
			// don't add the last vertex
			if (currentVertex != end) {
				face.add(currentVertex);
			}
			face.add(currentEdge.getA(), currentVertex);
		}
		
		// add the path into the face in reverse
		for (int eIndex = path.esize() - 1; eIndex >= 0; eIndex--) {
			Edge pathEdge = path.getEdges().get(eIndex);
			face.add(pathEdge.getB());
			face.add(pathEdge.getA());
			face.add(pathEdge.getB(), pathEdge.getA());
		}
		
		return face;
	}
	
	public Face getEndToStartFace(Vertex start, Vertex end, Path path) {
//		System.out.println("splitting face " + end + " " + start);
		Face face = new Face();
		
		// look for the edge containing the end (assumes edges are cyclically ordered)
		Edge currentEdge = null;
		int edgeIndex;
		for (edgeIndex = 0; edgeIndex < esize(); edgeIndex++) {
			Edge e = edges.get(edgeIndex);
			if (e.getA().equals(end)) {	// implicitly assuming a directed edge from A->B
				currentEdge = e;
				break;
			}
		}
		
		// start the face, not including the first vertex
		Vertex currentVertex = currentEdge.getB();
		face.add(currentVertex);
		face.add(end, currentVertex);
		
		// now run through the edges in order
		while (!currentVertex.equals(start) && face.esize() < esize()) {
//			System.out.println("currentV = " + currentVertex);
			if (edgeIndex == esize() - 1) {
				edgeIndex = 0;
			} else {
				edgeIndex++;
			}
			currentEdge = edges.get(edgeIndex);
//			System.out.println("currentEdgeIndex=" + edgeIndex + " currentE = " + currentEdge);
			currentVertex = currentEdge.getB();
			
			// don't add the last vertex
			if (currentVertex != start) {
				face.add(currentVertex);
			}
			face.add(currentEdge.getA(), currentVertex);
		}
		
		// add the path into the face
		for (int eIndex = 0; eIndex < path.esize(); eIndex++) {
			Edge pathEdge = path.getEdges().get(eIndex);
			face.add(pathEdge.getA());
			face.add(pathEdge.getB());
			face.add(pathEdge.getA(), pathEdge.getB());
		}
		
		return face;
	}

	public boolean containsAllVertices(List<Vertex> otherVertices) {
		for (Vertex other : otherVertices) {
			if (super.hasVertex(other)) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

    public void addAllVertices(List<Vertex> vertices) {
        this.vertices.addAll(vertices);
    }
    
    public void addAllEdges(List<Edge> edges) {
        this.edges.addAll(edges);
    }

    public boolean contains(Edge edge) {
        return this.edges.contains(edge);
    }

    public boolean contains(Vertex v) {
        return vertices.contains(v);
    }

    public boolean sharesEdge(Face face) {
        for (Edge e : edges) {
            for (Edge f : face.edges) {
                if (e.equals(f)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Edge getSharedEdge(Face face) {
        for (Edge e : edges) {
            for (Edge f : face.edges) {
                if (e.equals(f)) {
                    return e;
                }
            }
        }
        return null;
    }

    public int indexOf(Vertex v) {
        int index = 0;
        for (Vertex w : vertices) {
            if (w.equals(v)) {
                return index;
            }
            index++;
        }
        return -1;
    }

}
