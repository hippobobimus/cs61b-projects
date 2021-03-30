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
public class World extends Grid {
    private Random random;
    private boolean animate;
    private TERenderer ter;
    private UnionFind<Point> regions;
    private TETile[][] tiles;
    private State[][] states;

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
    public World(int width, int height, long seed, String animate) {
        super(width, height);

        this.random = new Random(seed);

        this.animate = animate.equals("animate") ? true : false;

        this.ter = new TERenderer();
        ter.initialize(getWidth(), getHeight());

        tiles = new TETile[width][height];
        regions = new UnionFind<>();
        states = new State[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.GRASS;
                Point p = get(x, y);
                regions.add(p);
                states[x][y] = State.EMPTY;
            }
        }
    }

    /**
     * Constructor without animate parameter. Defaults to no animation.
     * @param width width
     * @param height height
     * @param seed pseudo-random number generator seed
     */
    public World(int width, int height, long seed) {
        this(width, height, seed, "");
    }

    /**
     * Constructor without width and height parameters. Defaults to values in
     * the Constants class.
     * @param seed pseudo-random number generator seed
     * @param animate animation/no animation
     */
    public World(long seed, String animate) {
        this(WIDTH, HEIGHT, seed, animate);
    }

    /**
     * Constructor without width, height and animate parameters. Defaults to
     * width and height values in the Constants class and no animation.
     * @param seed pseudo-random number generator seed
     */
    public World(long seed) {
        this(WIDTH, HEIGHT, seed, "");
    }

    /* BUILD -----------------------------------------------------------------*/

    public void build() {
        // rooms
        RoomBuilder rb = new RoomBuilder(this);

        int attempts = 40;

        for (int i = 0; i < attempts; i++) {
            int result = rb.build();
            //System.out.println("  >>> " + (result != -1 ? "SUCCESS" : "FAILED"));
        }

        // maze
        MazeBuilder mb = new MazeBuilder(this);

        Point start = get(1, 1);

        mb.build(start);

        // bridge mazes and rooms
        BridgeBuilder bb = new BridgeBuilder(this);

        double probExtraConnections = 0.1;

        bb.build(probExtraConnections);

        // make maze more sparse
        mb.reduceDeadEnds(20);
    }

    /* ANIMATION -------------------------------------------------------------*/

    /**
     * Renders the current tile state to screen.
     */
    public void render() {
        TETile[][] t = getTiles();
        ter.renderFrame(t);
    }

    /**
     * If animation has been specified, renders the current tile state to screen
     * and pauses for 10ms.
     */
    public void animate() {
        if (!animate) {
            return;
        }

        render();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /* POINT LISTS -----------------------------------------------------------*/

    // TODO Move to MazeBuilder
    /**
     * Returns a list of all dead ends in the world.
     * @return dead ends list
     */
    public List<Point> listDeadEnds() {
        List<Point> result = new ArrayList<>();

        for (Point p : listAllPoints()) {
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
        List<Point> result = new ArrayList<>();

        for (Point exit : exits(p)) {  // exits throws excptn if p not in world
            if (isOpen(exit)) {
                result.add(exit);
            }
        }

        return result;
    }

    /* RANDOM ----------------------------------------------------------------*/

    /**
     * Returns a Random object that can be used to generate psuedo-random
     * numbers.
     * @return pseudo-random number generator
     */
    public Random getRandom() {
        return random;
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

    /* STATES ----------------------------------------------------------------*/

    // TODO remove these abstractions??
    //
    public void open(int x, int y) {
        Point p = get(x, y);
        open(p);
    }

    public void open(Point p) {
        setState(p, State.OPEN);
    }

    public void close(int x, int y) {
        Point p = get(x, y);
        close(p);
    }

    public void close(Point p) {
        setState(p, State.CLOSED);
    }

    public void clear(int x, int y) {
        Point p = get(x, y);
        clear(p);
    }

    public void clear(Point p) {
        setState(p, State.EMPTY);
    }

    /* HELPER METHODS ----------------*/

    /**
     * Returns the current state of the given point. Throws an excpetion
     * if the given point is not contained in the world.
     * @param p point
     * @return state
     */
    private State getState(Point p) {
        validatePoint(p);

        int x = p.getX();
        int y = p.getY();

        State s = states[x][y];

        return s;
    }

    /**
     * Sets the state of the given point to the given state, configuring the
     * tile at this point and its neighbours appropriately. Throws an excpetion
     * if the given point is not contained in the world.
     * @param p point
     * @param s state
     */
    private void setState(Point p, State s) {
        validatePoint(p);

        int x = p.getX();
        int y = p.getY();

        states[x][y] = s;

        configureTiles(p);
    }

    /**
     * Configures the tiles at and around the given point based on its state.
     * @param p point
     */
    private void configureTiles(Point p) {
        State s = getState(p);

        switch(s) {
            case OPEN:
                setTile(p, Tileset.FLOOR);

                for (Point neighbour : surrounding(p)) {
                    if (!isOpen(neighbour)) {
                        setTile(neighbour, Tileset.WALL);
                    }
                }

                break;
            case CLOSED:
                setTile(p, Tileset.WALL);
                break;
            case EMPTY:
                setTile(p, Tileset.GRASS);
                break;
            default:
                setTile(p, Tileset.NOTHING);
                break;
        }
    }

    /* TILES -----------------------------------------------------------------*/

    /**
     * Returns a 2D array of tiles corresponding to points in the 2D grid.
     * @return 2D tile array
     */
    private TETile[][] getTiles() {
        return tiles;
    }

    /**
     * Sets the tile at the given point to the given tile. Throws an exception
     * if the point is not contained in the world.
     * @param p point
     * @param tile tile
     */
    private void setTile(Point p, TETile tile) {
        validatePoint(p);
        int x = p.getX();
        int y = p.getY();
        tiles[x][y] = tile;
    }

    /**
     * Sets the tile at the point corresponding to the given coords to the given
     * tile. Throws an exception if the coords are outside the bounds of the
     * world.
     * @param x x-coord
     * @param y y-coord
     * @param tile tile
     */
    private void setTile(int x, int y, TETile tile) {
        Point p = get(x, y);  // get throws an exception if outside world
        setTile(p, tile);
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
        validatePoint(p);

        State s = getState(p);

        boolean result = s.equals(State.EMPTY) ? true : false;

        return result;
    }

    /**
     * Determines whether the given point is open. Throws an exception if the
     * point is not contained in the world.
     * @param p point
     * @return open?
     */
    public boolean isOpen(Point p) {
        validatePoint(p);

        State s = getState(p);

        boolean result = s.equals(State.OPEN) ? true : false;

        return result;
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

        if (!isOpen(p)) {
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

        if (isOpen(p)) {
            return false;
        }

        for (Point neighbour : surrounding(p)) {
            if (isOpen(neighbour)) {
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
