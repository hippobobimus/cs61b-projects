package byow.Core.tests;

import byow.Core.*;

import java.util.Random;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestEngine {
    @Test
    public void test() {
        Engine eng = new Engine();

        Random rand = new Random();

        for (int i = 0; i < 1000; i++) {
            Long seed = Math.abs(rand.nextLong());
            String input = "n" + Long.toString(seed) + "s";
            eng.interactWithInputString(input);
        }
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", TestEngine.class);
    }
}
