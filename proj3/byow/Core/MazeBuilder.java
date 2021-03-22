package byow.Core;

import static byow.Core.Constants.*;
import byow.TileEngine.TERenderer;
import byow.TileEngine.Tileset;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Uses a randomised Prim's algorithm to pseudo-randomly generate a connected
 * maze that fills the available grid area.
 * @author Rob Masters
 */
public class MazeBuilder {
    private Grid grid;
    private Random random;
    private int iterations;
    private int maxIterations;
    private boolean animate = false;
    private TERenderer ter;
    private Map<Point, Edge> edgeTo;
    private PriorityQueue<Point> fringe;
    private Point start;

    /* CONSTRUCTORS ----------------------------------------------------------*/

    /**
     * Full constructor. Creeates an object with associated build methods for
     * creating mazes in 2D tilespace. If "animate" is specified a TERenderer
     * must also be provided.
     * @param grid grid
     * @param random pseudo-random number generator
     * @param maxIterations max iterations when building a maze
     * @param animate animate?
     * @param ter renderer
     */
    public MazeBuilder(Grid grid, Random random, int maxIterations, String animate, TERenderer ter) {
        this.grid = grid;
        this.random = random;
        this.maxIterations = maxIterations;
        this.animate = animate.equals("animate") ? true : false;
        this.ter = ter;

        if (this.animate && ter == null) {
            throw new IllegalArgumentException("When animating, a TERenderer" +
                    "must be provided.");
        }

        // Compare Points in PQ by their priority field.
        Comparator<Point> cmp = (a, b) -> b.getPriority() - a.getPriority();
        fringe = new PriorityQueue<>(cmp);
        edgeTo = new HashMap<>();
    }

    /**
     * Constructor without specifying animation/renderer.
     * Defaults to no animation.
     * @param grid grid
     * @param random pseudo-random number generator
     * @param maxIterations max iterations when building a maze
     */
    public MazeBuilder(Grid grid, Random random, int maxIterations) {
        this(grid, random, maxIterations, "", null);
    }

    /**
     * Constructor without max iterations.
     * Defaults to no iteration limit.
     * @param grid grid
     * @param random pseudo-random number generator
     * @param animate animate?
     * @param ter renderer
     */
    public MazeBuilder(Grid grid, Random random, String animate, TERenderer ter) {
        this(grid, random, -1, animate, ter);
    }

    /**
     * Constructor without max iterations, or specifying animation/renderer.
     * Defaults to no iteration limit and no animation.
     * @param grid grid
     * @param random pseudo-random number generator
     */
    public MazeBuilder(Grid grid, Random random) {
        this(grid, random, -1, "", null);
    }

    /* PUBLIC METHODS --------------------------------------------------------*/

    /** Builds a maze starting from the given point, filling the available
     * reachable area. If the start point is not usable, returns -1. Otherwise
     * returns 0 upon successful completion.
     * @param start start point
     */
    public int buildMaze(Point start) {
        reset();

        this.start = start;

        if (!validStart(start)) {
            return -1;
        }

        fringe.add(start);
        buildMaze();

        return 0;
    }

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

    /**
     * Iterative helper method. Coninues to iterate until either the iteration
     * limit has been reached or there are no more points left to process in the
     * fringe.
     */
    private void buildMaze() {
        while (!reachedIterationLimit() && !fringe.isEmpty()) {
            iterations++;
            Point p = fringe.remove();
            System.out.println("* Process: " + p);
            process(p);
            //System.out.println("  >>> FRINGE:\n      " + fringe + "\n");
            //System.out.println("  >>> FRINGE LENGTH: " + fringe.size());
            //System.out.println("  >>> FRINGE HEAD: " + fringe.peek() + "\n");
        }
    }

    /**
     * Extends the maze with the given point if it is suitable and then adds any
     * available exits onward from this point to the fringe.
     * @param p point
     */
    private void process(Point p) {
        // Cease processing if the route ahead is not clear and remove the edge
        // to this point.
        if (!canProcess(p)) {
            edgeTo.remove(p);
            return;
        }

        // open point and assign floor tile, surrounded by wall.
        p.open();

        grid.setTile(p, Tileset.FLOOR);

        for (Point s : grid.surrounding(p)) {
            if (!s.isOpen()) {
                grid.setTile(s, Tileset.WALL);
            }
        }

        // add unopen exits to the fringe and corresponding edges to edgeTo.
        for (Point exit : grid.exits(p)) {
            if (exit.isOpen()) {
                continue;
            }

            exit.setPriority(random.nextInt());

            Edge edgeToExit = new Edge(p, exit);
            edgeTo.put(exit, edgeToExit);

            fringe.add(exit);
        }
        animateStep();
    }

    /**
     * Determines whether the given point can be processed. To be processed
     * it must not lie in a corner or at an edge, or have any open points
     * ahead of it. The start is an automatic pass as it has already been
     * checked.
     * @param p point
     */
    private boolean canProcess(Point p) {
        if (p.equals(start)) {
            return true;
        }

        Edge e = edgeTo.get(p);

        // point may have been processed before and had the edge to it removed.
        // (the fringe can contain the same point more than once)
        if (e == null) {
            return false;
        }

        Direction dir = e.direction();

        List<Point> ahead = grid.ahead(p, dir);

        // reached an edge or corner.
        if (ahead == null || ahead.size() < 5) {
            return false;
        }

        // make sure the points ahead are unopened.
        for (Point a : ahead) {
            if (a.isOpen()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks the current iteration value against the set limit and returns
     * true if it has been exceeded.
     * A negative iteration limit is interpreted as no iteration limit.
     * @return whether iteration limit has been reached
     */
    private boolean reachedIterationLimit() {
        if (maxIterations < 0 || iterations < maxIterations) {
            return false;
        }
        return true;
    }

    /**
     * Resets the mazebuilder so that it is ready to begin building a new maze.
     */
    private void reset() {
        iterations = 0;
    }

    /**
     * Checks whether a point is a valid start point for a maze. To be valid it
     * must not be a floor tile, or surrounded by any floor tiles and must not
     * be in a corner or at an edge.
     * @param start start point for maze
     */
    private boolean validStart(Point start) {
        if (!grid.contains(start)) {
            return false;
        }

        if (start.getTile().equals(Tileset.FLOOR)) {
            return false;
        }

        List<Point> surroundingPoints = grid.surrounding(start);

        if (surroundingPoints.size() < 8) {
            return false;
        }

        for (Point p : surroundingPoints) {
            if (p.isOpen()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Updates the current tile state to screen and pauses for 10ms.
     */
    private void animateStep() {
        if (!animate) {
            return;
        }
        ter.renderFrame(grid.getTiles());
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /* MAIN METHOD -----------------------------------------------------------*/

    /**
     * Draws a maze to screen starting at (1,1).
     */
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        Grid g = new Grid();

        Random rand = new Random(2873123);

        MazeBuilder mb = new MazeBuilder(g, rand, "animate", ter);

        Point start = g.get(1, 1);
        mb.buildMaze(start);

        ter.renderFrame(g.getTiles());
    }
}
