package byow.Core;

import java.util.HashMap;
import java.util.Map;

public class PointGraph {
    private Map<Point, Map<Direction, Point>> adjacencyMap;

    public PointGraph() {
        this.adjacencyMap = new HashMap<>();
    }

    public void addEdge(Point from, Point to) {
        if (!adjacencyMap.containsKey(from)){
            adjacencyMap.put(from, null);
        }
        if (!adjacencyMap.containsKey(to)) {
            adjacencyMap.put(to, null);
        }

        addEdgeHelper(from, to);
        addEdgeHelper(to, from);
    }

    private void addEdgeHelper(Point from, Point to) {
        Map<Direction, Point> dirMap = adjacencyMap.get(from);

        if (dirMap == null) {
            dirMap = new HashMap<>();
        }

        Direction d = Direction.from(from, to);

        dirMap.put(d, to);
    }

    public Point getNeighbour(Point p, Direction d) {
        Map<Direction, Point> dirMap = adjacencyMap.get(p);

        Point result = dirMap.get(d);

        return result;
    }
}
