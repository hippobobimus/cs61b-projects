package byow.Core;

import static byow.Core.Constants.*;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class RoomBuilder {
    private Grid grid;
    private Random random;
    private int attempts;
    private int maxAttempts;
    private boolean animate = false;
    private TERenderer ter;
    //private Map<Point, Edge> edgeTo;
    //private PriorityQueue<Point> fringe;
    //private Point start;

    /* CONSTRUCTORS ----------------------------------------------------------*/

    /**
     * Full constructor. Creeates an object with associated build methods for
     * creating rooms in 2D tilespace. If "animate" is specified a TERenderer
     * must also be provided.
     * @param grid grid
     * @param random pseudo-random number generator
     * @param maxIterations max iterations when building a maze
     * @param animate animate?
     * @param ter renderer
     */
    public RoomBuilder(Grid grid, Random random, int maxAttempts, String animate, TERenderer ter) {
        this.grid = grid;
        this.random = random;
        this.maxAttempts= maxAttempts;
        this.animate = animate.equals("animate") ? true : false;
        this.ter = ter;

        if (this.animate && ter == null) {
            throw new IllegalArgumentException("When animating, a TERenderer" +
                    "must be provided.");
        }

        // Compare Points in PQ by their priority field.
        //Comparator<Point> cmp = (a, b) -> b.getPriority() - a.getPriority();
        //fringe = new PriorityQueue<>(cmp);
        //edgeTo = new HashMap<>();
    }

    public void buildRooms() {
        for (int i = 0; i < maxAttempts; i++) {
            Region result = build();
            System.out.println("  >>> " + (result != null ? "SUCCESS" : "FAILED"));
            //animateStep();
        }
    }

    public Region build() {
        // Random room dimensions and position.
        int w = random.nextInt(MAX_ROOM_WIDTH - MIN_ROOM_WIDTH + 1) + MIN_ROOM_WIDTH;
        int h = random.nextInt(MAX_ROOM_HEIGHT - MIN_ROOM_HEIGHT + 1) + MIN_ROOM_HEIGHT;
        int x = random.nextInt(WIDTH - w);
        int y = random.nextInt(HEIGHT - h);
        Point origin = grid.get(x, y);

        System.out.println("Room build attempt: width=" + w + ", height=" + h + ", " +
                origin + ".");

        if (!areaIsEmpty(origin, w, h)) {
            return null;
        }

        //Room room = new Room(5, 7);

        Region region = new Region();
        Point t, b, l, r, flr;
        // Top and Bottom walls.
        for (int i = 0; i < w; i++) {
            t = grid.get(x + i, y + h - 1);
            b = grid.get(x + i, y);
            grid.setTile(t, Tileset.WALL);
            grid.setTile(b, Tileset.WALL);
            region.add(t);
            region.add(b);
        }
        // Left and Right walls.
        for (int i = 0; i < h; i++) {
            r = grid.get(x + w - 1, y + i);
            l = grid.get(x, y + i);
            grid.setTile(r, Tileset.WALL);
            grid.setTile(l, Tileset.WALL);
            region.add(r);
            region.add(l);
        }
        // Floor.
        for (int i = 1; i < w - 1; i++) {
            for (int j = 1; j < h - 1; j++) {
                flr = grid.get(x + i, y + j);
                grid.open(flr);
                grid.setTile(flr, Tileset.FLOOR);
            }
        }
        animateStep();
        return region;
    }

//    private boolean areaIsEmpty(Point origin, int width, int height) {
//        int x = origin.getX();
//        int y = origin.getY();
//        return areaIsEmpty(x, y, width, height);
//    }

    private boolean areaIsEmpty(Point p, int width, int height) {
        int x = p.getX();
        int y = p.getY();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                TETile t = grid.get(x + i, y + j).getTile();
                if (!t.equals(Tileset.NOTHING)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        Grid g = new Grid();

        Random rand = new Random(2873123);

        RoomBuilder rb = new RoomBuilder(g, rand, 30, "animate", ter);

        rb.buildRooms();

        ter.renderFrame(g.getTiles());

        // maze
        MazeBuilder mb = new MazeBuilder(g, rand, "animate", ter);

        Point start = g.get(1, 1);
        mb.buildMaze(start);

        ter.renderFrame(g.getTiles());
    }

    /**
     * Updates the current tile state to screen and pauses for 10ms.
     */
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


    private class Room implements Element {
        private int width;
        private int height;
        private TETile[][] tiles;
    
        /** Constructs a rectangular Room with the given dimensions. */
        Room(int width, int height) {
            validateDimensions(width, height);
            this.width = width;
            this.height = height;
            this.tiles = generateTiles();
        }
    
        public TETile[][] tiles() {
            return tiles;
        }
    
        public int getWidth() {
            return width;
        }
    
        public int getHeight() {
            return height;
        }
    
        /** HELPER METHODS */
    
        /** Checks that the given width and height are within the allowable min/max
          * bounds and throws an exception if they are not. */
        private void validateDimensions(int w, int h) {
            if (w > MAX_ROOM_WIDTH || w < MIN_ROOM_WIDTH) {
                throw new IllegalArgumentException("Room(): given width " +
                        w + "outside of allowed range (" + MIN_ROOM_WIDTH +
                        " to " + MAX_ROOM_WIDTH + ").");
            }
            if (h > MAX_ROOM_HEIGHT || h < MIN_ROOM_HEIGHT) {
                throw new IllegalArgumentException("Room(): given height " +
                        w + "outside of allowed range (" + MIN_ROOM_HEIGHT +
                        " to " + MAX_ROOM_HEIGHT + ").");
            }
        }
    
        /** Generates an array of tiles representing the walls and floor of the 
          * Room. */
        private TETile[][] generateTiles() {
            TETile[][] t = new TETile[width][height];
            // Top and Bottom walls.
            for (int i = 0; i < this.getWidth(); i++) {
                t[i][this.getHeight() - 1] = Tileset.WALL;
                t[i][0] = Tileset.WALL;
            }
            // Left and Right walls.
            for (int i = 0; i < this.getHeight(); i++) {
                t[this.getWidth() - 1][i] = Tileset.WALL;
                t[0][i] = Tileset.WALL;
            }
            // Fill floor.
            for (int i = 1; i < this.getWidth() - 1; i++) {
                for (int j = 1; j < this.getHeight() - 1; j++) {
                    t[i][j] = Tileset.FLOOR;
                }
            }
            return t;
        }
    }
}
