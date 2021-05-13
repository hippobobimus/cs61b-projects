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
    private HUD hud = new HUD(game);
    private TERenderer ter = new TERenderer();
    private InputProcessor inputProcessor = new InputProcessor(this.game);

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        InputSource inputSource = new KeyboardInputSource();
        inputProcessor.initialize(inputSource);

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
     *   Does not render to screen.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        InputSource inputSource = new StringInputSource(input);
        inputProcessor.initialize(inputSource);

        while (inputSource.possibleNextInput()) {
            inputProcessor.process();
            game.update();
        }

        TETile[][] finalWorldFrame = game.getFrame();
        return finalWorldFrame;
    }

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

    /**
     * Main game loop. On each cycle it first processes any available input,
     * then updates the game before rendering it to screen. The loop continues
     * indefinitely so exiting the game must be handled by a sub-process.
     */
    private void gameLoop() {
        while (true) {
            inputProcessor.process();
            game.update();
            render();
            StdDraw.pause(16);
        }
    }

    /* -----------------------------------------------------------------------*/

//    /**
//     */
//    private void update() {
//        switch(game.getState()) {
//            case BUILDING_LEVEL:
//                game.buildLevel();
//                break;
//            case IN_PLAY:
//                game.update();
//                hud.update();
//                break;
//        }
//    }

    /**
     */
    private void render() {
        StdDraw.clear(Color.BLACK);

        TETile[][] frame;

        switch (game.getState()) {
            case IN_PLAY:
                frame = game.getFrame();
                ter.renderFrame(frame);
                hud.render();
                break;
            case BUILDING_LEVEL:
                frame = game.getFrame();
                ter.renderFrame(frame);
                hud.render();
                break;
            case MAIN_MENU:
                drawStartMenu();
                break;
            case SEED_ENTRY:
                drawSeedEntryScreen();
                break;
            case COMMAND_ENTRY:
                drawCommandScreen();
                break;
            default:
                break;
        }

        StdDraw.show();
    }

    /* -----------------------------------------------------------------------*/

    private void drawStartMenu() {
        StdDraw.text(CENTER_X, CENTER_Y + 2, "New Game (N)");
        StdDraw.text(CENTER_X, CENTER_Y, "Load Game (L)");
        StdDraw.text(CENTER_X, CENTER_Y - 2, "Options (:)");
    }

    private void drawSeedEntryScreen() {
        StdDraw.text(CENTER_X, CENTER_Y + 1, "Enter a seed value followed by 'S'.");
    }

    private void drawCommandScreen() {
        String a = game.getAnimationSetting() ? "ON" : "OFF";

        StdDraw.text(CENTER_X, CENTER_Y + 2, "Commands:");
        StdDraw.text(CENTER_X, CENTER_Y, "Save and Quit (Q)");
        StdDraw.text(CENTER_X, CENTER_Y - 1, "Return to Game (R)");
        StdDraw.text(CENTER_X, CENTER_Y - 3, "Toggle Settings:");
        StdDraw.text(CENTER_X, CENTER_Y - 5,
                "Animate Level Generation (A): " + a);
    }

    /* MAIN METHOD -----------------------------------------------------------*/

    public static void main(String[] args) {
        Engine eng = new Engine();
        //eng.interactWithInputString("n12345s");
        eng.interactWithKeyboard();
    }
}
