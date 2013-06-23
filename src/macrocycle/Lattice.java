package macrocycle;

import graph.model.Graph;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;

public interface Lattice {
    
    public List<Point2D> getPointList();
    
    public List<Line2D> getLineList();
    
    public Lattice getDual();
    
    public Graph getGraph();
    
}
