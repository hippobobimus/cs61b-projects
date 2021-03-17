package byow.Core;

import byow.TileEngine.Tileset;
import byow.TileEngine.TERenderer;
import static byow.Core.Constants.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

/** Uses a randomised Prim's algorithm to pseudo-randomly generate a connected
  * maze that fills the available grid area. */
public class MazeBuilder {
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);
    private Grid grid;
    private Point start;
    private PriorityQueue<Point> fringe;
    private Map<Point, Edge> edgeTo;
    private int iterations = 0;
    private int maxIterations;
    private boolean animate = false;
    private TERenderer ter;

    /** CONSTRUCTORS ---------------------------------------------------------*/

    /** Defaults to no iteration limit and no animation. */
    public MazeBuilder(Grid grid, Point start) {
        this(grid, start, -1, "", null);
    }
    /** Defaults to no iteration limit. */
    public MazeBuilder(Grid grid, Point start, String animate, TERenderer ter) {
        this(grid, start, -1, animate, ter);
    }
    /** Defaults to no animation. */
    public MazeBuilder(Grid grid, Point start, int maxIterations) {
        this(grid, start, maxIterations, "", null);
    }
    /** If animate is specified a TERenderer must also be provided. */
    public MazeBuilder(Grid grid, Point start, int maxIterations, String animate, TERenderer ter) {
        this.grid = grid;
        // TODO check start in grid and valid.
        this.start = start;
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

        buildMaze(start);
    }

    /** PUBLIC METHODS -------------------------------------------------------*/

    /** Builds a maze starting from the point at the given coords. */
    public void buildMaze(Point start) {
        // TODO grid.validatePoint(x, y);
        fringe.add(start);
        buildMaze();
    }

    /** HELPER METHODS -------------------------------------------------------*/

    /** Iterative helper method. */
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

    /** */
    private void process(Point p) {
        // Cease processing if the route ahead is not clear.
        // TODO don't like this null check
        // TODO discard edge in edgeTo if not clear
        Edge e = edgeTo.get(p);
        if (e != null && !clearAhead(p)) {
            return;
        }

        // mark visited and assign floor tile, surrounded by wall.
        p.visit();
        grid.setTile(p, Tileset.FLOOR);
        for (Point s : grid.surrounding(p)) {
            if (!s.visited()) {
                grid.setTile(s, Tileset.WALL);
            }
        }

        // add available unvisited exits to the fringe.
        for (Point exit : grid.exits(p)) {
            if (exit.visited()) {
                continue;
            }
            exit.setPriority(RANDOM.nextInt());
            Edge edgeToExit = new Edge(p, exit);
            edgeTo.put(exit, edgeToExit);
            fringe.add(exit);
            //System.out.println("      >>> Add to fringe: " + exit);
        }
        animateStep();
    }

    private boolean carvable(Point p) {
        if (p.equals(start)) {
        }
        Edge e = edgeTo.get(p);
        Direction dir = e.direction();
        List<Point> ahead = grid.ahead(p, dir);
        //TODO verify in bounds
        if (ahead.size() < 5) {
            return false;
        }
        System.out.println("AHEAD = " + ahead);

        if (ahead == null) {
            System.out.println("  >>> clearAhead() of " + p + ": " + false);
            return false;
        }
        for (Point a : ahead) {
            System.out.println("checking... " + a);
            if (a.getTile().equals(Tileset.FLOOR)) {
                System.out.println("  >>> clearAhead() of " + p + ": " + false);
                return false;
            }
        }
        System.out.println("  >>> clearAhead() of " + p + ": " + true);
        return true;
    }
    private boolean clearAhead(Point p) {
        Edge e = edgeTo.get(p);
        Direction dir = e.direction();
        List<Point> ahead = grid.ahead(p, dir);
        //TODO verify in bounds
        if (ahead.size() < 5) {
            return false;
        }
        System.out.println("AHEAD = " + ahead);

        if (ahead == null) {
            System.out.println("  >>> clearAhead() of " + p + ": " + false);
            return false;
        }
        for (Point a : ahead) {
            System.out.println("checking... " + a);
            if (a.getTile().equals(Tileset.FLOOR)) {
                System.out.println("  >>> clearAhead() of " + p + ": " + false);
                return false;
            }
        }
        System.out.println("  >>> clearAhead() of " + p + ": " + true);
        return true;
    }

    /** Checks the current iteration value against the set limit and returns
      * true if it has been exceeded.
      * A negative iteration limit is interpreted as no iteration limit. */
    private boolean reachedIterationLimit() {
        if (maxIterations < 0 || iterations < maxIterations) {
            return false;
        }
        return true;
    }

    /** Updates the TERenderer with the current tile state, then pauses for
      * 10ms. */
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

    /** END OF HELPER METHODS */

    /** Draws a maze to screen starting at (1,1). */
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        Grid g = new Grid();
        Point start = g.get(1, 1);

        MazeBuilder mb = new MazeBuilder(g, start, "animate", ter);
        ter.renderFrame(g.getTiles());

        //mb.buildMaze(1, 1);
    }
}
