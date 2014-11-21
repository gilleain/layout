package layout;

import graph.model.IntGraph;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import planar.GraphEmbedder;
import planar.GraphEmbedding;
import draw.ParameterSet;
import draw.Representation;

public class GraphGridLayout {
    
    private int totalWidth;
    
    private int totalHeight;
    
    private int cellWidth;
    
    private int cellHeight;
    
    private ParameterSet params;
    
    private boolean showErrors;
    
    public GraphGridLayout(int cellWidth, int cellHeight, ParameterSet params) {
        this(cellWidth, cellHeight, params, true);
    }
    public GraphGridLayout(int cellWidth, int cellHeight, ParameterSet params, boolean showErrors) {
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.params = params;
        this.showErrors = showErrors;
    }
    
    public int getTotalWidth() {
        return totalWidth;
    }
    
    public int getTotalHeight() {
        return totalHeight;
    }
    
    public Map<IntGraph, Representation> layout(Iterable<IntGraph> graphs) {
        Map<IntGraph, Representation> repMap = new HashMap<IntGraph, Representation>();
        List<Representation> reps = new ArrayList<Representation>();
        
        int counter = 0;
        for (IntGraph g : graphs) {
            Representation rep = null;
            try {
                rep = makeRep(g, cellWidth, cellHeight, params); 
                reps.add(rep);
            } catch (Exception e) {
                if (showErrors) {
                    StackTraceElement[] st = e.getStackTrace();
                    if (st.length > 0) {
                        System.out.println("errr "  + counter + "\t" + e.getStackTrace()[0] + "\t" + g);
                    } else {
                        System.out.println("errr "  + counter + "\t" + g);
                    }
                }
            }
            repMap.put(g, rep); // may be null!
            counter++;
        }
        
        int padding = (int)params.get("padding");
        int s = reps.size();
        int m = (int) Math.floor(Math.sqrt(s));
        int n = m + 1;
        int cx = (cellWidth / 2) + padding;
        int cy = (cellHeight / 2) + padding;
        int i = 0;
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (i >= s) break;
                reps.get(i).centerOn(cx, cy);
//                System.out.println("i " + i + " cx " + cx + " cy " + cy);
                cx += cellWidth + (2 * padding);
                i++;
            }
            cy += cellHeight + (2 * padding);
            cx = (cellWidth / 2) + padding;
        }
        
        totalWidth = (n * cellWidth) + ((n + 2) * padding);
        totalHeight = (m * cellHeight) + ((m + 2) * padding);
        totalHeight = Math.max(totalHeight, cellHeight + (2 * padding));    // XXX FIXME
        totalWidth = Math.max(totalWidth, cellWidth + (2 * padding));    // XXX FIXME
        
        return repMap;
    }
    
    public Representation makeRep(IntGraph g, int w, int h, ParameterSet params) {
        GraphEmbedding ge = GraphEmbedder.embed(g);
        GraphLayout layout = new GraphLayout(params);
        Representation rep = layout.layout(ge, new Rectangle2D.Double(0, 0, w, h));
        return rep;
    }
    
}
