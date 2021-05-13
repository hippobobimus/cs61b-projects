package byow.Core;

import static byow.Core.Constants.*;
import byow.TileEngine.TETile;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A class that incorporates level contruction, objective placement and
 * collection, score tracking and user avatar movement.
 * @author Rob Masters
 */
public class Game {
    private GameState state;
    private GameState prevState;
    private boolean buildAnimation;
    private int score;
    private Random random;
    private NavigableTileGrid ntg;
    private LevelBuilder levelBuilder;
    private Avatar avatar;
    private Map<Point, Objective> objectives;
    private HUD hud;

    /* CONSTRUCTOR -----------------------------------------------------------*/

    /**
     * Full constructor.
     * Initial game state defaults to MAIN_MENU. Score reset to 0.
     */
    public Game() {
        this.state = GameState.MAIN_MENU;
        this.prevState = GameState.MAIN_MENU;
        this.buildAnimation = BUILD_ANIMATION_DEFAULT;
        this.score = 0;
    }

    /* PUBLIC METHODS --------------------------------------------------------*/

    /* INITIALISATION --------------------------------------------------------*/

    /**
     * Begins loading a level with an avatar and objectives. The given
     * seed affects random elements of the build process.
     * @param seed pseudo-random number generator seed
     */
    public void initialize(long seed) {
        this.random = new Random(seed);
        this.ntg = new NavigableTileGrid();
        this.levelBuilder = new LevelBuilder(ntg, random);
        this.avatar = new Avatar(ntg, random);
        this.objectives = new HashMap<>();
        this.hud = new HUD(this);

        setState(GameState.BUILDING_LEVEL);
    }

    /* LEVEL BUILDING --------------------------------------------------------*/

    /**
     * Progresses level build process. If build animation has been specified then
     * it progresses by a single step, otherwise it continues to completion.
     * @return whether load is complete
     */
    public boolean buildLevel() {
        boolean complete = false;

        // if animating build, progress by a single step else progress to
        // completion.
        if (buildAnimation) {
            complete = levelBuilder.build();
        } else {
            while (!complete) {
                complete = levelBuilder.build();
            }
        }

        if (complete) {
            positionAvatarAndObjectives();
            setState(GameState.IN_PLAY);
        }

        return complete;
    }

    /**
     * Randomly positions a number of objectives (set in the Constants class),
     * then randomly positions the user avatar on the pathway (but not on an
     * objective).
     */
    private void positionAvatarAndObjectives() {
        for (int i = 0; i < INITIAL_OBJECTIVES; i++) {
            Objective obj = new Objective(ntg, random);
            obj.move();
            objectives.put(obj.getPosition(), obj);
        }

        avatar.move(); // move to random start point.

        // ensure avatar does not start on an objective.
        while (objectives.containsKey(avatar.getPosition())) {
            avatar.move(); 
        }
    }

    /* AVATAR WRAPPER --------------------------------------------------------*/

    /**
     * Moves the user avatar one step in the given direction, if possible.
     * @param d direction of travel
     */
    public void moveAvatar(Direction d) {
        avatar.move(d);
    }

    /* UPDATING --------------------------------------------------------------*/

    /**
     * Updates the game. Either progresses the level build process if it is not
     * complete, or updates the hud and objectives if the game is in play.
     */
    public void update() {
        switch(getState()) {
            case BUILDING_LEVEL:
                buildLevel();
                break;
            case IN_PLAY:
                updateObjectives();
                hud.update();
                break;
        }
    }

    /**
     * Updates the objectives. Any that share a position with the user avatar
     * are 'taken' and the score is incremented.
     */
    public void updateObjectives() {
        Objective obj = objectives.get(avatar.getPosition());

        if (obj != null && !obj.isTaken()) {
            obj.take();
            score += 1;
        }
    }

    /**
     * Exits the program.
     */
    public void quit() {
        System.exit(0);
    }

    /* SCORING ---------------------------------------------------------------*/

    /**
     * The score.
     * @return the current score
     */
    public int getScore() {
        return score;
    }

    /* TILE FRAME ------------------------------------------------------------*/

    /**
     * Returns the current 2D tile array representing the current level state.
     * @return frame of tiles
     */
    public TETile[][] getFrame() {
        TETile[][] frame = new TETile[MAP_WIDTH][MAP_HEIGHT];

        // background
        TETile[][] background = ntg.getFrame();

        for (int i = 0; i < frame.length; i++) {
            for (int j = 0; j < frame[i].length; j++) {
                frame[i][j] = background[i][j];
            }
        }

        // Objectives
        for (Objective obj : objectives.values()) {
            Point objPos = obj.getPosition();

            frame[objPos.getX()][objPos.getY()] = obj.getTile();
        }

        // Avatar
        Point p = avatar.getPosition();

        if (p != null) {
            frame[p.getX()][p.getY()] = avatar.getTile();
        }

        return frame;
    }

    /* GAME STATE ------------------------------------------------------------*/

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

    /* ANIMATION SETTINGS ----------------------------------------------------*/

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
