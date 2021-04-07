package byow.Core;

import static byow.Core.Constants.*;
import byow.TileEngine.TETile;

import java.util.Random;

/**
 * A wrapper around the dungeon generation class BridgedWorld.
 * @author Rob Masters
 */
public class World {
    private BridgedWorld world;

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
        Random rand = new Random(seed);
        this.world = new BridgedWorld(width, height, rand, animate);
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

    /* PUBLIC METHODS --------------------------------------------------------*/

    /**
     * Populates the world with a randomly generated connected dungeon comprised
     * of rooms and tunnels.
     */
    public void build() {
        int roomPlacementAttempts = 40;
        int maxMazeAlgoIterations = -1;
        double probExtraConnections = 0.1;
        int deadEndPruningSteps = 20;

        world.buildRooms(roomPlacementAttempts);
        world.mazeFill(maxMazeAlgoIterations);
        world.bridgeRegions(probExtraConnections);
        world.reduceDeadEnds(deadEndPruningSteps);
    }

    /**
     * Renders the current world state to screen.
     */
    public void render() {
        world.render();
    }

    /**
     * Returns the current tile frame.
     * @return 2D tile array
     */
    public TETile[][] tiles() {
        return world.getTiles();
    }

    /* MAIN METHOD -----------------------------------------------------------*/

    /**
     * Builds a new world and renders it to screen.
     */
    public static void main(String[] args) {
        //World world = new World(2873123, "animate");
        World w = new World(2873123);
        w.build();
        w.render();
    }
}
