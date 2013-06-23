package test.macrocycle;

import graph.model.Graph;
import graph.model.GraphSignature;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import macrocycle.HexLattice;
import macrocycle.TriangleLattice;

import org.junit.Test;

import planar.Block;
import planar.BlockEmbedding;
import planar.CycleFinder;
import planar.DualFinder;
import planar.Edge;
import planar.PlanarBlockEmbedder;
import planar.SpanningTree;
import planar.Vertex;
import planar.visitor.ConnectedComponentFinder;

import combinatorics.SubsetLister;

import draw.ParameterSet;
import draw.Representation;

public class HexLatticeCyclesTest extends LatticeTest {
    
    public static final String OUT_DIR = "output/lattice/honeycomb";
    
    public Path2D makePolygon(HexLattice lattice, Block cycle) {
        List<Point2D> points = lattice.getPointList();
        Path2D polygon = new Path2D.Double();
        List<Vertex> cycleSortedVertices = cycleSort(cycle.getEdges());
        System.out.println(cycle.getEdges());
        System.out.println(cycleSortedVertices);
        boolean moveTo = true;
        for (Vertex v : cycleSortedVertices) {
            Point2D p = points.get(v.getIndex());
            if (moveTo) {
                polygon.moveTo(p.getX(), p.getY());
                moveTo = false;
            } else {
                polygon.lineTo(p.getX(), p.getY());
            }
        }
        polygon.closePath();
        return polygon;
    }
    
    public List<Vertex> cycleSort(List<Edge> edges) {
        List<Vertex> sorted = new ArrayList<Vertex>();
        Vertex prev = edges.get(0).getB();
        BitSet visited = new BitSet();
        visited.set(0);
        for (int count = 1; count < edges.size(); count++) {
            for (int index = 1; index < edges.size(); index++) {
                if (visited.get(index)) continue;
                Edge e = edges.get(index);
                if (e.getA().equals(prev)) {
                    prev = e.getB();
                    visited.set(index);
                    break;
                } else if (e.getB().equals(prev)) {
                    prev = e.getA();
                    visited.set(index);
                    break;
                } else {
                    continue;
                }
            }
            sorted.add(prev);
        }
        return sorted;
    }
    
    public Representation getCycleRepr(HexLattice lattice, Block cycle) {
        List<Point2D> points = lattice.getPointList();
        List<Line2D> lines   = lattice.getLineList();
        Graph graph = lattice.getGraph();
        
        Representation rep = new Representation();
        for (int vertexIndex = 0; vertexIndex < cycle.vsize(); vertexIndex++) {
            Vertex v = cycle.getVertex(vertexIndex);
            rep.addPoint(v, points.get(v.getIndex()));
        }
        
        for (Edge edge : cycle.getEdges()) {
            Vertex a = edge.getA();
            Vertex b = edge.getB();
            int eIndex = graph.getEdgeIndex(a.getIndex(), b.getIndex());
            rep.addLine(edge, lines.get(eIndex));
        }
        
        return rep;
    }
    
    public Representation getCycleAsFusaneRepr(HexLattice lattice, Block cycle) {
        List<Point2D> points = lattice.getPointList();
        List<Line2D> lines   = lattice.getLineList();
        Graph graph = lattice.getGraph();
        
        Representation rep = new Representation();
        for (int vertexIndex = 0; vertexIndex < cycle.vsize(); vertexIndex++) {
            Vertex v = cycle.getVertex(vertexIndex);
            rep.addPoint(v, points.get(v.getIndex()));
            for (int partnerIndex : graph.getConnected(vertexIndex)) {
                Vertex pV = new Vertex(partnerIndex);
                if (cycle.hasVertex(pV)) {
                    int eIndex = graph.getEdgeIndex(vertexIndex, partnerIndex);
                    rep.addLine(new Edge(v, pV), lines.get(eIndex));
                }
            }
        }
        
        return rep;
    }
    
    public Map<Integer, List<Block>> getCycles(int triWidth, int triHeight, double r) {
        // XXX - the hex lattice that is the dual of the tri one is made better! 
        TriangleLattice tri = new TriangleLattice(triWidth, triHeight, r);
        HexLattice dual = tri.getDual();
        
        return getCycles(dual);
    }
    
    public List<Representation> getCyclesAsRepr(int triWidth, int triHeight, double r, int size) {
        // XXX - the hex lattice that is the dual of the tri one is made better! 
        TriangleLattice tri = new TriangleLattice(triWidth, triHeight, r);
        HexLattice dual = tri.getDual();
        
        Map<Integer, List<Block>> hist = getCycles(dual);
        
        List<Representation> representations = new ArrayList<Representation>();
        for (Block cycle : hist.get(size)) {
            representations.add(getCycleRepr(dual, cycle));
        }
        return representations;
    }
    
    public Map<Integer, List<Block>> getCycles(HexLattice lattice) {
        Graph graph = lattice.getGraph();
        Map<Integer, List<Block>> hist = new HashMap<Integer, List<Block>>();
        for (Block cycle : CycleFinder.findAll(new Block(graph))) {
            if (cycle.vsize() == 0) continue;
            ConnectedComponentFinder finder = new ConnectedComponentFinder();
            cycle.accept(finder);
            if (finder.getComponents().size() == 1) {
                int size = cycle.esize();
                List<Block> cycles;
                if (hist.containsKey(size)) {
                    cycles = hist.get(size);
                } else {
                    cycles = new ArrayList<Block>();
                    hist.put(size, cycles);
                }
                cycles.add(cycle);
            }
        }
        return hist;
    }
    
    public Block fillOut(HexLattice lattice, Block cycle) {
        List<Point2D> points = lattice.getPointList();
        Graph graph = lattice.getGraph();
        
        Block rep = new Block(cycle);
        if (cycle.vsize() != cycle.esize()) {
            System.out.println("cycle " + cycle);
        }
        
        Path2D poly = makePolygon(lattice, cycle);
        List<Vertex> innerPoints = new ArrayList<Vertex>();
        for (int i = 0; i < points.size(); i++) {
            Point2D p = points.get(i);
            Vertex v = new Vertex(i);
            if (!cycle.hasVertex(v) && poly.contains(p)) {
                innerPoints.add(v);
                rep.add(v);
            }
        }
        System.out.println(innerPoints);
        
        // add the edges spanning the cycle
        for (int vertexIndex = 0; vertexIndex < cycle.vsize(); vertexIndex++) {
            Vertex v = cycle.getVertex(vertexIndex);
            rep.add(v);
            for (int partnerIndex : graph.getConnected(v.getIndex())) {
                Vertex pV = new Vertex(partnerIndex);
                if (cycle.hasVertex(pV) || innerPoints.contains(pV)) {
                    if (!rep.hasEdge(v, pV)) {
                        rep.add(v, pV);
                    }
                }
            }
        }
        
        // add the edges spanning the inner points
        for (Vertex v : innerPoints) {
            for (int o : graph.getConnected(v.getIndex())) {
                Vertex oV = new Vertex(o);
                if (contains(o, innerPoints) && !rep.hasEdge(v, oV)) {
                    rep.add(v, oV);
                }
            }
        }
        return rep;
    }
    
    private boolean contains(int i, List<Vertex> l) {
        for (Vertex v : l) {
            if (v.getIndex() == i) return true;
        }
        return false;
    }
    
    public List<Block> classify(HexLattice lattice, List<Block> cycles) {
        Map<String, Block> representatives = new HashMap<String, Block>();
        for (Block cycle : cycles) {
//            System.out.println(cycle + "\t" + CycleFinder.cycleToBitSet(cycle, new Block(lattice.getGraph())));
            Block rep = fillOut(lattice, cycle);
            
            BlockEmbedding embedding = null;
            try {
                embedding = PlanarBlockEmbedder.embedInCycle(new Block(cycle), rep, new Graph());
            } catch (NullPointerException npe) {
                try {
                    int id = Math.abs(cycle.toString().hashCode());
                    draw(rep, lattice, getParams(), "err_" + id + ".png");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("npe " + cycle);
            }
            
            if (embedding != null) {
//                System.out.println(
//                        "V = " + rep.vsize()
//                        + " E = " + rep.esize()
//                        + " F = " + embedding.getFaces().size() 
//                        + " " + rep);
                Graph dual = DualFinder.getDual(embedding);
                String sig = new GraphSignature(dual).toCanonicalString();
                if (!representatives.containsKey(sig)) {
//                    representatives.put(sig, rep);
                    representatives.put(sig, cycle);
                }
            }
        }
//        System.out.println(representatives.keySet());
        
        return new ArrayList<Block>(representatives.values());
    }
    
    private ParameterSet getParams() {
        ParameterSet params = new ParameterSet();
        params.set("pointRadius", 3);
        params.set("drawNumberLabels", 1);
        return params;
    }

    @Test
    public void smallLattice() {
        int triWidth = 9;
        int triHeight = 6;
        double r = 50;
        
        TriangleLattice tri = new TriangleLattice(triWidth, triHeight, r);
        HexLattice dual = tri.getDual();
        
        Map<Integer, List<Block>> hist = getCycles(dual);
        List<Integer> keys = new ArrayList<Integer>(hist.keySet());
        Collections.sort(keys);
        for (int size : keys) {
            List<Block> unc = hist.get(size);
            List<Block> cla = classify(dual, unc);
            System.out.println(size + "\t" + unc.size() + "\t" + cla.size());
        }
    }
    
    @Test
    public void smallLatticeClassification() throws IOException {
        int triWidth = 7;
        int triHeight = 4;
        double r = 50;
        
        TriangleLattice tri = new TriangleLattice(triWidth, triHeight, r);
        HexLattice dual = tri.getDual();
        
        Map<Integer, List<Block>> hist = getCycles(dual);
        
        ParameterSet params = new ParameterSet();
        params.set("pointRadius", 3);
        params.set("lineWidth", 2);
        params.set("drawNumberLabels", 0);
        
        List<Integer> keys = new ArrayList<Integer>(hist.keySet());
        Collections.sort(keys);
        for (int size : keys) {
            // XXX remove
//            if (size != 14) continue;

            List<Block> unclassified = hist.get(size);
            List<Block> classified = classify(dual, unclassified);
            System.out.println("size " + size + "\t" + unclassified.size() + "\t" + classified.size());
            draw(classified, dual, params, size + "");
            // XXX remove
//            if (size > 6) break;
        }
    }
    
    public void draw(List<Block> cycles, HexLattice lattice, ParameterSet params, String prefix) throws IOException {
        int rIndex = 0;
        for (Block cycle : cycles) {
            String filename = "m_" + prefix + "_" + rIndex + ".png";
            draw(cycle, lattice, params, filename);
            rIndex++;
        }
    }
    
    public void draw(Block cycle, HexLattice lattice, ParameterSet params, String filename) throws IOException {
        Representation rep = getCycleRepr(lattice, cycle);
        int w = 300;
        int h = 200;
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        
        g.setColor(Color.GRAY);
        draw(lattice, 3, 2, g);
        
        g.setColor(Color.RED);
        rep.draw(g, params);
        
//        g.setColor(Color.GREEN);
//        g.draw(makePolygon(lattice, cycle));
        
        System.out.println("Drawing to " + filename);
        ImageIO.write((RenderedImage)image, "PNG", getFileHandle(OUT_DIR, filename));
    }
    
    @Test
    public void drawInSmallLattice() throws IOException {
        int triWidth = 7;
        int triHeight = 4;
        double r = 50;
        int cycleSize = 10;
        
        ParameterSet params = new ParameterSet();
        params.set("pointRadius", 3);
        params.set("drawNumberLabels", 0);
        
        
        TriangleLattice tri = new TriangleLattice(triWidth, triHeight, r);
        HexLattice dual = tri.getDual();
        Map<Integer, List<Block>> hist = getCycles(dual);
        
        draw(hist.get(cycleSize), dual, params, cycleSize + "");
    }
    
    @Test
    public void cycleCombinations() throws IOException {
        int triWidth = 7;
        int triHeight = 4;
        double r = 50;
        TriangleLattice tri = new TriangleLattice(triWidth, triHeight, r);
        HexLattice dual = tri.getDual();
        Graph g = dual.getGraph();
        Block graph = new Block(g);
        List<Block> basis = CycleFinder.getCycleBasis(new SpanningTree(graph));
        
        ParameterSet params = new ParameterSet();
        params.set("pointRadius", 3);
        params.set("drawNumberLabels", 0);
        draw(basis, dual, params, "basis");
        System.out.println(g);
        List<CycleBitSetPair> cycleSets = getCycleSets(basis, graph);
        List<Block> twelveCycles = new ArrayList<Block>();
        for (CycleBitSetPair pair : cycleSets) {
            Block cycle = pair.cycle;
            List<BitSet> bitSets = pair.bitSets;
            if (cycle.vsize() == 12) {
                ConnectedComponentFinder finder = new ConnectedComponentFinder();
                cycle.accept(finder);
                List<Block> components = finder.getComponents();
                if (components.size() == 1) {
                    System.out.println("CC= " + components + "\tC=" + cycle + "\tS=" + bitSets);
                    twelveCycles.add(cycle);
                }
            }
        }
        draw(twelveCycles, dual, params, "exp_12");
    }
    
    @Test
    public void fillOutTest() {
        int triWidth = 7;
        int triHeight = 4;
        double r = 50;
        TriangleLattice tri = new TriangleLattice(triWidth, triHeight, r);
        HexLattice dual = tri.getDual();
        Block cycleA = new Block(new Graph("8:9, 9:10, 10:11, 11:12, 14:15, 8:15, 12:19, 19:20, 14:21, 21:22, 22:23, 23:24, 24:25, 25:26, 26:27, 20:27"));
        Block full = fillOut(dual, cycleA);
        try {
            draw(full, dual, getParams(), "tmp.png");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(full);
        BlockEmbedding embedding = null;
        try {
            embedding = PlanarBlockEmbedder.embedInCycle(new Block(cycleA), full, new Graph());
        } catch (NullPointerException npe) {
            System.out.println(npe);
        }
        
        if (embedding != null) {
          Graph innerDual = DualFinder.getDual(embedding);
          String sig = new GraphSignature(innerDual).toCanonicalString();
          System.out.println(sig);
        } else {
            System.out.println("embedding null");
        }
    }
    
    class CycleBitSetPair {
        public Block cycle;
        public List<BitSet> bitSets;
        
        public CycleBitSetPair(Block cycle, List<BitSet> bitSets) {
            this.cycle = cycle;
            this.bitSets = bitSets;
        }
    }
    
    public List<CycleBitSetPair> getCycleSets(List<Block> basis, Block graph) {
        List<CycleBitSetPair> map = new ArrayList<CycleBitSetPair>();
        List<BitSet> baseSets = new ArrayList<BitSet>();
        for (Block cycle : basis) {
            BitSet bitSet = CycleFinder.cycleToBitSet(cycle, graph);
            baseSets.add(bitSet);
        }
        SubsetLister<BitSet> subsetLister = new SubsetLister<BitSet>(baseSets);
        
        for (List<BitSet> subSet : subsetLister) {
            if (!viable(subSet)) continue;
            BitSet bS = CycleFinder.combine(subSet);
            Block cycle = CycleFinder.toCycle(bS, graph);
            map.add(new CycleBitSetPair(cycle, subSet));
        }
        return map;
    }
    
    public boolean viable(List<BitSet> subSet) {
        for (int i = 0; i < subSet.size(); i++) {
            BitSet setI = subSet.get(i);
            for (int j = i + 1; j < subSet.size(); j++) {
                BitSet setJ = subSet.get(j);
                if (setI.intersects(setJ)) {
                    continue;
                } else {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Test
    public void combineTest() {
        int triWidth = 7;
        int triHeight = 4;
        double r = 50;
        TriangleLattice tri = new TriangleLattice(triWidth, triHeight, r);
        HexLattice dual = tri.getDual();
        Graph g = dual.getGraph();
        Block graph = new Block(g);
//        List<Block> basis = CycleFinder.getCycleBasis(new SpanningTree(graph));
        
        BitSet a = makeBitSet(2, 3, 4, 5, 9, 10, 11, 13, 14, 15);
        BitSet b = makeBitSet(8, 10, 11, 13, 16, 17, 19, 21, 22, 23, 25, 26, 27, 28);
        BitSet c = makeBitSet(0, 1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 13, 14, 15);
        
        BitSet abc = xor(a, xor(b, c));
        BitSet acb = xor(a, xor(c, b));
        BitSet bac = xor(b, xor(a, c));
        BitSet bca = xor(b, xor(c, a));
        
        System.out.println("A + B + C " + CycleFinder.toCycle(abc, graph) + "\t" + abc);
        System.out.println("A + C + B " + CycleFinder.toCycle(acb, graph) + "\t" + acb);
        System.out.println("B + A + C " + CycleFinder.toCycle(bac, graph) + "\t" + bac);
        System.out.println("B + C + A " + CycleFinder.toCycle(bca, graph) + "\t" + bca);
    }
    
    public BitSet xor(BitSet a, BitSet b) {
        BitSet c = new BitSet();
        c.or(a);
        c.xor(b);
        return c;
    }
    
    public BitSet makeBitSet(int... n) {
        BitSet bS = new BitSet(n.length);
        for (int i : n) {
            bS.set(i);
        }
        return bS;
    }
    
}
