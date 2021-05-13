package byow.Core;

import static byow.Core.Constants.*;
import byow.TileEngine.TERenderer;

import java.util.Random;

/**
 * Provides random room build and placement functionality within a given
 * navigable tile grid.
 * @author Rob Masters
 */
public class RoomBuilder {
    private Random random;
    private NavigableTileGrid ntg;
    private int attempt;

    /* CONSTRUCTORS ----------------------------------------------------------*/

    /**
     * Full constructor. Creates an object with associated build methods for
     * creating rooms in a navigable tile grid.
     * @param ntg navigable tile grid
     * @param random pseudorandom number generator
     */
    public RoomBuilder(NavigableTileGrid ntg, Random random) {
        this.ntg = ntg;
        this.random = random;
        this.attempt = 0;
    }

    /* PUBLIC METHODS --------------------------------------------------------*/

    /**
     * Progresses the build process of a new randomly generated series of rooms
     * within the navigable tile grid by a single step. Here a single step is
     * one attempt to build a room. The process itself is limited by the maximum
     * number of attempts set in the Constants class.
     * @return whether the build has reached max attempts
     */
    public boolean build() {
        if (!isComplete()) {
            buildRoom();
        }

        return isComplete();
    }

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

    /**
     * Determines whether the build process has progressed to its limit.
     * @return whether build is complete
     */
    private boolean isComplete() {
        return this.attempt >= ROOM_PLACEMENT_ATTEMPTS;
    }

    /**
     * Builds a room with random dimensions and position within the grid,
     * referencing limits set in the Constants class.
     * @return success or fail
     */
    private int buildRoom() {
        // Random room dimensions and position.
        int w = random.nextInt(MAX_ROOM_WIDTH - MIN_ROOM_WIDTH + 1) + MIN_ROOM_WIDTH;
        int h = random.nextInt(MAX_ROOM_HEIGHT - MIN_ROOM_HEIGHT + 1) + MIN_ROOM_HEIGHT;
        int x = random.nextInt(ntg.getWidth() - w);
        int y = random.nextInt(ntg.getHeight() - h);

        return buildRoom(w, h, x, y);
    }

    /**
     * Full method for building a room with the given grid position and
     * dimensions.
     * Returns 0 if the room placement was successful, -1 otherwise.
     * If the points where the room's floor will be contain any pathway then the
     * attempt will fail. The attempt will also fail if the proposed room
     * position is outside the grid boundary.
     * @return success or fail
     */
    private int buildRoom(int w, int h, int x, int y) {
        attempt++; // increment attempts tracker

        Point origin = ntg.get(x, y);

        // check placement is viable.
        if (!areaIsEmpty(origin, w, h)) {
            return -1;
        }

        // Top and Bottom walls.
        for (int i = 0; i < w; i++) {
            ntg.closePath(x + i, y + h - 1);
            ntg.closePath(x + i, y);
        }

        // Left and Right walls.
        for (int i = 0; i < h; i++) {
            ntg.closePath(x + w - 1, y + i);
            ntg.closePath(x, y + i);
        }

        // Floor.
        for (int i = 1; i < w - 1; i++) {
            for (int j = 1; j < h - 1; j++) {
                ntg.openPath(x + i, y + j);
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
                if (ntg.isPath(x + i, y + j)) {
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
        NavigableTileGrid grid = new NavigableTileGrid(
                WINDOW_WIDTH, WINDOW_HEIGHT);

        TERenderer ter = new TERenderer();
        ter.initialize(WINDOW_WIDTH, WINDOW_HEIGHT);

        Random rand = new Random(2873123);

        RoomBuilder rb = new RoomBuilder(grid, rand);

        boolean done = false;
        while (!done) {
            done = rb.build();
            ter.renderFrame(grid.getFrame()); // animated build.
        }
    }
}
