package bearmaps;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Class that collects timing information about AList construction.
 */
public class TimeKDTree {
    private static void printTimingTable(List<Integer> Ns, List<Double> times, List<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        System.out.println("Timing table for Kd-Tree Construction");
        timeKDTreeConstruction();
        System.out.println();
    }

    /** Generates a random double coord value between -1000.0 and 1000.0. */
    private static double getRandomCoord(Random rnd) {
        double min = -1000.0;
        double max = 1000.0;
        return rnd.nextDouble() * (max - min) + min;
    }

    /** Generates a list of N random points. */
    private static List<Point> getRandomPoints(int N, Random rnd) {
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            double x = getRandomCoord(rnd);
            double y = getRandomCoord(rnd);
            points.add(new Point(x, y));
        }
        return points;
    }

    /** Builds a KDTree of size N with random coordinates. */
    private static void buildKDTree(int N, Random rnd) {
        List<Point> points = getRandomPoints(N, rnd);
        KDTree tree = new KDTree(points);
    }

    /** Builds a NaivePointSet of size N with random coordinates. */
    private static void buildNaivePointSet(int N, Random rnd) {
        List<Point> points = getRandomPoints(N, rnd);
        NaivePointSet tree = new NaivePointSet(points);
    }

    public static void timeKDTreeConstruction() {
        ArrayList<Integer> Ns = new ArrayList<>();
        ArrayList<Double> times = new ArrayList<>();
        ArrayList<Integer> opCounts = new ArrayList<>();

        long seed = 956;
        Random rnd = new Random(seed);

        int N = (int) 1E6;
        Stopwatch sw = new Stopwatch();
        buildKDTree(N, rnd);
        double timeInSeconds = sw.elapsedTime();
        Ns.add(N);
        times.add(timeInSeconds);
        opCounts.add(N);




        //KDTree tree = new KDTree(points);
        //NaivePointSet nn = new NaivePointSet(points);

        ////System.out.println(tree);

        //for(int i = 0; i < 1E4; i++) {
        //    Point target = new Point(getRandomCoord(rnd), getRandomCoord(rnd));
        //    double expected = Point.distance(target,
        //            nn.nearest(target.getX(), target.getY()));
        //    double actual = Point.distance(target,
        //            tree.nearest(target.getX(), target.getY()));
        //}

        printTimingTable(Ns, times, opCounts);
    }


}
