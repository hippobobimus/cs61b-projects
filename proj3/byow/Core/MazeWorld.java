package byow.Core;

import static byow.Core.Constants.*;

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
public class MazeWorld extends RoomWorld {
    private Map<Point, Edge> edgeTo;
    private DedupPQ<Point> fringe;
    private int maxIterations;
    private int iterations;

    /* CONSTRUCTORS ----------------------------------------------------------*/

    /**
     * Full constructor. Creates an object with associated methods for creating
     * and maintaining mazes in 2D tilespace.
     * @param width width
     * @param height height
     * @param random pseudorandom number generator
     * @param animate animation/no animation
     */
    public MazeWorld(int width, int height, Random random, String animate) {
        super(width, height, random, animate);

        // Compare Points in PQ by their priority field.
        Comparator<Point> cmp = (a, b) -> b.getPriority() - a.getPriority();
        fringe = new DedupPQ<>(cmp);

        edgeTo = new HashMap<>();
    }

    /* PUBLIC METHODS --------------------------------------------------------*/

    /**
     * Fills all the available space in the grid area with maze(s). Each maze
     * generation stops after the given number of maximum iterations. If a
     * negative value is given this is interpreted as no iteration limit.
     * @param maxIterations maximum iterations
     */
    public void mazeFill(int maxIterations) {
        this.maxIterations = maxIterations;

        for (Point start = findStart(); start != null; start = findStart()) {
            buildMaze(start);
        }
    }

    /**
     * Maze fill method with unlimited iterations during maze generation.
     */
    public void mazeFill() {
        mazeFill(-1);
    }

    /**
     * Retracts all dead ends in the pathway by up to the given number of steps
     * where possible (less if the dead end cannot be retracted further). The
     * greater the steps, the more sparse the maze becomes.
     * @param steps retraction steps
     */
    public void reduceDeadEnds(int steps) {
        List<Point> deadEnds;

        for (int i = 0; i < steps; i++) {
            deadEnds = pathway.listLeafPoints();
            reduceDeadEnds(deadEnds);
        }
    }

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

    /**
     * Finds a valid start point in the world. If one cannot be found, returns
     * null.
     * @return start point
     */
    private Point findStart() {
        List<Point> all = listAllPoints();

        for (Point p : listAllPoints()) {
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
        if (!contains(start)) {
            return false;
        }
        if (isAtGridBoundary(start)) {
            return false;
        }
        if (isPath(start)) {
            return false;
        }
        for (Point nbr : listNeighbours(start)) {
            if (isPath(nbr)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Builds a maze starting from the given point, filling the available
     * reachable area until either no more area remains or the iteration limit
     * is reached.
     * @param start start point
     */
    private void buildMaze(Point start) {
        fringe.add(start);

        for (iterations = 0; !reachedIterationLimit() && !fringe.isEmpty();
                iterations++) {
            Point p = fringe.remove();

            if (canProcess(p)) {
                process(p);
                animate();
            } else {
                edgeTo.remove(p);
            }
        }
    }

    /**
     * Extends the maze with the given point if it is suitable and then adds any
     * available routes onward in cardinal directions from this point to the
     * fringe.
     * @param p point
     */
    private void process(Point p) {
        openPath(p);

        // add cardinal neighbours not on the pathway to the fringe and
        // corresponding edges to edgeTo.
        for (Point nbr : listCardinalNeighbours(p)) {
            if (!isPath(nbr)) {
                nbr.setPriority(random.nextInt());
                fringe.add(nbr);
                edgeTo.put(nbr, new Edge(p, nbr));
            }
        }
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
            arc = listNeighbours(p);
        } else {
            arc = listArcNeighbours(p, e.direction());
        }

        // reached an edge or corner.
        if (isAtGridBoundary(p)) {
            return false;
        }

        // make sure the points in the arc ahead are unopened.
        for (Point a : arc) {
            if (isPath(a)) {
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
        closePath(p);
        setTileNeighbourhood(p);
        edgeTo.remove(p);
        animate();
    }

    /* MAIN METHOD -----------------------------------------------------------*/

    /**
     * Fills available screen area with maze(s), animating the process to
     * screen.
     */
    public static void main(String[] args) {
        Random rand = new Random(2873123);

        MazeWorld mw = new MazeWorld(WIDTH, HEIGHT, rand, "animate");

        mw.mazeFill(-1);

        mw.render();

        mw.reduceDeadEnds(20);

        mw.render();
    }
}
