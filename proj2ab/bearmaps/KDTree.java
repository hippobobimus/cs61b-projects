package bearmaps;

import java.util.List;

public class KDTree implements PointSet {
    private Node root;

    private class Node {
        private double x;
        private double y;
        private Node left, right;

        public Node(double x, double y) {
            this.x = x;
            this.y = y;
            this.left = null;
            this.right = null;
        }

        public Node(Point p) {
            this.x = p.getX();
            this.y = p.getY();
            this.left = null;
            this.right = null;
        }
    }

    private Node put(Point point, Node node, char partitionDim) {
        if (node == null) {
            return new Node(point);
        }
        double pointKey;
        double nodeKey;
        if (partitionDim == 'x') {
            pointKey = point.getX();
            nodeKey = node.x;
        } else if (partitionDim == 'y') {
            pointKey= point.getY();
            nodeKey = node.y;
        } else {
            pointKey = 0.0;
            nodeKey = 0.0;
            // throw exception
        }



        if (pointKey < nodeKey) {
            node.left = put(point, node.left, 'y');
        } else if (pointKey > nodeKey) {
            node.right = put(point, node.right, 'y');
        }
        return node;
    }

    private void put(Point point) {
        root = put(point, root, 'x');
    }

    /** Constructor. */
    public KDTree(List<Point> points) {
        for (Point p : points) {
            put(p);
        }
    }

    /** Returns the closest point to the inputted coordinates. */
    public Point nearest(double x, double y) {
        return new Point(0.0, 0.0);

    }
}
