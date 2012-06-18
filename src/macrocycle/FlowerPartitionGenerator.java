package macrocycle;

import group.Partition;

import java.util.ArrayList;
import java.util.List;

import combinatorics.PartitionCalculator;

/**
 * Given a ring of size n, returns the partitions of m = n + r, where r is the number of
 * subrings. For example, a ring of size 15 can be divided into 5 rings of size 4, or 4 
 * rings of size 5. The number r is in the range (3, n/3), as no subring is smaller than 3.
 * 
 * @author maclean
 * 
 *
 */
public class FlowerPartitionGenerator {
    
    public static List<Partition> getPartitions(int n) {
        List<Partition> partitions = new ArrayList<Partition>();
        int max = (int)Math.floor(n / 3);
        for (int r = 3; r <= max; r++) {
            int m = n + r;
            for (Partition p : PartitionCalculator.partition(m, r)) {
                if (hasMinPart(p, 3)) {
                    partitions.add(p);
                }
            }
        }
        return partitions;
    }

    /**
     * Checks a partition to see if all of its parts are greater than or equal to min
     * @param p
     * @param min
     * @return
     */
    private static boolean hasMinPart(Partition p, int min) {
        for (int index = 0; index < p.numberOfElements(); index++) {
            if (p.getCell(index).first() < min) {
                return false;
            }
        }
        return true;
    }
    
}
