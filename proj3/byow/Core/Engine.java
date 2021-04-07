package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.introcs.StdDraw;

public class Engine {
    private World world;
    private boolean gameInProgress = false;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        InputSource inputSource = new KeyboardInputSource();

        processInput(inputSource);
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        InputSource inputSource = new StringInputDevice(input);

        processInput(inputSource);

        TETile[][] finalWorldFrame = world.tiles();
        return finalWorldFrame;
    }

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

    /**
     * Takes an input source, parsing and processing all available input from it.
     * @param input input source
     */
    private void processInput(InputSource input) {
        while (input.possibleNextInput()) {
            char c = input.getNextKey();
            switch(c) {
                case 'N':
                    if (!gameInProgress) {
                        newGame(input);
                    }
                    break;
                case 'L':
                    if (!gameInProgress) {
                        loadGame();
                    }
                    break;
                case ':':
                    c = input.getNextKey();
                    processCommand(c);
                    break;
                default:
                    break;
            }
        }
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
        if (c == 'Q') {
            quit();
        }
    }

    /**
     * Interrogates the input source for the user entered seed value and then
     * generates a new world, rendering it to screen.
     * @param input input source
     */
    private void newGame(InputSource input) {
        this.gameInProgress = true;

        if (input.type().equals(InputType.KEYBOARD)) {
            StdDraw.clear();
            StdDraw.text(0.5, 0.5, "Enter seed value and type 'S' to confirm.");
        }

        String temp = "";

        char c;
        while (input.possibleNextInput()) {
            c = input.getNextKey();
            if (c == 'S') {
                break;
            } else if (Character.isDigit(c)) {
                temp += c;
            } else {
                throw new IllegalArgumentException("The input contains an" +
                        "invalid seed.");
            }
        }

        long seed = Long.parseLong(temp, 10);

        this.world = new World(seed);
        world.build();
        world.render();
    }

    /**
     * Loads the previous game state.
     */
    private void loadGame() {
        // TODO
    }

    /**
     * Ends the program and saves the current game state.
     */
    private void quit() {
        // TODO Save game state
        System.exit(0);
    }

    /* MAIN METHOD -----------------------------------------------------------*/

    public static void main(String[] args) {
        Engine eng = new Engine();
        //eng.interactWithInputString("n12345s");
        eng.interactWithKeyboard();
    }
}
