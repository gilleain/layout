package test.planar;

import org.junit.Test;

public class AngleTest {
    
    private double angle(double cx, double cy, double x, double y) {
        double a = Math.atan2((y - cy), (x - cx));
        if (a < 0) {
            return 2 * Math.PI + a;
        } else {
            return a;
        }
    }
    
    @Test
    public void clockwise() {
        double a = 0;
        int n = 6;
        double alpha = Math.toRadians(360.0 / n);
        double cx = 200;
        double cy = 200;
        double r = 50;
        for (int i = 0; i < n; i++) {
            double x = cx + (r * Math.cos(a));
            double y = cy + (r * Math.sin(a));
            double angle = angle(cx, cy, x, y);
            System.out.println(
                    String.format("%3.0f %3.0f %3.0f %3.0f", 
                            Math.toDegrees(a), x, y, Math.toDegrees(angle)));
            a += alpha;
            if (a >= 2 * Math.PI) {
                a -= 2 * Math.PI;
            }
        }
    }
    
}
