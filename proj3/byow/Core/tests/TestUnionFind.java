package byow.Core.tests;

import byow.Core.*;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the UnionFind class.
 * @author Rob Masters
 */
public class TestUnionFind {
    @Test
    public void testConnections() {
        UnionFind<Point> uf = new UnionFind<>();

        Point p1 = new Point(3, 9);
        Point p2 = new Point(2, 87);
        Point p3 = new Point(98, 37);

        uf.add(p1);
        uf.add(p2);
        uf.add(p3);

        assertFalse(uf.isConnected(p1, p2));
        assertFalse(uf.isConnected(p1, p3));

        uf.connect(p1, p3);

        assertFalse(uf.isConnected(p1, p2));
        assertTrue(uf.isConnected(p1, p3));

        Point p4 = new Point(8, 12);

        uf.add(p4);

        assertFalse(uf.isConnected(p4, p2));

        uf.connect(p3, p4);

        assertTrue(uf.isConnected(p4, p1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnectInvalidItem() {
        UnionFind<Point> uf = new UnionFind<>();

        Point p1 = new Point(3, 9);
        Point p2 = new Point(2, 87);
        Point p3 = new Point(98, 37);

        uf.add(p1);
        uf.add(p2);
        uf.add(p3);

        Point p4 = new Point(8, 12);

        uf.connect(p1, p4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsConnectedInvalidItem() {
        UnionFind<Point> uf = new UnionFind<>();

        Point p1 = new Point(3, 9);
        Point p2 = new Point(2, 87);
        Point p3 = new Point(98, 37);

        uf.add(p1);
        uf.add(p2);
        uf.add(p3);

        Point p4 = new Point(8, 12);

        uf.isConnected(p1, p4);
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", TestUnionFind.class);
    }
}

