package draw;

import graph.model.Vertex;

import java.awt.Color;
import java.util.Map;

import planar.BlockEmbedding;

public abstract class AbstractColorer implements Colorer {
    
    @Override
    public abstract Map<Vertex, Color> getColors(BlockEmbedding embedding);
    
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
