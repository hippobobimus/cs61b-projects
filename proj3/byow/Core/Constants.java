package byow.Core;

/**
 * Collection of fixed constant values used throughout the package.
 * @author Rob Masters
 */
public class Constants {
    // Dimensions of the gameplay area in tiles.
    public static final int MAP_WIDTH = 50;
    public static final int MAP_HEIGHT = 50;

    // Height of the top HUD bar in tiles.
    public static final int HUD_HEIGHT = 2;
    // Origin of text in the HUD, in tile units.
    public static final int HUD_ORIGIN_X = 0;
    public static final int HUD_ORIGIN_Y = MAP_HEIGHT + 1;

    // Dimensions of the window in tiles.
    public static final int WINDOW_WIDTH = MAP_WIDTH;
    public static final int WINDOW_HEIGHT = (MAP_HEIGHT + HUD_HEIGHT);

    // Dimensions of the window in pixels.
    public static final int WINDOW_WIDTH_PX = WINDOW_WIDTH * 16;
    public static final int WINDOW_HEIGHT_PX = WINDOW_HEIGHT * 16;

    // Element dimension limits.
    public static final int MIN_ROOM_WIDTH = 4;
    public static final int MAX_ROOM_WIDTH = 10;
    public static final int MIN_ROOM_HEIGHT = 4;
    public static final int MAX_ROOM_HEIGHT = 10;

    // Center of window.
    public static final int CENTER_X = WINDOW_WIDTH / 2;
    public static final int CENTER_Y = WINDOW_HEIGHT / 2;

    // Saving
    public static final String SAVE_FILE_PATH = "data.txt";

    // Level building.
    public static final int ROOM_PLACEMENT_ATTEMPTS = 40;
    public static final int MAZE_ITERATION_LIMIT = -1; // negative = no limit.
    public static final double EXTRA_BRIDGE_PROBABILITY = 0.1;
    public static final int DEAD_END_PRUNING_STEPS = 20;
    public static final int INITIAL_OBJECTIVES = 10;

    // Settings defaults
    public static final boolean BUILD_ANIMATION_DEFAULT = true;

}
