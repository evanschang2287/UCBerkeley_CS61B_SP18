import java.util.LinkedList;
import java.util.Queue;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        Queue<String>[] buckets = new Queue[256];
        for (int i = 0; i < 256; i++) {
            buckets[i] = new LinkedList();
        }

        boolean sorted = false;
        int length = 0;
        String[] sortedArr = new String[asciis.length];
        System.arraycopy(asciis, 0, sortedArr, 0, asciis.length);

        while (!sorted) {
            sorted = true;
            for (String item : sortedArr) {
                int idx = item.length() - length - 1;
                if (idx >= 0) {
                    sorted = false;
                    int ofASCII = item.charAt(idx);
                    buckets[ofASCII].add(item);
                } else {
                    buckets[item.charAt(0)].add(item);
                }
            }
            length++;

            int idx = 0;
            for (Queue<String> bucket : buckets) {
                while (!bucket.isEmpty()) {
                    sortedArr[idx] = bucket.remove();
                    idx++;
                }
            }
        }
        return sortedArr;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        String[] temp = new String[asciis.length];
        int[] range = new int[256];
        for (int i = 0; i < asciis.length; i++) {
            int idx = asciis[i].charAt(index); // implicitly convert char to int
            range[idx]++;
        }
        for (int i = 0; i < range.length - 1; i++) {
            range[i + 1] += range[i];
        }
        for (int i = asciis.length - 1; i >= 0; i--) {
            int idx = asciis[i].charAt(index);
            range[idx]--;
            temp[range[idx]] = asciis[i];
        }
        System.arraycopy(temp, 0, asciis, 0, temp.length);
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    public static void main(String[] args) {
        String[] original = {"cat", "cat", "dog", "cat", "dog", "person", "cat", "person", "person"};
        String[] sorted = sort(original);

        System.out.println("Original:");
        for (int i = 0; i < original.length; i++) {
            System.out.print(original[i] + " ");
        }
        System.out.println();

        System.out.println("Sorted:");
        for (int i = 0; i < sorted.length; i++) {
            System.out.print(sorted[i] + " ");
        }
    }
}
