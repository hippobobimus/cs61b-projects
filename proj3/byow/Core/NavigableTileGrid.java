package byow.Core;

import static byow.Core.Constants.*;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * An extension of the 2D grid of points, incorporating tiles, states, connected
 * regions and rendering to screen.
 * @author Rob Masters
 */
public class NavigableTileGrid extends TileGrid {
    private UnionFind<Point> regions;
    public PointGraph pathway;

    /* CONSTRUCTORS ----------------------------------------------------------*/

    /**
     * Full constructor for a 2D world with associated build method. The given
     * seed determines the result of the random build process. Building can be
     * animated by supplying the "animate" string.
     * @param width width
     * @param height height
     * @param seed pseudo-random number generator seed
     * @param animate animation/no animation
     */
    public NavigableTileGrid(int width, int height) {
        super(width, height);

        pathway = new PointGraph();

        regions = new UnionFind<>();

        for (Point p : listAllPoints()) {
            regions.add(p);
        }
    }

    /**
     * Add to path.
     */
    public void open(Point p) {
        addToPath(p);

        setTile(p, Tileset.FLOOR);
        for (Point s : surrounding(p)) {
            if (!pathway.contains(s)) {
                setTile(s, Tileset.WALL);
            }
        }
    }
    public void open(int x, int y) {
        open(get(x, y));
    }

    private void addToPath(Point p) {
        pathway.add(p);

        for (Point n : exits(p)) {
            if (pathway.contains(n)) {
                pathway.addEdge(p, n);
            }
        }
    }

    /**
     * Remove from path
     */
    public void close(Point p) {
        pathway.remove(p);

        closeTile(p);

        for (Point s : surrounding(p)) {
            if (!isOpen(s)) {
                closeTile(s);
            }
        }
    }

    private void closeTile(Point p) {
        for (Point s : surrounding(p)) {
            if (isOpen(s)) {
                setTile(p, Tileset.WALL);
                return;
            }
        }

        setTile(p, Tileset.GRASS);
    }

    public void close(int x, int y) {
        close(get(x, y));
    }

//    /**
//     * Constructor without animate parameter. Defaults to no animation.
//     * @param width width
//     * @param height height
//     * @param seed pseudo-random number generator seed
//     */
//    public World(int width, int height, long seed) {
//        this(width, height, seed, "");
//    }
//
//    /**
//     * Constructor without width and height parameters. Defaults to values in
//     * the Constants class.
//     * @param seed pseudo-random number generator seed
//     * @param animate animation/no animation
//     */
//    public World(long seed, String animate) {
//        this(WIDTH, HEIGHT, seed, animate);
//    }
//
//    /**
//     * Constructor without width, height and animate parameters. Defaults to
//     * width and height values in the Constants class and no animation.
//     * @param seed pseudo-random number generator seed
//     */
//    public World(long seed) {
//        this(WIDTH, HEIGHT, seed, "");
//    }

    /* BUILD -----------------------------------------------------------------*/

//    public void build() {
//        // rooms
//        RoomBuilder rb = new RoomBuilder(this);
//
//        int attempts = 40;
//
//        for (int i = 0; i < attempts; i++) {
//            int result = rb.build();
//            //System.out.println("  >>> " + (result != -1 ? "SUCCESS" : "FAILED"));
//        }
//
//        // maze
//        MazeBuilder mb = new MazeBuilder(this);
//
//        //Point start = get(1, 1);
//
//        mb.build();
//
//        // bridge mazes and rooms
//        BridgeBuilder bb = new BridgeBuilder(this);
//
//        double probExtraConnections = 0.1;
//
//        bb.build(probExtraConnections);
//
//        // make maze more sparse
//        //mb.reduceDeadEnds(20);
//    }

    /* POINT LISTS -----------------------------------------------------------*/

    // TODO Move to MazeBuilder
    /**
     * Returns a list of all dead ends in the world.
     * @return dead ends list
     */
    public List<Point> listDeadEnds() {
        List<Point> result = new ArrayList<>();

        for (Point p : pathway) {
            if (isDeadEnd(p)) {
                result.add(p);
            }
        }

        return result;
    }

    /**
     * Returns a list of open exits from the given point.
     * @return list open exits
     */
    public List<Point> listOpenExits(Point p) {
        return pathway.listNeighbours(p);
    }

    /* REGIONS ---------------------------------------------------------------*/

    /**
     * Connects the regions containing the given points. If one or more null
     * points are provided then no connections are made. Points p and q must be
     * contained in the world otherwise an exception will be thrown.
     * @param p point
     * @param q point
     */
    public void connect(Point p, Point q) {
        if (p == null || q == null) {
            return;
        }

        validatePoint(p);
        validatePoint(q);

        regions.connect(p, q);
    }

    /**
     * Determines whether the two given points are in the same connected region.
     * Points p and q must be contained in the world otherwise an exception will
     * be thrown.
     * @param p point
     * @param q point
     * @return connected/not connected
     */
    public boolean isConnected(Point p, Point q) {
        validatePoint(p);
        validatePoint(q);

        return regions.isConnected(p, q);
    }

    /* QUERIES ---------------------------------------------------------------*/

    /**
     * Determines whether the point at the given coordinates is empty. Throws an
     * exception if the coords are not within the bounds of the world.
     * @param x x-coord
     * @param y y-coord
     * @return empty?
     */
    public boolean isEmpty(int x, int y) {
        Point p = get(x, y);
        return isEmpty(p);
    }

    /**
     * Determines whether the given point is empty. Throws an exception if the
     * point is not contained in the world.
     * @param p point
     * @return empty?
     */
    public boolean isEmpty(Point p) {
        return !isOpen(p);
    }

    /**
     * Determines whether the given point is open. Throws an exception if the
     * point is not contained in the world.
     * @param p point
     * @return open?
     */
    public boolean isOpen(Point p) {
        validatePoint(p);

        return pathway.contains(p);
    }

    // TODO Move to MazeBuilder
    /**
     * Determines whether the given point is a dead end. Dead ends have only
     * one open exit. Throws an exception if the point is not contained in the
     * world.
     * @param p point
     * @return dead-end?
     */
    public boolean isDeadEnd(Point p) {
        validatePoint(p);

        if (!pathway.contains(p)) {
            return false;
        }

        List<Point> openExits = listOpenExits(p);

        if (openExits.size() == 1) {
            return true;
        }

        return false;
    }

    /**
     * A point can be cleared if it is not open and not surrounded by any open
     * points. Throws an exception if the point is not contained in the world.
     * @param p point
     * @return can be cleared
     */
    public boolean canClear(Point p) {
        validatePoint(p);

        if (pathway.contains(p)) {
            return false;
        }

        for (Point gridNeighbour : surrounding(p)) {
            if (pathway.contains(gridNeighbour)) {
                return false;
            }
        }

        return true;
    }

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

    /**
     * Checks that the given point is contained within the grid and, if not,
     * throws an exception.
     * @param p point
     */
    private void validatePoint(Point p) {
        if (!contains(p)) {
            throw new IllegalArgumentException();
        }
    }

    /* MAIN METHOD -----------------------------------------------------------*/

    /**
     * Builds a new world and renders it to screen.
     */
    public static void main(String[] args) {
        World world = new World(2873123, "animate");
        world.build();
        world.render();
    }
}

