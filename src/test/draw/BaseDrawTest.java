package test.draw;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

public class BaseDrawTest {
    
    public Image makeBlankImage(int w, int h) {
        Image img = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
        img.getGraphics().setColor(Color.WHITE);
        img.getGraphics().fillRect(0, 0, w, h);
        return img;
    }
    
    public File getFile(File dir, String filename) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, filename);
    }
    
    public File getFile(String OUT_DIR, String filename) {
        return getFile(new File(OUT_DIR), filename);
    }
    
}
