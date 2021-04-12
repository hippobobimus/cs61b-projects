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

    // Saving
    public static final String SAVE_FILE_PATH = "data.txt";
}
