package planar;

public class DualEdge {
    
    private Face faceA;
    
    private Face faceB;
    
    public DualEdge(Face faceA, Face faceB) {
        this.faceA = faceA;
        this.faceB = faceB;
    }

    public Face getFaceA() {
        return faceA;
    }

    public Face getFaceB() {
        return faceB;
    }
    
    public String toString() {
        return faceA + ":" + faceB;
    }
    
}
