public class Palindrome {
    /** Recursive helper method for public isPalindrome method. */
    private boolean isPalindrome(Deque<Character> d) {
        if (d.size() == 0 || d.size() == 1) {
            return true;
        } else if (!(d.removeFirst() == d.removeLast())) {
            return false;
        }
        return isPalindrome(d);
    }

    /** Returns true if the given word is a palindrome, otherwise returns
      * false. */
    public boolean isPalindrome(String word) {
        if (word == null) {
            return false;
        }
        return isPalindrome(wordToDeque(word));
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

