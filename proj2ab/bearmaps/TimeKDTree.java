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
    private static KDTree buildKDTree(int N, Random rnd) {
        List<Point> points = getRandomPoints(N, rnd);
        KDTree tree = new KDTree(points);
        return tree;
    }

    /** Builds a NaivePointSet of size N with random coordinates. */
    private static NaivePointSet buildNaivePointSet(int N, Random rnd) {
        List<Point> points = getRandomPoints(N, rnd);
        NaivePointSet set = new NaivePointSet(points);
        return set;
    }

    public static void timeKDTreeConstruction() {
        ArrayList<Integer> Ns = new ArrayList<>();
        ArrayList<Double> times = new ArrayList<>();
        ArrayList<Integer> opCounts = new ArrayList<>();

        long seed = 956;
        Random rnd = new Random(seed);

        for (int N = 31250; N <= 2_000_000; N *= 2) {
            Stopwatch sw = new Stopwatch();
            buildKDTree(N, rnd);
            double timeInSeconds = sw.elapsedTime();

            Ns.add(N);
            times.add(timeInSeconds);
            opCounts.add(N);
        }

        printTimingTable(Ns, times, opCounts);
    }

    public static void timeNaiveNearest() {
        ArrayList<Integer> Ns = new ArrayList<>();
        ArrayList<Double> times = new ArrayList<>();
        ArrayList<Integer> opCounts = new ArrayList<>();

        long seed = 956;
        Random rnd = new Random(seed);

        int queries = 1_000_000;

        for (int N = 125; N <= 1000; N *= 2) {
            NaivePointSet nps = buildNaivePointSet(N, rnd);

            Stopwatch sw = new Stopwatch();
            for (int j = 0; j < queries; j++) {
                double x = getRandomCoord(rnd);
                double y = getRandomCoord(rnd);
                nps.nearest(x, y);
            }
            double timeInSeconds = sw.elapsedTime();

            Ns.add(N);
            times.add(timeInSeconds);
            opCounts.add(queries);
        }

        printTimingTable(Ns, times, opCounts);
    }

    public static void timeKDTreeNearest() {
        ArrayList<Integer> Ns = new ArrayList<>();
        ArrayList<Double> times = new ArrayList<>();
        ArrayList<Integer> opCounts = new ArrayList<>();

        long seed = 956;
        Random rnd = new Random(seed);

        int queries = 1_000_000;

        for (int N = 31250; N <= 1_000_000; N *= 2) {
            KDTree tree = buildKDTree(N, rnd);

            Stopwatch sw = new Stopwatch();
            for (int j = 0; j < queries; j++) {
                double x = getRandomCoord(rnd);
                double y = getRandomCoord(rnd);
                tree.nearest(x, y);
            }
            double timeInSeconds = sw.elapsedTime();

            Ns.add(N);
            times.add(timeInSeconds);
            opCounts.add(queries);
        }

        printTimingTable(Ns, times, opCounts);
    }

    public static void main(String[] args) {
        System.out.println("Timing table for Kd-Tree Construction");
        timeKDTreeConstruction();
        System.out.println();

        System.out.println("Timing table for Naive Nearest");
        timeNaiveNearest();
        System.out.println();

        System.out.println("Timing table for Kd-Tree Nearest");
        timeKDTreeNearest();
        System.out.println();
    }
}
