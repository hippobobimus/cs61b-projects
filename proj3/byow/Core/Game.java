package byow.Core;

import byow.TileEngine.TETile;

// TODO Merge Game and World classes.
/**
 * @author Rob Masters
 */
public class Game {
    private World world;
    private GameState state;
    private GameState prevState;
    private boolean buildAnimation;

    /* CONSTRUCTOR -----------------------------------------------------------*/

    public Game() {
        this.world = new World();
        this.state = GameState.MAIN_MENU;
        this.prevState = GameState.MAIN_MENU;
        this.buildAnimation = false;
    }

    /* PUBLIC METHODS --------------------------------------------------------*/

    /**
     * Builds a level a begins gameplay. The given seed value is the basis for
     * the random level generation.
     * @param seed seed for pseudo-random number generation
     */
    public void initialize(long seed) {
        //setState(GameState.IN_PLAY);

        this.world.initialize(seed, this.buildAnimation);

        setState(GameState.LOADING_LEVEL);
        //this.world.build();
    }

    public void loadLevel() {
        boolean complete = advanceLoadLevel();

        if (complete) {
            setState(GameState.IN_PLAY);
        }
    }

    /**
     */
    private boolean advanceLoadLevel() {
        if (this.buildAnimation) {
            return this.world.animatedBuild();
        } else {
            return this.world.build();
        }
    }

    /**
     * Moves the user avatar one step in the given direction, if possible.
     * @param d direction of travel
     */
    public void moveAvatar(Direction d) {
        world.moveAvatar(d);
    }

    /**
     * Returns the current 2D tile array representing the world state.
     * @return frame of tiles
     */
    public TETile[][] getFrame() {
        return world.getFrame();
    }

    /**
     * Returns the current game state.
     * @return game state
     */
    public GameState getState() {
        return state;
    }

    /**
     * Returns the game state preceding the current one.
     * @return previous game state
     */
    public GameState getPreviousState() {
        return prevState;
    }

    /**
     * Sets the game state.
     * @param s game state
     */
    public void setState(GameState s) {
        this.prevState = this.state;
        this.state = s;
    }

    /**
     * Enables animation during the level build process.
     */
    public void enableBuildAnimation() {
        this.buildAnimation = true;
    }

    /**
     * Disables animation during the level build process.
     */
    public void disableBuildAnimation() {
        this.buildAnimation = false;
    }

    /**
     * Toggles animation during the level build process.
     */
    public void toggleBuildAnimation() {
        this.buildAnimation = !this.buildAnimation;
    }

    /**
     * Returns the current animation setting.
     * @return animation setting
     */
    public boolean getAnimationSetting() {
        return this.buildAnimation;
    }
}
