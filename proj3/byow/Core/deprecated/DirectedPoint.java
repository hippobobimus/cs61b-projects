package byow.Core;

public class DirectedPoint extends Point {
    private Direction direction;
    private int priority;

    DirectedPoint(int x, int y, Direction d, int priority) {
        super(x, y);
        this.direction = d;
        this.priority = priority;
    }

    public Direction getDirection() {
        return direction;
    }
    public void setDirection(Direction d) {
        direction = d;
    }
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        String s = "{" + getX() + ", " + getY() + ", " + getDirection() + ", priority=" + getPriority() + ", visited=" + visited() + "}";
        return s;
    }
}
