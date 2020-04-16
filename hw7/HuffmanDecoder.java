import java.util.LinkedList;
import java.util.List;

public class HuffmanDecoder {
    public static void main(String[] args) {
        ObjectReader or = new ObjectReader(args[0]);

        /* First object in .huf file is a Huffman Coding Trie. */
        BinaryTrie bt = (BinaryTrie) or.readObject();
        /* Second object in .huf file is the number of symbols. */
        Integer numOfSymbols = (Integer) or.readObject();
        /* Third object in .huf file is a long bit sequence. */
        BitSequence bs = (BitSequence) or.readObject();

        List<BitSequence> allUnmatched = new LinkedList<>();
        char[] charArray = new char[numOfSymbols];
        for (int i = 0; i < charArray.length; i++) {
            Match m = bt.longestPrefixMatch(bs);
            int firstN = m.getSequence().length();
            int lastN = bs.length() - firstN;
            if (m.getSymbol() != '\0') {
                charArray[i] = m.getSymbol();
            } else {
                BitSequence unmatched = bs.allButFirstNBits(firstN);
                allUnmatched.add(unmatched);
            }
            bs = bs.lastNBits(lastN);
        }
        FileUtils.writeCharArray(args[1], charArray);
    }
}
