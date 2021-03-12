package byow.Core;

import byow.TileEngine.Tileset;

import java.util.PriorityQueue;
import java.util.Random;

public class MazeBuilder {
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);
    private Grid grid;
    private PriorityQueue<Point> fringe;
    private int iterations = 0; // iterations
    private int maxIterations;

    /** Defaults to no iteration limit. */
    MazeBuilder(Grid grid) {
        this(grid, -1);
    }

    MazeBuilder(Grid grid, int maxIterations) {
        this.grid = grid;
        this.maxIterations = maxIterations;
        fringe = new PriorityQueue<Point>((a, b)->b.getPriority()-a.getPriority());
    }

    /** Builds a maze starting from the point at the given coords. */
    private void buildMaze(int x, int y) {
        Point start = grid.get(1, 1);
        fringe.add(start);
        buildMaze();
    }

    /** Recursive helper method. */
    private void buildMaze() {
        iterations++;
        if (reachedIterationLimit()) {
            return;
        }
        if (fringe.isEmpty()) {
            return;
        }
        Point p = fringe.remove();
        System.out.println("* Process: " + p);
        process(p);
        System.out.println("  >>> FRINGE:\n      " + fringe + "\n");
        buildMaze();
    }

    private void process(Point p) {
        int x = p.getX();
        int y = p.getY();

        // pathway clear?
        if (p.getDirection() != null && !clearAhead(p)) {
            return;
        }

        grid.setTile(p, Tileset.FLOOR);
        p.visit();

        for (Point s : grid.surrounding(p)) {
            if (!s.visited()) {
                grid.setTile(s, Tileset.WALL);
            }
        }

        for (Point exit : grid.exits(p)) {
            if (exit.visited()) {
                continue;
            }
            exit.setPriority(RANDOM.nextInt());
            System.out.println("      >>> Add to fringe: " + exit);
            fringe.add(exit);
        }
    }

    private boolean reachedIterationLimit() {
        if (maxIterations < 0 || iterations < maxIterations) {
            return false;
        }
        return true;
    }
}
