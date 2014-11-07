package draw;

import graph.model.Edge;
import graph.model.Vertex;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * A graphical representation of a graph.
 * 
 * @author maclean
 *
 */
public class Representation {
	
	private Map<Vertex, Point2D> points;
	
	private Map<Edge, Line2D> lines;
	
	public Representation() {
		this.points = new HashMap<Vertex, Point2D>();
		this.lines = new HashMap<Edge, Line2D>();
	}
	
	public void addPoint(Vertex vertex, Point2D point) {
		this.points.put(vertex, point);
	}
	
	public Point2D getPoint(Vertex vertex) {
		return points.get(vertex);
	}
	
	public void addLine(Edge edge, Line2D line) {
		this.lines.put(edge, line);
	}
	
	public void draw(Graphics2D g, ParameterSet params) {
		draw(g, params, null);
	}

	public List<Point2D> getPoints() {
		return new ArrayList<Point2D>(points.values());
	}
	
	public void centerOn(int x, int y) {
	    centerOn(new Point2D.Double(x, y));
	}
	
	public void centerOn(Point2D c) {
	    Rectangle2D bounds = null;
	    for (Point2D p : points.values()) {
	        if (bounds == null) {
	            bounds = new Rectangle2D.Double(p.getX(), p.getY(), 0, 0);
	        } else {
	            bounds.add(p);
	        }
	    }
	    double dx = c.getX() - bounds.getCenterX();
	    double dy = c.getY() - bounds.getCenterY();
	    for (Point2D p : points.values()) {
	        p.setLocation(p.getX() + dx, p.getY() + dy);
	    }
	    for (Line2D line : lines.values()) {
	        Point2D p1 = line.getP1();
	        Point2D p2 = line.getP2();
	        p1.setLocation(p1.getX() + dx, p1.getY() + dy);
	        p2.setLocation(p2.getX() + dx, p2.getY() + dy);
	        line.setLine(p1, p2);
	    }
	}

	public void draw(Graphics2D g, ParameterSet parameterSet, Map<Vertex, Color> colorMap) {
	    draw(g, parameterSet, colorMap, null);
	}
	
	public void draw(Graphics2D g, ParameterSet parameterSet, Map<Vertex, Color> vertexColorMap, Map<Edge, Color> edgeColorMap) {
		double width = parameterSet.get("lineWidth");
		if (width != 1) {
			g.setStroke(new BasicStroke((float) width));
		}
		for (Edge edge : lines.keySet()) {
		    Line2D line = lines.get(edge);
		    Color savedColor = g.getColor();
		    Stroke savedStroke = g.getStroke();
		    if (edgeColorMap != null && edgeColorMap.containsKey(edge)) {
		        Color color = edgeColorMap.get(edge);
		        if (color != null) {
		            g.setStroke(new BasicStroke(3));
		            g.setColor(color);
		        }
		    }
		    g.draw(line);
		    g.setColor(savedColor);
		    g.setStroke(savedStroke);
		}
		
		double pointRadius = parameterSet.get("pointRadius");
		boolean drawNumberLabels = parameterSet.get("drawNumberLabels") == 1; 
		double d = pointRadius * 2;
		for (Vertex vertex : points.keySet()) {
			Point2D point = points.get(vertex);
			double x = point.getX() - pointRadius;
			double y = point.getY() - pointRadius;
			Color savedColor = g.getColor();
			if (vertexColorMap != null) {
				Color color = vertexColorMap.get(vertex);
				if (color != null) {
					g.setColor(color);
				}
			}
			g.fill(new Ellipse2D.Double(x, y, d, d));
			
			if (drawNumberLabels) {
    			g.setColor(Color.BLACK);
    			g.drawString(String.valueOf(vertex.getIndex()), (int)x, (int)y);
			}
			g.setColor(savedColor);
		}
		
	}

	public List<Vertex> getVertices() {
		return new ArrayList<Vertex>(points.keySet());
	}
	
	public String toString() {
	    StringBuffer sb = new StringBuffer();
	    for (Vertex v : points.keySet()) {
	        sb.append(v).append("\t").append(points.get(v)).append("\n");
	    }
	    for (Edge edge : lines.keySet()) {
            Line2D line = lines.get(edge);
	        sb.append(String.format("%s %s %s", edge, line.getP1(), line.getP2()));
	    }
	    return sb.toString();
	}

    public Vertex getVertex(int index) {
        for (Vertex v : points.keySet()) {
            if (v.getIndex() == index) {
                return v;
            }
        }
        return null;
    }

    public void add(Representation other) {
        points.putAll(other.points);
        lines.putAll(other.lines);
    }

}
