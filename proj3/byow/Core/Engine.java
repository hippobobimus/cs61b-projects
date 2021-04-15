package byow.Core;

import static byow.Core.Constants.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.introcs.StdDraw;

public class Engine {
    private Game game = new Game();
    private TERenderer ter = new TERenderer();
    private InputProcessor inputProcessor = new InputProcessor(this.game);
    private InputSource input;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        this.input = new KeyboardInputSource();

        ter.initialize(WINDOW_WIDTH, WINDOW_HEIGHT);
        StdDraw.setPenColor(255, 255, 255);

        gameLoop();
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
        this.input = new StringInputDevice(input);

        while (this.input.possibleNextInput()) {
            processInput();
        }

        TETile[][] finalWorldFrame = game.getFrame();
        return finalWorldFrame;
    }

    private void gameLoop() {
        while (input.possibleNextInput()) {
            processInput();
            update();
            render();

            StdDraw.pause(16);
        }
    }

    private void processInput() {
        if (input.hasNextKey()) {
            char c = input.getNextKey();
            inputProcessor.process(c);
        }
    }

    private void update() {
        switch(game.getState()) {
            case LOADING_LEVEL:
                game.loadLevel();
                break;
        }
    }

    private void render() {
        StdDraw.clear(Color.BLACK);

        TETile[][] frame;

        switch (game.getState()) {
            case MAIN_MENU:
                drawStartMenu();
                break;
            case SEED_ENTRY:
                drawSeedEntryScreen();
                break;
            case LOADING_LEVEL:
                frame = game.getFrame();
                ter.renderFrame(frame);
                drawHUD();
                break;
            case IN_PLAY:
                frame = game.getFrame();
                ter.renderFrame(frame);
                drawHUD();
                break;
            case COMMAND_ENTRY:
                drawCommandScreen();
                break;
            default:
                break;
        }

        StdDraw.show();
    }

    private void drawStartMenu() {
        StdDraw.text(CENTER_X, CENTER_Y + 2, "New Game (N)");
        StdDraw.text(CENTER_X, CENTER_Y, "Load Game (L)");
        StdDraw.text(CENTER_X, CENTER_Y - 2, "Options (:)");
    }

    private void drawSeedEntryScreen() {
        StdDraw.text(CENTER_X, CENTER_Y + 1, "Enter a seed value followed by 'S'.");
    }

    private void drawHUD() {
        StdDraw.text(HUD_ORIGIN_X + 10, HUD_ORIGIN_Y, "This is the HUD.");
    }

    private void drawCommandScreen() {
        String a = game.getAnimationSetting() ? "ON" : "OFF";

        StdDraw.text(CENTER_X, CENTER_Y + 2, "Commands:");
        StdDraw.text(CENTER_X, CENTER_Y, "Save and quit (Q)");
        StdDraw.text(CENTER_X, CENTER_Y - 1, "Return to game (R)");
        StdDraw.text(CENTER_X, CENTER_Y - 3, "Toggle settings:");
        StdDraw.text(CENTER_X, CENTER_Y - 5,
                "Animate level generation (A): " + a);
    }

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

    /* MAIN METHOD -----------------------------------------------------------*/

    public static void main(String[] args) {
        Engine eng = new Engine();
        //eng.interactWithInputString("n12345s");
        eng.interactWithKeyboard();
    }
}
