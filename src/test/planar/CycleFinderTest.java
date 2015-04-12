package test.planar;

import graph.model.Block;
import graph.model.CycleFinder;
import graph.model.IntGraph;
import graph.model.SpanningTree;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import combinatorics.SubsetLister;

public class CycleFinderTest {
    
    @Test
    public void fusedPentagons() {
        IntGraph g = new IntGraph("0:1,0:4,1:2,2:3,3:4,3:7,4:5,5:6,6:7");
        Block b = new Block(g);
        SpanningTree tree = new SpanningTree(b);
        System.out.println("T= " + tree.getTree());
        System.out.println("cE= " + tree.getCycleEdges());
        List<Block> basis = CycleFinder.getCycleBasis(tree);
        System.out.println("FCS= " + basis);
        int counter = 0;
        List<Block> all = new ArrayList<Block>();
        CycleFinder.expand(basis, all, b);
        for (Block cycle : all) {
            System.out.println(counter + "\t" + cycle);
            counter++;
        }
    }
	
	public void find(Block b) {
	    SpanningTree tree = new SpanningTree(b);
        List<Block> basis = CycleFinder.getCycleBasis(tree);
        int basisCounter = 0;
        for (Block cycle : basis) {
            System.out.println(basisCounter + "\t" + cycle);
            basisCounter++;
        }
        
        List<Block> all = new ArrayList<Block>();
        CycleFinder.expand(basis, all, b);
        int fullCounter = 0;
		for (Block cycle : all) {
		    System.out.println(fullCounter + "\t" + cycle.getEdgeCount() + "\t" + cycle);
            fullCounter++;
		}
		
		// this assertion fails for disconnected graphs - does the CC number count in the formula?
		Assert.assertEquals((b.getEdgeCount() - b.getVertexCount() + 1), basisCounter);
		
		Assert.assertEquals((int)Math.pow(2, basisCounter), fullCounter);
	}
	
	public void debugFind(Block b) {
	    SpanningTree tree = new SpanningTree(b);
        List<Block> basis = CycleFinder.getCycleBasis(tree);
        List<BitSet> baseSets = new ArrayList<BitSet>();
        
        int basisCounter = 0;
        for (Block cycle : basis) {
            baseSets.add(CycleFinder.cycleToBitSet(cycle, b));
            System.out.println(basisCounter + "\t" + cycle);
            basisCounter++;
        }
        
        SubsetLister<BitSet> subsetLister = new SubsetLister<BitSet>(baseSets);
        
        int fullCounter = 0;
        for (List<BitSet> subSet : subsetLister) {
            if (!viable(subSet)) continue;
            BitSet bS = CycleFinder.combine(subSet);
            Block cycle = CycleFinder.toCycle(bS, b);
            System.out.println(fullCounter + "\t" + cycle.getEdgeCount() + "\t" + cycle + "\t" + subSet);
            fullCounter++;
        }
	}
	
	@Test
	public void simpleCycleTest() {
	    Block b = new Block(3);
	    b.add(0, 1, 2);
	    b.add(1, 2);
	    debugFind(b);
	}
	
	@Test
	public void commuteTest() {
	    Block b = new Block(8);
        b.add(0, 1, 7);
        b.add(1, 2, 6);
        b.add(2, 3, 5);
        b.add(3, 4);
        b.add(4, 5);
        b.add(5, 6);
        b.add(6, 7);
	    
        BitSet bsA = makeBitSet(2, 3, 5, 8);
        BitSet bsB = makeBitSet(2, 3, 4, 6, 7, 8);
        BitSet bsC = makeBitSet(0, 1, 2, 4, 6, 7, 8, 9);
        
        System.out.println("A + B " + xor(bsA, bsB));
        System.out.println("B + A " + xor(bsB, bsA));
        
        System.out.println("A + C " + xor(bsA, bsC));
        System.out.println("C + A " + xor(bsC, bsA));
        
        System.out.println("A + B + C" + xor(xor(bsA, bsB), bsC));
        System.out.println("B + A + C" + xor(xor(bsB, bsA), bsC));
        
        System.out.println("A + C + B" + xor(xor(bsA, bsC), bsB));
        System.out.println("C + A + B" + xor(xor(bsC, bsA), bsB));
        
        System.out.println("C + B + A" + xor(xor(bsC, bsB), bsA));
        System.out.println("B + C + A" + xor(xor(bsB, bsC), bsA));
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
	public void fused566Cycles() {
		Block b = new Block(13);
		b.add(0, 1, 2);
		b.add(1, 3);
		b.add(2, 4, 5);
		b.add(3, 6);
		b.add(4, 8);
		b.add(5, 6, 7);
		b.add(7, 9, 10);
		b.add(8, 10);
		b.add(9, 11);
		b.add(10, 12);
		b.add(11, 12);
		find(b);
	}
	
	@Test
	public void disconnectedGraph() {
		Block b = new Block(7);
		b.add(0, 1, 2);
		b.add(1, 3);
		b.add(2, 3);
		b.add(4, 5, 6);
		b.add(5, 6);
		find(b);
	}
	
	@Test
	public void cuneane() {
		Block b = new Block(8);
		b.add(0, 1, 3, 5);
		b.add(1, 2, 7);
		b.add(2, 3, 7);
		b.add(3, 4);
		b.add(4, 5, 6);
		b.add(5, 6);
		b.add(6, 7);
		find(b);
	}
	
	@Test
	public void cube() {
		Block b = new Block(8);
		b.add(0, 1, 5, 7);
		b.add(1, 2, 6);
		b.add(2, 3, 7);
		b.add(3, 4, 6);
		b.add(4, 5, 7);
		b.add(5, 6);
		find(b);
	}
	
	@Test
	public void fusedSquares() {
		Block b = new Block(8);
		b.add(0, 1, 7);
		b.add(1, 2, 6);
		b.add(2, 3, 5);
		b.add(3, 4);
		b.add(4, 5);
		b.add(5, 6);
		b.add(6, 7);
		debugFind(b);
	}
}
