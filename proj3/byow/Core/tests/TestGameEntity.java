package byow.Core.tests;

import byow.Core.*;
import static byow.Core.Constants.*;
import byow.TileEngine.Tileset;
import byow.TileEngine.TETile;

import java.util.Random;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the GameEntity class.
 * @author Rob Masters
 */
public class TestGameEntity {
    @Test
    public void testGetSetTile() {
        TETile tile = Tileset.WALL;
        NavigableTileGrid ntg = new NavigableTileGrid();
        Random rand = new Random();

        GameEntity ent = new GameEntity(tile, ntg, rand);

        assertEquals(tile, ent.getTile());

        tile = Tileset.FLOOR;

        ent.setTile(tile);
        assertEquals(tile, ent.getTile());
    }

    @Test
    public void testMove() {
        TETile tile = Tileset.WALL;
        NavigableTileGrid ntg = new NavigableTileGrid();
        Random rand = new Random();

        GameEntity ent = new GameEntity(tile, ntg, rand);

        assertEquals(null, ent.getPosition());

        // Create a pathway area of two points.
        // q is to the RIGHT of p.
        Point p = ntg.get(0, 0);
        Point q = ntg.get(1, 0);

        ntg.openPath(p);
        ntg.openPath(q);

        // Test movement within this pathway.
        //
        // Start at p.
        ent.move(p);
        assertEquals(p, ent.getPosition());

        // Move to q.
        ent.move(Direction.RIGHT);
        assertEquals(q, ent.getPosition());

        // No further pathway in this direction, so should not move.
        ent.move(Direction.RIGHT);
        assertEquals(q, ent.getPosition());

        // Return to p.
        ent.move(Direction.LEFT);
        assertEquals(p, ent.getPosition());

        // No further pathway in this direction, so should not move.
        ent.move(Direction.UP);
        assertEquals(p, ent.getPosition());
    }
}

