package byow.Core.tests;

import byow.Core.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestPointGraph {
    @Test
    public void testAddContains() {
        PointGraph pg = new PointGraph();

        Point p1 = new Point(3, 4);
        Point p2 = new Point(7, 2);
        Point p3 = new Point(1, 80);

        assertFalse(pg.contains(p1));
        assertFalse(pg.contains(p2));
        assertFalse(pg.contains(p3));

        pg.add(p1);
        assertTrue(pg.contains(p1));

        pg.add(p2);
        assertTrue(pg.contains(p2));

        pg.add(p3);
        assertTrue(pg.contains(p3));
    }

    @Test
    public void testAddEdge() {
        PointGraph pg = new PointGraph();

        Point p1 = new Point(1, 1);
        Point p2 = new Point(0, 2);
        Point p3 = new Point(1, 0);

        pg.add(p1);
        pg.add(p2);
        pg.add(p3);

        pg.addEdge(p1, p3);

        assertEquals(p3, pg.getNeighbour(p1, Direction.DOWN));
        assertEquals(p1, pg.getNeighbour(p3, Direction.UP));

        pg.addEdge(p2, p1);

        assertEquals(p1, pg.getNeighbour(p2, Direction.DOWN_RIGHT));
        assertEquals(p2, pg.getNeighbour(p1, Direction.UP_LEFT));
    }

    @Test
    public void testRemove() {
        PointGraph pg = new PointGraph();

        Point p1 = new Point(3, 4);
        Point p2 = new Point(7, 2);
        Point p3 = new Point(1, 80);

        pg.add(p1);
        pg.add(p2);
        pg.add(p3);

        pg.remove(p2);

        assertTrue(pg.contains(p1));
        assertFalse(pg.contains(p2));
        assertTrue(pg.contains(p3));
    }

    @Test
    public void testSize() {
        PointGraph pg = new PointGraph();

        Point[] points = new Point[10];

        for (int i = 0; i < 10; i++) {
            assertEquals(i, pg.size());

            Point p = new Point(i, 2 * i);

            pg.add(p);
            points[i] = p;
        }

        for (int i = 0; i < 10; i++) {
            assertEquals(10 - i, pg.size());
            pg.remove(points[i]);
        }

        assertEquals(0, pg.size());
    }

    @Test
    public void testGetNeighbour() {
        // TODO
    }

    @Test
    public void testListNeighbours() {
        // TODO
    }

    @Test
    public void testLeaf() {
        PointGraph pg = new PointGraph();

        Point p1 = new Point(1, 1);
        Point p2 = new Point(0, 2);
        Point p3 = new Point(1, 0);

        pg.add(p1);
        pg.add(p2);
        pg.add(p3);

        pg.addEdge(p1, p2);
        pg.addEdge(p1, p3);

        assertFalse(pg.isLeaf(p1));
        assertTrue(pg.isLeaf(p2));
        assertTrue(pg.isLeaf(p3));

        pg.remove(p2);

        assertTrue(pg.isLeaf(p1));
        assertTrue(pg.isLeaf(p3));
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", TestPointGraph.class);
    }
}
