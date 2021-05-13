package byow.Core.tests;

import byow.Core.*;
import static byow.Core.Constants.*;
import byow.TileEngine.TETile;

import java.util.Random;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestEngine {
    @Test
    public void testConsistency() {
        Engine eng = new Engine();

        Random rand = new Random();

        TETile[][] frame = eng.interactWithInputString(getSeedString(0));

        TETile[][] tempFrame;
        for (int i = 0; i < 1000; i++) {
            Long seed = Math.abs(rand.nextLong());
            String input = getSeedString(seed);

            TETile[][] run1 = eng.interactWithInputString(input);
            TETile[][] run2 = eng.interactWithInputString(input);
            assertTrue(compareFrames(run1, run2));
        }
    }

    private boolean compareFrames(TETile[][] f0, TETile[][] f1) {
        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                if (!f0[x][y].equals(f1[x][y])) {
                    System.out.println("FRAMES NOT EQUAL!");
                    System.out.println("x-coord=" + x + ", y-coord=" + y);
                    System.out.println("tiles=" + f0[x][y] + ", " + f1[x][y]);
                    return false;
                }
            }
        }

        return true;
    }

    private String getSeedString(long seed) {
        String result = "n" + Long.toString(seed) + "s";
        return result;
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", TestEngine.class);
    }
}
