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
 * Uses a randomised Prim's algorithm to pseudo-randomly generate a connected
 * maze that fills the available world area.
 * @author Rob Masters
 */
public class MazeBuilder { //implements Builder {
    private World world;
    private Random random;
    private int iterations;
    private int maxIterations;
    private Map<Point, Edge> edgeTo;
    private PriorityQueue<Point> fringe;
    private Point start;

    /* CONSTRUCTORS ----------------------------------------------------------*/

    /**
     * Full constructor. Creates an object with associated build methods for
     * creating mazes in 2D tilespace.
     * @param world world
     * @param maxIterations max iterations when building a maze
     */
    public MazeBuilder(World world, int maxIterations) {
        this.world = world;
        this.maxIterations = maxIterations;

        this.random = world.getRandom();

        // Compare Points in PQ by their priority field.
        Comparator<Point> cmp = (a, b) -> b.getPriority() - a.getPriority();
        fringe = new PriorityQueue<>(cmp);
        edgeTo = new HashMap<>();
    }

    /**
     * Constructor without max iterations. Defaults to no iteration limit.
     * @param world world
     */
    public MazeBuilder(World world) {
        this(world, -1);
    }

    /* PUBLIC METHODS --------------------------------------------------------*/

    /** Builds a maze starting from the given point, filling the available
     * reachable area. If the start point is not usable, returns -1. Otherwise
     * returns 0 upon successful completion.
     * @param start start point
     */
    //@Override
    public void build() {
        for (start = findStart(); start != null; start = findStart()) {
            System.out.println(start);
            buildMaze();
        }
        //start = findStart();
        //buildMaze();
//        reset();
//
//        this.start = start;
//
//        if (!validStart(start)) {
//            return -1;
//        }
//
//        fringe.add(start);
//        buildMaze();
//
//        return 0;
    }

    /**
     * Retracts all dead ends in the world by up to the given number of steps
     * where possible (less if the dead end cannot be retracted further). The
     * greater the steps, the more sparse the maze becomes.
     * @param steps retraction steps
     */
    public void reduceDeadEnds(int steps) {
        List<Point> deadEnds = world.listDeadEnds();

        for (int i = 0; i < steps; i++) {
            deadEnds = reduceDeadEnds(deadEnds);
        }
    }

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

    /**
     * Finds a valid start point in the world. If one cannot be found, returns
     * null.
     * @return start point
     */
    private Point findStart() {
        List<Point> all = world.listAllPoints();

        for (Point p : world.listAllPoints()) {
            if (isValidStart(p)) {
                return p;
            }
        }

        return null;
    }

    /**
     * Builds a single maze originating at the instances current start point.
     * Throws an exception if the start point is not valid.
     */
    private void buildMaze() {
        reset();

        if (!isValidStart(start)) {
            throw new IllegalArgumentException(
                    "Invalid start point for maze: " + start);
        }

        fringe.add(start);

        buildMazeIter();
    }

    /**
     * Iterative helper method. Coninues to iterate until either the iteration
     * limit has been reached or there are no more points left to process in the
     * fringe.
     */
    private void buildMazeIter() {
        while (!reachedIterationLimit() && !fringe.isEmpty()) {
            iterations++;
            Point p = fringe.remove();
            //System.out.println("* Process: " + p);
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
        // Cease processing if the route is not clear and remove the edge to
        // this point.
        if (!canProcess(p)) {
            edgeTo.remove(p);
            return;
        }

        // open point, which assigns floor tile, surrounded by wall.
        world.open(p);

        // connect to previous point
        Edge e = edgeTo.get(p);
        if (e != null) {  // start will not have an edge
            Point from = e.from();
            world.connect(from, p);
        }

        // add unopen exits to the fringe and corresponding edges to edgeTo.
        for (Point exit : world.exits(p)) {
            if (world.isOpen(exit)) {
                continue;
            }

            exit.setPriority(random.nextInt());

            Edge edgeToExit = new Edge(p, exit);
            edgeTo.put(exit, edgeToExit);

            fringe.add(exit);
        }
        world.animate();
    }

    /**
     * Determines whether the given point can be processed. To be processed
     * it must not lie in a corner or at an edge, or have any open points
     * in the arc ahead of it. The start is an automatic pass as it has already
     * been checked.
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

        List<Point> arc = world.arc(p, dir);

        // reached an edge or corner.
        if (arc == null || arc.size() < 5) {
            return false;
        }

        // make sure the points in the arc ahead are unopened.
        for (Point a : arc) {
            if (world.isOpen(a)) {
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
    private boolean isValidStart(Point start) {
        if (!world.contains(start)) {
            return false;
        }

        if (world.isOpen(start)) {
            return false;
        }

        List<Point> surroundingPoints = world.surrounding(start);

        // check for edge / corner position.
        if (surroundingPoints.size() < 8) {
            return false;
        }

        for (Point p : surroundingPoints) {
            if (world.isOpen(p)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Retracts each one of the given dead ends by a single step, returning an
     * updated list of dead ends.
     * @param deadEnds list of dead ends
     * @return updated list of dead ends
     */
    private List<Point> reduceDeadEnds(List<Point> deadEnds) {
        List<Point> newDeadEnds = new ArrayList<>();;

        for (Point deadEnd : deadEnds) {
            Point newDE = edgeTo.get(deadEnd).from();
            retractDeadEnd(deadEnd);
            if (world.isDeadEnd(newDE)) {
                newDeadEnds.add(newDE);
            }
        }

        return newDeadEnds;
    }

    /**
     * Closes the given point, removes the edge to it and, if possible, clears
     * the points that were ahead of it.
     * @param p point
     */
    private void retractDeadEnd(Point p) {
        world.close(p);

        Edge e = edgeTo.get(p);

        // ignore start position.
        if (e == null) {  
            return;
        }

        Direction dir = e.direction();

        List<Point> ahead = world.ahead(p, dir);

        // clear points ahead if possible.
        for (Point a : ahead) {
            if (world.canClear(a)) {
                world.clear(a);
            }
        }

        // remove edge to point.
        edgeTo.remove(p);

        world.animate();
    }

    /* MAIN METHOD -----------------------------------------------------------*/

    /**
     * Draws a maze to screen starting at (1,1).
     */
    public static void main(String[] args) {
        World world = new World(2873123, "animate");

        MazeBuilder mb = new MazeBuilder(world);

        //Point start = world.get(1, 1);
        mb.build();

        world.render();
    }
}
