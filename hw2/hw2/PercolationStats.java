package hw2;

import java.lang.IllegalArgumentException;
import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;
import org.junit.Test;
import static org.junit.Assert.*;

public class PercolationStats {
    /** Array of fraction of open sites in each experiment. */
    private double[] samples;

    /** Helper method that performs one experiment and returns the fraction of
      * open sites. */
    private double sample(Percolation p, int N) {
        while (!p.percolates()) {
            int row, col;
            do {
                row = StdRandom.uniform(N);
                col = StdRandom.uniform(N);
            } while (p.isOpen(row, col));
            p.open(row, col);
        }
        return (double) p.numberOfOpenSites() / (N * N);
    }

    /** Constructor: Perform T independent experiments on an N-by-N grid. */
    public PercolationStats(int N, int T, PercolationFactory pF) {
        if (N <= 0 || T <= 0) {
            String e =
                "PercolationStats(): N and T must be greater than 0. N = "
                + N + " T = " + T + ".";
            throw new IllegalArgumentException(e);
        }
        samples = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation p = pF.make(N);
            samples[i] = sample(p, N);
        }
    }

    /** Sample mean of percolation threshold. */
    public double mean() {
        return StdStats.mean(samples);
    }

    /** Sample standard deviation of percolation threshold. */
    public double stddev() {
        double result = 0.0;
        double m = mean();
        for (double x : samples) {
            result += Math.pow(x - m, 2.0);
        }
        result /= samples.length;
        return Math.pow(result, 0.5);
    }

    /** Low endpoint of 95% confidence interval. */
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.pow(samples.length, 0.5);
    }

    /** Low endpoint of 95% confidence interval. */
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.pow(samples.length, 0.5);
    }

    public static class TestPercolationStats {
        @Test
        public void testPercolationThreshold() {
            PercolationFactory pf = new PercolationFactory();
            PercolationStats ps = new PercolationStats(100, 100, pf);
            double l = ps.confidenceLow();
            double h = ps.confidenceHigh();

            System.out.println(
                    "Lower bound = " + l + ", upper bound = " + h + ".");
            assertTrue(l <= 0.593 && h >= 0.593);
        }
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", TestPercolationStats.class);
    }
}
