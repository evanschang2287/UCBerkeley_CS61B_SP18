import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class BinaryTrie implements Serializable {
    private Node root;

    private static class Node implements Serializable, Comparable<Node> {
        private final char ch;
        private final int freq;
        private final Node left, right;

        public Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        private boolean isLeaf() {
            return left == null && right == null;
        }

        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }

    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> me : frequencyTable.entrySet()) {
            char c = me.getKey();
            int f = me.getValue();
            pq.add(new Node(c, f, null, null));
        }

        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.add(parent);
        }
        root = pq.poll();
    }

    public Match longestPrefixMatch(BitSequence querySequence) {
        Node temp = root;
        for (int i = 0; i < querySequence.length(); i++) {
            if (temp.isLeaf()) {
                return new Match(querySequence.firstNBits(i), temp.ch);
            } else {
                if (querySequence.bitAt(i) == 0) {
                    temp = temp.left;
                } else {
                    temp = temp.right;
                }
            }
        }
        return new Match(querySequence, temp.ch);
    }

    private void lutHelper(Node n, Map<Character, BitSequence> m, String s) {
        if (n.isLeaf()) {
            m.put(n.ch, new BitSequence(s));
            return;
        }

        lutHelper(n.left, m, s + '0');
        lutHelper(n.right, m, s + '1');
    }

    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> lut = new HashMap<>();
        lutHelper(root, lut, "");

        return lut;
    }

}
