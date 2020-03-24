package synthesizer;
import java.util.Iterator;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestIterator {
    @Test
    public void testIterator() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(8);
        for (int i = 0; i < arb.capacity; i++) {
            arb.enqueue(i);
        }
        arb.dequeue();
        arb.dequeue();
        arb.dequeue();
        arb.enqueue(8);
        arb.enqueue(9);

        //              L  F
        // arb = [8, 9, N, 3, 4, 5, 6, 7]
        // index  0  1  2  3  4  5  6  7

        Iterator<Integer> iter = arb.iterator();
        while (iter.hasNext()) {
            System.out.print(iter.next() + " ");
        }
    }
}
