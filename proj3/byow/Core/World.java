package byow.Core;

import static byow.Core.Constants.*;
import byow.TileEngine.TETile;

import java.util.Random;

/**
 * A wrapper around the dungeon generation class BridgedWorld.
 * @author Rob Masters
 */
public class World {
    private AvatarWorld world;

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
        this.world = new AvatarWorld(width, height, rand, animate);
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

        moveAvatar();
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

    /**
     * Moves the avatar to the given point. The point must be on the pathway,
     * if it isn't the avatar doesn't move.
     * @param p destination point
     */
    public void moveAvatar(Point p) {
        world.move(p);
    }


    /**
     * Moves the avatar to a random point within the pathway. If there are no
     * points available on the pathway, the avatar doesn't move.
     */
    public void moveAvatar() {
        world.move();
    }

    /**
     * Moves the avatar one step in the given direction. The avatar will only
     * move in cardinal directions (UP, DOWN, LEFT, RIGHT) and onyl within the
     * pathway.
     * @param d direction to move in
     */
    public void moveAvatar(Direction d) {
        world.move(d);
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
