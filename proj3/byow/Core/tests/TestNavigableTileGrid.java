package byow.Core.tests;

import byow.Core.*;
import static byow.Core.Constants.*;

import java.util.Random;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the NavigableTileGrid class.
 * @author Rob Masters
 */
public class TestNavigableTileGrid {
    @Test
    public void testPathOpenClose() {
        NavigableTileGrid ntg = new NavigableTileGrid();

        Point p = ntg.get(2, 3);

        assertFalse(ntg.isPath(p));

        ntg.openPath(p);
        assertTrue(ntg.isPath(p));

        ntg.closePath(p);
        assertFalse(ntg.isPath(p));
    }

    @Test
    public void testConnectedPath() {
        NavigableTileGrid ntg = new NavigableTileGrid();

        Point p0 = ntg.get(2, 3);
        Point p1 = ntg.get(3, 3);

        assertFalse(ntg.isConnected(p0, p1));

        ntg.openPath(p0);
        ntg.openPath(p1);

        assertTrue(ntg.isConnected(p0, p1));
    }
}
