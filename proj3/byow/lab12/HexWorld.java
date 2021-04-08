package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;
import java.lang.IllegalArgumentException;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private TETile[][] tiles;

    HexWorld() {
        tiles = new TETile[WIDTH][HEIGHT];
        fillWithEmptyTiles();
    }

    public TETile[][] tiles() {
        return tiles;
    }

    /** Draws the world to screen. */
    public void draw() {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(tiles);
    }

    private void addRow(int x, int y, int length, TETile tile) {
        for (int i = 0; i < length; i++) {
            tiles[x + i][y] = tile;
        }
    }

    private int rowOffset(int row, int s) {
        if (row < s) {
            return s - 1 - row;
        }
        return row - s;
    }

    private int rowLength(int row, int s) {
        if (row < s) {
            return s + 2 * row;
        }
        return 5 * s - 2 - 2 * row;
    }

    /** Adds a hexagon of side length s to the given position in the world. */
    public void addHexagon(int x, int y, int s, TETile tile) {
        if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT ) {
            throw new IllegalArgumentException("addHexagon(): coordinates out of bounds.");
        }
        if (tile == null) {
            tile = Tileset.NOTHING;
        }

        int start, length;
        for (int row = 0; row < 2 * s; row++) {
            start = x + rowOffset(row, s);
            length = rowLength(row, s);
            addRow(start, y + row, length, tile);
        }
    }

    /** HELPER METHODS */

    private void fillWithEmptyTiles() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }


    /** END OF HELPER METHODS */

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        HexWorld hw = new HexWorld();
        hw.addHexagon(0, 0, 4, Tileset.FLOWER);
        hw.addHexagon(11, 0, 3, Tileset.FLOWER);
        hw.addHexagon(19, 0, 2, Tileset.FLOWER);

        ter.renderFrame(hw.tiles());
    }


//
//
//    private static final long SEED = 2873123;
//    private static final Random RANDOM = new Random(SEED);
//
//    /**
//     * Fills the given 2D array of tiles with RANDOM tiles.
//     * @param tiles
//     */
//    public static void fillWithRandomTiles(TETile[][] tiles) {
//        int height = tiles[0].length;
//        int width = tiles.length;
//        for (int x = 0; x < width; x += 1) {
//            for (int y = 0; y < height; y += 1) {
//                tiles[x][y] = randomTile();
//            }
//        }
//    }
//
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
//    private static class World {
//        private TETile[][] tiles;
//
//        World() {
//            tiles = new TETile[WIDTH][HEIGHT];
//            fillWithEmptyTiles();
//        }
//
//        public TETile[][] tiles() {
//            return tiles;
//        }
//
//        /** Adds a hexagon of side length s to the given position in the world. */
//        public void addHexagon(int x, int y, int s) {
//        }
//
//        private void fillWithEmptyTiles() {
//            for (int x = 0; x < WIDTH; x++) {
//                for (int y = 0; y < HEIGHT; y++) {
//                    tiles[x][y] = randomTile();
//                }
//            }
//        }
//    }
}
