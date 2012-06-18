package draw;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Graph;
import model.GraphSignature;
import planar.BlockEmbedding;
import planar.Vertex;
import signature.SymmetryClass;

public class SignatureColorer implements Colorer {

	public Map<Vertex, Color> getColors(BlockEmbedding embedding) {
		Graph graph = embedding.getGraph();
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

	
	 /**
     * Get a color for a value 'v' between vmin and vmax.
     * 
     * @param v the point on the ramp to make a color for
     * @param vmin the minimum value in the range
     * @param vmax the maximum value in the range
     * @return the color for v
     */
    public static Color colorRamp(int v, int vmin, int vmax) {
        double r = 1.0;
        double g = 1.0;
        double b = 1.0;
        if (v < vmin) {
            v = vmin;
        }
        if (v > vmax) {
            v = vmax;
        }
        int dv = vmax - vmin;

        try {
            if (v < (vmin + 0.25 * dv)) {
                r = 0.0;
                g = 4.0 * (v - vmin) / dv;
            } else if (v < (vmin + 0.5 * dv)) {
                r = 0.0;
                b = 1.0 + 4.0 * (vmin + 0.25 * dv - v) / dv;
            } else if (v < (vmin + 0.75 * dv)) {
                r = 4.0 * (v - vmin - 0.5 * dv) / dv;
                b = 0.0;
            } else {
                g = 1.0 + 4.0 * (vmin + 0.75 * dv - v) / dv;
                b = 0.0;
            }
            float[] hsb = Color.RGBtoHSB(
                    (int) (r * 255), (int) (g * 255), (int) (b * 255), null);
            return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        } catch (ArithmeticException zde) {
            float[] hsb = Color.RGBtoHSB(0, 0, 0, null);
            return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        }

    }


}
