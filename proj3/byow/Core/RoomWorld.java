package byow.Core;

import static byow.Core.Constants.*;

import java.util.Random;

/**
 * Adds random room build and placement functionality to the world.
 * @author Rob Masters
 */
public class RoomWorld extends NavigableTileGrid {
    public Random random;

    /* CONSTRUCTORS ----------------------------------------------------------*/

    /**
     * Full constructor. Creates an object with associated build methods for
     * creating rooms in 2D tilespace.
     * @param width width
     * @param height height
     * @param random pseudorandom number generator
     * @param animate animation/no animation
     */
    public RoomWorld(int width, int height, Random random, String animate) {
        super(width, height, animate);
        this.random = random;
    }

    /* PUBLIC METHODS --------------------------------------------------------*/

    /**
     * Makes the given number of attempts to place a new randomly generated room
     * into the grid. If the points where the room's floor will be contain any
     * pathway then the attempt will fail. The attempt will also fail if the
     * proposed room position is outside the grid boundary.
     * @param attempts room placement attempts
     */
    public void buildRooms(int attempts) {
        for (int i = 0; i < attempts; i++) {
            build();
            animate();
        }
    }

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

    /**
     * Builds a room with random dimensions and position within the world.
     * Returns 0 if the room placement was successful, -1 otherwise.
     * @return success or fail
     */
    private int build() {
        // Random room dimensions and position.
        int w = random.nextInt(MAX_ROOM_WIDTH - MIN_ROOM_WIDTH + 1) + MIN_ROOM_WIDTH;
        int h = random.nextInt(MAX_ROOM_HEIGHT - MIN_ROOM_HEIGHT + 1) + MIN_ROOM_HEIGHT;
        int x = random.nextInt(getWidth() - w);
        int y = random.nextInt(getHeight() - h);

        return build(w, h, x, y);
    }

    /**
     * Builds a room with the given grid position and dimensions.
     * Returns 0 if the room placement was successful, -1 otherwise.
     * @return success or fail
     */
    private int build(int w, int h, int x, int y) {
        Point origin = get(x, y);

        // check placement is viable
        if (!areaIsEmpty(origin, w, h)) {
            return -1;
        }

        // Top and Bottom walls.
        for (int i = 0; i < w; i++) {
            closePath(x + i, y + h - 1);
            closePath(x + i, y);
        }

        // Left and Right walls.
        for (int i = 0; i < h; i++) {
            closePath(x + w - 1, y + i);
            closePath(x, y + i);
        }

        // Floor.
        for (int i = 1; i < w - 1; i++) {
            for (int j = 1; j < h - 1; j++) {
                openPath(x + i, y + j);
            }
        }

        return 0;
    }

    /**
     * Checks whether any points in the given area are on the pathway. Point
     * given is the bottom left corner of the area.
     * @param p bottom left point
     * @param width width
     * @param height height
     * @return empty?
     */
    private boolean areaIsEmpty(Point p, int width, int height) {
        int x = p.getX();
        int y = p.getY();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (isPath(x + i, y + j)) {
                    return false;
                }
            }
        }

        return true;
    }

    /* MAIN METHOD -----------------------------------------------------------*/

    /**
     * Makes 40 attempts to build a room, rendering steps to screen.
     */
    public static void main(String[] args) {
        Random rand = new Random(2873123);

        RoomWorld rw = new RoomWorld(WIDTH, HEIGHT, rand, "animate");

        rw.buildRooms(40);

        rw.render();
    }
}
