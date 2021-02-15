package bearmaps;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Random;

public class KDTreeTest {

    @Test
    public void testConstructor() {
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(2, 3));
        points.add(new Point(4, 2));
        points.add(new Point(4, 5));
        points.add(new Point(3, 3));
        points.add(new Point(1, 5));
        points.add(new Point(4, 4));

        KDTree tree = new KDTree(points);

        System.out.println("Printing KDTree...");
        System.out.println(tree);
        assertEquals(
                "{[1.0, 5.0], [2.0, 3.0], [4.0, 2.0], [3.0, 3.0], [4.0, 5.0], "
                + "[4.0, 4.0], }", tree.toString());
    }

    @Test
    public void testNearestLectureExample() {
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(2, 3));
        points.add(new Point(4, 2));
        points.add(new Point(4, 5));
        points.add(new Point(3, 3));
        points.add(new Point(1, 5));
        points.add(new Point(4, 4));

        KDTree tree = new KDTree(points);
        NaivePointSet nn = new NaivePointSet(points);

        Point target = new Point(0, 7);
        double expected = Point.distance(target,
                nn.nearest(target.getX(), target.getY()));
        double actual = Point.distance(target,
                tree.nearest(target.getX(), target.getY()));

        assertEquals(expected, actual, 1E-8);
    }

    private double getRandomCoord(Random rnd) {
        double min = -1000.0;
        double max = 1000.0;
        return rnd.nextDouble() * (max - min) + min;
    }

    @Test
    public void testNearest() {
        long seed = 956;
        Random rnd = new Random(seed);
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < 1E6; i++) {
            double x = getRandomCoord(rnd);
            double y = getRandomCoord(rnd);
            points.add(new Point(x, y));
        }
        KDTree tree = new KDTree(points);
        NaivePointSet nn = new NaivePointSet(points);

        //System.out.println(tree);

        for(int i = 0; i < 1E4; i++) {
            Point target = new Point(getRandomCoord(rnd), getRandomCoord(rnd));
            double expected = Point.distance(target,
                    nn.nearest(target.getX(), target.getY()));
            double actual = Point.distance(target,
                    tree.nearest(target.getX(), target.getY()));
            assertEquals(expected, actual, 1E-8);
        }
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", KDTreeTest.class);
    }
}
