package byow.Core;

import java.util.Random;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the MazeBuilder class.
 * @author Rob Masters
 */
public class TestMazeBuilder {
    @Test
    public void testStartPoint() {
        Grid g = new Grid();
        Random rand = new Random(2873123);
        MazeBuilder mb = new MazeBuilder(g, rand);

        Point validStart = g.get(1, 1);
        Point invalidStart = g.get(-1, 1);

        // start outside grid area
        int result1 = mb.buildMaze(invalidStart);
        assertEquals(-1, result1);

        // start ok
        int result2 = mb.buildMaze(validStart);
        assertEquals(0, result2);

        // start already used
        int result3 = mb.buildMaze(validStart);
        assertEquals(-1, result3);
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", TestPoint.class);
    }
}
