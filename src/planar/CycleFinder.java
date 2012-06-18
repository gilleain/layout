package planar;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import planar.visitor.ConnectedComponentFinder;

import combinatorics.SubsetLister;

/**
 * Find all cycles in a graph using a spanning tree.
 * 
 * @author maclean
 *
 */
public class CycleFinder {
    
    public static Block getMaxCycle(Block block) {
        // XXX -assumes the graph is a block!
        return CycleFinder.getMaxCycle(CycleFinder.findAll(block));
    }
    
    public static Block getMaxCycle(List<Block> cycles) {
        int maxCycleSize = -1;
        for (Block cycle : cycles) {
            int size = cycle.esize();
//            System.out.println("cycle of size " + size + " = " + cycle);
            if (size > maxCycleSize) {
                maxCycleSize = size;
            }
        }
        Block maxCycle = null;
        for (Block cycle : cycles) {
            int size = cycle.esize();
            if (size == maxCycleSize) {
                ConnectedComponentFinder finder = new ConnectedComponentFinder();
                finder.visit(cycle, cycle.getVertex(0));
                if (finder.getComponents().size() == 1) {
                    maxCycle = cycle;   // what if there is more than one?
                }
            }
        }
        return maxCycle;
    }
    
    public static List<Block> getSortedConnectedSimpleCycles(List<Block> cycles) {
        List<Block> connectedCycles = new ArrayList<Block>();
        for (Block cycle : cycles) {
            if (cycle.esize() == cycle.vsize()) {
                ConnectedComponentFinder finder = new ConnectedComponentFinder();
                cycle.accept(finder);
                if (finder.getComponents().size() == 1) {
                    connectedCycles.add(cycle);
                }
            }
        }
        Collections.sort(connectedCycles, new Comparator<Block>() {

            public int compare(Block b0, Block b1) {
                return ((b0.esize() < b1.esize()) ? 1: (b0.esize() > b1.esize())? -1 : 0); 
            }
            
        });
//        for (Block cycle : connectedCycles) {
//            ConnectedComponentFinder finder = new ConnectedComponentFinder();
//            cycle.accept(finder);
//            finder.visit(cycle, cycle.getVertex(0));
//            System.out.println(finder.getComponents().size() 
//                    + "\t" + cycle + "\t" + finder.getComponents());
//        }
        return connectedCycles;
    }
	
	public static BitSet cycleToBitSet(Block cycle, Block graph) {
		BitSet bitSet = new BitSet(graph.esize());
		int i = 0;
		for (Edge e : graph.edges) {
			if (cycle.hasEdge(e)) {
				bitSet.set(i);
			}
			i++;
		}
		return bitSet;
	}
	
	public static List<Block> getCycleBasis(SpanningTree tree) {
	    // get the fundamental cycles
        List<Block> cycleBasis = new ArrayList<Block>();
        for (Edge edge : tree.getCycleEdges()) {
            Path p = tree.getPath(edge.getA(), edge.getB());
            List<Edge> c = new ArrayList<Edge>();
            c.addAll(p.edges);
            c.add(edge);
            cycleBasis.add(new Block(null, c));
        }
        return cycleBasis;
	}
	
	public static List<Block> findAll(Block block) {
		// get the fundamental cycles
		List<Block> cycleBasis = getCycleBasis(new SpanningTree(block));
		
		// expand these by combining them
		List<Block> allCycles = new ArrayList<Block>();
		expand(cycleBasis, allCycles, block);
		return allCycles;
	}
	
//	private static void print(BitSet bitSet, Block cycle, Block graph) {
//		for (int i = 0; i < graph.esize(); i++) {
//			System.out.print(bitSet.get(i)? 1 : 0);
//		}
//		System.out.print(" " + cycle);
//		System.out.println();
//	}
	
	public static void expand(List<Block> basis, List<Block> all, Block graph) {
		List<BitSet> baseSets = new ArrayList<BitSet>();
		for (Block cycle : basis) {
			BitSet bitSet = cycleToBitSet(cycle, graph);
			baseSets.add(bitSet);
		}
		SubsetLister<BitSet> subsetLister = new SubsetLister<BitSet>(baseSets);
		List<BitSet> combinations = new ArrayList<BitSet>();
		for (List<BitSet> subSet : subsetLister) {
		    combinations.add(combine(subSet));
		}
		
		for (BitSet bS : combinations) {
			all.add(toCycle(bS, graph));
		}
	}
	
	public static BitSet combine(List<BitSet> bitSets) {
	    BitSet combination = new BitSet();
	    if (bitSets.size() == 0) {
	        return combination;
	    } else {
	        combination.or(bitSets.get(0));
	    }
	    for (int index = 1; index < bitSets.size(); index++) {
	        BitSet set = bitSets.get(index);
            BitSet setOr = new BitSet();
            setOr.or(set);
            setOr.xor(combination);
            combination = setOr;
	    }
	    return combination;
	}
	
	public static Block toCycle(BitSet bS, Block graph) {
		List<Edge> edges = new ArrayList<Edge>();
		for (int i = bS.nextSetBit(0); i >= 0; i = bS.nextSetBit(i + 1)) {
			Edge e = graph.edges.get(i);
			edges.add(e);
		}
		return new Block(null, edges);
	}

}
