package byow.Core;

import static byow.Core.Constants.*;

/**
 * Created by hug.
 */
import edu.princeton.cs.introcs.StdDraw;

public class KeyboardInputSource implements InputSource {
    private static final boolean PRINT_TYPED_KEYS = true;
    private static final InputType type = InputType.KEYBOARD;

    public KeyboardInputSource() {
        StdDraw.setCanvasSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        StdDraw.text(0.5, 0.55, "New Game (N)");
        StdDraw.text(0.5, 0.5, "Load Game (L)");
        StdDraw.text(0.5, 0.45, "Quit (:Q)");
    }

    @Override
    public char getNextKey() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (PRINT_TYPED_KEYS) {
                    System.out.print(c);
                }
                return c;
            }
        }
    }

    @Override
    public boolean possibleNextInput() {
        return true;
    }

    @Override
    public InputType type() {
        return type;
    }
}
