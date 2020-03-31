package hw3.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        double low = oomages.size() / 50.0;
        double high = oomages.size() / 2.5;

        int[] buckets = new int[M];
        for (Oomage o : oomages) {
            int bucketNum = (o.hashCode() & 0x7FFFFFFF) % M;
            buckets[bucketNum] += 1;
        }

        for (int bNum : buckets) {
            if (bNum < low || bNum > high) {
                return false;
            }
        }
        return true;
    }
}
