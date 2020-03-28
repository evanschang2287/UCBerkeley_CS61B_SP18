package lab9;

import com.sun.source.tree.Tree;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with a null key.");
        }
        if (p == null) {
            return null;
        }
        int cmp = key.compareTo(p.key);
        if (cmp < 0) {
            return getHelper(key, p.left);
        } else if (cmp > 0) {
            return getHelper(key, p.right);
        } else {
            return p.value;
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with a null key.");
        }
        if (p == null) {
            size += 1;
            return new Node(key, value);
        }

        int cmp = key.compareTo(p.key);
        if (cmp < 0) {
            p.left = putHelper(key, value, p.left);
        } else if (cmp > 0) {
            p.right = putHelper(key, value, p.right);
        } else {
            p.value = value;
        }

        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Store the keys in InOrder(L-R-V) pattern. */
    private void InOrderAddKey(Node p, TreeSet<K> contained) {
        if (p != null) {
            InOrderAddKey(p.left, contained);
            contained.add(p.key);
            InOrderAddKey(p.right, contained);
        }
    }

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> contained = new TreeSet<>();
        InOrderAddKey(root, (TreeSet<K>) contained);

        return contained;
    }

    public void showKey() {
        Set<K> contained = keySet();
        Iterator<K> keyIter = contained.iterator();
        while(keyIter.hasNext()) {
            System.out.print(keyIter.next() + " ");
        }
        System.out.println();
    }

    private Node predecessor(Node p) {
        Node temp = p.left;
        while (temp.right != null) {
            temp = temp.right;
        }
        return temp;
    }

    private Node delete(Node T, K dKey) {
        if (T == null) {
            return T;
        }

        int cmp = dKey.compareTo(T.key);
        if (cmp < 0) {
            T.left = delete(T.left, dKey);
        } else if (cmp > 0) {
            T.right = delete(T.right, dKey);
        } else { /* T.key == dKey */
            if (T.left == null || T.right == null) { /* One or no child */
                if (T.left == null) {
                    T = T.right;
                } else {
                    T = T.left;
                }
            } else {
                Node predecessor = predecessor(T);
                T.left = delete(T.left, predecessor.key);
                T.key = predecessor.key;
                T.value = predecessor.value;
            }
        }

        return T;
    }

    private V removeHelper(K key, V value) {
        V removedV = get(key);
        if (removedV == null) {
            return null;
        }
        if (value != null && !removedV.equals(value)) {
            return null;
        }
        root = delete(root, key);

        return removedV;
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        if (key == null) {
            return null;
        }
        size -= 1;

        return removeHelper(key, null);
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        if (key == null || value == null) {
            return null;
        }
        size -= 1;

        return removeHelper(key, value);
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    public static void main(String[] args) {
        BSTMap<String, Integer> bstmap = new BSTMap<>();
        bstmap.put("hello", 5);
        bstmap.put("cat", 10);
        bstmap.put("fish",22);
        bstmap.put("zebra", 90);
        bstmap.put("zebra", 200);
        Integer get1 = bstmap.get("zebra");
        Integer get2 = bstmap.get("dog");
        bstmap.showKey();

        bstmap.remove("zebra");
        bstmap.showKey();
    }
}
