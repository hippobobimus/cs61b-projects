package byow.Core;

/**
 * Interface for input sources that provides the ability to get input by
 * character, as well as determine whether the input has been exhausted
 * and what specific type it is.
 */
public interface InputSource {
    public char getNextKey();
    public boolean possibleNextInput();
    public InputType type();
}
