package byow.Core;

import static byow.Core.Constants.*;
import byow.TileEngine.TERenderer;

import java.util.Random;

/**
 * A class for constructing new levels within a navigable tile grid. Levels
 * consist of a fully connected combination of rooms and corridors, and are
 * randomly generated using a pseudo-random number generator based upon a given
 * seed value.
 * @author Rob Masters
 */
public class LevelBuilder {
    private NavigableTileGrid ntg;
    private Random random;
    private RoomBuilder roomBuilder;
    private MazeBuilder mazeBuilder;
    private BridgeBuilder bridgeBuilder;
    private boolean roomBuildComplete;
    private boolean mazeBuildComplete;
    private boolean bridgeBuildComplete;

    /* CONSTRUCTOR -----------------------------------------------------------*/

    /**
     * Full constructor. Any levels built will be held within the given
     * navigable tile grid and the build process' random elements will depend
     * upon the given pseudo-random number generator.
     * @param ntg navigable tile grid
     * @param random pseudo-random number generator
     */
    public LevelBuilder(NavigableTileGrid ntg, Random random) {
        this.ntg = ntg;
        this.random = random;

        this.roomBuilder = new RoomBuilder(ntg, random);
        this.mazeBuilder = new MazeBuilder(ntg, random);
        this.bridgeBuilder = new BridgeBuilder(ntg, random);

        this.roomBuildComplete = false;
    }

    /* PUBLIC METHODS --------------------------------------------------------*/

    /**
     * Randomly generates a new level within the navigable tile grid. The build
     * process can be altered by setting values within the Constants class.
     */
    public void build() {
        roomBuilder.build(ROOM_PLACEMENT_ATTEMPTS);
        mazeBuilder.build(MAX_MAZE_BUILD_STEPS);
        bridgeBuilder.build(EXTRA_BRIDGE_PROBABILITY);
        mazeBuilder.reduceDeadEnds(DEAD_END_PRUNING_STEPS);
    }

    public boolean animatedBuild() {
        if (!roomBuildComplete) {
            this.roomBuildComplete = roomBuilder.animatedBuild();
            return false;
        } else {
            mazeBuilder.build(MAX_MAZE_BUILD_STEPS);
            bridgeBuilder.build(EXTRA_BRIDGE_PROBABILITY);
            mazeBuilder.reduceDeadEnds(DEAD_END_PRUNING_STEPS);
        }
        return true;
    }

    /* MAIN METHOD -----------------------------------------------------------*/

    /**
     * Builds a new level within the available grid area, rendering to screen.
     */
    public static void main(String[] args) {
        NavigableTileGrid grid = new NavigableTileGrid(
                WINDOW_WIDTH, WINDOW_HEIGHT);

        Random rand = new Random(2873123);

        LevelBuilder lb = new LevelBuilder(grid, rand);

        lb.build();

        TERenderer ter = new TERenderer();
        ter.initialize(WINDOW_WIDTH, WINDOW_HEIGHT);
        ter.renderFrame(grid.getFrame());
    }
}
