package byow.Core;

import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Extends the GameEntity class to include the specific avatar tiles, with
 * functionality for updating the tile depending on the direction of travel.
 * @author Rob Masters
 */
public class Avatar extends GameEntity {

    /* CONSTRUCTORS ----------------------------------------------------------*/

    /**
     * Full constructor.
     * @param ntg navigable tile grid
     * @param random pseudorandom number generator
     */
    public Avatar(NavigableTileGrid ntg, Random random) {
        super(Tileset.AVATAR_DEFAULT, ntg, random);
    }

    /* PUBLIC METHODS --------------------------------------------------------*/

    /**
     * Moves the avatar one step (tile space) in the given direction, if the
     * destination is on the pathway. If the destination is not on the pathway
     * the avatar doesn't move. The avatar tile is updated to represent the
     * direction of travel even if the avatar doesn't move.
     * @param d direction to move in
     */
    @Override
    public void move(Direction d) {
        setAvatarTile(d);

        super.move(d);
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
                setTile(Tileset.AVATAR_UP);
                break;
            case DOWN:
                setTile(Tileset.AVATAR_DOWN);
                break;
            case LEFT:
                setTile(Tileset.AVATAR_LEFT);
                break;
            case RIGHT:
                setTile(Tileset.AVATAR_RIGHT);
                break;
            default:
                break;
        }
    }
}
