package byow.Core;

public class Edge {
    private Point p;
    private Point q;
    private Direction direction;

    public Edge(Point p, Point q) {
        this.p = p;
        this.q = q;
        this.direction = Direction.from(p, q);
    }

    public Point from() {
        return p;
    }

    public Point to() {
        return q;
    }

    public Direction direction() {
        return direction;
    }
}
