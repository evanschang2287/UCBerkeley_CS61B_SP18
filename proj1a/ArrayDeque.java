public class ArrayDeque<T> {
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

    public void resize(int capacity) {
        T[] temp = (T[]) new Object[capacity];
        if (nextFirst > nextLast) {
            System.arraycopy(items, (nextFirst + 1) % items.length, temp, 0, size);
        }
        else {
            System.arraycopy(items, (nextFirst + 1) % items.length, temp, 0, items.length - (nextFirst + 1));
            System.arraycopy(items, 0, temp, items.length - (nextFirst + 1), nextLast);
        }
        items = temp;
        nextFirst = items.length - 1;
        nextLast = size;
    }

    public void reduce(int capacity) {
        T[] temp = (T[]) new Object[capacity];
        if (nextFirst > nextLast) {
            System.arraycopy(items, (nextFirst + 1) % items.length, temp, 0, items.length - (nextFirst + 1));
            System.arraycopy(items, 0, temp, items.length - (nextFirst + 1), nextLast);
        }
        else {
            System.arraycopy(items, (nextFirst + 1) % items.length, temp, 0, size);
        }
        items = temp;
        nextFirst = items.length - 1;
        nextLast = size;
    }

    /** addFirst will add the item at position nextFirst */
    public void addFirst(T item) {
        if (isFull()) resize(items.length * 2);

        items[nextFirst] = item;
        nextFirst = (nextFirst - 1 + items.length) % items.length;
        size += 1;
    }

    /** addLast will add the item at position nextLast */
    public void addLast(T item) {
        if (isFull()) resize(items.length * 2);

        items[nextLast] = item;
        nextLast = (nextLast + 1) % items.length;
        size += 1;
    }

    public boolean isSparse() {
        return items.length >= 16 && (double) size / items.length < 0.25;
    }

    public boolean isFull() {
        return size == items.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

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

    public T removeFirst() {
        T removed = items[(nextFirst + 1) % items.length];
        items[(nextFirst + 1) % items.length] = null;
        nextFirst = (nextFirst + 1) % items.length;
        size -= 1;

        if (isSparse()) reduce(items.length / 2);

        return removed;
    }

    public T removeLast() {
        T removed = items[(nextLast - 1 + items.length) % items.length];
        items[(nextLast - 1 + items.length) % items.length] = null;
        nextLast = (nextLast - 1 + items.length) % items.length;
        size -= 1;

        if (isSparse()) reduce(items.length / 2);

        return removed;
    }

    public T get(int index) {
        return items[(nextFirst + 1 + index) % items.length];
    }

    private T getRecursiveHelper(int start, int index) {
        if (index == 0) return get(start);
        else            return getRecursiveHelper(start + 1, index - 1);
    }

    public T getRecursive(int index) {
        if (index >= (nextFirst + 1 + size) % items.length) return null;

        return getRecursiveHelper(0, index);
    }

    public static void main(String[] args) {
        ArrayDeque<String> arr = new ArrayDeque<String>();

        arr.addLast("a");
        System.out.print("nF = " + arr.nextFirst + ", nL = " + arr.nextLast + ", size = " + arr.size + "\n");
        arr.printDeque();

        arr.addLast("b");
        System.out.print("nF = " + arr.nextFirst + ", nL = " + arr.nextLast + ", size = " + arr.size + "\n");
        arr.printDeque();

        arr.addFirst("c");
        System.out.print("nF = " + arr.nextFirst + ", nL = " + arr.nextLast + ", size = " + arr.size + "\n");
        arr.printDeque();

        arr.addLast("d");
        System.out.print("nF = " + arr.nextFirst + ", nL = " + arr.nextLast + ", size = " + arr.size + "\n");
        arr.printDeque();

        arr.addLast("e");
        System.out.print("nF = " + arr.nextFirst + ", nL = " + arr.nextLast + ", size = " + arr.size + "\n");
        arr.printDeque();

        arr.addFirst("f");
        System.out.print("nF = " + arr.nextFirst + ", nL = " + arr.nextLast + ", size = " + arr.size + "\n");
        arr.printDeque();

        arr.addFirst("g");
        System.out.print("nF = " + arr.nextFirst + ", nL = " + arr.nextLast + ", size = " + arr.size + "\n");
        arr.printDeque();

        arr.addLast("h");
        System.out.print("nF = " + arr.nextFirst + ", nL = " + arr.nextLast + ", size = " + arr.size + "\n");
        arr.printDeque();

        arr.addFirst("i");
        System.out.print("nF = " + arr.nextFirst + ", nL = " + arr.nextLast + ", size = " + arr.size + "\n");
        arr.printDeque();

        arr.removeLast();
        System.out.print("nF = " + arr.nextFirst + ", nL = " + arr.nextLast + ", size = " + arr.size + "\n");
        arr.printDeque();

        arr.removeLast();
        System.out.print("nF = " + arr.nextFirst + ", nL = " + arr.nextLast + ", size = " + arr.size + "\n");
        arr.printDeque();

        arr.removeFirst();
        System.out.print("nF = " + arr.nextFirst + ", nL = " + arr.nextLast + ", size = " + arr.size + "\n");
        arr.printDeque();

        arr.removeFirst();
        System.out.print("nF = " + arr.nextFirst + ", nL = " + arr.nextLast + ", size = " + arr.size + "\n");
        arr.printDeque();

        arr.removeLast();
        System.out.print("nF = " + arr.nextFirst + ", nL = " + arr.nextLast + ", size = " + arr.size + "\n");
        arr.printDeque();

        arr.removeFirst();
        System.out.print("nF = " + arr.nextFirst + ", nL = " + arr.nextLast + ", size = " + arr.size + "\n");
        arr.printDeque();

        arr.removeFirst();
        System.out.print("nF = " + arr.nextFirst + ", nL = " + arr.nextLast + ", size = " + arr.size + "\n");
        arr.printDeque();

        arr.removeLast();
        System.out.print("nF = " + arr.nextFirst + ", nL = " + arr.nextLast + ", size = " + arr.size + "\n");
        arr.printDeque();

        arr.removeFirst();
        System.out.print("nF = " + arr.nextFirst + ", nL = " + arr.nextLast + ", size = " + arr.size + "\n");
        arr.printDeque();

        System.out.print(arr.isEmpty());

        arr.printDeque();

    }


}
