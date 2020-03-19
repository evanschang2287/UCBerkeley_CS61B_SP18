public class ArrayDeque<T> implements Deque<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 0;
        nextLast = nextFirst + 1;
    }

    private void resize(int capacity) {
        T[] temp = (T[]) new Object[capacity];
        if (nextFirst > nextLast) {
            System.arraycopy(items, (nextFirst + 1) % items.length, temp, 0, size);
        } else {
            System.arraycopy(items, (nextFirst + 1) % items.length, temp,
                    0, items.length - (nextFirst + 1));
            System.arraycopy(items, 0, temp, items.length - (nextFirst + 1), nextLast);
        }
        items = temp;
        nextFirst = items.length - 1;
        nextLast = size;
    }

    private void reduce(int capacity) {
        T[] temp = (T[]) new Object[capacity];
        if (nextFirst > nextLast) {
            System.arraycopy(items, (nextFirst + 1) % items.length, temp,
                    0, items.length - (nextFirst + 1));
            System.arraycopy(items, 0, temp, items.length - (nextFirst + 1), nextLast);
        } else {
            System.arraycopy(items, (nextFirst + 1) % items.length, temp, 0, size);
        }
        items = temp;
        nextFirst = items.length - 1;
        nextLast = size;
    }

    @Override
    /** addFirst will add the item at position nextFirst */
    public void addFirst(T item) {
        if (isFull()) {
            resize(items.length * 2);
        }

        items[nextFirst] = item;
        nextFirst = (nextFirst - 1 + items.length) % items.length;
        size += 1;
    }

    @Override
    /** addLast will add the item at position nextLast */
    public void addLast(T item) {
        if (isFull()) {
            resize(items.length * 2);
        }

        items[nextLast] = item;
        nextLast = (nextLast + 1) % items.length;
        size += 1;
    }

    private boolean isSparse() {
        return items.length >= 16 && (double) size / items.length < 0.25;
    }

    private boolean isFull() {
        return size == items.length;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        int i = (nextFirst + 1) % items.length;
        int times = 0;
        while (times < size) {
            System.out.print(items[i] + " ");
            i = (i + 1) % items.length;
            times += 1;
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        T removed = items[(nextFirst + 1) % items.length];
        items[(nextFirst + 1) % items.length] = null;
        nextFirst = (nextFirst + 1) % items.length;
        size -= 1;
        if (size < 0) {
            size = 0;
        }

        if (isSparse()) {
            reduce(items.length / 2);
        }

        return removed;
    }

    @Override
    public T removeLast() {
        T removed = items[(nextLast - 1 + items.length) % items.length];
        items[(nextLast - 1 + items.length) % items.length] = null;
        nextLast = (nextLast - 1 + items.length) % items.length;
        size -= 1;
        if (size < 0) {
            size = 0;
        }

        if (isSparse()) {
            reduce(items.length / 2);
        }

        return removed;
    }

    @Override
    public T get(int index) {
        return items[(nextFirst + 1 + index) % items.length];
    }

    private T getRecursiveHelper(int start, int index) {
        if (index == 0) {
            return get(start);
        } else {
            return getRecursiveHelper(start + 1, index - 1);
        }
    }

    private T getRecursive(int index) {
        if (index >= (nextFirst + 1 + size) % items.length) {
            return null;
        }

        return getRecursiveHelper(0, index);
    }
}
