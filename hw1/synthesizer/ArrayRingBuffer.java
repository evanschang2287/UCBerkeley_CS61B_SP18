package synthesizer;
import java.util.Iterator;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        rb = (T[]) new Object[capacity];
        this.capacity = capacity;
        first = 0;
        last = 0;
        fillCount = 0;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    @Override
    public void enqueue(T x) {
        if (isFull()) {
            throw new RuntimeException("Ring buffer overflow");
        }
        rb[last] = x;
        last = (last + 1) % rb.length;
        fillCount += 1;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        T deleted = rb[first];
        rb[first] = null;
        first = (first + 1) % rb.length;
        fillCount -= 1;

        return deleted;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    @Override
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }

        return rb[first];
    }

    public Iterator<T> iterator() {
        return new ARBIterator();
    }

    private class ARBIterator implements Iterator<T> {
        private int iter;
        private int count;

        public ARBIterator() {
            iter = first;
            count = 0;
        }

        public boolean hasNext() {
            return count < capacity;
        }

        public T next() {
            T returned = rb[iter];
            iter = (iter + 1) % rb.length;
            count += 1;

            return returned;
        }
    }
}
