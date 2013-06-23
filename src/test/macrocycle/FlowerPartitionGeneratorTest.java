package test.macrocycle;

import group.Partition;
import macrocycle.FlowerPartitionGenerator;

import org.junit.Test;

public class FlowerPartitionGeneratorTest {
    
    public void test(int n) {
        int count = 0;
        for (Partition p : FlowerPartitionGenerator.getPartitions(n)) {
            System.out.println(n + "\t" + count + "\t" + p);
            count++;
        }
    }
    
    @Test
    public void test8To12() {
        for (int n = 8; n <= 12; n++) {
            test(n);
        }
    }
    
}
