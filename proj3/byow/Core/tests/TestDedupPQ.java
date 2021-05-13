package byow.Core.tests;

import byow.Core.*;

import java.util.Comparator;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestDedupPQ {
    @Test
    public void testConstructor() {
        Comparator<Point> cmp = (a, b) -> b.getPriority() - a.getPriority();
        DedupPQ<Point> ddpq = new DedupPQ<>(cmp);
    }

    @Test
    public void testAdd() {
        Comparator<Point> cmp = (a, b) -> b.getPriority() - a.getPriority();
        DedupPQ<Point> ddpq = new DedupPQ<>(cmp);

        Point p = new Point(2, 3);

        boolean result0 = ddpq.add(p);
        assertTrue(result0);
        boolean result1 = ddpq.add(p);
        assertFalse(result1);
    }

    @Test
    public void testOffer() {
        Comparator<Point> cmp = (a, b) -> b.getPriority() - a.getPriority();
        DedupPQ<Point> ddpq = new DedupPQ<>(cmp);

        Point p = new Point(2, 3);

        boolean result0 = ddpq.offer(p);
        assertTrue(result0);
        boolean result1 = ddpq.offer(p);
        assertFalse(result1);
    }

    @Test
    public void testPoll() {
        Comparator<Point> cmp = (a, b) -> b.getPriority() - a.getPriority();
        DedupPQ<Point> ddpq = new DedupPQ<>(cmp);

        Point p0 = new Point(2, 3);
        Point p1 = new Point(1, 9);

        p0.setPriority(0);
        p1.setPriority(1);

        ddpq.add(p0);
        ddpq.add(p1);

        assertEquals(p1, ddpq.poll());
    }

    @Test
    public void testRemove() {
        Comparator<Point> cmp = (a, b) -> b.getPriority() - a.getPriority();
        DedupPQ<Point> ddpq = new DedupPQ<>(cmp);

        Point p0 = new Point(2, 3);
        Point p1 = new Point(1, 9);

        p0.setPriority(0);
        p1.setPriority(1);

        ddpq.add(p0);
        ddpq.add(p1);

        assertEquals(p1, ddpq.remove());
        assertEquals(p0, ddpq.remove());
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", TestDedupPQ.class);
    }
}
