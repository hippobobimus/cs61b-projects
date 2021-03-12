package byow.Core;

public class Edge {
    private Point p;
    private Point q;

    public Edge(Point p, Point q) {
        this.p = p;
        this.q = q;
    }

    public Point from() {
        return p;
    }

    public Point to() {
        return q;
    }

    public Direction direction() {
        int deltaX = q.getX() - p.getX();
        int deltaY = q.getY() - p.getY();
        return null;
    }
}
