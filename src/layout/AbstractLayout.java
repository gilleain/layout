package layout;

import java.awt.geom.Point2D;

public class AbstractLayout {
    
    public double angle(Point2D ppV, Point2D ppA, Point2D ppB) {
        double dxA  = ppA.getX() - ppV.getX();
        double dxB  = ppB.getX() - ppV.getX();
        double dyA  = ppA.getY() - ppV.getY();
        double dyB  = ppB.getY() - ppV.getY();
        double mA   = Math.sqrt((dxA * dxA) + (dyA * dyA));
        double mB   = Math.sqrt((dxB * dxB) + (dyB * dyB));
        return Math.acos(((dxA / mA) * (dxB / mB)) + ((dyA / mA) * (dyB / mB)));
    }

    public double angle(Point2D pA, Point2D pB) {
        double a = Math.atan2((pB.getY() - pA.getY()), 
                               pB.getX() - pA.getX());
        if (a < 0) {
            return 2 * Math.PI + a;
        } else {
            return a;
        }
    }
}
