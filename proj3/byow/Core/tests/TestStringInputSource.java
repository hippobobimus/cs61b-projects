package byow.Core.tests;

import byow.Core.*;
import static byow.Core.Constants.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestStringInputSource {
    @Test
    public void testHasKeyGetKey() {
        Game g = new Game();
        InputSource is = new StringInputSource("N123S");

        assertTrue(is.hasNextKey());
        assertEquals('N', is.getNextKey());

        assertTrue(is.hasNextKey());
        assertEquals('1', is.getNextKey());

        assertTrue(is.hasNextKey());
        assertEquals('2', is.getNextKey());

        assertTrue(is.hasNextKey());
        assertEquals('3', is.getNextKey());

        assertTrue(is.hasNextKey());
        assertEquals('S', is.getNextKey());

        assertFalse(is.hasNextKey());
    }
}
