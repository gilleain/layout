package macrocycle;

import graph.model.Graph;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class SquareLattice implements Lattice {
    
    private List<Point2D> latticePoints;
    
    private List<Line2D> latticeLines;
    
    private Graph latticeGraph;
    
    private int currentWidthInSquares;
    
    private int currentHeightInSquares;
    
    private double r;
    
    public SquareLattice(int widthInSquares, int heightInSquares, double r) {
        latticePoints = new ArrayList<Point2D>();
        latticeLines = new ArrayList<Line2D>();
        latticeGraph = new Graph();
        this.r = r;
        
        make(widthInSquares, heightInSquares, r, 0, 0, latticeGraph, latticePoints, latticeLines);
        
        // store the current dimensions
        currentWidthInSquares = widthInSquares;
        currentHeightInSquares = heightInSquares;
    }
    
    public SquareLattice(List<Point2D> latticePoints, List<Line2D> latticeLines, Graph graph) {
        this.latticePoints = latticePoints;
        this.latticeLines = latticeLines;
        this.latticeGraph = graph;
    }
    
    private void make(int widthInSquares, int heightInSquares, 
                      double r, double xStart, double yStart,
                      Graph graph, List<Point2D> latticePoints, List<Line2D> latticeLines) {
        double x = xStart;
        double y = yStart;
        int pointIndex = 0;
        for (int row = 0; row < heightInSquares; row++) {
            x = xStart;
            for (int col = 0; col < widthInSquares; col++) {
                Point2D point = new Point2D.Double(x, y);
                latticePoints.add(point);
                
                // horizontal edges
                if (col > 0) {
                    makeEdge(pointIndex - 1, pointIndex, graph, latticePoints, latticeLines);
                }
                
                // vertical edges
                if (row > 0) {
                    makeEdge(pointIndex - widthInSquares, pointIndex, graph, latticePoints, latticeLines);
                }
                // TODO - should 'r' be the dimension of a square inscribed in a circle of radius r?
                x += r; 
                pointIndex++;
            }
            y += r;
        }
    }
    
    public int getWidth() {
        return currentWidthInSquares;
    }
    
    public int getHeight() {
        return currentHeightInSquares;
    }
    
    private void makeEdge(int a, int b, Graph graph, List<Point2D> points, List<Line2D> lines) {
        graph.makeEdge(a, b);
        lines.add(new Line2D.Double(points.get(a), points.get(b)));
    }

    public List<Point2D> getPointList() {
        return latticePoints;
    }

    public List<Line2D> getLineList() {
        return latticeLines;
    }

    public Lattice getDual() {
        List<Point2D> dualPoints = new ArrayList<Point2D>();
        List<Line2D> dualLines = new ArrayList<Line2D>();
        Graph dualGraph = new Graph();
        make(currentWidthInSquares - 1, currentHeightInSquares - 1, r, r / 2, r / 2, 
             dualGraph, dualPoints, dualLines);
        return new SquareLattice(dualPoints, dualLines, dualGraph);
    }
    
    public Graph getGraph() {
        return latticeGraph;
    }
    
}
