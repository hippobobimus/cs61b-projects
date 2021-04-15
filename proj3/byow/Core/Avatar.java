package byow.Core;

import static byow.Core.Constants.*;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Adds functionality for a user controllable avatar capable of moving within
 * the confines of the pathway in a navigable tile grid.
 * @author Rob Masters
 */
public class Avatar {
    private NavigableTileGrid ntg;
    private Random random;
    private Point position;
    private TETile avatarTile;
    private TETile positionTerrain;

    /* CONSTRUCTORS ----------------------------------------------------------*/

    /**
     * Full constructor. Sets the grid in which the avatar will move, though
     * the avatar's initial position is off grid (null).
     * @param ntg navigable tile grid
     * @param random pseudorandom number generator
     */
    public Avatar(NavigableTileGrid ntg, Random random) {
        this.ntg = ntg;
        this.random = random;

        this.avatarTile = Tileset.AVATAR_DEFAULT;
        this.position = null;
    }

    /* PUBLIC METHODS --------------------------------------------------------*/

    /**
     * Moves the avatar one step (tile space) in the given direction, if the
     * destination is on the pathway. If the destination is not on the pathway
     * the avatar doesn't move. The avatar tile is updated to represent the
     * direction of travel even if the avatar doesn't move.
     * @param d direction to move in
     */
    public void move(Direction d) {
        setAvatarTile(d);
        ntg.setTile(position, avatarTile);

        Point destination = ntg.pathway.getNeighbour(this.position, d);
        if (destination != null) {
            move(destination);
        }
    }

    /**
     * Moves the avatar to a random point within the pathway. If there are no
     * points available on the pathway, the avatar doesn't move.
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
     * Moves the avatar to the given point. If the point is not on the pathway
     * then the avatar doesn't move.
     * @param p destination point
     */
    public void move(Point p) {
        if (ntg.isPath(p)) {
            // return current position to underlying terrain.
            if (position != null) {
                ntg.setTile(position, positionTerrain);
            }

            // configure new position.
            this.position = p;
            this.positionTerrain = ntg.getTile(p);
            ntg.setTile(p, avatarTile);
        }
    }

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

    /**
     * Sets the avatar tile based on the direction of movement. Only acts when
     * the direction is cardinal (UP, DOWN, LEFT, RIGHT).
     * @param d direction of intended travel
     */
    private void setAvatarTile(Direction d) {
        switch(d) {
            case UP:
                avatarTile = Tileset.AVATAR_UP;
                break;
            case DOWN:
                avatarTile = Tileset.AVATAR_DOWN;
                break;
            case LEFT:
                avatarTile = Tileset.AVATAR_LEFT;
                break;
            case RIGHT:
                avatarTile = Tileset.AVATAR_RIGHT;
                break;
            default:
                break;
        }
    }
}
