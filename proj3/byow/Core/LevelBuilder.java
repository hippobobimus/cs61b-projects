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
    private boolean roomsComplete;
    private boolean mazeComplete;
    private boolean bridgesComplete;
    private boolean pruningComplete;

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

        this.roomsComplete = false;
        this.mazeComplete = false;
        this.bridgesComplete = false;
        this.pruningComplete = false;
    }

    /* PUBLIC METHODS --------------------------------------------------------*/

    /**
     * Progresses the build process of a new randomly generated level within the
     * navigable tile grid by a single step.
     * The process itself depends on parameters set in the Constants class.
     * @return whether the level build is complete
     */
    public boolean build() {
        if (!roomsComplete) {
            roomsComplete = roomBuilder.build();
        } else if (!mazeComplete) {
            mazeComplete = mazeBuilder.build();
        } else if (!bridgesComplete) {
            bridgesComplete = bridgeBuilder.build();
        } else if (!pruningComplete) {
            pruningComplete = mazeBuilder.reduceDeadEnds();
        }

        return isComplete();
    }

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

    /**
     * Determines whether the level build process is fully complete.
     * @return whether the level build is finished
     */
    private boolean isComplete() {
        return roomsComplete && mazeComplete && bridgesComplete
            && pruningComplete;
    }

    /* MAIN METHOD -----------------------------------------------------------*/

    /**
     * Builds a new level within the available grid area, rendering to screen.
     */
    public static void main(String[] args) {
        NavigableTileGrid grid = new NavigableTileGrid(
                WINDOW_WIDTH, WINDOW_HEIGHT);

        TERenderer ter = new TERenderer();
        ter.initialize(WINDOW_WIDTH, WINDOW_HEIGHT);

        Random rand = new Random(2873123);

        LevelBuilder lb = new LevelBuilder(grid, rand);

        boolean done = false;
        while (!done) {
            done = lb.build();
            ter.renderFrame(grid.getFrame()); // animated build
        }
    }
}
