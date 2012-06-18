package layout;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Vector2d;

import planar.Edge;
import planar.BlockEmbedding;
import planar.Face;
import planar.Vertex;
import draw.Representation;

/**
 * An implementation of Bor Plestenjak's algorithm for drawing Schlegel Diagrams.
 * 
 * NOTE : Does not work particularly well, as the force returned by vectorFunc 
 * (which is Fuv in the paper) always seems to have a very large magnitude - mainly 
 * because it is the cube of the difference between u and v.
 * 
 * As a result, the length of the force vector is never smaller than the cool value, so kV
 * is always equal to the cool value. So ... hmm wait. Now it works, sortof.
 * 
 * @author maclean
 *
 */
public class PlestenjakRefiner implements Refiner {
	
	private class PEdge {
		
		public Vertex v;
		
		public Vertex u;
		
		public PEdge(Vertex v, Vertex u) {
			this.v = v;
			this.u = u;
		}
		
		public String toString() {
			return v + ":" + u;
		}
		
	}
	
	private Rectangle2D canvas;
	
	private double dim;
	
	/**
	 * Yeah, yeah this shouldn't exist, but it does : deal with it.
	 */
	private final static int MAX_CYCLES = 300;

	public PlestenjakRefiner(Rectangle2D canvas) {
		this.canvas = canvas;
		this.dim = Math.min(canvas.getWidth(), canvas.getHeight());
	}

	public Representation refine(Representation representation, BlockEmbedding embedding) {
		Map<Vertex, List<Vertex>> cm = embedding.getCM();
		Map<Vertex, Integer> per = calculatePeriphricities(cm, embedding.getExternalFace());
		System.out.println(per);
		List<PEdge> edges = getEdges(cm);
		int maxPer = Collections.max(per.values());
		double A = 1.4 + (maxPer / 24);
		int i = 0;
		double epsilon = 0.1;
		double maxdis = 2 * epsilon;
		Face outerFace = embedding.getExternalFace();
		
		while (maxdis > epsilon && i < MAX_CYCLES) {
			maxdis = 0;
			i += 1;
			double coolValue = cool(i);
			Map<Vertex, Vector2d> forces = new HashMap<Vertex, Vector2d>();
			for (PEdge edge : edges) {
				Vertex v = edge.v;
				Vertex u = edge.u;
				Vector2d fUV = vectorFunc(representation.getPoint(v), representation.getPoint(u));
				double fC = funcC(v, u, per, A, maxPer); 
//				System.out.println(v + " " + u + " " + fUV + " " + fC);
				fUV.scale(fC);
				if (forces.containsKey(v)) {
					forces.get(v).sub(fUV);
				} else {
					forces.put(v, fUV);
				}
				
				if (forces.containsKey(u)) {
					forces.get(u).add(fUV);
				} else {
					forces.put(u, fUV);
				}
			}
//			System.out.println(i + " " + forces.values());
			for (Vertex v : cm.keySet()) {
				if (outerFace.hasVertex(v)) {
					continue;
				}
				Vector2d fV = forces.get(v);
				double len = fV.length();
//				System.out.println(len);
				double kV = Math.min(len, coolValue);
				Point2D p = representation.getPoint(v);
				double x = p.getX() + (kV * (fV.x / len));
				double y = p.getY() + (kV * (fV.y / len));
				p.setLocation(x, y);
				if (kV > maxdis) {
					maxdis = kV;
				}
			}
//			System.out.print("Round " + i + " d " + maxdis + " c " + coolValue + " ");
//			print(representation.getPoints());
		}
		
		// TODO : don't change the points in the original
		Representation refined = new Representation();
		
		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		double maxY = Double.MIN_VALUE;
		for (Vertex v : cm.keySet()) {
			Point2D p = representation.getPoint(v);
			if (p.getX() < minX) minX = p.getX();
			if (p.getY() < minY) minY = p.getY();
			if (p.getX() > maxX) maxX = p.getX();
			if (p.getY() > maxY) maxY = p.getY();
		}
		double w = maxX - minX;
		double h = maxY - minY;
		double cx = minX + (w / 2);
		double cy = minY + (h / 2);
		double xx = canvas.getCenterX();
		double yy = canvas.getCenterY();
		double sc = Math.min(dim / w, dim / h);
		for (Vertex v : cm.keySet()) {
			Point2D p = representation.getPoint(v);
			double x = p.getX();
			double y = p.getY();
			refined.addPoint(v, 
					new Point2D.Double(xx + (sc * (x - cx)), yy + (sc * (y - cy))));
		}
//		System.out.println("Scaled " + minX + " " + minY 
//							   + " " + maxX + " " + maxY 
//							   + " " + w + " " + h + " " 
//							   + cx + " " + cy);
//		print(refined.getPoints());
		for (Vertex v : cm.keySet()) {
			for (Vertex u : cm.get(v)) {
				Point2D pv = refined.getPoint(v);
				Point2D pu = refined.getPoint(u);
				refined.addLine(new Edge(v, u), new Line2D.Double(pv, pu));
			}
		}
		return refined;
	}
	
	private void print(List<Point2D> points) {
		for (Point2D p : points) {
			System.out.print("[" + Math.round(p.getX()) + ", " + Math.round(p.getY()) + "]");
		}
		System.out.println();
	}
	
	private Vector2d vectorFunc(Point2D v, Point2D u) {
		double vx = v.getX();
		double vy = v.getY();
		double ux = u.getX();
		double uy = u.getY();
		Vector2d vv = new Vector2d(v.getX(), v.getY());
		Vector2d uu = new Vector2d(u.getX(), u.getY());
		Vector2d vvSubUU = new Vector2d(vv);
		vvSubUU.sub(uu);
		double len = vvSubUU.length();
		double x = Math.pow(vx - ux, 3) / len;
		double y = Math.pow(vy - uy, 3) / len;
		
		return new Vector2d(x, y);
	}
	
	private double funcC(Vertex v, Vertex u, Map<Vertex, Integer> per, double A, int maxPer) {
		int x = per.get(v);
		int y = per.get(u);
		return Math.exp(A * (((2 * maxPer) - x - y ) / maxPer));
	}
	
	private List<PEdge> getEdges(Map<Vertex, List<Vertex>> cm) {
		List<PEdge> edges = new ArrayList<PEdge>();
		for (Vertex v : cm.keySet()) {
			for (Vertex u : cm.get(v)) {
				boolean addNew = true;
				for (PEdge edge : edges) {
					if ((edge.v == v && edge.u == u) ||
							(edge.v == u && edge.u == v) ) {
						addNew = false;
						break;
					} 
				}
				if (addNew) {
					edges.add(new PEdge(v, u));
				}
			}
		}
		return edges;
	}
	
	private Map<Vertex, Integer> calculatePeriphricities(Map<Vertex, List<Vertex>> cm, Face outerFace) {
		Map<Vertex, Integer> per = new HashMap<Vertex, Integer>();
		List<Vertex> toVisit = new ArrayList<Vertex>();
		for (Vertex v : outerFace) {
			per.put(v, 0);
			toVisit.add(v);
		}
		
		int periphricity = 1;
		while (!toVisit.isEmpty()) {
			List<Vertex> nextLayer = new ArrayList<Vertex>();
			for (Vertex v : toVisit) {
				for (Vertex u : cm.get(v)) {
					if (!per.containsKey(u)) {
						per.put(u, periphricity);
						nextLayer.add(u);
					}
				}
			}
			toVisit = nextLayer;
			periphricity++;
		}
		
		return per;
	}
	
	private double cool(int i) {
		double x = dim / 30.0;
		double y = (double) -i / 60.0;
		return x * Math.exp(y);
	}

}
