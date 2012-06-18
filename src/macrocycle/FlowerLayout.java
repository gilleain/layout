package macrocycle;

import group.Partition;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import planar.Edge;
import planar.Vertex;
import draw.ParameterSet;
import draw.Representation;

public class FlowerLayout {
    
    private ParameterSet params;
    
    public FlowerLayout(ParameterSet params) {
        this.params = params;
    }
    
    public Representation layout(Partition partition, Rectangle2D canvas) {
        System.out.println("layout of " + partition);
        double border = params.get("border");

        double w = canvas.getWidth();
        double h = canvas.getHeight();
        
        // TODO - proper radius, not just (1/6*minDim) 
        double r = (Math.min(w, h) - border) / 6;
        
        double cx = w / 2;
        double cy = h / 2;
        Point2D center = new Point2D.Double(cx, cy);
        
        Representation representation = new Representation();
        int numberOfParts = partition.numberOfElements();
        makeInitialRing(numberOfParts, cx, cy, r, representation);
        for (int index = 0; index < numberOfParts; index++) {
            int part = partition.getCell(index).first();
            Vertex a = representation.getVertex(index);
            Vertex b = (index == numberOfParts - 1)? representation.getVertex(0) : representation.getVertex(index + 1);
//            System.out.println("curve from " + a + " to " + b);
            makeCurve(part, a, b, center, r, representation);
        }
//        System.out.println(representation);
        return representation;
    }

    private void makeCurve(
            int size, Vertex start, Vertex end, Point2D center, double r, Representation representation) {
        Point2D pA = representation.getPoint(start);
        Point2D pB = representation.getPoint(end);
        Point2D curveCenter = getNextCenter(center, pA, pB, r, r);
        
        double alpha = Math.toRadians(360.0 / size);
        
        double currentAngle = angle(curveCenter, pA) + alpha;
        double cx = curveCenter.getX();
        double cy = curveCenter.getY();
        Vertex prev = start;
        int startIndex = representation.getPoints().size();
        for (int index = startIndex; index < startIndex + size - 2; index++) {
            Vertex vertex = new Vertex(index);
            double xp = cx + (r * Math.cos(currentAngle));
            double yp = cy + (r * Math.sin(currentAngle));
            Point2D p= new Point2D.Double(xp, yp);
            representation.addPoint(vertex, p);
            representation.addLine(new Edge(prev, vertex), new Line2D.Double(representation.getPoint(prev), p));
            currentAngle += alpha;
            if (currentAngle >= 2 * Math.PI) {
                currentAngle -= 2 * Math.PI;
            }
            prev = vertex;
        }
        Point2D pX = representation.getPoint(prev);
        Point2D pY = representation.getPoint(end);
        representation.addLine(new Edge(prev, end), new Line2D.Double(pX, pY));
    }
    
    private double angle(Point2D centralPoint, Point2D p) {
        double a = Math.atan2((p.getY() - centralPoint.getY()), 
                               p.getX() - centralPoint.getX());
        if (a < 0) {
            return 2 * Math.PI + a;
        } else {
            return a;
        }
    }
    
    private Point2D getNextCenter(Point2D fromCenter, Point2D pA, Point2D pB, double r1, double r2) {
        double eX = (pA.getX() + pB.getX()) / 2;
        double eY = (pA.getY() + pB.getY()) / 2;
        double cX = fromCenter.getX();
        double cY = fromCenter.getY();
        double dX = eX - cX;
        double dY = eY - cY;
        double separation = r1 + r2;
        return new Point2D.Double(cX + ((dX / r1) * separation), cY + ((dY / r1) * separation));
    }

    private void makeInitialRing(
            int numberOfParts, double cx, double cy, double r, Representation representation) {
        double alpha = Math.toRadians(360.0 / numberOfParts);
        double currentAngle = 0.0;
        for (int i = 0; i < numberOfParts; i++) {
            double x = cx + (r * Math.cos(currentAngle));
            double y = cy + (r * Math.sin(currentAngle));
            
            Vertex planarVertex = new Vertex(i); // FIXME
            Point2D currentPoint = new Point2D.Double(x, y);
            representation.addPoint(planarVertex, currentPoint);
            
//          System.out.println("p[" + i + "] =" + currentPoint);
            currentAngle += alpha;
            if (currentAngle >= 2 * Math.PI) {
                currentAngle -= 2 * Math.PI;
            }
        }
    }
    
}
