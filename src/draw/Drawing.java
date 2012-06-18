package draw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import layout.Layout;
import layout.Refiner;
import planar.BlockEmbedding;
import planar.Vertex;

public class Drawing {
	
	private BlockEmbedding embedding;
	
	private Layout layout;
	
	private Refiner refiner;
	
	private Colorer colorer;
	
	public Drawing(BlockEmbedding embedding, Layout layout) {
		this.embedding = embedding;
		this.layout = layout;
	}
	
	public Drawing(BlockEmbedding embedding, Layout layout, Refiner refiner) {
		this(embedding, layout);
		this.refiner = refiner;
	}
	
	public Drawing(BlockEmbedding embedding, Layout layout, Refiner refiner, Colorer colorer) {
		this(embedding, layout, refiner);
		this.colorer = colorer;
	}
	
	public void draw(Graphics2D g, int w, int h) {
		draw(g, new Rectangle2D.Double(0, 0, w, h), new ParameterSet());
	}
	
	/**
	 * Draw to fit in a fixed canvas of w*h.
	 * 
	 * @param g
	 * @param w
	 * @param h
	 */
	public void draw(Graphics2D g, Rectangle2D canvas, ParameterSet params) {
		// layout as a cycle
		Representation representation = layout.layout(embedding, canvas);
		Map<Vertex, Color> colorMap = null;
		if (colorer != null) {
			colorMap = colorer.getColors(embedding);
		}
		if (refiner != null) {
			Representation refined = refiner.refine(representation, embedding);
			refined.draw(g, params, colorMap);
		} else {
			representation.draw(g, params, colorMap);
		}
	}
	
}
