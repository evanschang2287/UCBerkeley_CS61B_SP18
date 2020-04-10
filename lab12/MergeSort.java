import edu.princeton.cs.algs4.Queue;

import java.util.Iterator;

public class MergeSort {
    /**
     * Removes and returns the smallest item that is in q1 or q2.
     *
     * The method assumes that both q1 and q2 are in sorted order, with the smallest item first. At
     * most one of q1 or q2 can be empty (but both cannot be empty).
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      The smallest item that is in q1 or q2.
     */
    private static <Item extends Comparable> Item getMin(Queue<Item> q1, Queue<Item> q2) {
        if (q1.isEmpty()) {
            return q2.dequeue();
        } else if (q2.isEmpty()) {
            return q1.dequeue();
        } else {
            // Peek at the minimum item in each queue (which will be at the front, since the
            // queues are sorted) to determine which is smaller.
            Comparable q1Min = q1.peek();
            Comparable q2Min = q2.peek();
            if (q1Min.compareTo(q2Min) <= 0) { //q1Min <= q2Min
                // Make sure to call dequeue, so that the minimum item gets removed.
                return q1.dequeue();
            } else {
                return q2.dequeue();
            }
        }
    }

    /** Returns a queue of queues that each contain one item from items. */
    private static <Item extends Comparable> Queue<Queue<Item>> makeSingleItemQueues(Queue<Item> items) {
        Queue<Queue<Item>> qOfQueues = new Queue<>();
        for (Item item : items) {
            Queue<Item> addedQueue = new Queue<>();
            addedQueue.enqueue(item);
            qOfQueues.enqueue(addedQueue);
        }
        return qOfQueues;
    }

    /**
     * Returns a new queue that contains the items in q1 and q2 in sorted order.
     *
     * This method should take time linear in the total number of items in q1 and q2.  After
     * running this method, q1 and q2 will be empty, and all of their items will be in the
     * returned queue.
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      A Queue containing all of the q1 and q2 in sorted order, from least to
     *              greatest.
     *
     */
    private static <Item extends Comparable> Queue<Item> mergeSortedQueues(Queue<Item> q1, Queue<Item> q2) {
        Queue<Item> mergedQueue = new Queue<>();
        int N = q1.size() + q2.size();
        for (int i = 0; i < N; i++) {
            Item addedItem = getMin(q1, q2);
            mergedQueue.enqueue(addedItem);
        }
        return mergedQueue;
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> mergeSort(Queue<Item> items) {
        Queue<Item> queue = new Queue<>();
        Queue<Queue<Item>> singleItemQueue = makeSingleItemQueues(items);
        Iterator<Queue<Item>> qIter = singleItemQueue.iterator();

        while (qIter.hasNext()) {
            queue = mergeSortedQueues(queue, qIter.next());
        }

        return queue;
    }

    /* Test client of mergeSort. */
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

        Queue<String> mergedQ1 = MergeSort.mergeSort(q1);
        System.out.println(mergedQ1.toString());  // Should be: Alice Bob Cathy Ethan Kate Mike Vanessa Zion

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

        Queue<String> mergedQ2 = MergeSort.mergeSort(q2);
        System.out.println(mergedQ2.toString());  //Should be: Dog Elephant Fox Giraffe Lion Mouse Whale Zebra
    }
}
