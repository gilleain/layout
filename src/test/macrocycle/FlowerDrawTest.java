package test.macrocycle;

import group.Partition;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import macrocycle.FlowerPartitionGenerator;
import macrocycle.FlowerLayout;

import org.junit.Test;

import draw.ParameterSet;
import draw.Representation;

public class FlowerDrawTest {
    
    public static final String OUT_DIR = "output/honey";
    
    public void draw(int n, String filenameRoot) throws IOException {
        File dir = new File(OUT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        ParameterSet params = new ParameterSet();
        params.set("drawNumberLabels", 0);
        params.set("pointRadius", 3);
        FlowerLayout layout = new FlowerLayout(params);
        int w = 300;
        int h = 300;
        Rectangle2D canvas = new Rectangle2D.Double(0, 0, w, h);
        
        for (Partition p : FlowerPartitionGenerator.getPartitions(n)) {
            Representation representation = layout.layout(p, canvas);
            String name = getName(p);
            File outFile = new File(dir, name + "_" + filenameRoot + ".png");
            BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D)image.getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, w, h);
            representation.draw(g, params);
            ImageIO.write(image, "PNG", outFile);
        }
    }

    private String getName(Partition p) {
        String name = "";
        for (int index = 0; index < p.numberOfElements(); index++) {
            name += p.getCell(index).first();
            if (index < p.numberOfElements() - 1) {
                name += "_";
            }
        }
        return name;
    }
    
    @Test
    public void draw9() throws IOException {
        draw(9, "n9");
    }
    
    @Test
    public void draw12() throws IOException {
        draw(12, "n12");
    }
    
    @Test
    public void draw16() throws IOException {
        draw(16, "n16");
    }
    
}
