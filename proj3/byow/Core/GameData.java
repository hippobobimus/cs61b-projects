package byow.Core;

import static byow.Core.Constants.SAVE_FILE_PATH;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A class for handling save game data. Data can be written out to a file and
 * read back in at a later time, delivered as an InputSource.
 * @author Rob Masters
 */
public class GameData {
    /**
     * Returns an input source containing the data from the save file.
     * @return save file as input source
     */
    public static InputSource getInputSource() {
        InputSource result = new StringInputDevice(load());
        return result;
    }

    /**
     * Stores the given data string to the save file, overwriting existing data.
     * @param data data to be stored
     */
    public static void overwriteSaveData(String data) {
        save(data, false);
    }

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

    /**
     * Stores the given data string to the save file, either appending or
     * overwriting existing data as specified.
     * @param data data to be stored
     * @param append whether to append or overwrite existing data
     */
    private static void save(String data, boolean append) {
        try (FileWriter fw = new FileWriter(SAVE_FILE_PATH, append)) {
            fw.write(data);
            System.out.println("...save complete.");
        } catch (IOException e) {
            System.out.println("Failed to save game.");
            e.printStackTrace();
        }
    }

    /**
     * Retrieves data stored to the save file and returns it as a string.
     * @return previously saved data
     */
    private static String load() {
        StringBuilder result = new StringBuilder();

        try (FileReader fr = new FileReader(SAVE_FILE_PATH)) {
            int ch = fr.read();
            while (ch != -1) {
                result.append((char) ch);
                ch = fr.read();
            }
        } catch (FileNotFoundException e) {
            System.out.println("There is no save file...");
        } catch (IOException e) {
            System.out.println("Failed to load game.");
            e.printStackTrace();
        }

        return result.toString();
    }

    /* MAIN METHOD -----------------------------------------------------------*/

    public static void main(String[] args) {
        GameData.overwriteSaveData("Testing 123...");

        InputSource input = GameData.getInputSource();

        while (input.possibleNextInput()) {
            char c = input.getNextKey();
            System.out.print(c);
        }
    }
}
