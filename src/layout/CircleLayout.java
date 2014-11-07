package layout;

import graph.model.Edge;
import graph.model.Graph;
import graph.model.Vertex;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Map;

import draw.ParameterSet;
import draw.Representation;

/**
 * All vertices are placed on a circle, with edges crossing or otherwise.
 * 
 * @author maclean
 *
 */
public class CircleLayout implements SimpleLayout {
	
	private ParameterSet params;
	
	public CircleLayout(ParameterSet params) {
		this.params = params;
	}
	
	public Representation layout(Graph graph, Rectangle2D canvas) {
		Representation repr = new Representation();
		double border = params.get("border");
		double w = canvas.getWidth();
		double h = canvas.getHeight();
		double r = (Math.min(w, h) - border) / 2; 
		int n = graph.getVertexCount();
		double cx = w / 2;
		double cy = h / 2;
		double alpha = Math.toRadians(360.0 / n);
		double currentAngle = 0.0;
		for (int i = 0; i < n; i++) {
			double x = cx + (r * Math.cos(currentAngle));
			double y = cy + (r * Math.sin(currentAngle));
			
			Vertex planarVertex = new Vertex(i); // FIXME
			Point2D currentPoint = new Point2D.Double(x, y);
			repr.addPoint(planarVertex, currentPoint);
			
//			System.out.println("p[" + i + "] =" + currentPoint);
			currentAngle += alpha;
			if (currentAngle >= 2 * Math.PI) {
				currentAngle -= 2 * Math.PI;
			}
		}
		List<Vertex> vertices = repr.getVertices();
		Map<Integer, List<Integer>> connectionTable = graph.getConnectionTable();
		for (int i = 0; i < n; i++) {
			Vertex v = vertices.get(i);
			Point2D pi = repr.getPoint(v); 
			for (int j : connectionTable.get(i)) {
				Vertex u = vertices.get(j);
				Point2D pj = repr.getPoint(u);
				repr.addLine(new Edge(v, u), new Line2D.Double(pi, pj));
			}
		}
		return repr;
	}

}
