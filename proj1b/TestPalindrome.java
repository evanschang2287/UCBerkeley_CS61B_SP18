import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        boolean result1 = palindrome.isPalindrome("racecar");
        boolean result2 = palindrome.isPalindrome("noon");
        boolean result3 = palindrome.isPalindrome("horse");
        boolean result4 = palindrome.isPalindrome("rancor");
        boolean result5 = palindrome.isPalindrome("aaaaab");
        boolean result6 = palindrome.isPalindrome("");
        boolean result7 = palindrome.isPalindrome("a");

        assertTrue(result1);
        assertTrue(result2);
        assertFalse(result3);
        assertFalse(result4);
        assertFalse(result5);
        assertTrue(result6);
        assertTrue(result7);
    }
}
