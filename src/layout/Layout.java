package layout;

import graph.model.Vertex;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import planar.BlockEmbedding;
import draw.Representation;

public interface Layout {
	
	public Representation layout(BlockEmbedding em, Rectangle2D canvas);
	
	public Representation layout(BlockEmbedding em, Vertex start, Point2D startPoint, Rectangle2D canvas);

}
