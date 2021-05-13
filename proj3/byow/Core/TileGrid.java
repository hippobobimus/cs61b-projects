package byow.Core;

import static byow.Core.Constants.*;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

/**
 * An extension of the 2D grid of points incorporating tiles.
 * @author Rob Masters
 */
public class TileGrid extends Grid {
    private TERenderer ter;
    private TETile[][] tiles;

    /* CONSTRUCTORS ----------------------------------------------------------*/

    /**
     * Full constructor for a 2D tile grid. Initially the tile grid is filled
     * with grass tiles.
     * @param width width of the map
     * @param height height of the map
     * @param animate animation/no animation
     */
    public TileGrid(int width, int height) {
        super(width, height);

        tiles = new TETile[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.GRASS;
                Point p = get(x, y);
            }
        }
    }

    /* TILES -----------------------------------------------------------------*/

    /**
     * Returns a 2D array of tiles corresponding to points in the 2D grid.
     * @return 2D tile array
     */
    public TETile[][] getFrame() {
        return tiles;
    }

    /**
     * Returns the tile at the given position. Throws an exception if the point
     * is not contained within the world.
     * @param p point
     */
    public TETile getTile(Point p) {
        int x = p.getX();
        int y = p.getY();

        return tiles[x][y];
    }

    /**
     * Sets the tile at the given point to the given tile. Throws an exception
     * if the point is not contained in the world.
     * @param p point
     * @param tile tile
     */
    public void setTile(Point p, TETile tile) {
        validatePoint(p);
        int x = p.getX();
        int y = p.getY();
        tiles[x][y] = tile;
    }

    /**
     * Sets the tile at the point corresponding to the given coords to the given
     * tile. Throws an exception if the coords are outside the bounds of the
     * world.
     * @param x x-coord
     * @param y y-coord
     * @param tile tile
     */
    public void setTile(int x, int y, TETile tile) {
        Point p = get(x, y);  // get throws an exception if outside world
        setTile(p, tile);
    }

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

    /**
     * Checks that the given point is contained within the grid and, if not,
     * throws an exception.
     * @param p point
     */
    private void validatePoint(Point p) {
        if (!contains(p)) {
            throw new IllegalArgumentException();
        }
    }
}
