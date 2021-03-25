package byow.Core;

import static byow.Core.Constants.*;

import java.util.Random;

public class RoomBuilder {
    private World world;
    private Random random;

    /* CONSTRUCTORS ----------------------------------------------------------*/

    /**
     * Full constructor. Creates an object with associated build methods for
     * creating rooms in 2D tilespace.
     * @param world world
     */
    public RoomBuilder(World world) {
        this.world = world;
        this.random = world.getRandom();
    }

    /* PUBLIC METHODS --------------------------------------------------------*/

    /**
     * Builds a room with random dimensions and position within the world.
     * Returns 0 if the room placement was successful, -1 otherwise.
     * @return success or fail
     */
    public int build() {
        // Random room dimensions and position.
        int w = random.nextInt(MAX_ROOM_WIDTH - MIN_ROOM_WIDTH + 1) + MIN_ROOM_WIDTH;
        int h = random.nextInt(MAX_ROOM_HEIGHT - MIN_ROOM_HEIGHT + 1) + MIN_ROOM_HEIGHT;
        int x = random.nextInt(world.getWidth() - w);
        int y = random.nextInt(world.getHeight() - h);

        Point origin = world.get(x, y);

        System.out.println("Room build attempt: width=" + w + ", height=" + h + ", " +
                origin + ".");

        // check placement is viable
        if (!areaIsEmpty(origin, w, h)) {
            return -1;
        }

        // Top and Bottom walls.
        for (int i = 0; i < w; i++) {
            world.close(x + i, y + h - 1);
            world.close(x + i, y);
        }

        // Left and Right walls.
        for (int i = 0; i < h; i++) {
            world.close(x + w - 1, y + i);
            world.close(x, y + i);
        }

        // Floor.
        Point previous = null;
        Point flr;

        for (int i = 1; i < w - 1; i++) {
            for (int j = 1; j < h - 1; j++) {
                flr = world.get(x + i, y + j);

                world.open(flr);
                world.connect(previous, flr);

                previous = flr;
            }
        }

        world.animate();

        return 0;
    }

    /**
     * Checks whether the area is empty. Point given is the bottom left corner
     * of the area.
     * @param p bottom left point
     * @param width width
     * @param height height
     * @return empty
     */
    private boolean areaIsEmpty(Point p, int width, int height) {
        int x = p.getX();
        int y = p.getY();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                boolean empty = world.isEmpty(x + i, y + j);

                if (!empty) {
                    return false;
                }
            }
        }

        return true;
    }

    /* MAIN METHOD -----------------------------------------------------------*/

    /**
     * Makes 10 attempts to build a room, rendering steps to screen.
     */
    public static void main(String[] args) {
        World world = new World(2873123, "animate");

        RoomBuilder rb = new RoomBuilder(world);

        for (int i = 0; i < 10; i++) {
            rb.build();
        }

        world.render();
    }
}
