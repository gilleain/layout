package layout;

import graph.model.Graph;
import graph.model.GraphFileReader;

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
    
    public GraphGridLayout(int cellWidth, int cellHeight, ParameterSet params) {
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.params = params;
    }
    
    public int getTotalWidth() {
        return totalWidth;
    }
    
    public int getTotalHeight() {
        return totalHeight;
    }
    
    public Map<Graph, Representation> layout(GraphFileReader graphs) {
        Map<Graph, Representation> repMap = new HashMap<Graph, Representation>();
        List<Representation> reps = new ArrayList<Representation>();
        
        int counter = 0;
        for (Graph g : graphs) {
            Representation rep = null;
            try {
                rep = makeRep(g, cellWidth, cellHeight, params); 
                reps.add(rep);
            } catch (Exception e) {
                StackTraceElement[] st = e.getStackTrace();
                if (st.length > 0) {
                    System.out.println("errr "  + counter + "\t" + e.getStackTrace()[0] + "\t" + g);
                } else {
                    System.out.println("errr "  + counter + "\t" + g);
                }
            }
            repMap.put(g, rep); // may be null!
            counter++;
        }
        
        int padding = (int)params.get("padding");
        int n = reps.size();
        int m = (int) Math.floor(Math.sqrt(n));
        int cx = (cellWidth / 2) + padding;
        int cy = (cellHeight / 2) + padding;
        int i = 0;
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < m + 1; c++) {
                if (i >= n) break;
                reps.get(i).centerOn(cx, cy);
                System.out.println("i " + i + " cx " + cx + " cy " + cy);
                cx += cellWidth + padding;
                i++;
            }
            cy += cellHeight + padding;
            cx = (cellWidth / 2) + padding;
        }
        
        totalWidth = (m + 1) * (cellWidth + padding);
        totalHeight = m * (cellHeight + padding);
        
        return repMap;
    }
    
    public Representation makeRep(Graph g, int w, int h, ParameterSet params) {
        GraphEmbedding ge = GraphEmbedder.embed(g);
        GraphLayout layout = new GraphLayout(params);
        Representation rep = layout.layout(ge, new Rectangle2D.Double(0, 0, w, h));
        return rep;
    }
    
}
