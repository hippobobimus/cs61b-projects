package byow.Core.tests;

import byow.Core.*;
import static byow.Core.Constants.*;
import byow.TileEngine.Tileset;

import java.util.List;
import java.util.Random;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Rob Masters
 */
public class TestBridgeBuilder {
    @Test
    public void testConnected() {
        Random rand = new Random(123L);

        NavigableTileGrid ntg = new NavigableTileGrid();

        RoomBuilder rb = new RoomBuilder(ntg, rand);
        MazeBuilder mb = new MazeBuilder(ntg, rand);
        BridgeBuilder bb = new BridgeBuilder(ntg, rand);

        // build rooms.
        while (!rb.build()) {
        }
        // fill space with mazes.
        while (!mb.build()) {
        }

        // get path
        List<Point> pathPoints = ntg.pathway.listAllPoints();

        // ensure path is not fully connected
        boolean connected = true;
        for (Point p : pathPoints) {
            for (Point q : pathPoints) {
                connected &= ntg.isConnected(p, q);
            }
        }
        assertFalse(connected);

        // build bridges.
        while (!bb.build()) {
        }

        // ensure path is fully connected
        for (Point p : pathPoints) {
            for (Point q : pathPoints) {
                connected = ntg.isConnected(p, q);
                if (!connected) {
                    System.out.println("Points not connected!");
                    System.out.println("Points= " + p + ", " + q);
                }
                assertTrue(connected);
            }
        }
    }
}
