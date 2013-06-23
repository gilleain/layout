package macrocycle;

import graph.model.Graph;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author maclean
 *
 */
public class HexLattice implements Lattice {
    
    private List<Point2D> latticePoints;
    
    private List<Line2D> latticeLines;
    
    private Graph latticeGraph;
    
    /**
     * The radius of the circle centered at a hexagon
     */
    private double r;
    
    /**
     * The distance from the center of a hexagon to the midpoint of a line
     */
    private double s;
    
    private static final double UP = Math.toRadians(270);
    
    private static final double LOWERRIGHT = Math.toRadians(30);

    private static final double UPPERRIGHT = Math.toRadians(330);
    
    /**
     * Make a lattice of w * h hexagons.
     *  
     * @param widthInHexagons
     * @param heightInHexagons
     * @param r radius of the circle each hexagon is inscribed in
     */
    public HexLattice(int widthInHexagons, int heightInHexagons, double r) {
//        latticePoints = new HashMap<Integer, Point2D>();
        latticePoints = new ArrayList<Point2D>();
        latticeLines = new ArrayList<Line2D>();
        latticeGraph = new Graph();
        s = (Math.sqrt(3) * r) / 2;
        int pointIndex = 0;
        double x = s;
        double y = r;
        pointIndex = makeNCycle(x, y, 0, 6, UP);
        makeEdge(0, 5); // close the initial cycle
        x += 2 * s;
        int hexIndex = 1;
        for (int row = 0; row < heightInHexagons; row++) {
            int colStart = (row == 0)? 1 : 0;
            int rowMax = (row % 2 == 0)? widthInHexagons : widthInHexagons - 1;
            System.out.println("new row max = " + rowMax + " start = " + colStart + " x " + x + " y " + y);
            for (int col = colStart; col < rowMax; col++) {
                int pointsToAdd;
                double startAngle;
                int startTo = pointIndex;
                int startFrom;
                int closeTo;
                if (row == 0 && col == 1) {                         // A
                    pointsToAdd = 4;
                    startAngle = UP;
                    closeTo = pointIndex - 4;
                    startFrom = 1;
                } else if (row == 0 && col > 1) {                   // B
                    pointsToAdd = 4;
                    startAngle = UP;
                    startFrom = pointIndex - 3;
                    closeTo = pointIndex - 2;
                } else if (row > 0 && col == 0 && row % 2 == 0) {   // C
                    pointsToAdd = 4;
                    startAngle = LOWERRIGHT;
                    startFrom = calculateRightNeighbourIndex(widthInHexagons, row, hexIndex);
                    closeTo = pointIndex - 4;
                } else if (col == 0 && row % 2 == 1) {              // D
                    pointsToAdd = 3;
                    startAngle = LOWERRIGHT;
                    startFrom = calculateRightNeighbourIndex(widthInHexagons, row, hexIndex);
                    closeTo = pointIndex - 2;
                } else if (col < widthInHexagons - 1) {             // E
                    pointsToAdd = 2;
                    startAngle = LOWERRIGHT;
                    startFrom = calculateRightNeighbourIndex(widthInHexagons, row, hexIndex);
                    closeTo = calculateRightNeighbourIndex(widthInHexagons, row, hexIndex);
                } else {                                            // F
                    pointsToAdd = 3;
                    startAngle = UPPERRIGHT;
                    startFrom = calculateRightNeighbourIndex(widthInHexagons, row, hexIndex);
                    closeTo = pointIndex - 4;
                }
                pointIndex = makeNCycle(x, y, pointIndex, pointsToAdd, startAngle);
                makeEdge(startFrom, startTo);           // make the start edge
                makeEdge(pointIndex - 1, closeTo);      // make the closing edge
                x += 2 * s;
                hexIndex++;
            }
            if (row % 2 == 0) {
                x = 2 * s;
            } else {
                x = s;
            }
            y += 2 * r;
        }
    }
    
    public HexLattice(List<Point2D> latticePoints, List<Line2D> latticeLines, Graph latticeGraph) {
        this.latticePoints = latticePoints;
        this.latticeLines = latticeLines;
        this.latticeGraph = latticeGraph;
    }
    
    public Lattice getDual() {
        return new TriangleLattice(3, 3, r);   // TODO!
    }
    
    private int calculateLeftNeighbourIndex(int hexWidth, int row, int hexIndex) {
        if (row % 2 == 0) {
            return (hexIndex - hexWidth) * 6;
        } else {
            return hexIndex - hexWidth;
        }
    }
    
    private int calculateRightNeighbourIndex(int hexWidth, int row, int hexIndex) {
        if (row % 2 == 0) {
            return hexIndex - hexWidth;
        } else {
            return hexIndex - hexWidth;
        }
    }
    
    private void makeEdge(int indexA, int indexB) {
        latticeGraph.makeEdge(indexA, indexB);
        latticeLines.add(new Line2D.Double(latticePoints.get(indexA), latticePoints.get(indexB)));
    }
    
    private int makeNCycle(double cx, double cy, int index, int n, double startAngle) {
        System.out.println("cycle of size " + n + " index " + index + " startAngle " + Math.toDegrees(startAngle));
        double alpha = Math.toRadians(360.0 / 6);
        double currentAngle = startAngle;
        Point2D prev = null;
        for (int i = index; i < index + n; i++) {
            double x = cx + (r * Math.cos(currentAngle));
            double y = cy + (r * Math.sin(currentAngle));
            
            Point2D point = new Point2D.Double(x, y);
            latticePoints.add(point);
            if (prev != null) {
                latticeLines.add(new Line2D.Double(prev, point));
                latticeGraph.makeEdge(index - 1, index);
            }
            
            currentAngle += alpha;
            if (currentAngle >= 2 * Math.PI) {
                currentAngle -= 2 * Math.PI;
            }
            prev = point;
        }
        return index + n;
    }
    
    public List<Point2D> getPointList() {
//        return new ArrayList<Point2D>(latticePoints.values());
        return latticePoints;
    }
    
    public List<Line2D> getLineList() {
        return latticeLines;
    }

    public Graph getGraph() {
        return latticeGraph;
    }
    
}
