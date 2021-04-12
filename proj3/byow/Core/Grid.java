package byow.Core;

import static byow.Core.Constants.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a 2D grid of points, incorporating methods for the retrieval of
 * specific points and their neighbours.
 * @author Rob Masters
 */
public class Grid {
    private int width, height;
    private Point[][] points;
    private List<Point> pointsList;

    /* CONSTRUCTORS ----------------------------------------------------------*/

    /** Full constructor for a 2D grid of points.
     * @param width width of grid
     * @param height height of grid
     */
    public Grid(int width, int height) {
        this.width = width;
        this.height = height;

        points = new Point[width][height];
        pointsList = new ArrayList<Point>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Point p = new Point(x, y);
                points[x][y] = p;
                pointsList.add(p);
            }
        }
    }

    /* PUBLIC METHODS --------------------------------------------------------*/

    /**
     * Returns the height of the grid.
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the width of the grid.
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the Point at the given grid coordinates. Throws an exception
     * if the given coords are outside of the grid boundaries.
     * @param x x-coord
     * @param y y-coord
     * @return point
     */
    public Point get(int x, int y) {
        Point result;

        try {
            result = points[x][y];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(
                    "get(): The given grid coordinates are outside the bounds " +
                    "of the Grid.");
        }

        return result;
    }

    /**
     * Determines whether the given point is contained within the grid.
     * @param p point
     * @return whether point is in the grid
     */
    public boolean contains(Point p) {
        int x = p.getX();
        int y = p.getY();

        // Within coordinate bounds of grid?
        if (!contains(x, y)) {
            return false;
        }

        // Is the point in the grid at these coords equal to the point given?
        Point pointAtCoords = get(x, y);
        if (!p.equals(pointAtCoords)) {
            return false;
        }

        return true;
    }

    /**
     * Determines whether the given x,y-coordinates are contained within the
     * grid.
     * @param x x-coord
     * @param y y-coord
     * @return whether coords are in the grid
     */
    public boolean contains(int x, int y) {
        if (x < 0 || x >= width) {
            return false;
        }
        if (y < 0 || y >= height)  {
            return false;
        }
        return true;
    }

    /**
     * Determines whether the given point is at an edge/corner of the grid.
     * Throws an exception if the given point is not contained within the grid.
     * @param p point
     * @return true/false
     */
    public boolean isAtGridBoundary(Point p) {
        validatePoint(p);

        List<Point> nbrs = listNeighbours(p);

        if (nbrs.size() == 8) {
            return false;
        }

        return true;
    }

    /* POINT LISTS -----------------------------------------------------------*/

    /**
     * Returns a list of all points in the grid.
     * @return list of all points
     */
    public List<Point> listAllPoints() {
        return pointsList;
    }

    /**
     * Returns the (up to 8) Points directly surrounding a given Point. Only
     * returns points contained within the grid boundaries.
     * @param p point
     * @return surrounding points
     */
    public List<Point> listNeighbours(Point p) {
        List<Direction> dirs = Direction.listAll();
        return neighbours(p, dirs);
    }

    /**
     * Returns the (up to 4) Points directly above, below and to the left and right of the
     * given Point. Only returns points contained within the grid boundaries.
     * @param p point
     * @return cardinal points
     */
    public List<Point> listCardinalNeighbours(Point p) {
        List<Direction> dirs = Direction.listCardinal();
        return neighbours(p, dirs);
    }

    /**
     * Returns the (up to 5) points directly ahead and to the sides of the given
     * point relative to the given direction. Only returns points contained
     * within the grid boundaries.
     * @param p point
     * @param d direction
     * @return arc of points
     */
    public List<Point> listArcNeighbours(Point p, Direction d) {
        List<Direction> dirs = d.listArc();
        return neighbours(p, dirs);
    }

    /**
     * Returns the (up to 3) points directly ahead of the given point relative
     * to the Direction of travel. Only returns points contained within the grid
     * boundaries.
     * @param p point
     * @param d direction of travel
     * @return points ahead
     */
    public List<Point> listNeighboursAhead(Point p, Direction d) {
        List<Direction> dirs = d.listAhead();
        return neighbours(p, dirs);
    }

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

    /**
     * Throws an exception if the point is not contained in the grid.
     * @param p point
     */
    private void validatePoint(Point p) {
        if (!contains(p)) {
            throw new IllegalArgumentException(
                    "Point not contained in grid: " + p);
        }
    }

    /**
     * Returns a List of Points neighbouring the given Point in the given
     * directions.
     * @param p point
     * @param dirs directions
     * @return points list
     */
    private List<Point> neighbours(Point p, List<Direction> dirs) {
        List<Point> result = new ArrayList<>();
        int x = p.getX();
        int y = p.getY();

        for (Direction dir : dirs) {
            int nx = dir.transformX(x);
            int ny = dir.transformY(y);
            if (contains(nx, ny)) {
                Point neighbour = get(nx, ny);
                result.add(neighbour);
            }
        }
        return result;
    }
}
