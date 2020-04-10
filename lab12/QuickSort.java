import edu.princeton.cs.algs4.Queue;

import java.util.Iterator;

public class QuickSort {
    /**
     * Returns a new queue that contains the given queues catenated together.
     *
     * The items in q2 will be catenated after all of the items in q1.
     */
    private static <Item extends Comparable> Queue<Item> catenate(Queue<Item> q1, Queue<Item> q2) {
        Queue<Item> catenated = new Queue<Item>();
        for (Item item : q1) {
            catenated.enqueue(item);
        }
        for (Item item: q2) {
            catenated.enqueue(item);
        }
        return catenated;
    }

    /** Returns a random item from the given queue. */
    private static <Item extends Comparable> Item getRandomItem(Queue<Item> items) {
        int pivotIndex = (int) (Math.random() * items.size());
        Item pivot = null;
        // Walk through the queue to find the item at the given index.
        for (Item item : items) {
            if (pivotIndex == 0) {
                pivot = item;
                break;
            }
            pivotIndex--;
        }
        return pivot;
    }

    /**
     * Partitions the given unsorted queue by pivoting on the given item.
     *
     * @param unsorted  A Queue of unsorted items
     * @param pivot     The item to pivot on
     * @param less      An empty Queue. When the function completes, this queue will contain
     *                  all of the items in unsorted that are less than the given pivot.
     * @param equal     An empty Queue. When the function completes, this queue will contain
     *                  all of the items in unsorted that are equal to the given pivot.
     * @param greater   An empty Queue. When the function completes, this queue will contain
     *                  all of the items in unsorted that are greater than the given pivot.
     */
    private static <Item extends Comparable> void partition(Queue<Item> unsorted, Item pivot, Queue<Item> less, Queue<Item> equal, Queue<Item> greater) {
        for (Item item : unsorted) {
            int cmp = item.compareTo(pivot);
            if (cmp < 0) {
                less.enqueue(item);
            } else if (cmp > 0) {
                greater.enqueue(item);
            } else {
                equal.enqueue(item);
            }
        }
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> quickSort(Queue<Item> items) {
        /* Base case */
        if (items.size() <= 1) {
            return items;
        }

        Queue<Item> lessQueue = new Queue<>();
        Queue<Item> equalQueue = new Queue<>();
        Queue<Item> greaterQueue = new Queue<>();
        Item pivot = getRandomItem(items);

        partition(items, pivot, lessQueue, equalQueue, greaterQueue);
        Queue<Item> sortedLess = quickSort(lessQueue);
        Queue<Item> sortedGreater = quickSort(greaterQueue);

        Queue<Item> sortedQueue = catenate(sortedLess, equalQueue);
        sortedQueue = catenate(sortedQueue, sortedGreater);

        return sortedQueue;
    }

    /* Test client of Quick Sort. */
    public static void main(String[] args) {
        Queue<String> q1 = new Queue<>();
        q1.enqueue("Alice");
        q1.enqueue("Vanessa");
        q1.enqueue("Ethan");
        q1.enqueue("Bob");
        q1.enqueue("Zion");
        q1.enqueue("Mike");
        q1.enqueue("Kate");
        q1.enqueue("Cathy");
        System.out.println(q1.toString());

        Queue<String> quickQ1 = QuickSort.quickSort(q1);
        System.out.println(quickQ1.toString());  // Should be: Alice Bob Cathy Ethan Kate Mike Vanessa Zion

        System.out.println();

        Queue<String> q2 = new Queue<>();
        q2.enqueue("Dog");
        q2.enqueue("Giraffe");
        q2.enqueue("Whale");
        q2.enqueue("Elephant");
        q2.enqueue("Zebra");
        q2.enqueue("Lion");
        q2.enqueue("Fox");
        q2.enqueue("Mouse");
        System.out.println(q2.toString());

        Queue<String> quickQ2 = QuickSort.quickSort(q2);
        System.out.println(quickQ2.toString());  //Should be: Dog Elephant Fox Giraffe Lion Mouse Whale Zebra
    }
}
