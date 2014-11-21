package layout;

import graph.model.IntGraph;

import java.awt.geom.Rectangle2D;

import draw.Representation;

public interface SimpleLayout {
    
    public Representation layout(IntGraph graph, Rectangle2D canvas);
    
}
