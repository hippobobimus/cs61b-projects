import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByN(1);
    static CharacterComparator offByTwo = new OffByN(2);
    static CharacterComparator offByThree = new OffByN(3);

    @Test
    public void testEqualChars() {
        assertTrue(offByOne.equalChars('a', 'b'));
        assertTrue(offByOne.equalChars('r', 'q'));
        assertFalse(offByOne.equalChars('a', 'e'));
        assertFalse(offByOne.equalChars('z', 'a'));
        assertFalse(offByOne.equalChars('a', 'a'));
        assertTrue(offByOne.equalChars('&', '%'));

        assertTrue(offByTwo.equalChars('a', 'c'));
        assertTrue(offByTwo.equalChars('s', 'q'));
        assertFalse(offByTwo.equalChars('a', 'e'));
        assertFalse(offByTwo.equalChars('z', 'a'));
        assertFalse(offByTwo.equalChars('a', 'a'));
        assertTrue(offByTwo.equalChars('&', '$'));

        assertTrue(offByThree.equalChars('a', 'd'));
        assertTrue(offByThree.equalChars('s', 'p'));
        assertFalse(offByThree.equalChars('a', 'e'));
        assertFalse(offByThree.equalChars('z', 'a'));
        assertFalse(offByThree.equalChars('a', 'a'));
        assertTrue(offByThree.equalChars('&', '#'));
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("failed", TestOffByN.class);
    }
}
