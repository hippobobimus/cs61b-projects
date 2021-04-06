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

        ddpq.add(p);
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", TestDedupPQ.class);
    }
}
