public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> deque = new LinkedListDeque<Character>();

        for (int i = 0; i < word.length(); i++) {
            deque.addLast(word.charAt(i));
        }

        return deque;
    }

    public boolean isPalindrome(String word) {
        if (word.length() == 1 || word.length() == 0) {
            return true;
        }

        Palindrome palindrome = new Palindrome();
        Deque<Character> deque = palindrome.wordToDeque(word);

        for (int i = 0; i < word.length() / 2; i++) {
            if (deque.removeFirst() == deque.removeLast()) {
                continue;
            }
            return false;
        }
        return true;
    }
}
