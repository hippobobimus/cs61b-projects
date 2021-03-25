package byow.Core;

import static byow.Core.Constants.*;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;
import java.util.List;
//import java.util.Map;
import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.PriorityQueue;
//import java.util.AbstractMap;


public class World extends Grid {
    private Random random;
    private boolean animate;
    private TERenderer ter;
    private TETile[][] tiles;
    private UnionFind<Point> regions;

    private int totalRooms;
    private int totalFloor;

    /* CONSTRUCTORS ----------------------------------------------------------*/

    public World(int width, int height, long seed, String animate) {
        super(width, height);

        this.random = new Random(seed);

        this.animate = animate.equals("animate") ? true : false;

        this.ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        tiles = new TETile[width][height];
        regions = new UnionFind<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.NOTHING;
                Point p = get(x, y);
                regions.add(p);
            }
        }
    }

    public World(int width, int height, long seed) {
        this(width, height, seed, "");
    }

    public World(long seed, String animate) {
        this(WIDTH, HEIGHT, seed, animate);
    }

    public World(long seed) {
        this(WIDTH, HEIGHT, seed, "");
    }

    /* BUILD -----------------------------------------------------------------*/

    public void build() {
        // Rooms
        RoomBuilder rb = new RoomBuilder(this);

        for (int i = 0; i < 40; i++) {
//            int x = random.nextInt(WIDTH - w);
//            int y = random.nextInt(HEIGHT - h);
//            Point start = grid.get(x, y);
            int result = rb.build();
            System.out.println("  >>> " + (result != -1 ? "SUCCESS" : "FAILED"));
        }

        // maze
        MazeBuilder mb = new MazeBuilder(this);

        Point start = get(1, 1);
        mb.build(start);

        openBridges();

        mb.reduceDeadEnds(10);
        //List<Point> bridges = getBridgePoints();
        //System.out.println(bridges);
    }

    /* BRIDGING REGIONS ------------------------------------------------------*/

    private List<Point> getBridgePoints() {
        List<Point> result = new ArrayList<>();

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                Point p = get(x, y);
                if (isBridge(p)) {
                    result.add(p);
                }
            }
        }

        return result;
    }

    private boolean isBridge(Point p) {
        int count = 0;
        List<Point> openExits = new ArrayList<>();
        for (Point exit : exits(p)) {
            if (isOpen(exit)) {
                count++;
                openExits.add(exit);
            }
        }

        if (count != 2) {
            return false;
        }

        if (isConnected(openExits.get(0), openExits.get(1))) {
            return false;
        }

        return true;
    }

    private void openBridge(Point bridge) {
        open(bridge);
        for (Point exit : exits(bridge)) {
            if (isOpen(exit)) {
                connect(bridge, exit);
            }
        }
    }

    private void openBridges() {
        List<Point> bridges = getBridgePoints();

        for (Point bridge : bridges) {
            double r = random.nextDouble();
            if (isBridge(bridge)) {
                openBridge(bridge);
            } else if (r < 0.1) {
                openBridge(bridge);
            }
        }
    }

    /* RANDOM ----------------------------------------------------------------*/

    public Random getRandom() {
        return random;
    }

    /* ANIMATION -------------------------------------------------------------*/

    public void render() {
        TETile[][] t = getTiles();
        ter.renderFrame(t);
    }

    /**
     * Renders the current tile state to screen and pauses for 10ms.
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

    /* REGIONS ---------------------------------------------------------------*/

    public void connect(Point p, Point q) {
        if (p == null || q == null) {
            return;
        }
        regions.connect(p, q);
    }

    public boolean isConnected(Point p, Point q) {
        return regions.isConnected(p, q);
    }

    /* TILES ---------------------------------------------------------------*/

    public TETile getTile(Point p) {
        // TODO check contains
        int x = p.getX();
        int y = p.getY();
        return tiles[x][y];
    }


    // TODO make private
    public TETile[][] getTiles() {
        return tiles;
    }

    public void setTile(Point p, TETile tile) {
        // TODO check contains
        int x = p.getX();
        int y = p.getY();
        tiles[x][y] = tile;
        //p.setTile(tile);
    }

    public void setTile(int x, int y, TETile tile) {
        // TODO check contains
        Point p = get(x, y);
        setTile(p, tile);
    }

    public void open(int x, int y) {
        Point p = get(x, y);
        open(p);
    }

    public void open(Point p) {
        if (!contains(p)) {
            throw new IllegalArgumentException();
        }

        p.open();
        setTile(p, Tileset.FLOOR);
        for (Point s : surrounding(p)) {
            if (!s.isOpen()) {
                setTile(s, Tileset.WALL);
            }
        }
    }

    public List<Point> openExits(Point p) {
        List<Point> result = new ArrayList<>();

        for (Point exit : exits(p)) {
            if (isOpen(exit)) {
                result.add(exit);
            }
        }

        return result;
    }

    public void close(int x, int y) {
        Point p = get(x, y);
        close(p);
    }

    public void close(Point p) {
        if (!contains(p)) {
            throw new IllegalArgumentException();
        }

        p.close();
        setTile(p, Tileset.WALL);
    }

    public void clear(int x, int y) {
        Point p = get(x, y);
        clear(p);
    }

    public void clear(Point p) {
        if (!contains(p)) {
            throw new IllegalArgumentException();
        }

        p.close();
        setTile(p, Tileset.NOTHING);
    }

    public boolean isOpen(Point p) {
        return p.isOpen();
    }

    public boolean isEmpty(int x, int y) {
        Point p = get(x, y);
        return isEmpty(p);
    }

    public boolean isEmpty(Point p) {
        TETile t = getTile(p);
        if (t.equals(Tileset.NOTHING)) {
            return true;
        }
        return false;
    }

    /**
     * Returns a list of all dead ends in the world.
     * @return dead ends list
     */
    public List<Point> allDeadEnds() {
        List<Point> result = new ArrayList<>();

        for (Point p : allPoints()) {
            if (isDeadEnd(p)) {
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
    public boolean isDeadEnd(Point p) {
        if (!isOpen(p)) {
            return false;
        }

        List<Point> openExits = openExits(p);

        if (openExits.size() == 1) {
            return true;
        }

        return false;
    }

    /**
     * A point can be cleared if it is not open and not surrounded by any open
     * points.
     * @param p point
     * @return can be cleared
     */
    public boolean canClear(Point p) {
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


    public static void main(String[] args) {
        World world = new World(2873123, "animate");
        world.build();
        world.render();
    }
}
