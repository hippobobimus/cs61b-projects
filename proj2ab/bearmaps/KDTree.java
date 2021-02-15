package bearmaps;

import java.util.List;

public class KDTree implements PointSet {
    private Node root;
    private int DIM;

    private class Node {
        private Point point;
        private Node left, right;
        private int partitionIndex;

        public Node(Point point, int depth) {
            this.point = point;
            this.left = null;
            this.right = null;
            this.partitionIndex = depth % DIM;
        }

        /** Returns the squared Euclidean distance between the Node's Point
          * and the given target Point. */
        public double distance(Point target) {
            return Point.distance(target, point);
        }

        /** Returns the Point coordinate value that the Node uses for
          * partitioning. */
        public double key() {
            if (partitionIndex == 0) {
                return point.getX();
            } else {
                return point.getY();
            }
        }

        /** Returns the coordinate value from a given Point corresponding to the
          * dimension in which the Node partitions. */
        public double keyFrom(Point p) {
            if (partitionIndex == 0) {
                return p.getX();
            } else {
                return p.getY();
            }
        }
    }

    private Node put(Point p, Node n, int depth) {
        if (n == null) {
            return new Node(p, depth);
        }
        double nKey = n.key();
        double pKey = n.keyFrom(p);

        depth++;

        if (pKey < nKey) {
            n.left = put(p, n.left, depth);
        } else if (pKey >= nKey) {
            n.right = put(p, n.right, depth);
        }

        return n;
    }

    /** Adds Point p to the tree. */
    private void put(Point p) {
        root = put(p, root, 0);
    }

    /** Constructor. */
    public KDTree(List<Point> points) {
        DIM = 2;
        for (Point p : points) {
            put(p);
        }
    }

    private Node nearest(Node n, Point target, Node best) {
        if (n == null) {
            return best;
        }
        if (n.distance(target) < best.distance(target)) {
            best = n;
        }
        double nKey = n.key();
        double targetKey = n.keyFrom(target);
        Node goodSide, badSide;
        if (targetKey < nKey) {
            goodSide = n.left;
            badSide = n.right;
        } else {
            goodSide = n.right;
            badSide = n.left;
        }
        best = nearest(goodSide, target, best);

        // prune here based on best possible squared Euclidean distance
        double bestPossBadSideDistance = Math.pow(targetKey - nKey, 2);
        if (bestPossBadSideDistance < best.distance(target)) {
            best = nearest(badSide, target, best);
        }

        return best;
    }

    /** Returns the closest point to the inputted coordinates. */
    @Override
    public Point nearest(double x, double y) {
        Point target = new Point(x, y);
        return nearest(root, target, root).point;
    }

    private StringBuilder toString(Node n, StringBuilder sb) {
        if (n == null) {
            return sb;
        }
        sb = toString(n.left, sb);
        sb.append("[");
        sb.append(n.point.getX());
        sb.append(", ");
        sb.append(n.point.getY());
        sb.append("], ");
        sb = toString(n.right, sb);
        return sb;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("{");
        toString(root, result);
        result.append("}");
        return result.toString();
    }
}
