package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.lang.IndexOutOfBoundsException;
import org.junit.Test;
import static org.junit.Assert.*;

public class Percolation {
    private WeightedQuickUnionUF sites;
    private WeightedQuickUnionUF filledSites;
    private int gridDim;
    private int WQUUFDim;
    private int virtualTop;
    private int virtualBottom;
    private boolean[] isOpen;
    private int openTotal;

    /** Creates an N-by-N grid with all sites initially blocked. */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException(
                    "Percolation(): dimension must be greater than 0. Given: "
                    + N + ".");
        }
        gridDim = N;
        WQUUFDim = N * N + 2;
        virtualTop = WQUUFDim - 2;
        virtualBottom = WQUUFDim - 1;
        sites = new WeightedQuickUnionUF(WQUUFDim);
        filledSites = new WeightedQuickUnionUF(WQUUFDim);
        isOpen = new boolean[WQUUFDim];
        for (int i = 0; i < WQUUFDim; i++) {
            isOpen[i] = false;
        }
        openTotal = 0;
    }

    /** Helper method that converts (row, col) coords into a WQUUF index. */
    private int xyTo1D(int row, int col) {
        return row * gridDim + col;
    }

    /** Open the site (row, col) if it is not open already. */
    public void open(int row, int col) {
        if (row >= gridDim || col >= gridDim || row < 0 || col < 0) {
            throw new IndexOutOfBoundsException(
                    "open(): row and column parameters must be between 0 and "
                    + (gridDim - 1) + ". Given (" + row + ", " + col + ").");
        }
        if (isOpen(row, col)) {
            return;
        }

        int site = xyTo1D(row, col);
        int above = xyTo1D(row - 1, col);
        int below = xyTo1D(row + 1, col);
        int left = xyTo1D(row, col - 1);
        int right = xyTo1D(row, col + 1);

        isOpen[site] = true;
        openTotal++;

        if (row == 0) {
            sites.union(site, virtualTop);
            filledSites.union(site, virtualTop);
        }
        // Do not connect filledSites to virtualBottom to prevent backwash.
        if (row == gridDim - 1) {
            sites.union(site, virtualBottom);
        }
        if (row != 0 && isOpen[above]) {
            sites.union(site, above);
            filledSites.union(site, above);
        }
        if (row != gridDim - 1 && isOpen[below]) {
            sites.union(site, below);
            filledSites.union(site, below);
        }
        if (col != 0 && isOpen[left]) {
            sites.union(site, left);
            filledSites.union(site, left);
        }
        if (col != gridDim - 1 && isOpen[right]) {
            sites.union(site, right);
            filledSites.union(site, right);
        }
    }

    /** Returns true if the site (row, col) is open. */
    public boolean isOpen(int row, int col) {
        int site = xyTo1D(row, col);
        return isOpen[site];
    }

    /** Returns true if the site (row, col) is full. */
    public boolean isFull(int row, int col) {
        int site = xyTo1D(row, col);
        return filledSites.connected(site, virtualTop);
    }

    /** Returns the number of open sites. */
    public int numberOfOpenSites() {
        return openTotal;
    }

    /** Returns true if the system percolates, i.e. there is a connected path
      * from top to bottom. */
    public boolean percolates() {
        return sites.connected(virtualTop, virtualBottom);
    }

    public static class TestPercolation {
        @Test
        public void test() {
            Percolation grid = new Percolation(5);

            assertEquals(0, grid.numberOfOpenSites());
            assertFalse(grid.percolates());

            grid.open(0, 1);
            assertEquals(1, grid.numberOfOpenSites());
            assertTrue(grid.isFull(0, 1));
            assertFalse(grid.percolates());

            grid.open(2, 1);
            assertEquals(2, grid.numberOfOpenSites());
            assertFalse(grid.isFull(2, 1));
            assertFalse(grid.percolates());

            grid.open(1, 1);
            grid.open(2, 2);
            grid.open(3, 2);
            grid.open(4, 2);
            assertEquals(6, grid.numberOfOpenSites());
            assertTrue(grid.isFull(2, 1));
            assertTrue(grid.percolates());

            // test for backwash.
            grid.open(4, 4);
            grid.open(3, 4);
            assertEquals(8, grid.numberOfOpenSites());
            assertFalse(grid.isFull(4, 4));
            assertFalse(grid.isFull(3, 4));
            assertTrue(grid.percolates());
        }
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", TestPercolation.class);
    }
}
