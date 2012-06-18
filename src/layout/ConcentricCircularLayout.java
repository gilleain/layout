package layout;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import planar.BlockEmbedding;
import planar.Edge;
import planar.Face;
import planar.Vertex;
import draw.Representation;

public class ConcentricCircularLayout implements Layout {
	
	public Representation layout(BlockEmbedding embedding, Rectangle2D canvas) {
	    return layout(embedding, null, null, canvas);
	}
	
	public Representation layout(BlockEmbedding embedding, Vertex fromVertex, Point2D fromPoint, Rectangle2D canvas) {
        Map<Vertex, Point2D> positions = new HashMap<Vertex, Point2D>();
        if (fromVertex != null && fromPoint != null) {
            positions.put(fromVertex, fromPoint);
        }
        
        // layout as a cycle
        int border = 10;
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        double r = (Math.min(w, h) - border) / 2; 
        double cx = canvas.getCenterX();
        double cy = canvas.getCenterY();
        List<List<Vertex>> shells = getShells(embedding, fromVertex);
        for (List<Vertex> shell : shells) {
            positions.putAll(circularLayout(shell, cx, cy, r, fromVertex, fromPoint));
            r /= 2;
        }
        
        Map<Vertex, List<Vertex>> cm = embedding.getCM();
        Representation representation = new Representation();
        for (Vertex vertex : positions.keySet()) {
            Point2D point = positions.get(vertex); 
            representation.addPoint(vertex, point);
        }
        List<Vertex> keys = new ArrayList<Vertex>(cm.keySet());
        Collections.sort(keys, new Comparator<Vertex>() {

            public int compare(Vertex v1, Vertex v2) {
                return (v1.getIndex() > v2.getIndex())? 1 :
                    ((v1.getIndex() < v2.getIndex())? -1 : 0);
            }
        });
        for (Vertex v : keys) {
            for (Vertex u : fromMap(v, cm)) {
                Point2D pv = fromPMap(v, positions);
                Point2D pu = fromPMap(u, positions);
                representation.addLine(new Edge(v, u), new Line2D.Double(pv, pu));
            }
        }
        
//        representation.addPoint(new Vertex(99), new Point2D.Double(cx, cy));
        return representation;
    }

    private List<List<Vertex>> getShells(BlockEmbedding embedding, Vertex fromVertex) {
		List<List<Vertex>> shells = new ArrayList<List<Vertex>>();
		List<Vertex> outerFace = new ArrayList<Vertex>();
		Face externalFace = embedding.getExternalFace();
		List<Edge> edges = externalFace.getEdges();
		int startingEdgeIndex;
		boolean goForward = true;
		if (fromVertex == null) {
		    startingEdgeIndex = 0;
		} else {
		    startingEdgeIndex = 0;
		    for (Edge e : edges) {
		        if (e.getA().equals(fromVertex)) {
		            goForward = true;
		            break;
		        } else if (e.getB().equals(fromVertex)) {
		            goForward = false;
		            break;
		        }
		        startingEdgeIndex++;
		    }
		}
		int cyclicIndex = startingEdgeIndex;
		int count = 0;
		while (count < edges.size()) {
		    Edge e = edges.get(cyclicIndex);
		    if (goForward) {
		        outerFace.add(e.getA());
		    } else {
		        outerFace.add(e.getB());
		    }
		    count++;
		    if (goForward && cyclicIndex >= edges.size()) {
		        cyclicIndex = 0;
		    } else if (!goForward && cyclicIndex <= 0) {
		        cyclicIndex = edges.size() - 1;
		    } else {
		        if (goForward) {
		            cyclicIndex++;
		        } else {
		            cyclicIndex--;
		        }
		    }
		}
		shells.add(outerFace);
		build(embedding, outerFace, shells);
		return shells;
	}
	
	private void build(BlockEmbedding embedding, List<Vertex> prevLayer, List<List<Vertex>> shells) {
//		System.out.println(prevLayer);
		List<Vertex> nextLayer = new ArrayList<Vertex>();
		for (Vertex vertex : prevLayer) {
			List<Vertex> connectedVertices = fromMap(vertex, embedding.getCM());
			for (Vertex connected : connectedVertices) {
				if (!inList(connected, nextLayer) && !inShells(connected, shells)) {
					nextLayer.add(connected);
				}
			}
		}
		if (nextLayer.isEmpty()) {
			return;
		} else {
			shells.add(nextLayer);
			build(embedding, nextLayer, shells);
		}
	}
	
	private Map<Vertex, Point2D> circularLayout(
			List<Vertex> vertices, double cx, double cy, double r, Vertex fromVertex, Point2D fromPt) {
		Map<Vertex, Point2D> positions = new HashMap<Vertex, Point2D>();
		int vcount = vertices.size();
		double alpha = Math.toRadians(360.0 / vcount);
//		System.out.println("v " + vertices + " alpha " + Math.toDegrees(alpha));
		
		double currentAngle = 0.0;
		for (int vindex = 0; vindex < vcount; vindex++) {
			Vertex vertex = vertices.get(vindex);
			if (fromVertex != null && vertex.equals(fromVertex)) {
			    currentAngle = angle(new Point2D.Double(cx, cy), fromPt);
			} else {
    			double xp = cx + (r * Math.cos(currentAngle));
    			double yp = cy + (r * Math.sin(currentAngle));
    			positions.put(vertex, new Point2D.Double(xp, yp));
    //			System.out.println("ext face pos : " + positions.get(vertex));
			}
//			System.out.println("v " + vertex + " ca " + Math.toDegrees(currentAngle));
			currentAngle += alpha;
			if (currentAngle >= 2 * Math.PI) {
			    currentAngle -= 2 * Math.PI;
			}
		}
		return positions;
	}
	
	private double angle(Point2D pA, Point2D pB) {
	    double a = Math.atan2((pB.getY() - pA.getY()), 
	            pB.getX() - pA.getX());
	    if (a < 0) {
	        return 2 * Math.PI + a;
	    } else {
	        return a;
	    }
	}
	
	private boolean inShells(Vertex vertex, List<List<Vertex>> shells) {
		for (List<Vertex> layer : shells) {
			if (inList(vertex, layer)) {
				return true;
			}
		}
		return false;
	}
	
	private List<Vertex> fromMap(Vertex vertex, Map<Vertex, List<Vertex>> map) {
		for (Vertex key : map.keySet()) {
			if (vertex.equals(key)) {
				return map.get(key);
			}
		}
		return new ArrayList<Vertex>();
	}
	
	private boolean inList(Vertex vertex, List<Vertex> list) {
		for (Vertex other : list) {
			if (other.equals(vertex)) {
				return true;
			}
		}
		return false;
	}
	
	private Point2D fromPMap(Vertex vertex, Map<Vertex, Point2D> map) {
		for (Vertex key : map.keySet()) {
			if (vertex.equals(key)) {
				return map.get(key);
			}
		}
		return new Point2D.Double();
	}

}
