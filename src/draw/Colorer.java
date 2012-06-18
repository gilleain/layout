package draw;

import java.awt.Color;
import java.util.Map;

import planar.BlockEmbedding;
import planar.Vertex;

public interface Colorer {
	
	public Map<Vertex, Color> getColors(BlockEmbedding embedding);

}
