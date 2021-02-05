import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testIsPalindrome() {
        // Default character comparison (equality).
        assertTrue(palindrome.isPalindrome("racecar"));
        assertFalse(palindrome.isPalindrome("rancor"));
        assertFalse(palindrome.isPalindrome("aaaaab"));
        assertTrue(palindrome.isPalindrome("a"));
        assertTrue(palindrome.isPalindrome(""));
        assertFalse(palindrome.isPalindrome(null));

        // OffByOne character comparison.
        OffByOne ob1 = new OffByOne();
        assertTrue(palindrome.isPalindrome("flake", ob1));
        assertFalse(palindrome.isPalindrome("racecar", ob1));
        assertFalse(palindrome.isPalindrome("rancor", ob1));
        assertFalse(palindrome.isPalindrome("aaaaab", ob1));
        assertTrue(palindrome.isPalindrome("a", ob1));
        assertTrue(palindrome.isPalindrome("", ob1));
        assertFalse(palindrome.isPalindrome(null, ob1));

        assertTrue(palindrome.isPalindrome("racecar", null));

        // OffByOne character comparison.
        OffByN ob2 = new OffByN(2);
        assertTrue(palindrome.isPalindrome("clone", ob2));
        assertFalse(palindrome.isPalindrome("racecar", ob2));
        assertFalse(palindrome.isPalindrome("rancor", ob2));
        assertFalse(palindrome.isPalindrome("aaaaab", ob2));
        assertTrue(palindrome.isPalindrome("a", ob2));
        assertTrue(palindrome.isPalindrome("", ob2));
        assertFalse(palindrome.isPalindrome(null, ob2));
    }

    @Test
    public void testWordToDeque() {
        Deque<Character> d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("failed", TestPalindrome.class);
    }
}
