package bearmaps;

import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

public class NaivePointSet implements PointSet {
    private List<Point> points;

    public NaivePointSet(List<Point> points) {
        this.points = points;
    }

    public Point nearest(double x, double y) {
        Point target = new Point(x, y);
        Point result = points.get(0);
        for (Point pt : points) {
            if (Point.distance(pt, target) < Point.distance(result, target)) {
                result = pt;
            }
        }
        return result;
    }

    public static class TestNaivePointSet {
        @Test
        public void test() {
            Point p1 = new Point(1.1, 2.2);
            Point p2 = new Point(3.3, 4.4);
            Point p3 = new Point(-2.9, 4.2);
            
            NaivePointSet nn = new NaivePointSet(List.of(p1, p2, p3));
            Point ret = nn.nearest(3.0, 4.0); // returns p2
            assertEquals(3.3, ret.getX(), 0.0);
            assertEquals(4.4, ret.getY(), 0.0);
        }
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", TestNaivePointSet.class);
    }
}
