package byow.Core;

import static byow.Core.Constants.*;

import edu.princeton.cs.introcs.StdDraw;

/**
 * A heads up display showing stats about the current game.
 * @authot Rob Masters
 */
public class HUD {
    private Game game;
    private String contents = "HANGRY CHICKENS";

    /**
     * Full constructor. Requires a Game as parameter so that it can current
     * game stats such as the score.
     * @param game game
     */
    public HUD(Game game) {
        this.game = game;
    }

    /**
     * Updates the contents of the hud, but does not render it to screen.
     */
    public void update() {
        contents = "HANGRY CHICKENS --- Score: " + game.getScore();
    }

    /**
     * Draws the hud to screen.
     */
    public void render() {
        StdDraw.text(HUD_ORIGIN_X + 10, HUD_ORIGIN_Y, contents);
    }
}
