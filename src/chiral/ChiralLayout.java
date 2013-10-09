package chiral;

import graph.model.Graph;

import java.awt.geom.Rectangle2D;

import draw.Representation;

public interface ChiralLayout {
    
    public Representation layout(
            Graph graph, CombinatorialMap cm, Rectangle2D canvas);
    
}
