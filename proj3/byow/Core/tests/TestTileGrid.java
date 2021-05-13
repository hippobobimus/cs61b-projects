package byow.Core.tests;

import byow.Core.*;
import static byow.Core.Constants.*;
import byow.TileEngine.Tileset;
import byow.TileEngine.TETile;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the TileGrid class.
 * @author Rob Masters
 */
public class TestTileGrid {
    @Test
    public void testGetSetTile() {
        TileGrid tg = new TileGrid(MAP_WIDTH, MAP_HEIGHT);

        Point p = tg.get(2, 3);
        TETile tile = Tileset.WALL;

        tg.setTile(p, tile);
        assertEquals(tile, tg.getTile(p));

        tile = Tileset.FLOOR;
        tg.setTile(p, tile);
        assertEquals(tile, tg.getTile(p));
    }
}
