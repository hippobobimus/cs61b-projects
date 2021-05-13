package byow.Core;

import static byow.Core.Constants.*;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * An entity that exists within the confines of a navigable tile grid, with 
 * methods for changing its tile and moving it within the pathway of the grid.
 * @author Rob Masters
 */
public class GameEntity {
    private NavigableTileGrid ntg;
    private Random random;
    private Point position;
    private TETile tile;
    private TETile underlyingTile;

    /* CONSTRUCTORS ----------------------------------------------------------*/

    /**
     * Full constructor. Sets the entity's tile and the grid in which it will
     * exist, though the initial position is off grid (null).
     * @param tile entity's tile
     * @param ntg navigable tile grid
     * @param random pseudorandom number generator
     */
    public GameEntity(TETile tile, NavigableTileGrid ntg, Random random) {
        this.ntg = ntg;
        this.random = random;

        this.tile = tile;
        this.position = null;
    }

    /* PUBLIC METHODS --------------------------------------------------------*/

    /**
     * The point in the grid in which the entity is positioned.
     * @return point where entity is located
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Changes the entity's tile to the given tile.
     * @param newTile change to this tile
     */
    public void setTile(TETile newTile) {
        this.tile = newTile;
    }

    /**
     * Returns the tile currently associated with the entity.
     * @return entity's tile
     */
    public TETile getTile() {
        return tile;
    }

    /**
     * Moves the entity one step (tile space) in the given direction, if the
     * destination is on the pathway. If the destination is not on the pathway
     * the entity doesn't move.
     * @param d direction to move in
     */
    public void move(Direction d) {
        Point destination = ntg.pathway.getNeighbour(this.position, d);
        if (destination != null) {
            move(destination);
        }
    }

    /**
     * Moves the entity to a random point within the pathway. If there are no
     * points available on the pathway, the entity doesn't move.
     */
    public void move() {
        List<Point> pathPoints = ntg.pathway.listAllPoints();
        int pathSize = pathPoints.size();

        // no path to move on to.
        if (pathSize == 0) {
            return;
        }

        int randIndex = random.nextInt(pathSize);

        move(pathPoints.get(randIndex));
    }

    /**
     * Moves the entity to the given point. If the point is not on the pathway
     * then the entity doesn't move.
     * @param p destination point
     */
    public void move(Point p) {
        if (ntg.isPath(p)) {
            this.position = p;
        }
    }
}
