import java.util.LinkedList;
import java.util.List;

public class HuffmanDecoder {
    public static void main(String[] args) {
        ObjectReader or = new ObjectReader(args[0]);

        Object firstObj = or.readObject();
        Object secondObj = or.readObject();
        Object thirdObj = or.readObject();

        BinaryTrie bt;
        int numOfSymbols;
        BitSequence bs;

        if (thirdObj == null) {
            bt = (BinaryTrie) firstObj;
            bs = (BitSequence) secondObj;
            numOfSymbols = bs.length();
        } else {
            bt = (BinaryTrie) firstObj;
            numOfSymbols = (int) secondObj;
            bs = (BitSequence) thirdObj;
        }

        char[] charArray = new char[numOfSymbols];
        for (int i = 0; i < charArray.length; i++) {
            Match m = bt.longestPrefixMatch(bs);
            charArray[i] = m.getSymbol();
            bs = bs.allButFirstNBits(m.getSequence().length());
        }
        FileUtils.writeCharArray(args[1], charArray);
    }
}
