public class Palindrome {
    /** Recursive helper method for public isPalindrome method. */
    private boolean isPalindrome(Deque<Character> d, CharacterComparator cc) {
        if (d.size() == 0 || d.size() == 1) {
            return true;
        } else if (!(cc.equalChars(d.removeFirst(), d.removeLast()))) {
            return false;
        }
        return isPalindrome(d, cc);
    }

    /** Returns true if the given word is a palindrome, otherwise returns
      * false. */
    public boolean isPalindrome(String word) {
        OffByN eq = new OffByN(0);
        if (word == null) {
            return false;
        }
        return isPalindrome(wordToDeque(word), eq);
    }

    /** Returns true if the word is a palindrome according to the provided
      * character comparison test. */
    public boolean isPalindrome(String word, CharacterComparator cc) {
        if (word == null) {
            return false;
        } else if (cc == null) {
            return isPalindrome(word);
        }
        return isPalindrome(wordToDeque(word), cc);
    }

    /** Given a String, returns a Deque where the characters appear in the 
      * same order as the String. */
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> d = new LinkedListDeque<>();

        for (int i = 0; i < word.length(); i++) {
            d.addLast(word.charAt(i));
        }
        return d;
    }
}
