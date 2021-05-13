package byow.Core;

import static byow.Core.Constants.*;
import byow.TileEngine.TERenderer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Uses a randomised Prim's algorithm to pseudo-randomly generate connected
 * maze(s) that fills the available grid area. Incorporates the ability to
 * retract dead end corridors within the generated maze.
 * @author Rob Masters
 */
public class MazeBuilder {
    private NavigableTileGrid ntg;
    private Random random;
    private Map<Point, Edge> edgeTo;
    private DedupPQ<Point> fringe;
    private Point start = null;
    private int iterations;
    private int reductionIterations;

    /* CONSTRUCTORS ----------------------------------------------------------*/

    /**
     * Full constructor. Creates an object with associated methods for creating
     * and maintaining mazes in a navigable tile grid.
     * @param ntg navigable tile grid
     * @param random pseudorandom number generator
     */
    public MazeBuilder(NavigableTileGrid ntg, Random random) {
        this.ntg = ntg;
        this.random = random;

        // Compare Points in PQ by their priority field.
        Comparator<Point> cmp = (a, b) -> b.getPriority() - a.getPriority();
        this.fringe = new DedupPQ<>(cmp);

        this.edgeTo = new HashMap<>();

        this.iterations = 0;
        this.reductionIterations = 0;
    }

    /* PUBLIC METHODS --------------------------------------------------------*/

    /**
     * Incrementally fills all the available space in the grid area with
     * maze(s). This function advances the process by a single step, which
     * corresponds to a single point being added to the pathway. The iteration
     * limit can be set in the Constants class in order to end the process
     * early.
     * @return whether the build is complete
     */
    public boolean build() {
        if (reachedIterationLimit()) {
            return true;
        }

        // get a new start point?
        if (start == null || fringe.isEmpty()) {
            start = findStart();

            if (start == null) {
                return true; // no more valid starts => complete
            } else {
                fringe.add(start);
            }
        }

        // step.
        boolean stepDone = false;

        Point p = fringe.remove();

        if (canProcess(p)) {
            stepDone = process(p); // step done when path expanded.
        } else {
            edgeTo.remove(p);
        }

        if (!stepDone) {
            build();
        }

        return false;
    }

    /**
     * Retracts all dead ends in the pathway by a further step. The total number
     * of steps is limited and this limit is set in the Constants class. The
     * greater the steps, the more sparse the maze becomes.
     * @return whether the retraction step limit has been reached
     */
    public boolean reduceDeadEnds() {
        List<Point> deadEnds = ntg.pathway.listLeafPoints();

        if (reductionIterations < DEAD_END_PRUNING_STEPS) {
            reduceDeadEnds(deadEnds);
            reductionIterations++;
            return false;
        }
        return true;
    }

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

    /**
     * Finds a valid start point in the grid. If one cannot be found, returns
     * null.
     * @return start point
     */
    private Point findStart() {
        List<Point> all = ntg.listAllPoints();

        for (Point p : all) {
            if (isValidStart(p)) {
                return p;
            }
        }

        return null;
    }

    /**
     * Checks whether a point is a valid start point for a maze. To be valid it
     * must not be on the pathway, or surrounded by any points on the pathway,
     * and must not be in a corner or at an edge of the grid area.
     * @param start start point
     */
    private boolean isValidStart(Point start) {
        if (!ntg.contains(start)) {
            return false;
        }
        if (ntg.isAtGridBoundary(start)) {
            return false;
        }
        if (ntg.isPath(start)) {
            return false;
        }
        for (Point nbr : ntg.listNeighbours(start)) {
            if (ntg.isPath(nbr)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Extends the maze with the given point if it is suitable and then adds any
     * available routes onward in cardinal directions from this point to the
     * fringe.
     * @param p point
     * @return whether the point was added to the path
     */
    private boolean process(Point p) {
        boolean success = false;

        ntg.openPath(p);

        if (ntg.isPath(p)) {
            success = true;
        }

        // add cardinal neighbours not on the pathway to the fringe and
        // corresponding edges to edgeTo.
        for (Point nbr : ntg.listCardinalNeighbours(p)) {
            if (!ntg.isPath(nbr)) {
                nbr.setPriority(random.nextInt());
                fringe.add(nbr);
                edgeTo.put(nbr, new Edge(p, nbr));
            }
        }

        iterations++;

        return success;
    }

    /**
     * Determines whether the given point can be processed by the maze builder.
     * To be processed it must not lie in a corner or at an edge of the grid,
     * or have any pathway in the arc ahead. A start point must have no
     * surrounding points on the pathway.
     * @param p point
     */
    private boolean canProcess(Point p) {
        Edge e = edgeTo.get(p);
        List<Point> arc;

        // start points will have no edgeTo entry, so consider all neighbours.
        if (e == null) {
            arc = ntg.listNeighbours(p);
        } else {
            arc = ntg.listArcNeighbours(p, e.direction());
        }

        // reached an edge or corner.
        if (ntg.isAtGridBoundary(p)) {
            return false;
        }

        // make sure the points in the arc ahead are unopened.
        for (Point a : arc) {
            if (ntg.isPath(a)) {
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
        if (MAZE_ITERATION_LIMIT < 0 || iterations < MAZE_ITERATION_LIMIT) {
            return false;
        }
        return true;
    }

    /**
     * Retracts each one of the given dead ends by a single step, returning an
     * updated list of dead ends.
     * @param deadEnds list of dead ends
     * @return updated list of dead ends
     */
    private void reduceDeadEnds(List<Point> deadEnds) {
        for (Point deadEnd : deadEnds) {
            retractDeadEnd(deadEnd);
        }
    }

    /**
     * Closes the given point, removes the edge to it and, if possible, clears
     * the points that were ahead of it.
     * @param p point
     */
    private void retractDeadEnd(Point p) {
        ntg.closePath(p);
        ntg.setTileNeighbourhood(p);
        edgeTo.remove(p);
    }

    /* MAIN METHOD -----------------------------------------------------------*/

    /**
     * Fills available screen area with maze(s), animating the process to
     * screen.
     */
    public static void main(String[] args) {
        NavigableTileGrid grid = new NavigableTileGrid(
                WINDOW_WIDTH, WINDOW_HEIGHT);

        TERenderer ter = new TERenderer();
        ter.initialize(WINDOW_WIDTH, WINDOW_HEIGHT);

        Random rand = new Random(2873123);

        MazeBuilder mb = new MazeBuilder(grid, rand);

        boolean done = false;
        while (!done) {
            done = mb.build();
            ter.renderFrame(grid.getFrame()); // animated build.
        }
    }
}
