package byow.Core;

import static byow.Core.Constants.*;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import java.util.Random;

/**
 * A world comprised of a navigable tile grid in which levels can be built and
 * a user controlled avatar can be moved.
 * @author Rob Masters
 */
public class World {
    private Random random;
    private NavigableTileGrid ntg;
    private LevelBuilder levelBuilder;
    private Avatar avatar;

    /* CONSTRUCTORS ----------------------------------------------------------*/

    /* PUBLIC METHODS --------------------------------------------------------*/

    /**
     * Full initializer for a 2D world with associated build method. The given
     * seed determines the result of the random build process. Building can be
     * animated by supplying the "animate" string.
     * @param seed pseudo-random number generator seed
     * @param animate animation/no animation // TODO
     */
    public void initialize(long seed, boolean animate) {
        this.random = new Random(seed);
        this.ntg = new NavigableTileGrid(MAP_WIDTH, MAP_HEIGHT);
        this.levelBuilder = new LevelBuilder(ntg, random);
        this.avatar = new Avatar(ntg, random);
    }

    /**
     * Initializer without animate parameter. Defaults to no animation.
     * @param seed pseudo-random number generator seed
     */
    public void initialize(long seed) {
        initialize(seed, false);
    }

    /**
     * Populates the world with a randomly generated connected dungeon comprised
     * of rooms and tunnels, placing the player avatar in a random position on
     * the pathway.
     */
    public boolean build() {
        levelBuilder.build();

        avatar.move(); // to random start point.

        return true;
    }

    /**
     */
    public boolean animatedBuild() {
        boolean complete = levelBuilder.animatedBuild();
        if (complete) {
            avatar.move(); // to random start point.
        }

        return complete;
    }

    /**
     * Returns the current tile frame.
     * @return 2D tile array
     */
    public TETile[][] getFrame() {
        return ntg.getFrame();
    }

    /* AVATAR WRAPPER --------------------------------------------------------*/

    /**
     * Moves the avatar to a random point on the pathway. If there are no points
     * available on the pathway, the avatar doesn't move.
     */
    public void moveAvatar() {
        avatar.move();
    }

    /**
     * Moves the avatar to the given point. The point must be on the pathway,
     * if it isn't the avatar doesn't move.
     * @param p destination point
     */
    public void moveAvatar(Point p) {
        avatar.move(p);
    }

    /**
     * Moves the avatar one step in the given direction. The avatar will only
     * move in cardinal directions (UP, DOWN, LEFT, RIGHT) and only within the
     * pathway.
     * @param d direction to move in
     */
    public void moveAvatar(Direction d) {
        avatar.move(d);
    }

    /* MAIN METHOD -----------------------------------------------------------*/

    /**
     * Builds a new world and renders it to screen.
     */
    public static void main(String[] args) {
        World w = new World();
        w.initialize(2873123L);
        w.build();
        TERenderer ter = new TERenderer();
        ter.initialize(WINDOW_WIDTH, WINDOW_HEIGHT);
        ter.renderFrame(w.getFrame());
    }
}

