package byow.Core;

/**
 * An input device formed from a String.
 */
public class StringInputDevice implements InputSource  {
    private String input;
    private int index;
    private static final InputType type = InputType.STRING;

    /**
     * Constructor. Takes an input string which forms the basis of the data
     * the input device renders.
     * @param s input string
     */
    public StringInputDevice(String s) {
        index = 0;
        input = s;
    }

    /**
     * Returns the next character from the input string. Lower case characters
     * are converted to upper case.
     * @return next input character
     */
    @Override
    public char getNextKey() {
        char returnChar = Character.toUpperCase(input.charAt(index));
        index += 1;
        return returnChar;
    }

    /**
     * Signals whether there is still input characters remaining, or if the 
     * input string has been exhausted.
     * @return true input remaining, false input exhausted
     */
    @Override
    public boolean possibleNextInput() {
        return index < input.length();
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
