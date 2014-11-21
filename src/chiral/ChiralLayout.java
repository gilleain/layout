package chiral;

import graph.model.IntGraph;

import java.awt.geom.Rectangle2D;

import draw.Representation;

public interface ChiralLayout {
    
    public Representation layout(
            IntGraph graph, CombinatorialMap cm, Rectangle2D canvas);
    
}
