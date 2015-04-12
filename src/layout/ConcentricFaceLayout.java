package layout;

import graph.model.Edge;
import graph.model.Path;
import graph.model.Vertex;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import planar.BlockEmbedding;
import planar.Face;
import draw.Representation;

public class ConcentricFaceLayout extends BaseCircularLayout implements Layout {

    private double r;
    
    private double edgeLen;
    
    public ConcentricFaceLayout(double r, double edgeLen) {
        this.r = r;
        this.edgeLen = edgeLen;
    }
    
    @Override
    public Representation layout(BlockEmbedding em, Rectangle2D canvas) {
        return layout(em, null, null, canvas);
    }

    @Override
    public Representation layout(
            BlockEmbedding em, Vertex start, Point2D startPoint, Rectangle2D canvas) {
        Representation rep = new Representation();
        
        FaceLayerDecomposition decomp = new FaceLayerDecomposition(em);
        List<Face> faces = em.getFaces();
        
        // layout the core as the N-1th layer
        List<Integer> core = decomp.getCore();
        Point2D center = new Point2D.Double(canvas.getCenterX(), canvas.getCenterY());
        List<Edge> outerPath = layoutCore(core, faces, center, rep);
        
        // for the rest of the layers, layout spokes and arches
        Iterator<List<Integer>> layerIterator = decomp.getInnerLayers();
        while (layerIterator.hasNext()) {
            List<Integer> layer = layerIterator.next();
            List<Edge> nextOuterPath = new ArrayList<Edge>();
            for (int faceIndex : layer) {
                Face face = faces.get(faceIndex);
                layoutFace(face, outerPath, nextOuterPath, center, rep);
            }
          
            outerPath = nextOuterPath;
        }
        
        return rep;
    }
    
    private void layoutFace(Face face, List<Edge> outerPath, 
                            List<Edge> nextOP, Point2D totalCenter, Representation rep) {
        Edge spokeA = null;
        Edge pathEdgeA2 = null;
        Edge pathEdgeA1 = null;
        Edge spokeB = null;
        Edge pathEdgeB1 = null;
        Edge pathEdgeB2 = null;
//        System.out.println("laying out Face " + face + " in " + outerPath);
        
        int opSize = outerPath.size();
        for (Edge faceEdge : face.getEdges()) {
            for (int outerPathIndex = 0; outerPathIndex < outerPath.size(); outerPathIndex++) {
                Edge pathEdge = outerPath.get(outerPathIndex); 
                if (faceEdge.adjacent(pathEdge) && !outerPath.contains(faceEdge)) {
                    if (spokeA == null) {
                        spokeA = faceEdge;
                        pathEdgeA1 = pathEdge;
                        int nextIndex = (outerPathIndex < opSize - 1)? outerPathIndex + 1 : 0; 
                        Edge nextPathEdge = outerPath.get(nextIndex);
                        if (spokeA.adjacent(nextPathEdge)) {
                            pathEdgeA2 = nextPathEdge;
                        } else {
                            int prevIndex = (outerPathIndex > 0)? outerPathIndex - 1 : opSize - 1;
                            pathEdgeA2 = outerPath.get(prevIndex);
                        }
                    } else {
                        spokeB = faceEdge;
                        pathEdgeB1 = pathEdge;
                        int nextIndex = (outerPathIndex < opSize - 1)? outerPathIndex + 1 : 0; 
                        Edge nextPathEdge = outerPath.get(nextIndex);
                        if (spokeB.adjacent(nextPathEdge)) {
                            pathEdgeB2 = nextPathEdge;
                        } else {
                            int prevIndex = (outerPathIndex > 0)? outerPathIndex - 1 : opSize - 1;
                            pathEdgeB2 = outerPath.get(prevIndex);
                        } 
                    }
                }
            }
        }
//        System.out.println("spoke A " + spokeA + " pathA1 " + pathEdgeA1 + " pathA2 " + pathEdgeA2 + " in " + face);
        Vertex archStart = layoutSpoke(spokeA, pathEdgeA1, pathEdgeA2, face, rep);
//        System.out.println("spoke B " + spokeB + " pathB1 " + pathEdgeB1 + " pathB2 " + pathEdgeB2 + " in " + face);
        Vertex archEnd   = layoutSpoke(spokeB, pathEdgeB1, pathEdgeB2, face, rep);
        nextOP.addAll(layoutArch(
                archStart, spokeA, archEnd, spokeB, face, outerPath, totalCenter, rep));
    }
    
    private Vertex layoutSpoke(Edge spoke, Edge edgeA, Edge edgeB, Face face, Representation rep) {
        Vertex centerVertex = spoke.getSharedVertex(edgeA);
        Vertex otherVertex = edgeB.other(centerVertex);
        assert centerVertex != null;
        Point2D pC = rep.getPoint(centerVertex);
        Point2D pP = rep.getPoint(otherVertex);
        Point2D pN = rep.getPoint(edgeA.other(centerVertex));
//        System.out.println("pC " + centerVertex + " = " + f(pC) + 
//                " pP " + otherVertex + " = " + f(pP) + 
//                " pN " + edgeA.other(centerVertex) + " = " + f(pN));
        Point2D p = getOpposingPoint(pC, pP, pN, r);
        if (Double.isNaN(p.getX()) || Double.isNaN(p.getY())) {
            Point2D fc = getCenter(face, rep);
//            System.out.println("FACE CENTER = " + fc);
            p = getOpposingPoint(pC, fc, r);    // XXX wrong way round?
        }
        Vertex spokeVertex = spoke.other(centerVertex); 
        rep.addPoint(spokeVertex, p);
//        System.out.println("spoke " + spokeVertex + " @ " + f(p));
        rep.addLine(spoke, new Line2D.Double(pC, p));
        return spokeVertex;
    }
    
    private Point2D getCenter(Face face, Representation rep) {
        double avgX = 0;
        double avgY = 0;
        for (Vertex v : face) {
            Point2D p = rep.getPoint(v);
            if (p != null) {
                avgX += p.getX();
                avgY += p.getY();
            }
        }
        return new Point2D.Double(avgX / face.getVertexCount(), avgY / face.getVertexCount());
    }
    
//    private String f(Point2D p) {
//        if (p == null) {
//            return "NULL";
//        } else {
//            return String.format("(%.1f,  %.1f)", p.getX(), p.getY());
//        }
//    }
//    
//    private String d(double rad) {
//        return String.valueOf(Math.round(Math.toDegrees(rad)));
//    }
//    
    private List<Edge> layoutArch(Vertex archStart, Edge spokeA, 
                                   Vertex archEnd, Edge spokeB, 
                                   Face face, List<Edge> outerPath,
                                   Point2D totalCenter,
                                   Representation rep) {
//        System.out.println("Getting arch of " + face + " from " + archStart + " to " + archEnd);
        Path arch = new Path();
        List<Edge> edges = face.getEdges();
        
        // get the index of the spoke 
        int startIndex = edges.indexOf(spokeA);

        // the arch edge should be the next one in the face
        int nextIndex = startIndex + 1;
        if (nextIndex >= face.getEdgeCount()) {
            nextIndex = 0; 
        }
        Edge archEdge = edges.get(nextIndex);
        
        // check that the next edge is forward (CW) around the face
        boolean forwards = true;
        int edgeIndex;
        if (outerPath.contains(archEdge)) {
            int prevIndex = (startIndex == 0)? edges.size() - 1 : startIndex - 1;
            archEdge = edges.get(prevIndex);
            forwards = false;
            edgeIndex = prevIndex;
        } else {
            edgeIndex = nextIndex;
        }
        
        // check for single-edge arches
        Vertex other = archEdge.other(archStart);
        if (archEdge == null || archStart == null || archEnd == null || other == null) {
            System.out.println("null here");
        }
        if (archEdge.other(archStart).equals(archEnd)) {
            arch.addEdge(archEdge);
        } else {
        
            Vertex prev = archStart;
            while (prev != archEnd) {
                archEdge = edges.get(edgeIndex);
                Vertex next = archEdge.other(prev);
                arch.add(prev);
                arch.add(next);
                arch.add(prev, next);
//                System.out.println("prev " + prev + " archEdge " + archEdge);
               
                if (forwards) {
                    if (edgeIndex < edges.size()) {
                        edgeIndex++;
                    } else {
                        edgeIndex = 0;
                    }
                } else {
                    if (edgeIndex > 0) {
                        edgeIndex--;
                    } else {
                        edgeIndex = edges.size() - 1;
                    }
                }
                prev = archEdge.other(prev);
            }
        }
        
//        System.out.println("Laying out arch " + arch);
        Point2D archStartP = rep.getPoint(archStart);
        Point2D archEndP = rep.getPoint(archEnd);
        List<Edge> newOuterPath = new ArrayList<Edge>();
        if (arch.getVertexCount() == 2) {
            Edge edge = face.getEdge(archStart, archEnd);
            rep.addLine(edge, new Line2D.Double(archStartP, archEndP));
            newOuterPath.add(edge);
        } else {
            Point2D archCenter = getArchCenter(archStartP, archEndP);
            double currentAngle = angle(archCenter, archStartP);
            
            boolean isLeft = super.isLeft(totalCenter, archStartP, archEndP);
//            System.out.println("ISLEFT " + isLeft + " ARCH " + arch);
            
            double addAngle = Math.toRadians(180 / (arch.getVertexCount() - 1));
            Point2D prevPoint = archStartP;
            
            List<Edge> archEdges = arch.getEdges();
    
            int archEdgeIndex = 0;
            int vertexIndex = 1;
//            System.out.println("Arch from " + d(currentAngle) + 
//                               " deg by " + d(addAngle) + " deg , Forwards = " + forwards);
            for (int counter = 1; counter < arch.getVertexCount() - 1; counter++) {
                Vertex vertex = arch.getVertex(vertexIndex);
                if (isLeft) {
                    currentAngle += addAngle;
                    if (currentAngle >= 2 * Math.PI) {
                        currentAngle -= 2 * Math.PI;
                    }
                } else {
                    currentAngle -= addAngle;
                    if (currentAngle <= 0) {
                        currentAngle += 2 * Math.PI;
                    }
                }
                Point2D nextP = makeNextPoint(archCenter, currentAngle, edgeLen);
                rep.addPoint(vertex, nextP);
//                System.out.println("setting " + vertex + " to " + 
//                                   f(nextP) + " ang = " + d(currentAngle));
                Line2D line = new Line2D.Double(prevPoint, nextP);
                vertexIndex++;
                prevPoint = nextP;

                if (archEdgeIndex < arch.getEdgeCount()) {
                    Edge edge = archEdges.get(archEdgeIndex);
                    rep.addLine(edge, line);
                    newOuterPath.add(edge);
                    archEdgeIndex++;
                }
            }
        }
//        System.out.println("Returning outer path " + arch.getEdges());
//        return newOuterPath;
        return arch.getEdges();
    }

    private Point2D getArchCenter(Point2D archStartP, Point2D archEndP) {
        double mx = (archStartP.getX() + archEndP.getX()) / 2;
        double my = (archStartP.getY() + archEndP.getY()) / 2;
        // XXX - for now, just use the midpoint
        return new Point2D.Double(mx, my);
    }

    private List<Edge> layoutCore(
            List<Integer> core, List<Face> faces, Point2D center, Representation rep) {
        List<Edge> outerPath = null;
        if (core.size() == 1) { // simple cyclic core
            System.out.println("CORE is singular");
            Face face = faces.get(core.get(0));
            circularLayout(face, face.getVertexCount(), center.getX(), center.getY(), r, null, null, rep);
            for (Edge e : face.getEdges()) {
                Line2D line = new Line2D.Double(rep.getPoint(e.getA()), rep.getPoint(e.getB()));
                rep.addLine(e, line);
            }
            outerPath = face.getEdges();
        } else if (core.size() == 2) {  // pair-core
            System.out.println("CORE is dual");
            outerPath = layoutDualCore(core, faces, center, rep);
        } else {
            // TODO
            System.out.println("CORE is cyclic");
        }
        return outerPath;
    }
    
    private List<Edge> layoutDualCore(
            List<Integer> core, List<Face> faces, Point2D center, Representation rep) {
        Face faceA = faces.get(core.get(0));
        Face faceB = faces.get(core.get(1));
//        System.out.println("Dual core; face A = " + faceA + " faceB = " + faceB);
        List<Edge> edgesA = faceA.getEdges();
        List<Edge> edgesB = faceB.getEdges();
        
        Edge sharedEdge = faceA.getSharedEdge(faceB);
        List<Edge> outerPath = new ArrayList<Edge>();
        double cx = center.getX();
        double cy = center.getY();
        double pAx = cx - r;
        circularLayout(faceA, faceA.getVertexCount(), pAx, cy, r, null, null, rep);
        
        // add the edges of face A to the outer path
        int sharedEdgeIndexA = edgesA.indexOf(sharedEdge);
        int edgeIndexA = (sharedEdgeIndexA < edgesA.size() - 1)? sharedEdgeIndexA + 1 : 0;
        for (int counter = 0; counter < edgesA.size() - 1; counter++) {
            outerPath.add(edgesA.get(edgeIndexA));
            if (edgeIndexA < edgesA.size() - 1) {
                edgeIndexA++;
            } else {
                edgeIndexA = 0;
            }
        }
        
        int sharedEdgeIndexB = edgesB.indexOf(sharedEdge);
        int prevIndexB = (sharedEdgeIndexB == 0)? edgesB.size() - 1 : sharedEdgeIndexB - 1;
        Edge prevInB = edgesB.get(prevIndexB);
        int nextIndexB = (sharedEdgeIndexB == edgesB.size() - 1)? 0 : sharedEdgeIndexB + 1;
        boolean forwards;
        int currentIndex;
        if (prevInB.adjacent(outerPath.get(outerPath.size() - 1))) {
            forwards = false;
            currentIndex = prevIndexB;
        } else {
            forwards = true;
            currentIndex = nextIndexB;
        }
        
        // now go through face, laying out as if an arc, putting edges in OP as we go
        Edge currentEdge = edgesB.get(currentIndex);
        Vertex prev = sharedEdge.getSharedVertex(currentEdge);
        
        Point2D pA = rep.getPoint(prev);
        Point2D pB = rep.getPoint(sharedEdge.other(prev));
        Point2D centerB = getNextCenter(new Point2D.Double(pAx, cy), pA, pB);
//        System.out.println("center A " + pAx + ", " + cy + " centerB " + f(centerB));
        double angle = angle(centerB, pA);
        
        boolean isLeft = super.isLeft(pB, pA, centerB);
        
        double addAngle = Math.toRadians(360.0 / faceB.getVertexCount());
        double currentAngle = angle;
        for (int counter = 0; counter < faceB.getVertexCount() - 2; counter++) {
            currentEdge = edgesB.get(currentIndex);
            outerPath.add(currentEdge);
            Vertex currentVertex = currentEdge.other(prev);
            if (isLeft) {
                currentAngle += addAngle;
                if (currentAngle >= 2 * Math.PI) {
                    currentAngle -= 2 * Math.PI;
                }
            } else {
                currentAngle -= addAngle;
                if (currentAngle <= 0) {
                    currentAngle += 2 * Math.PI;
                }
            }
            Point2D nextP = makeNextPoint(centerB, currentAngle, edgeLen);
            rep.addPoint(currentVertex, nextP);
//            System.out.println("setting " + currentVertex + " to " + nextP);
            prev = currentVertex;
            if (forwards) {
                if (currentIndex < edgesB.size() - 1) {
                    currentIndex++;
                } else {
                    currentIndex = 0;
                }
            } else {
                if (currentIndex == 0) {
                    currentIndex = edgesB.size() - 1;
                } else {
                    currentIndex--;
                }
            }
        }
        
        outerPath.add(edgesB.get(currentIndex));
        
        return outerPath;
    }
}
