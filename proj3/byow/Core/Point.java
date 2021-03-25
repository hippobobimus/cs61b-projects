package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

// TODO remove tile/open/navigable. should be part of world class only
/**
 * A class that represents an individual point from a 2D tiled area.
 * @author Rob Masters
 */
public class Point {
    private int x;
    private int y;
    private TETile tile;
    private boolean open;
    private boolean navigable; // TODO
    private int priority;

    /* CONSTRUCTOR METHODS ---------------------------------------------------*/

    /** 
     * Full constructor for a Point in 2D tile-space given x and y coordinates,
     * a tile variant and an integer priority value.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param tile TETile object
     * @param priority // TODO remove and add to builder?
     */
    public Point(int x, int y, TETile tile, int priority) {
        this.x = x;
        this.y = y;
        this.open = false;
        this.priority = priority;
        setTile(tile);
    }

    /**
     * Constructor without priority. Priority defaults to 0.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param tile TETile object
     */
    public Point(int x, int y, TETile tile) {
        this(x, y, tile, 0);
    }

    /**
     * Constructor without a tile or priority. The tile defaults to 
     * Tileset.NOTHING and priority defaults to 0.
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Point(int x, int y) {
        this(x, y, Tileset.NOTHING, 0);
    }

    /* PUBLIC GETTERS AND SETTERS --------------------------------------------*/

    /**
     * x-coordinate of the point.
     * @return x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * y-coordinate of the point.
     * @return y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Priority of the point. Used for queueing.
     * @return priority value
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Sets the priority value of the point.
     * @param priority priority value for queueing
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * The tile associated with the point. Used for visual representation.
     * @return tile object
     */
    public TETile getTile() {
        return tile;
    }

    /**
     * Set the tile associated with the point.
     * @param tile tile object
     */
    public void setTile(TETile tile) {
        this.tile = tile;
        if (tile.equals(Tileset.FLOOR)) {
            this.navigable = true;
        }
    }

    /**
     * Returns true if the point is open, false otherwise.
     * @return open
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Marks the point as open.
     */
    public void open() {
        open = true;
    }

    /**
     * Marks the point as not open.
     */
    public void close() {
        open = false;
    }

    // TODO is this required?
    public boolean navigable() {
        return navigable;
    }

    /* OVERRIDDEN METHODS ----------------------------------------------------*/

    /**
     * String representation of the point.
     * @return string representation
     */
    @Override
    public String toString() {
        String s = "{" + getX() + ", " + getY() + ", " + getTile()
            + ", priority=" + getPriority() + ", open=" + isOpen() + "}";
        return s;
    }
}
