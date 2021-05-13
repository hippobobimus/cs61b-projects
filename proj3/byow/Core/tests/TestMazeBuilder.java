package byow.Core.tests;

import byow.Core.*;
import static byow.Core.Constants.*;

import java.util.Random;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the MazeBuilder class.
 * @author Rob Masters
 */
public class TestMazeBuilder {
    @Test
    public void testFullyConnectedMaze() {
        NavigableTileGrid ntg = new NavigableTileGrid();
        Random rand = new Random(123L);

        MazeBuilder mb = new MazeBuilder(ntg, rand);

        Point start = ntg.get(1, 1);
        Point previous = start;

        // build maze to completion.
        while (!mb.build()) {
        }

        // Make sure all of the path is fully connected.
        Point reference = null;
        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                Point p = ntg.get(x, y);
                if (ntg.isPath(p)) {
                    if (reference == null) {
                        reference = p;
                    }
                    assertTrue(ntg.isConnected(p, reference));
                }
            }
        }
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", TestMazeBuilder.class);
    }
}
