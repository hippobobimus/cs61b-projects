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
public class MazeBuilder implements Builder {
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
    @Override
    public int build(Point start) {
        reset();

        this.start = start;

        if (!validStart(start)) {
            return -1;
        }

        fringe.add(start);
        buildMaze();

        return 0;
    }

    /**
     */
    public void reduceDeadEnds(int iterations) {
        //findDeadEnds();
        for (int i = 0; i < iterations; i++) {
            reduceDeadEnds();
        }
    }

    /**
     */
    private void reduceDeadEnds() {
        List<Point> deadEnds = findDeadEnds();

        for (Point deadEnd : deadEnds) {
            retractDeadEnd(deadEnd);
        }
    }

    /**
     */
    private void retractDeadEnd(Point p) {
        p.close();

        Edge e = edgeTo.get(p);
        if (e == null) {
            return;
        }
        Direction dir = e.direction();

        List<Point> ahead = world.ahead(p, dir);

        for (Point a : ahead) {
            if (world.isOpen(a)) {
                return;
            }
        }

        for (Point a : ahead) {
            world.clear(a);
        }

        world.animate();
    }

    /**
     * Returns a list of all dead ends in the world.
     * @return dead ends list
     */
    private List<Point> findDeadEnds() {
        List<Point> result = new ArrayList<>();

        for (Point p : world.allPoints()) {
            if (isDeadEnd(p)) {
                System.out.println(p);
                result.add(p);
            }
        }

        return result;
    }

    /**
     * Determines whether the given point is a dead end. Dead ends have only
     * one open exit.
     * @param p point
     * @return dead-end?
     */
    private boolean isDeadEnd(Point p) {
        if (!world.isOpen(p)) {
            return false;
        }

        List<Point> openExits = world.openExits(p);
        System.out.println("Open exits: " + openExits);

        if (openExits.size() == 1) {
            return true;
        }

        return false;
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
            if (exit.isOpen()) {
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
    private boolean validStart(Point start) {
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

    /* MAIN METHOD -----------------------------------------------------------*/

    /**
     * Draws a maze to screen starting at (1,1).
     */
    public static void main(String[] args) {
        World world = new World(2873123, "animate");

        MazeBuilder mb = new MazeBuilder(world);

        Point start = world.get(1, 1);
        mb.build(start);

        world.render();
    }
}
