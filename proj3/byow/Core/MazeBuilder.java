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

public class MazeBuilder {
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);
    private Grid grid;
    private PriorityQueue<Point> fringe;
    private int iterations = 0; // iterations
    private int maxIterations;

    //TODO
    private Map<Point, Edge> edgeTo;

    /** Defaults to no iteration limit. */
    public MazeBuilder(Grid grid) {
        this(grid, -1);
    }

    public MazeBuilder(Grid grid, int maxIterations) {
        this.grid = grid;
        this.maxIterations = maxIterations;
        // Compare Points in PQ by their priority field.
        Comparator<Point> pqComparator = (a, b) -> b.getPriority() - a.getPriority();
        fringe = new PriorityQueue<>(pqComparator);
        edgeTo = new HashMap<>();
    }

    /** Builds a maze starting from the point at the given coords. */
    public void buildMaze(int x, int y) {
        //grid.validatePoint(x, y);
        Point start = grid.get(x, y);
        fringe.add(start);
        buildMaze();
    }

    /** Iterative helper method. */
    public void buildMaze() {
        while (!reachedIterationLimit() && !fringe.isEmpty()) {
            iterations++;
            Point p = fringe.remove();
            System.out.println("* Process: " + p);
            process(p);
            //System.out.println("  >>> FRINGE:\n      " + fringe + "\n");
            System.out.println("  >>> FRINGE LENGTH: " + fringe.size());
            System.out.println("  >>> FRINGE HEAD: " + fringe.peek() + "\n");
        }
    }
//    /** Recursive helper method. */
//    private void buildMaze() {
//        iterations++;
//        if (reachedIterationLimit()) {
//            return;
//        }
//        if (fringe.isEmpty()) {
//            return;
//        }
//        Point p = fringe.remove();
//        System.out.println("* Process: " + p);
//        process(p);
//        //System.out.println("  >>> FRINGE:\n      " + fringe + "\n");
//        System.out.println("  >>> FRINGE LENGTH: " + fringe.size());
//        System.out.println("  >>> FRINGE HEAD: " + fringe.peek() + "\n");
//        buildMaze();
//    }

    private void process(Point p) {
        System.out.print("CHECK CONSISTENCY: ");
        boolean check = p == grid.get(p.getX(), p.getY());
        System.out.println(check ? "OK" : "FAIL");



        Edge e = edgeTo.get(p);

        // Cease processing if the route ahead is not clear.
        // TODO don't like this null check
        if (e != null && !clearAhead(p)) {
            return;
        }

        grid.setTile(p, Tileset.FLOOR);
        p.visit();
        System.out.println(p);
        System.out.println(grid.get(p.getX(), p.getY()));

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
            Edge edgeToExit = new Edge(p, exit);
            edgeTo.put(exit, edgeToExit);
            fringe.add(exit);
            System.out.print("CHECK CONSISTENCY: ");
            check = exit == grid.get(exit.getX(), exit.getY());
            System.out.println(check ? "OK" : "FAIL");
        }
        update();
        try {
            Thread.sleep(10);
        } catch (InterruptedException excpt) {
            excpt.printStackTrace();
        }
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

    private boolean reachedIterationLimit() {
        if (maxIterations < 0 || iterations < maxIterations) {
            return false;
        }
        return true;
    }

    /** HELPER METHODS */



    /** END OF HELPER METHODS */

    /** Draws the world to screen. */
    private TERenderer ter;
    private void draw() {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(grid.getTiles());
    }
    private void start() {
        ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
    }
    private void update() {
        ter.renderFrame(grid.getTiles());
    }

    public static void main(String[] args) {
        Grid g = new Grid();
        MazeBuilder mb = new MazeBuilder(g);
        mb.start();
        mb.buildMaze(1, 1);
        //mb.draw();
    }
}
