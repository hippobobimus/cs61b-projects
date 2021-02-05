public class OffByN implements CharacterComparator {
    private int N;

    /** Constructor. */
    public OffByN(int n) {
        N = n;
    }

    /** Returns true if characters are off by N from each other. */
    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == N;
    }
}
