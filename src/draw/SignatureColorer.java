package draw;

import graph.model.IntGraph;
import graph.model.GraphSignature;
import graph.model.Vertex;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import planar.BlockEmbedding;
import signature.SymmetryClass;

public class SignatureColorer extends AbstractColorer {

	public Map<Vertex, Color> getColors(BlockEmbedding embedding) {
		IntGraph graph = embedding.getGraph();
		GraphSignature signature = new GraphSignature(graph);
		List<SymmetryClass> symmetryClasses = signature.getSymmetryClasses();
		tmp_print(symmetryClasses);
		
		// TODO : auto-gen
		List<Color> colors = getColors(symmetryClasses.size());
		
		Map<Vertex, Color> colorMap = new HashMap<Vertex, Color>();
		int i = 0;
		for (SymmetryClass symCl : symmetryClasses) {
			Color color = colors.get(i);
			for (int index : symCl) {
				Vertex v = embedding.getVertexWithIndex(index);
//				System.out.println(v + " " + index + " " + color);
				colorMap.put(v, color);
			}
			i++;
		}
		return colorMap;
	}
	
	private void tmp_print(List<SymmetryClass> symmetryClasses) {
		for (SymmetryClass symCl : symmetryClasses) {
			System.out.print("[ ");
			for (int i : symCl) {
				System.out.print(i + " ");
			}
			System.out.print("] ");
		}
		System.out.println();
	}
	
	/**
     * Get N colors as a list.
     * 
     * @param number the number of colors to generate
     * @return a list of colors.
     */
    public static List<Color> getColors(int number) {
        List<Color> colors = new ArrayList<Color>();
        for (int i = 0; i < number; i++) {
            colors.add(colorRamp(i, 0, number));
        }
        return colors;
    }

}
