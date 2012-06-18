package layout;

import planar.BlockEmbedding;
import draw.Representation;

public interface Refiner {
	
	public Representation refine(Representation representation, BlockEmbedding embedding);

}
