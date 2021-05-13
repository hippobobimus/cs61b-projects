package byow.Core.tests;

import byow.Core.*;
import static byow.Core.Constants.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestGrid {
    @Test
    public void testDims() {
        Grid g = new Grid(10, 20);

        assertEquals(10, g.getWidth());
        assertEquals(20, g.getHeight());
    }

    @Test
    public void testContents() {
        Grid g = new Grid(10, 20);

        Point p = g.get(2, 3);
        Point q = new Point(2, 3);

        assertTrue(g.contains(p));
        assertFalse(g.contains(q));
    }

    @Test
    public void testBoundary() {
        Grid g = new Grid(10, 20);

        Point p0 = g.get(0, 3);
        Point p1 = g.get(2, 19);
        Point p2 = g.get(9, 3);
        Point p3 = g.get(2, 0);

        assertTrue(g.isAtGridBoundary(p0));
        assertTrue(g.isAtGridBoundary(p1));
        assertTrue(g.isAtGridBoundary(p2));
        assertTrue(g.isAtGridBoundary(p3));

        Point p4 = g.get(2, 3);

        assertFalse(g.isAtGridBoundary(p4));
    }
}
