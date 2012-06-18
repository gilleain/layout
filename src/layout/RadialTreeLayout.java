package layout;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import model.Graph;
import planar.Edge;
import planar.Vertex;
import tree.TreeCenterFinder;
import draw.ParameterSet;
import draw.Representation;

/**
 * Layout a tree from its center vertex outwards.
 * 
 * @author maclean
 *
 */
public class RadialTreeLayout implements SimpleLayout {
    
    private ParameterSet params;
    
    // the minimum number of neighbours to use in angle calculations
    private int MIN_NEIGHBOURS = 3;
    
    public RadialTreeLayout(ParameterSet params) {
        this.params = params;
    }

    public Representation layout(Graph tree, Rectangle2D canvas) {
        Representation representation = new Representation();
        Point2D centerPoint = new Point2D.Double(canvas.getCenterX(), canvas.getCenterY());
        List<Integer> centerIndices = TreeCenterFinder.findCenter(tree);
        if (centerIndices.size() == 1) {
            layout(tree, centerIndices.get(0), -1, centerPoint, representation);
        } else {
            int indexA = centerIndices.get(0);
            int indexB = centerIndices.get(1);
            layoutFromCenterPair(tree, indexA, indexB, centerPoint, representation);
        }
        
        return representation;
    }

    /**
     * Add to an existing representation, from an already placed vertex (pV) with additional placed
     * vertices pA and pB.
     * 
     * @param tree the tree to layout
     * @param pV the placed vertex that is the root of the tree
     * @param pA a placed vertex attached to pV
     * @param pB a placed vertex attached to pV
     * @param representation the representation to fill
     */
    public void layout(Graph tree, Vertex pV, Vertex pA, Vertex pB, Representation representation) {
        int index = pV.getIndex();
        
        Point2D ppV = representation.getPoint(pV);
        Point2D ppA = representation.getPoint(pA);
        Point2D ppB = representation.getPoint(pB);
        double beta = Math.toDegrees(angle(ppV, ppA, ppB));
        
        List<Integer> neighbours = tree.getConnected(index);
        int n = neighbours.size() + 1;
        double addAngle = Math.toRadians((360 - beta) / n);
        double angleA = angle(ppV, ppA);
        double angleB = angle(ppV, ppB);
//        System.out.println("A " + pA + "\t" + Math.round(Math.toDegrees(angleA)) +
//                          " B " + pB + "\t" +  Math.round(Math.toDegrees(angleB)));
        
        double currentAngle;
        double halfPi = Math.PI / 2;        //  90 deg
        double threeTwoPi = halfPi * 3;     // 270 deg
        if ((angleA < halfPi && angleB > threeTwoPi) || (angleB < halfPi && angleA > threeTwoPi) ) {
            currentAngle = Math.min(angleA, angleB);
        } else {
            currentAngle = Math.max(angleA, angleB);
        }
//        System.out.println("start angle " + Math.round(Math.toDegrees(currentAngle)) +
//                           " add angle " + Math.round(Math.toDegrees(addAngle)));
        for (int neighbour : neighbours) {
            currentAngle += addAngle;
            if (currentAngle >= 2 * Math.PI) {
                currentAngle -= 2 * Math.PI;
            }
//            System.out.println("current angle " + Math.round(Math.toDegrees(currentAngle)) + 
//                               " for " + neighbour);
            Point2D nextPoint = makeNextPoint(ppV, currentAngle);
            representation.addLine(new Edge(pV, new Vertex(neighbour)), 
                                   new Line2D.Double(ppV, nextPoint));
            layout(tree, neighbour, index, nextPoint, representation);
        }
    }
    
    private double angle(Point2D ppV, Point2D ppA, Point2D ppB) {
        double dxA  = ppA.getX() - ppV.getX();
        double dxB  = ppB.getX() - ppV.getX();
        double dyA  = ppA.getY() - ppV.getY();
        double dyB  = ppB.getY() - ppV.getY();
        double mA   = Math.sqrt((dxA * dxA) + (dyA * dyA));
        double mB   = Math.sqrt((dxB * dxB) + (dyB * dyB));
        return Math.acos(((dxA / mA) * (dxB / mB)) + ((dyA / mA) * (dyB / mB)));
    }

    private void layout(Graph tree, int index, int parentIndex, Point2D point, Representation rep) {
        Vertex vertex = new Vertex(index);
        rep.addPoint(vertex, point);
        placeConnected(tree, vertex, index, parentIndex, point, rep);
    }
    
    private void placeConnected(
            Graph tree, Vertex vertex, int index, int parentIndex, Point2D point, Representation rep) {
        List<Integer> neighbours = tree.getConnected(index);
        int unplacedNeighbours = (parentIndex == -1)? neighbours.size() : neighbours.size();
        int n = Math.max(MIN_NEIGHBOURS, unplacedNeighbours);
        double addAngle = Math.toRadians(360 / n);
        Vertex parentVertex = new Vertex(parentIndex);
        double startAngle;
        if (rep.getPoint(parentVertex) == null) {
            startAngle = 0; 
        } else {
            startAngle = angle(point, rep.getPoint(parentVertex));
        }
        //      System.out.println(String.format("parent %d starting angle %2.0f", 
        //                                        parentIndex, Math.toDegrees(startAngle)));
        double currentAngle = startAngle;
        for (int neighbour : neighbours) {
            if (neighbour != parentIndex) {
                currentAngle += addAngle;
                if (currentAngle >= 2 * Math.PI) {
                    currentAngle -= 2 * Math.PI;
                }
                //              System.out.println(String.format("%d %d %d %2.0f %2.0f", index, neighbour, n, 
                //                      Math.toDegrees(currentAngle), Math.toDegrees(addAngle)));
                Point2D nextPoint = makeNextPoint(point, currentAngle);
                rep.addLine(new Edge(vertex, new Vertex(neighbour)), 
                        new Line2D.Double(point, nextPoint));
                layout(tree, neighbour, index, nextPoint, rep);
            }
        }
    }
    
    private Point2D makeNextPoint(Point2D point, double currentAngle) {
        double edgeLen = params.get("edgeLength");
        double x = point.getX() + (edgeLen * Math.cos(currentAngle));
        double y = point.getY() + (edgeLen * Math.sin(currentAngle));
        return new Point2D.Double(x, y);
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

    private void layoutFromCenterPair(
            Graph tree, int centerIndexA, int centerIndexB, Point2D centerPoint, Representation rep) {
        double edgeLen = params.get("edgeLength");
        
        double centerX = centerPoint.getX();
        double centerY = centerPoint.getY();
        
        Point2D centerPointA = new Point2D.Double(centerX - (edgeLen / 2), centerY);
        Point2D centerPointB = new Point2D.Double(centerX + (edgeLen / 2), centerY);
        
        rep.addPoint(new Vertex(centerIndexB), centerPointB);
        rep.addLine(new Edge(new Vertex(centerIndexA), new Vertex(centerIndexB)), 
                    new Line2D.Double(centerPointA, centerPointB));
        
        layout(tree, centerIndexA, centerIndexB, centerPointA, rep);
        layout(tree, centerIndexB, centerIndexA, centerPointB, rep);
    }
    
}
