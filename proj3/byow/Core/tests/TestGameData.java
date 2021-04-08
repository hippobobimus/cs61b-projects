package byow.Core.tests;

import byow.Core.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestGameData {
    @Test
    public void testSaveLoad() {
        String testData = "This is a test...";

        GameData.overwriteSaveData(testData);

        InputSource input = GameData.getInputSource();

        // Lower case converted to upper case by input source.
        String expected = "THIS IS A TEST...";

        for (char c : expected.toCharArray()) {
            assertEquals(c, input.getNextKey());
        }
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", TestGameData.class);
    }
}
