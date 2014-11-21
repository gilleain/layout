package macrocycle;

import graph.model.IntGraph;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class TriangleLattice implements Lattice {
    
    private double s;
    
    private List<Point2D> latticePoints;
    
    private List<Line2D> latticeLines;
    
    private IntGraph latticeGraph;
    
    private int currentWidthInTri;
    
    private int currentHeightInTri;
    
    public TriangleLattice(int widthInTriangles, int heightInTriangles, double s) {
        this.s = s;
        latticePoints = new ArrayList<Point2D>();
        latticeLines = new ArrayList<Line2D>();
        latticeGraph = new IntGraph();
        
        double hS = s / 2;
        double x = 0;
        double y = 0;
        int index = 0;
        for (int row = 0; row < heightInTriangles + 1; row++) {
            boolean oddRow = (row % 2 == 0);
            int halfWidth = (widthInTriangles + 1) / 2;
            int colMax;
            if (oddRow) {
                colMax = halfWidth;
                x = hS;
            } else {
                colMax = halfWidth + 1;
                x = 0;
            }
            Point2D prev = null;
            for (int col = 0; col < colMax; col++) {
                x += s;
                Point2D point = new Point2D.Double(x, y);
                latticePoints.add(point);
                if (prev != null) {
                    latticeLines.add(new Line2D.Double(prev, point));
                }
                if (row > 0) {
                    if (col < colMax) {
                        int partner = getRightPartner(index, widthInTriangles);
                        makeEdge(index, partner);
                    }
                    if (row % 2 == 0 || col > 0) {
                        int partner = getLeftPartner(index, widthInTriangles);
                        makeEdge(index, partner);
                    }
                }
                prev = point;
                index++;
            }
            y += s;
        }
        
        // store the current dimensions
        currentWidthInTri = widthInTriangles;
        currentHeightInTri = heightInTriangles;
    }
    
    public HexLattice getDual() {
        return makeDual(currentWidthInTri, currentHeightInTri);
    }
    
    private HexLattice makeDual(int widthInTriangles, int heightInTriangles) {
        List<Point2D> dualPoints = new ArrayList<Point2D>();
        List<Line2D> dualLines = new ArrayList<Line2D>();
        IntGraph dualGraph = new IntGraph();
        int halfWidth = (widthInTriangles + 1) / 2;
        int triangleIndex = 0;
        int pointIndex = halfWidth;
        for (int row = 0; row < heightInTriangles; row++) {
            Point2D prev = null;
            boolean evenRow = (row % 2 == 0);
            for (int col = 0; col < widthInTriangles; col++) {
                boolean evenCol = (col % 2 == 0);
                int a;
                int b;
                int c;
                if ((evenRow && evenCol) || (!evenRow && !evenCol)) {
                    a = pointIndex;
                    b = pointIndex + 1;
                    c = getRightPartner(pointIndex, widthInTriangles);
                    pointIndex++;
                } else if ((evenRow && !evenCol) || (!evenRow && evenCol)) {
                    a = pointIndex;
                    b = getLeftPartner(pointIndex, widthInTriangles);
                    c = getRightPartner(pointIndex, widthInTriangles);
                } else {
                    a = b = c = -1;
                }
//                System.out.println(triangleIndex + "\t " + row + "," + col + "\t: {\t" + a + ",\t" + b + ",\t" + c + "}");
                Point2D midPoint = getMidPoint(a, b, c);
                dualPoints.add(midPoint);
                
                // make the horizontal lines
                if (prev != null) {
                    dualLines.add(new Line2D.Double(prev, midPoint));
                    dualGraph.makeEdge(triangleIndex - 1, triangleIndex);
                }
                
                // make the vertical lines
                if (row > 0) {
                    if (evenRow) {
                        if (!evenCol) {
                            makeLine(triangleIndex - widthInTriangles, triangleIndex, dualPoints, dualLines, dualGraph);
                        }
                    } else {
                        if (evenCol) {
                            makeLine(triangleIndex - widthInTriangles, triangleIndex, dualPoints, dualLines, dualGraph);
                        }
                    }
                }
                
                prev = midPoint;
                triangleIndex++;
            }
            pointIndex++;
        }
        return new HexLattice(dualPoints, dualLines, dualGraph);
    }
    
    private void makeLine(int a, int b, List<Point2D> points, List<Line2D> lines, IntGraph graph) {
        graph.makeEdge(a, b);
        lines.add(new Line2D.Double(points.get(a), points.get(b)));
    }
    
    private Point2D getMidPoint(int a, int b, int c) {
        return getMidPoint(latticePoints.get(a), latticePoints.get(b), latticePoints.get(c));
    }
    
    private Point2D getMidPoint(Point2D a, Point2D b, Point2D c) {
        return new Point2D.Double((a.getX() + b.getX() + c.getX()) / 3, (a.getY() + b.getY() + c.getY()) / 3);
    }
    
    private void makeEdge(int a, int b) {
        latticeGraph.makeEdge(a, b);
        latticeLines.add(new Line2D.Double(latticePoints.get(a), latticePoints.get(b)));
    }
    
    private int getLeftPartner(int index, int widthInTriangles) {
        int w2 = widthInTriangles / 2;
        return index - w2 - 2;
    }
    
    private int getRightPartner(int index, int widthInTriangles) {
        int w2 = widthInTriangles / 2;
        return index - w2 - 1;
    }
    
    public double getSeparation() {
        return s;
    }
    
    public List<Point2D> getPointList() {
        return latticePoints;
    }
    
    public List<Line2D> getLineList() {
        return latticeLines;
    }
    
    public IntGraph getGraph() {
        return latticeGraph;
    }
    
}
