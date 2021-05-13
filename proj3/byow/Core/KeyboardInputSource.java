package byow.Core;

import static byow.Core.Constants.*;

import edu.princeton.cs.introcs.StdDraw;

/**
 * An input device that retrieves key entries on the keyboard.
 */
public class KeyboardInputSource implements InputSource {
    private static final boolean PRINT_TYPED_KEYS = true;
    private static final InputType type = InputType.KEYBOARD;

    /**
     * Returns the next character entered on the keyboard. Lower case characters
     * are converted to upper case.
     * @return next input character
     */
    @Override
    public char getNextKey() {
        if (StdDraw.hasNextKeyTyped()) {
            char c = Character.toUpperCase(StdDraw.nextKeyTyped());
            if (PRINT_TYPED_KEYS) {
                System.out.print(c);
            }
            return c;
        }
        return Character.MIN_VALUE;
    }

    /**
     * Determines whether there is available input ready to be served.
     * @return presence of available input
     */
    @Override
    public boolean hasNextKey() {
        return StdDraw.hasNextKeyTyped();
    }

    /**
     * Always signals true since this device waits for keyboard input
     * until the program ends.
     * @return true
     */
    @Override
    public boolean possibleNextInput() {
        return true;
    }

    /**
     * Returns the type of this InputDevice.
     * @return input device type
     */
    @Override
    public InputType type() {
        return type;
    }
}
