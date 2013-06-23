package layout;

import graph.model.Graph;

import java.awt.geom.Rectangle2D;

import draw.Representation;

public interface SimpleLayout {
    
    public Representation layout(Graph graph, Rectangle2D canvas);
    
}
