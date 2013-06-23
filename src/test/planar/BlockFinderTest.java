package test.planar;

import graph.model.Graph;
import graph.model.GraphFileReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import org.junit.Test;

import planar.Block;
import planar.GraphObject;
import planar.SpanningTree;
import planar.visitor.BlockFinder;

public class BlockFinderTest {
    
    public void findBlocks(GraphObject g) {
        System.out.println(g);
        BlockFinder finder = new BlockFinder();
        g.accept(finder);
        for (Block block : finder.getBlocks()) {
            System.out.println(block);
        }
        finder.outputInternals();
    }
    
    @Test
    public void bookTest() {
        findBlocks(new GraphObject(new Graph("0:1,0:3,0:4,0:5,1:5,2:3,3:4")));
    }
    
    @Test
    public void fourStar() {
        findBlocks(new GraphObject(new Graph("0:1,0:2,0:3,0:4")));
    }
    
    @Test
    public void triangularDumbellGraph() {
        findBlocks(new GraphObject(new Graph("0:1,0:2,1:2,2:3,3:4,4:5,5:6,5:7,6:7")));
    }
    
    public void graphListTest(String filePath) throws FileNotFoundException {
        BlockFinder finder = new BlockFinder();
        int count = 0;
        for (Graph g : new GraphFileReader(new FileReader(filePath))) {
            GraphObject go = new GraphObject(g);
            go.accept(finder);
            List<Block> blocks = finder.getBlocks();
            SpanningTree tree = new SpanningTree(go);
            System.out.println(
                             count + "\t" + 
                             blocks.size() + "\t" + 
                             tree.getCycleEdges().size() + "\t"
                             + g + "\t" + blocks);
            count++;
        }
    }
    
    @Test
    public void graphListTestOn5() throws FileNotFoundException {
        graphListTest("output/hybrid/five_x.txt");
    }
    
    @Test
    public void graphListTestOn6() throws FileNotFoundException {
        graphListTest("output/hybrid/six_x.txt");
    }
    
    @Test
    public void graphListTestOn7() throws FileNotFoundException {
        graphListTest("output/hybrid/seven_x.txt");
    }
    
}
