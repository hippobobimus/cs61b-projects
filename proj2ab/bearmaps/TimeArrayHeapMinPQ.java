package bearmaps;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Class that collects timing information about AList construction.
 */
public class TimeArrayHeapMinPQ {
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

    /** Builds a ArrayHeapMinPQ of size N with random priorities. */
    private static ArrayHeapMinPQ<String> buildArrayHeapMinPQ(int N, Random rnd) {
        ArrayHeapMinPQ<String> pq = new ArrayHeapMinPQ<>();
        Iterator<Integer> rndInts = rnd.ints(0, 100_000_000).distinct().iterator();
        for (int i = 0; i < N; i++) {
            pq.add("hi" + i, rndInts.next());
        }
        return pq;
    }

    /** Builds a NaiveMinPQ of size N with random priorities. */
    private static NaiveMinPQ<String> buildNaiveMinPQ(int N, Random rnd) {
        NaiveMinPQ<String> pq = new NaiveMinPQ<>();
        Iterator<Integer> rndInts = rnd.ints(N, 0, 1_000_000).distinct().iterator();
        for (int i = 0; i < N; i++) {
            pq.add("hi" + i, rndInts.next());
        }
        return pq;
    }

    public static void timeArrayHeapMinPQConstruction() {
        ArrayList<Integer> Ns = new ArrayList<>();
        ArrayList<Double> times = new ArrayList<>();
        ArrayList<Integer> opCounts = new ArrayList<>();

        long seed = 956;
        Random rnd = new Random(seed);

        for (int N = 31250; N <= 2_000_000; N *= 2) {
            Stopwatch sw = new Stopwatch();
            buildArrayHeapMinPQ(N, rnd);
            double timeInSeconds = sw.elapsedTime();

            Ns.add(N);
            times.add(timeInSeconds);
            opCounts.add(N);
        }

        printTimingTable(Ns, times, opCounts);
    }

    public static void timeArrayHeapMinPQRemoveSmallest() {
        ArrayList<Integer> Ns = new ArrayList<>();
        ArrayList<Double> times = new ArrayList<>();
        ArrayList<Integer> opCounts = new ArrayList<>();

        long seed = 956;
        Random rnd = new Random(seed);

        for (int N = 31250; N <= 2_000_000; N *= 2) {
            ArrayHeapMinPQ<String> pq = buildArrayHeapMinPQ(N, rnd);

            Stopwatch sw = new Stopwatch();
            for (int i = 0; i < N; i++) {
                pq.removeSmallest();
            }
            double timeInSeconds = sw.elapsedTime();

            Ns.add(N);
            times.add(timeInSeconds);
            opCounts.add(N);
        }

        printTimingTable(Ns, times, opCounts);
    }

    public static void timeArrayHeapMinPQChangePriority() {
        ArrayList<Integer> Ns = new ArrayList<>();
        ArrayList<Double> times = new ArrayList<>();
        ArrayList<Integer> opCounts = new ArrayList<>();

        long seed = 956;
        Random rnd = new Random(seed);

        for (int N = 31250; N <= 2_000_000; N *= 2) {
            ArrayHeapMinPQ<String> pq = buildArrayHeapMinPQ(N, rnd);
            Iterator<Integer> rndInts = rnd.ints(0, 100_000_000)
                .distinct().iterator();

            Stopwatch sw = new Stopwatch();
            for (int i = 0; i < N; i++) {
                pq.changePriority("hi" + i, rndInts.next());
            }
            double timeInSeconds = sw.elapsedTime();

            Ns.add(N);
            times.add(timeInSeconds);
            opCounts.add(N);
        }

        printTimingTable(Ns, times, opCounts);
    }
    public static void main(String[] args) {
        System.out.println("Timing table for ArrayHeapMinPQ Construction");
        timeArrayHeapMinPQConstruction();
        System.out.println();

        System.out.println("Timing table for ArrayHeapMinPQ removeSmallest method");
        timeArrayHeapMinPQRemoveSmallest();
        System.out.println();

        System.out.println("Timing table for ArrayHeapMinPQ changePriority method");
        timeArrayHeapMinPQChangePriority();
        System.out.println();
    }
}

