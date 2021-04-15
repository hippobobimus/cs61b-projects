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

    /* CONSTRUCTOR -----------------------------------------------------------*/

    /**
     */
    public InputProcessor(Game game) {
        this.game = game;
    }

    /* PUBLIC METHODS --------------------------------------------------------*/

    /**
     */
    public void process(char c) {
        GameState state = game.getState();

        switch(state) {
            case MAIN_MENU:
                processMainMenu(c);
                break;
            case SEED_ENTRY:
                processSeedEntry(c);
                break;
            case LOADING_LEVEL:
                // IGNORE USER ENTRY
                break;
            case COMMAND_ENTRY:
                processCommand(c);
                break;
            case IN_PLAY:
                processInPlay(c);
                break;
            default:
                break;
        }
    }

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

    private long getSeed() {
        String seedStr = this.seedBuilder.toString();
        long seed = Long.parseLong(seedStr, 10);
        return seed;
    }

    private void processInPlay(char c) {
        switch(c) {
            case 'W':
                move(Direction.UP);
                history.append(c);
                break;
            case 'A':
                move(Direction.LEFT);
                history.append(c);
                break;
            case 'S':
                move(Direction.DOWN);
                history.append(c);
                break;
            case 'D':
                move(Direction.RIGHT);
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

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

    /**
     * Moves the avatar in the given direction and renders the updated world to
     * screen.
     * @param d direction of travel
     */
    private void move(Direction d) {
        game.moveAvatar(d);
    }

    /**
     * Processes any commands entered by the user. Commands are preceded by a
     * colon (':').
     *     Current commands:
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
                quit();
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
        InputSource savedInput = GameData.getInputSource();

        while (savedInput.possibleNextInput()) {
            char c = savedInput.getNextKey();
            process(c);
        }
    }

    /**
     * Ends the program and saves the current game state.
     */
    private void quit() {
        String data = history.toString();
        GameData.overwriteSaveData(data);
        System.exit(0);
    }
}
