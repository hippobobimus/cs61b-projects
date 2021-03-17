package byow.Core;

/**
 * Representation of an immutable directed graph edge in 2D tile-space.
 * @author Rob Masters
 */
public class Edge {
    private Point start;
    private Point end;
    private Direction direction;

    /* CONSTRUCTOR -----------------------------------------------------------*/

    /** 
     * Full constructor for a directed graph edge in 2D tile-space given x and y
     * coordinates.
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Edge(Point start, Point end) {
        this.start = start;
        this.end = end;
        this.direction = Direction.from(start, end);
    }

    /* GETTERS ---------------------------------------------------------------*/

    /**
     * Point at which the edge originates.
     * @return start point
     */
    public Point from() {
        return start;
    }

    /**
     * Point at which the edge terminates.
     * @return end point
     */
    public Point to() {
        return end;
    }

    /**
     * Direction of the edge.
     * @return direction object
     */
    public Direction direction() {
        return direction;
    }
}
