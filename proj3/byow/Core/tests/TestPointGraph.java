package byow.Core.tests;

import byow.Core.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the PointGraph data structure.
 * @author Rob Masters
 */
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
    public void testAddRemoveEdgeAndNeighbours() {
        PointGraph pg = new PointGraph();

        Point p1 = new Point(1, 1);
        Point p2 = new Point(0, 1);
        Point p3 = new Point(1, 0);

        pg.add(p1);
        pg.add(p2);
        pg.add(p3);

        pg.addEdge(p1, p3);

        assertEquals(p3, pg.getNeighbour(p1, Direction.DOWN));
        assertEquals(p1, pg.getNeighbour(p3, Direction.UP));

        pg.removeEdge(p1, p3);

        assertEquals(null, pg.getNeighbour(p1, Direction.DOWN));
        assertEquals(null, pg.getNeighbour(p3, Direction.UP));

        pg.addEdge(p2, p1);

        assertEquals(p1, pg.getNeighbour(p2, Direction.RIGHT));
        assertEquals(p2, pg.getNeighbour(p1, Direction.LEFT));

        pg.removeEdge(p2, p1);

        assertEquals(null, pg.getNeighbour(p2, Direction.RIGHT));
        assertEquals(null, pg.getNeighbour(p1, Direction.LEFT));
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
    public void testListNeighbours() {
        PointGraph pg = new PointGraph();

        Point p1 = new Point(1, 1);
        Point p2 = new Point(1, 2);
        Point p3 = new Point(1, 0);

        pg.add(p1);
        pg.add(p2);
        pg.add(p3);

        pg.addEdge(p1, p3);
        pg.addEdge(p2, p1);

        Set<Point> expected = new HashSet<>();
        expected.add(p2);
        expected.add(p3);

        Set<Point> actual = new HashSet<>(pg.listNeighbours(p1));

        assertEquals(expected, actual);
    }

    @Test
    public void testListAllPoints() {
        PointGraph pg = new PointGraph();

        Point p1 = new Point(1, 1);
        Point p2 = new Point(0, 2);
        Point p3 = new Point(1, 0);

        pg.add(p1);
        pg.add(p2);
        pg.add(p3);

        Set<Point> expected = new HashSet<>();
        expected.add(p1);
        expected.add(p2);
        expected.add(p3);

        Set<Point> actual = new HashSet<>(pg.listAllPoints());

        assertEquals(expected, actual);
    }

    @Test
    public void testListLeafPoints() {
        PointGraph pg = new PointGraph();

        // center point.
        Point p = new Point(1, 1);
        // cardinal neighbours.
        Point n1 = new Point(1, 0);
        Point n2 = new Point(1, 2);
        Point n3 = new Point(0, 1);
        Point n4 = new Point(2, 1);

        pg.add(p);
        pg.add(n1);
        pg.add(n2);
        pg.add(n3);
        pg.add(n4);

        pg.addEdge(p, n1);
        pg.addEdge(p, n2);
        pg.addEdge(p, n3);
        pg.addEdge(p, n4);

        Set<Point> expected = new HashSet<>();
        expected.add(n1);
        expected.add(n2);
        expected.add(n3);
        expected.add(n4);

        Set<Point> actual = new HashSet<>(pg.listLeafPoints());

        assertEquals(expected, actual);
    }

    @Test
    public void testDegree() {
        PointGraph pg = new PointGraph();

        Point p1 = new Point(1, 1);
        Point p2 = new Point(1, 2);
        Point p3 = new Point(1, 0);

        pg.add(p1);
        pg.add(p2);
        pg.add(p3);

        assertEquals(0, pg.degree(p1));

        pg.addEdge(p1, p2);

        assertEquals(1, pg.degree(p1));

        pg.addEdge(p1, p3);

        assertEquals(2, pg.degree(p1));

        pg.removeEdge(p1, p3);

        assertEquals(1, pg.degree(p1));
    }

    @Test
    public void testLeaf() {
        PointGraph pg = new PointGraph();

        Point p1 = new Point(1, 1);
        Point p2 = new Point(1, 2);
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
