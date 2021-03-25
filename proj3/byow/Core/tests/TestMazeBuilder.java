package byow.Core.tests;

import byow.Core.*;
import static byow.Core.Constants.*;
import byow.TileEngine.Tileset;

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
        World g = new World(2873123);
        MazeBuilder mb = new MazeBuilder(g);

        Point validStart = g.get(1, 1);

        // start ok
        int result2 = mb.build(validStart);
        assertEquals(0, result2);

        // start already used
        int result3 = mb.build(validStart);
        assertEquals(-1, result3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStartPoint() {
        World g = new World(2873123);
        MazeBuilder mb = new MazeBuilder(g);

        Point validStart = g.get(1, 1);
        Point invalidStart = g.get(-1, 1);

        // start outside grid area
        int result1 = mb.build(invalidStart);
        assertEquals(-1, result1);
    }

    @Test
    public void testConnections() {
        World world = new World(2873123);
        MazeBuilder mb = new MazeBuilder(world);

        Point start = world.get(1, 1);
        Point previous = start;

        mb.build(start);

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                Point p = world.get(x, y);
                if (world.getTile(p).equals(Tileset.FLOOR)) {
                    assertTrue(world.isConnected(p, previous));
                    assertTrue(world.isConnected(p, start));
                    previous = p;
                }
            }
        }
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", TestMazeBuilder.class);
    }
}
