package draw;

import graph.model.Vertex;

import java.awt.Color;
import java.util.Map;

import planar.BlockEmbedding;

public interface Colorer {
	
	public Map<Vertex, Color> getColors(BlockEmbedding embedding);

}
