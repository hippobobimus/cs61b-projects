package byow.Core;

import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.AbstractMap;
import java.lang.IllegalArgumentException;

import static byow.Core.Constants.*;

public class World {
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);
    private TETile[][] tiles;

//    private Map<Point, List<Point>> adjVertices;
//    private Point[][] grid;

    private int totalRooms;
    private int totalFloor;

    World() {
        tiles = new TETile[WIDTH][HEIGHT];
//        adjVertices = new HashMap<Point, List<Point>>();
//        grid = new Point[WIDTH][HEIGHT];
        fillWithEmptyTiles();
    }

    private PriorityQueue<DirectedPoint> fringe;
    //private PriorityQueue<Map.Entry<DirectedPoint, Integer>> fringe;
    private Grid mazeGrid;
    private Map<Point, Point> edgeTo;

    private void generateMaze() {
        mazeGrid = new Grid(WIDTH, HEIGHT);
        fringe = new PriorityQueue<DirectedPoint>((a, b)->b.getPriority()-a.getPriority());
        //fringe = new PriorityQueue<Map.Entry<DirectedPoint, Integer>>((a, b)->b.getValue()-a.getValue());
        edgeTo = new HashMap<Point, Point>();

        int priority = 0;
        //DirectedPoint start = new DirectedPoint(1, 1, null, priority);
        DirectedPoint start = mazeGrid.get(1, 1);
        start.setPriority(0);
        //fringe.add(new AbstractMap.SimpleEntry<>(start, priority));
        fringe.add(start);

        makeMaze();
    }

    private int count = 0;
    private void makeMaze() {
        //System.out.println("start: " + mazeGrid.get(1, 1));
        count++;
//        if (count == 1000) {
//            return;
//        }
        if (fringe.isEmpty()) {
            return;
        }
        //DirectedPoint p = fringe.remove().getKey();
        DirectedPoint p = fringe.remove();
        System.out.println("* Process: " + p);
        processPoint(p);
        System.out.println("  >>> FRINGE:\n      " + fringe + "\n");
        makeMaze();
    }

    private boolean clearAhead(DirectedPoint p) {
        List<DirectedPoint> ahead = mazeGrid.pointsAhead(p);

        if (ahead == null) {
            System.out.println("  >>> clearAhead() of " + p + ": " + false);
            return false;
        }
        for (Point a : ahead) {
            //System.out.println("ahead: " + a);
            if (a.getTile().equals(Tileset.FLOOR)) {
                System.out.println("  >>> clearAhead() of " + p + ": " + false);
                return false;
            }
        }
        System.out.println("  >>> clearAhead() of " + p + ": " + true);
        return true;
    }

    private void processPoint(DirectedPoint p) {
        int x = p.getX();
        int y = p.getY();

        // still clear?
        if (p.getDirection() != null && !clearAhead(p)) {
            //p.setTile(Tileset.FLOWER);
            //tiles[x][y] = p.getTile();
            return;
        }


        p.setTile(Tileset.FLOOR);
        tiles[x][y] = p.getTile();
        p.visit();
        //System.out.println(p);
        //System.out.println("start: " + mazeGrid.get(1, 1));
        //System.out.println(p);

        for (DirectedPoint s : mazeGrid.surrounding(p)) {
            if (s.visited()) {
                continue;
            }
            s.setTile(Tileset.WALL);
            tiles[s.getX()][s.getY()] = s.getTile();
        }

        for (DirectedPoint n : mazeGrid.neighbours(p)) {
            if (n.visited()) {
                continue;
            }
            if (clearAhead(n)) {
                int priority = RANDOM.nextInt();
                n.setPriority(priority);
                System.out.println("      >>> Add to fringe: " + n);
                //fringe.add(new AbstractMap.SimpleEntry<>(n, priority));
                fringe.add(n);
            } else {
                //n.setTile(Tileset.FLOWER);
                //tiles[n.getX()][n.getY()] = n.getTile();
            }
            //n.setTile(Tileset.FLOWER);
            //tiles[n.getX()][n.getY()] = n.getTile();
        }
    }

//    private boolean isEmpty(int x, int y) {
//        return false;
//    }
//
//    private void makeCorridor() {
//        TETile[1][1] = Tileset.FLOOR;
//        Point up = null;
//    }

//    private boolean canCarve(Point p) {
//        int x = p.getX();
//        int y = p.getY();
//
//        return false;
//        
//    }

    private void fillWithEmptyTiles() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                tiles[x][y] = Tileset.NOTHING;
                Point p = new Point(x, y, Tileset.NOTHING);
                List<Point> l = new ArrayList<>();
//                adjVertices.put(p, l);
//                grid[x][y] = p;
            }
        }
    }

//    public void addEdge(Point p1, Point p2) {
//        adjVertices.get(p1).add(p2);
//    }

    public TETile[][] tiles() {
        return tiles;
    }

    /** Adds the given Element's tiles to the World's tile array with the
      * origin at the given position. */
    public void add(Element e, int x, int y) {
        Point p = new Point(x, y);
        this.add(e, p);
    }

    public void add(Element e, Point p) {
        int x = p.getX();
        int y = p.getY();
        TETile[][] elemTiles = e.tiles();

        for (int i = 0; i < e.getWidth(); i++) {
            for (int j = 0; j < e.getHeight(); j++) {
                tiles[x + i][y + j] = elemTiles[i][j];
            }
        }
    }

    private boolean areaIsEmpty(Point origin, int width, int height) {
        int x = origin.getX();
        int y = origin.getY();
        return areaIsEmpty(x, y, width, height);
    }

    private boolean areaIsEmpty(int x, int y, int width, int height) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                TETile t = tiles[x + i][y + j];
                if (!t.equals(Tileset.NOTHING)) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Adds a Room with random size and position to the World. */
    private void addRoom() {
        // Random room dimensions and position.
        int w = RANDOM.nextInt(MAX_ROOM_WIDTH - MIN_ROOM_WIDTH + 1) + MIN_ROOM_WIDTH;
        int h = RANDOM.nextInt(MAX_ROOM_HEIGHT - MIN_ROOM_HEIGHT + 1) + MIN_ROOM_HEIGHT;
        int x = RANDOM.nextInt(WIDTH - w);
        int y = RANDOM.nextInt(HEIGHT - h);
        if (areaIsEmpty(x, y, w, h)) {
            Room r = new Room(w, h);
            add(r, x, y);
        }
    }







    /** Draws the world to screen. */
    public void draw() {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(tiles);
    }

//    private void addRow(int x, int y, int length, TETile tile) {
//        for (int i = 0; i < length; i++) {
//            tiles[x + i][y] = tile;
//        }
//    }
//
//    private int rowOffset(int row, int s) {
//        if (row < s) {
//            return s - 1 - row;
//        }
//        return row - s;
//    }
//
//    private int rowLength(int row, int s) {
//        if (row < s) {
//            return s + 2 * row;
//        }
//        return 5 * s - 2 - 2 * row;
//    }
//
//    /** Adds a hexagon of side length s to the given position in the world. */
//    public void addHexagon(int x, int y, int s, TETile tile) {
//        if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT ) {
//            throw new IllegalArgumentException("addHexagon(): coordinates out of bounds.");
//        }
//        if (tile == null) {
//            tile = Tileset.NOTHING;
//        }
//
//        int start, length;
//        for (int row = 0; row < 2 * s; row++) {
//            start = x + rowOffset(row, s);
//            length = rowLength(row, s);
//            addRow(start, y + row, length, tile);
//        }
//    }

    /** HELPER METHODS */



    /** END OF HELPER METHODS */

    public static void main(String[] args) {
        World w = new World();
        w.run(args);
    }
    public void run(String[] args) {
        //for (int i = 0; i < 1000; i++) {
        //    addRoom();
        //}
        generateMaze();
        draw();
    }


//    private static final long SEED = 2873123;
//    private static final Random RANDOM = new Random(SEED);
//    /** Picks a RANDOM tile with a 33% change of being
//     *  a wall, 33% chance of being a flower, and 33%
//     *  chance of being empty space.
//     */
//    private static TETile randomTile() {
//        int tileNum = RANDOM.nextInt(3);
//        switch (tileNum) {
//            case 0: return Tileset.WALL;
//            case 1: return Tileset.FLOWER;
//            case 2: return Tileset.NOTHING;
//            default: return Tileset.NOTHING;
//        }
//    }
//

//    private class Point {
//        private int x;
//        private int y;
//
//        Point(int x, int y) {
//            this.x = x;
//            this.y = y;
//        }
//
//        public int getX() {
//            return x;
//        }
//        public int getY() {
//            return y;
//        }
//    }
}
