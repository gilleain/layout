package macrocycle;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;

import model.Graph;

public interface Lattice {
    
    public List<Point2D> getPointList();
    
    public List<Line2D> getLineList();
    
    public Lattice getDual();
    
    public Graph getGraph();
    
}
