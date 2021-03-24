package byow.Core.tests;

import byow.Core.*;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the Edge class.
 * @author Rob Masters
 */
public class TestEdge {
    @Test
    public void testConstructorAndGetters() {
        Point start = new Point(1, 2);
        Point end = new Point(2, 3);
        Edge e = new Edge(start, end);

        assertEquals(start, e.from());
        assertEquals(end, e.to());
        assertEquals(Direction.UP_RIGHT, e.direction());
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", TestEdge.class);
    }
}
