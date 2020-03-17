public class LinkedListDeque<T> {
    private class Node {
        public T data;
        public Node prev;
        public Node next;

        public Node(T d, Node p, Node n) {
            data = d;
            prev = p;
            next = n;
        }
    }

    private Node sentinel;  // first = sentinel.next; last = sentinel.prev;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node( null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public void addFirst(T item) {
        Node temp = new Node(item, sentinel, sentinel.next);
        sentinel.next = temp;
        temp.next.prev = temp;
        size += 1;
    }

    public void addLast(T item) {
        Node temp = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.next = temp;
        sentinel.prev = temp;
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node iter = sentinel.next;
        while (iter.data != null) {
            System.out.print(iter.data + " ");
            iter = iter.next;
        }
        System.out.println();
    }

    public T removeFirst() {
        if (sentinel.next == null) return null;

        Node removed = sentinel.next;
        sentinel.next = removed.next;
        removed.next.prev = removed.prev;
        size -= 1;

        return removed.data;
    }

    public T removeLast() {
        if (sentinel.prev == null) return null;

        Node removed = sentinel.prev;
        sentinel.prev = removed.prev;
        removed.prev.next = removed.next;
        size -= 1;

        return removed.data;
    }

    public T get(int index) {
        if (index >= size) return null;

        Node iter = sentinel;
        while (index > 0) {
            iter = iter.next;
            index -= 1;
        }
        return iter.data;
    }

    private T getRecursiveHelper(Node currentNode, int index) {
        if (index == 0) return currentNode.data;
        else            return getRecursiveHelper(currentNode.next, index-1);
    }

    public T getRecursive(int index) {
        if (index >= size) return null;

        return getRecursiveHelper(sentinel.next, index);
    }
}
