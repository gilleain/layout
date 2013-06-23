package layout;

import graph.model.Graph;
import graph.tree.TreeCenterFinder;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.BitSet;
import java.util.List;

import planar.Block;
import planar.BlockEmbedding;
import planar.GraphEmbedding;
import planar.Vertex;
import draw.ParameterSet;
import draw.Representation;

public class GraphLayout {
    
    private ParameterSet params;
    
    public GraphLayout(ParameterSet params) {
        this.params = params;
    }
    
    public Representation layout(GraphEmbedding embedding, Rectangle2D canvas) {
        Representation rep = new Representation();
        Graph partTree = embedding.getPartGraph();
//        System.out.println("B=" + embedding.getBlockParts() + 
//                           " T= " + embedding.getTreeParts() + " " + 
//                           " BT= " + partTree);
        int centerIndex;
        if (partTree.esize() == 0) {
            centerIndex = 0;
        } else {
            centerIndex = TreeCenterFinder.findUniqueCenter(partTree);
            if (centerIndex == -1) {
                // XXX = the part graph is a cycle! - arbitrary choice!
                centerIndex = 0;
            }
        }
        Point2D center = new Point2D.Double(canvas.getCenterX(), canvas.getCenterY());
        layout(embedding, rep, partTree, centerIndex, -1, null, center, new BitSet(), canvas);
        return rep;
    }
    
    private void layout(GraphEmbedding embedding, Representation rep,
                        Graph partTree, int partIndex, int parentIndex, Vertex articulationVertex,
                        Point2D partCenter, BitSet laidOut, Rectangle2D canvas) {
        Block part = embedding.getPart(partIndex);
        Point2D p = rep.getPoint(articulationVertex);
        boolean isTreePart = embedding.isTreePart(partIndex); 
        if (isTreePart) {
            RadialTreeLayout layout = new RadialTreeLayout(params);
            Graph g = part.asGraph();
            if (articulationVertex == null) {
                // XXX - can't we have layout(g, p)?
                double x = partCenter.getX();
                double y = partCenter.getY();
                rep.add(layout.layout(g, new Rectangle2D.Double(x, y, 1, 1)));
            } else {
                List<Vertex> connected = 
                    embedding.getConnectedInOuterCycle(parentIndex, articulationVertex);
                Vertex pA = connected.get(0);
                Vertex pB = connected.get(1);
                layout.layout(g, articulationVertex, pA, pB, rep);
            }
        } else {
            double r = getRadiusForBlock(partIndex, embedding);
            double x = partCenter.getX() - r;
            double y = partCenter.getY() - r;
            Rectangle2D blockCanvas = new Rectangle2D.Double(x, y, 2 * r, 2 * r);
//            System.out.println("canvas for block " + partIndex + " " + blockCanvas);
            ConcentricCircularLayout layout = new ConcentricCircularLayout();
            BlockEmbedding blockEmbedding = embedding.getBlockPart(partIndex); 
            if (articulationVertex == null) {
                rep.add(layout.layout(blockEmbedding, blockCanvas));
            } else {
                rep.add(layout.layout(blockEmbedding, articulationVertex, p, blockCanvas));
            }
        }
        laidOut.set(partIndex);
        
        // catch the case of an empty part tree (single part - should be a vertex...)
        if (partTree.esize() == 0) return;
        
        for (int nextIndex : partTree.getConnected(partIndex)) {
            if (laidOut.get(nextIndex)) continue;
            Block nextPart = embedding.getPart(nextIndex);
            Vertex nextArticulationVertex = getArticulationVertex(part, nextPart);
            Point2D nextCenter;
            if (isTreePart) {
                nextCenter = makeNextCenterForTreeToBlock(part, nextIndex, embedding, nextArticulationVertex, rep);
            } else {
                if (embedding.isTreePart(nextIndex)) {
                    nextCenter = makeNextCenterForBlockToTree(partIndex, nextIndex, embedding, nextArticulationVertex, rep);
                } else {
                    nextCenter = makeNextCenterForBlockToBlock(partIndex, nextIndex, embedding, nextArticulationVertex, rep);
                }
            }
//            rep.addPoint(new Vertex(50), nextCenter);
            layout(embedding, rep, partTree, nextIndex, partIndex, nextArticulationVertex, nextCenter, laidOut, canvas);
        }
        
    }
    
    // return the first vertex that connects these blocks
    private Vertex getArticulationVertex(Block a, Block b) {
        for (Vertex v : a) {
            if (b.hasVertex(v)) {
                return v;
            }
        }
        return null;
    }
    
    private Point2D makeNextCenterForBlockToBlock(
            int partIndex, int nextPartIndex, GraphEmbedding embedding, Vertex articulationVertex, Representation rep) {
        List<Vertex> connected = embedding.getConnectedInOuterCycle(partIndex, articulationVertex);
        assert connected.size() == 2;   // if this is not true, something has gone seriously wrong!
        
        Point2D pV = rep.getPoint(articulationVertex);
        Point2D pA = rep.getPoint(connected.get(0));
        Point2D pB = rep.getPoint(connected.get(1));
        double r = getRadiusForBlock(nextPartIndex, embedding);
        return getOpposingPoint(pV, pA, pB, r);
    }
    
    private Point2D makeNextCenterForBlockToTree(
            int partIndex, int nextPartIndex, GraphEmbedding embedding, Vertex articulationVertex, Representation rep) {
        List<Vertex> connected = embedding.getConnectedInOuterCycle(partIndex, articulationVertex);
        assert connected.size() == 2;   // if this is not true, something has gone seriously wrong!
        
        Point2D pV = rep.getPoint(articulationVertex);
        Point2D pA = rep.getPoint(connected.get(0));
        Point2D pB = rep.getPoint(connected.get(1));
        double edgeLen = params.get("edgeLength");
        
        // XXX - this is not the center of the tree, but the start point!
        return getOpposingPoint(pV, pA, pB, edgeLen);
    }
    
    private Point2D getOpposingPoint(Point2D pV, Point2D pA, Point2D pB, double l) {
        double dX = (pA.getX() - pV.getX()) + (pB.getX() - pV.getX());
        double dY = (pA.getY() - pV.getY()) + (pB.getY() - pV.getY());
        double m = Math.sqrt((dX * dX) + (dY * dY));
        double x = pV.getX() + (l * (-dX / m));
        double y = pV.getY() + (l * (-dY / m));
        
        return new Point2D.Double(x, y);
    }
    
    private Point2D makeNextCenterForTreeToBlock(
            Block part, int nextPartIndex, GraphEmbedding embedding, Vertex articulationVertex, Representation rep) {
        List<Vertex> connected = part.getConnected(articulationVertex);
        Point2D nextCenter = new Point2D.Double();
        
        // for a single parent, just get extrapolate by r along vector p->v 
        double r = getRadiusForBlock(nextPartIndex, embedding);
        if (connected.size() == 1) {
            Vertex parent = connected.get(0);
            Point2D parentPoint = rep.getPoint(parent);
            Point2D articulationPoint = rep.getPoint(articulationVertex);
            double x = articulationPoint.getX();
            double y = articulationPoint.getY();
            double pX = parentPoint.getX();
            double pY = parentPoint.getY();
            double dX = x - pX;
            double dY = y - pY;
            double l = Point2D.distance(x, y, pX, pY);
            nextCenter = new Point2D.Double(x + ((dX / l) * r), y + ((dY / l) * r));
        } else {
            // ??
        }
        return nextCenter;
    }

    private double getRadiusForBlock(int blockIndex, GraphEmbedding embedding) {
        int outerCycleSize = embedding.getBlockEmbedding(blockIndex).getExternalFace().vsize();
        double edgeLen = params.get("edgeLength");
        return edgeLen / Math.sin(Math.toRadians(360 / (2 * outerCycleSize)));
    }
   
}
