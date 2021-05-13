package byow.Core;

import byow.TileEngine.TETile;

import edu.princeton.cs.introcs.StdDraw;

/**
 * Processes incoming input from a given input source and updates the game
 * object accordingly.
 * @author Rob Masters
 */
public class InputProcessor {
    private Game game;
    private StringBuilder history = new StringBuilder();
    private StringBuilder seedBuilder = new StringBuilder();

    private InputSource inputBuffer;
    private InputSource input;

    /* CONSTRUCTOR -----------------------------------------------------------*/

    /**
     * Full constructor.
     * @param game game object
     */
    public InputProcessor(Game game) {
        this.game = game;
    }

    /* PUBLIC METHODS --------------------------------------------------------*/

    /**
     * Sets the input source for this processor.
     * @param input input source
     */
    public void initialize(InputSource input) {
        this.input = input;
    }

    /**
     * Looks for input first in the buffer and then in the designated
     * input source, processing one input character (if available).
     */
    public void process() {
        // Don't process input while the level is being built.
        if (game.getState().equals(GameState.BUILDING_LEVEL)) {
            return;
        }

        // Exhaust the input buffer before processing user input.
        if (inputBuffer != null && inputBuffer.possibleNextInput()) {
            process(inputBuffer.getNextKey());
        } else if (input.hasNextKey()) {
            char c = input.getNextKey();
            process(c);
        }
    }

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

    /**
     * Dispatches input in different ways depending on the current game state.
     * @param c input character
     */
    private void process(char c) {
        GameState state = game.getState();

        switch(state) {
            case IN_PLAY:
                processInPlay(c);
                break;
            case MAIN_MENU:
                processMainMenu(c);
                break;
            case SEED_ENTRY:
                processSeedEntry(c);
                break;
            case BUILDING_LEVEL:
                // IGNORE USER ENTRY
                break;
            case LOADING_GAME:
                // IGNORE USER ENTRY
                break;
            case COMMAND_ENTRY:
                processCommand(c);
                break;
            default:
                break;
        }
    }

    /**
     * Processes input while the game state is IN_PLAY.
     */
    private void processMainMenu(char c) {
        switch(c) {
            case 'L':
                loadGame();
                break;
            case 'N':
                game.setState(GameState.SEED_ENTRY);
                this.history.append(c);
                break;
            case ':':
                game.setState(GameState.COMMAND_ENTRY);
                break;
            default:
                break;
        }
    }

    /**
     * Processes input while the game state is SEED_ENTRY.
     */
    private void processSeedEntry(char c) {
        switch(c) {
            case 'S':
                this.history.append(c);
                game.initialize(getSeed());
                break;
            default:
                this.seedBuilder.append(c);
                this.history.append(c);
                break;
        }
    }

    /**
     * Takes the stored seed in the form of a StringBuilder and returns it
     * as a long.
     * @return seed
     */
    private long getSeed() {
        String seedStr = this.seedBuilder.toString();
        long seed = Long.parseLong(seedStr, 10);
        return seed;
    }

    /**
     * Processes input while the game state is IN_PLAY.
     */
    private void processInPlay(char c) {
        switch(c) {
            case 'W':
                game.moveAvatar(Direction.UP);
                history.append(c);
                break;
            case 'A':
                game.moveAvatar(Direction.LEFT);
                history.append(c);
                break;
            case 'S':
                game.moveAvatar(Direction.DOWN);
                history.append(c);
                break;
            case 'D':
                game.moveAvatar(Direction.RIGHT);
                history.append(c);
                break;
            case 'H':
                // TODO toggle help
                break;
            case ':':
                game.setState(GameState.COMMAND_ENTRY);
                break;
            default:
                break;
        }
    }

    /**
     * Processes any commands entered by the user while the game state is
     * COMMAND_ENTRY. Commands are preceded by a colon (':').
     *     Current commands:
     *         'A' = toggle animation of level build
     *         'R' = return to previous game state
     *         'Q' = quit
     * Does nothing if the given character doesn't correspond to a command.
     * @param input input source
     */
    private void processCommand(char c) {
        // These inputs are not stored to the history.
        switch(c) {
            case 'A':
                game.toggleBuildAnimation();
                break;
            case 'Q':
                saveGame();
                game.quit();
                break;
            case 'R':
                game.setState(game.getPreviousState());
                break;
            default:
                break;
        }
    }

    /**
     * Loads the previous game state.
     */
    private void loadGame() {
        System.out.println("Loading...");
        InputSource savedInput = GameData.getInputSource();

        this.inputBuffer = savedInput;
    }

    /**
     * Saves the current game state.
     */
    private void saveGame() {
        String data = history.toString();
        GameData.overwriteSaveData(data);
    }
}
