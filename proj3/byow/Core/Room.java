package byow.Core;
//import org.junit.Test;
//import static org.junit.Assert.*;

import static byow.Core.Constants.*;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.lang.IllegalArgumentException;

public class Room implements Element {
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
