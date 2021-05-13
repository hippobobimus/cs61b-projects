package byow.Core;

import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Extends the GameEntity class to include the specific avatar tiles, with
 * functionality for updating the tile depending on the direction of travel.
 * @author Rob Masters
 */
public class Objective extends GameEntity {
    private Avatar avatar;
    private boolean taken;

    /* CONSTRUCTORS ----------------------------------------------------------*/

    /**
     * Full constructor.
     * @param ntg navigable tile grid
     * @param random pseudorandom number generator
     */
    public Objective(NavigableTileGrid ntg, Random random) {
        super(Tileset.OBJECTIVE_UNTAKEN, ntg, random);
        this.taken = false;
    }

    /* PUBLIC METHODS --------------------------------------------------------*/

    public boolean isTaken() {
        return taken;
    }

    public void take() {
        this.taken = true;
        setTile(Tileset.OBJECTIVE_TAKEN);
    }

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

}

