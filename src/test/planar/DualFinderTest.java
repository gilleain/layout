package test.planar;

import graph.model.Graph;

import org.junit.Test;

import planar.BlockEmbedding;
import planar.DualFinder;
import planar.DualTreeEmbedder;
import planar.Face;

public class DualFinderTest {
    
    public void test(Graph g) {
        BlockEmbedding e = DualTreeEmbedder.embed(g);
        Graph dual = DualFinder.getDual(e);
        int counter = 0;
        for (Face f : e.getFaces()) {
            System.out.println(counter + "\t" + f);
            counter++;
        }
        System.out.println(dual);
    }
    
    @Test
    public void fusane6_0_Test() {
        Graph g = new Graph("0:1,0:5,0:6,10:11,11:12,12:13,14:15,15:16,16:17,18:19,18:22," +
                            "19:20,19:25,1:2,1:9,20:21,22:23,23:24,24:25,2:18,2:3,3:21,3:4," +
                            "4:5,6:10,6:7,7:13,7:8,8:14,8:9,9:17");
       test(g);
    }
    
    @Test
    public void fusane6_1_Test() {
        Graph g = new Graph("0:1,0:5,0:6,1:2,1:9,2:3,3:4,4:5,4:18,5:21,6:7,6:10,7:8,7:13,8:9,8:14," +
                            "9:17,10:11,11:12,12:13,14:15,15:16,16:17,18:19,18:22,19:20,19:25," +
                            "20:21,22:23,23:24,24:25");
        test(g);
    }
    
    @Test
    public void fusane6_2_Test() {
        Graph g = new Graph("0:1,0:5,0:6,1:2,1:9,2:3,3:4,3:18,4:5,4:21,6:7,6:10,7:8,7:13,8:9,8:14," +
                            "9:17,10:11,11:12,12:13,14:15,15:16,16:17,18:19,18:22,19:20,19:25,20:21," +
                            "22:23,23:24,24:25");
        test(g);
    }
    
}
