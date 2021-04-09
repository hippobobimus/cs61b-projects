package byow.Core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A graph data structure designed to hold Point objects.
 * Each point may have up to 4 edges connecting it to neighbouring points
 * in the cardinal directions UP, DOWN, LEFT, RIGHT.
 * @author Rob Masters
 */
public class PointGraph implements Iterable<Point> {
    private Map<Point, Map<Direction, Point>> adjacencyMap;
    private Set<Point> points;
    private int size;

    /* CONSTRUCTOR -----------------------------------------------------------*/

    /**
     * Full constructor for a graph data structure containing Point objects.
     */
    public PointGraph() {
        this.adjacencyMap = new HashMap<>();
        this.points = new HashSet<>();
        this.size = 0;
    }

    /* PUBLIC METHODS --------------------------------------------------------*/

    /**
     * Returns a list of all points on the pathway.
     * @return list of all pathway points
     */
    public List<Point> listAllPoints() {
        List<Point> result = new ArrayList<>(points);
        return result;
    }

    /**
     * Returns a list of all leaf points (degree 1) in the graph.
     * @return list of leaf points
     */
    public List<Point> listLeafPoints() {
        List<Point> result = new ArrayList<>();

        for (Point p : points) {
            if (isLeaf(p)) {
                result.add(p);
            }
        }

        return result;
    }

    /**
     * Returns true if the given point has degree 1. Throws an exception if the
     * given point is not contained in the graph.
     * @param p point
     * @return leaf?
     */
    public boolean isLeaf(Point p) {
        validatePoint(p);

        if (degree(p) == 1) {
            return true;
        }

        return false;
    }

    /**
     * Returns the degree of a vertex/point in the graph. Throws an exception
     * if the given point is not contained in the graph.
     * @param p point
     * @return degree
     */
    public int degree(Point p) {
        validatePoint(p);

        Map<Direction, Point> dirMap = adjacencyMap.get(p);

        return dirMap.size();
    }

    /**
     * Adds the given point to the graph as a vertex. Does nothing if the point
     * is already contained in the graph.
     * @param p point
     */
    public void add(Point p) {
        if (contains(p)) {
            return;
        }

        adjacencyMap.put(p, new HashMap<>());
        points.add(p);

        size++;
    }

    /**
     * Adds a bidirectional edge between the two given points. Throws an
     * exception if any of the given points are not contained in the graph.
     * @param p point
     * @param q point
     */
    public void addEdge(Point p, Point q) {
        validatePoint(p);
        validatePoint(q);

        Direction d = Direction.from(p, q);
        validateDirection(d);

        // undirected graph requires edges in both directions.
        addEdgeHelper(p, q);
        addEdgeHelper(q, p);
    }

    /**
     * Removes the given point from the graph and any edges that include it as
     * either the origin or terminus. Does nothing if the given point is not
     * contained in the graph.
     * @param p point
     */
    public void remove(Point p) {
        if (!contains(p)) {
            return;
        }

        Map<Direction, Point> dirMap = adjacencyMap.get(p);

        for (Map.Entry<Direction, Point> e : dirMap.entrySet()) {
            Direction d = e.getKey();
            Point q = e.getValue();

            adjacencyMap.get(q).remove(d.opposite());
        }

        adjacencyMap.remove(p);
        points.remove(p);

        size--;
    }

    /**
     * TODO
     */
    public void removeEdge(Point p, Point q) {
        validatePoint(p);
        validatePoint(q);
        validateDirection(Direction.from(p, q));

        removeEdgeHelper(p, q);
        removeEdgeHelper(q, p);
    }

    private void removeEdgeHelper(Point from, Point to) {
        Map<Direction, Point> dirMap = adjacencyMap.get(from);

        Direction d = Direction.from(from, to);
        dirMap.remove(d, to);
    }

    /**
     * Returns true if the graph contains the given point.
     * @param p point
     * @return contains point?
     */
    public boolean contains(Point p) {
        return adjacencyMap.containsKey(p);
    }

    /**
     * Returns the current number of points in the graph.
     * @return total points
     */
    public int size() {
        return size;
    }

    /**
     * Returns the point neighbouring the given point in the given direction.
     * Returns null if there is not a neighbouring point in the given direction.
     * Throws an exception if the given point is not contained in the graph.
     * @param p point
     * @param d direction
     * @return neighbouring point
     */
    public Point getNeighbour(Point p, Direction d) {
        validatePoint(p);
        validateDirection(d);

        Map<Direction, Point> dirMap = adjacencyMap.get(p);

        Point result = dirMap.get(d);

        return result;
    }

    /**
     * Lists all points attached to edges originating at the given point.
     * Throws an exception if the given point is not contained in the graph.
     * @param p point
     * @return neighbouring points
     */
    public List<Point> listNeighbours(Point p) {
        validatePoint(p);

        List<Point> result = new ArrayList<>();

        Map<Direction, Point> dirMap = adjacencyMap.get(p);

        for (Point n : dirMap.values()) {
            result.add(n);
        }

        return result;
    }

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

    /**
     * Adds an edge between two points in the direction from-to.
     * @param from origin point
     * @param to terminal point
     */
    private void addEdgeHelper(Point from, Point to) {
        Map<Direction, Point> dirMap = adjacencyMap.get(from);

        Direction d = Direction.from(from, to);

        dirMap.put(d, to);
    }

    /**
     * Throws an exception if the point is not contained in the graph.
     */
    private void validatePoint(Point p) {
        if (!contains(p)){
            throw new IllegalArgumentException(
                    "The PointGraph does not contain the point: " + p);
        }
    }

    /**
     * Throws an exception if the direction is not cardinal (UP, DOWN, LEFT,
     * RIGHT).
     * @param d direction
     */
    private void validateDirection(Direction d) {
        if (!d.isCardinal()){
            throw new IllegalArgumentException(
                    "The PointGraph only accepts cardinal directions. Given: " +
                    d);
        }
    }

    /* OVERRIDEN METHODS -----------------------------------------------------*/

    /**
     * String representation of the graph.
     * @return string representation
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append("{");

        for (Map.Entry<Point, Map<Direction, Point>> e : adjacencyMap.entrySet()) {
            s.append("[");
            s.append(e.getKey());
            s.append(", ");
            s.append(e.getValue());
            s.append("], ");
        }

        s.append("}");

        return s.toString();
    }

    /**
     * Iterator over points in the graph.
     * @return iterator
     */
    @Override
    public Iterator<Point> iterator() {
        return points.iterator();
    }
}
