package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

/**
 * A class that represents an individual point from a 2D tiled area.
 * @author Rob Masters
 */
public class Point {
    private int x;
    private int y;
    private int priority;

    /* CONSTRUCTOR METHODS ---------------------------------------------------*/

    /** 
     * Full constructor for a Point in 2D tile-space given x and y coordinates
     * and an integer priority value.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param priority // TODO remove and add to world?
     */
    public Point(int x, int y, int priority) {
        this.x = x;
        this.y = y;
        this.priority = priority;
    }

    /**
     * Constructor without priority. Priority defaults to 0.
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Point(int x, int y) {
        this(x, y, 0);
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

    /* OVERRIDDEN METHODS ----------------------------------------------------*/

    /**
     * String representation of the point.
     * @return string representation
     */
    @Override
    public String toString() {
        String s = "{" + getX() + ", " + getY() + ", priority=" +
            getPriority() + "}";
        return s;
    }
}
