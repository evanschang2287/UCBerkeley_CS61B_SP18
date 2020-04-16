import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> freqTable = new HashMap<>();
        for (char c : inputSymbols) {
            if (freqTable.containsKey(c)) {
                int freq =  freqTable.get(c);
                freqTable.put(c, freq + 1);
            } else {
                freqTable.put(c, 1);
            }
        }
        return freqTable;
    }

    public static void main(String[] args) {
        char[] inputSymbols = FileUtils.readFile(args[0]);
        Map<Character, Integer> freqTable = buildFrequencyTable(inputSymbols);
        BinaryTrie bt = new BinaryTrie(freqTable);

        ObjectWriter ow = new ObjectWriter(args[0] + ".huf");
        ow.writeObject(bt);

        Integer numOfSymbols = 0;
        for (Map.Entry<Character, Integer> m : freqTable.entrySet()) {
            Integer freq = m.getValue();
            numOfSymbols += freq;
        }
        if (numOfSymbols != 0) {
            ow.writeObject(numOfSymbols);
        }

        Map<Character, BitSequence> lookupTable = bt.buildLookupTable();
        List<BitSequence> bs = new LinkedList<>();

        for (char c : inputSymbols) {
            BitSequence bits = lookupTable.get(c);
            bs.add(bits);
        }

        BitSequence allBS = BitSequence.assemble(bs);
        ow.writeObject(allBS);
    }
}
