package byow.Core;

import java.util.stream.Stream;
import static java.util.stream.Collectors.toList;
import static java.lang.Math.abs;
import java.util.List;
import java.util.function.Predicate;

public enum Direction {
    RIGHT (1, 0, 0),
    UP_RIGHT (1, 1, 45),
    UP (0, 1, 90),
    UP_LEFT (-1, 1, 135),
    LEFT (-1, 0, 180),
    DOWN_LEFT (-1, -1, 225),
    DOWN (0, -1, 270),
    DOWN_RIGHT (1, -1, 315);

    private final int x, y, angle;

    private Direction(int x, int y, int angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getAngle() {
        return angle;
    }

    public static Direction from(int angle) {
        Direction result = stream()
            .filter(dir -> dir.getAngle() == angle)
            .findFirst()
            .orElse(null);
        return result;
    }

    public static Direction from(int dx, int dy) {
        Direction result = stream()
            .filter(dir -> dir.getX() == dx && dir.getY() == dy)
            .findFirst()
            .orElse(null);
        return result;
    }

    public static Direction from(Point start, Point end) {
        int dx = end.getX() - start.getX();
        int dy = end.getY() - start.getY();
        return from(dx, dy);
    }

//    public static Direction from(Edge e) {
//        return from(e.from(), e.to());
//    }

    //public List<Direction> ahead() {
    //}
    //
    private static Stream<Direction> stream() {
        return Stream.of(Direction.values());
    }

    public static List<Direction> listAll() {
        List<Direction> result = stream()
                                    .collect(toList());
        return result;
    }

    public static List<Direction> listCardinal() {
        Predicate<Direction> exclude = d -> abs(d.getX()) != abs(d.getY());
        List<Direction> result = stream()
                                    .filter(exclude)
                                    .collect(toList());
        return result;
    }

    public Direction opposite() {
        return from(-this.getX(), -this.getY());
    }

    /** Transforms the direction by rotating it by 45 degrees. The argument
      * supplied determines whether the rotation is anticlockwise (1) or 
      * clockwise (-1). */
    private Direction rotateAnticlockwise(int degrees) {
        int newAngle = (this.getAngle() + degrees) % 360;
        Direction result = from(newAngle);
        return result;
    }

    public Direction rotateAnticlockwise() {
        return rotateAnticlockwise(45);
    }

    public Direction rotateClockwise() {
        return rotateAnticlockwise(315);
    }

    /** Returns a subset of the List of all Directions, excluding the opposite
      * Direction and the two Directions found by rotating clockwise and 
      * anticlockwise from the opposite Direction. */
    public List<Direction> listAhead() {
        Direction opp = this.opposite();
        Direction opp_acw = opp.rotateAnticlockwise();
        Direction opp_cw = opp.rotateClockwise();

        Predicate<Direction> exclude = (Direction d) -> {
            return !(d.equals(opp) || d.equals(opp_acw) || d.equals(opp_cw));
        };

        List<Direction> result = stream()
                                    .filter(exclude)
                                    .collect(toList());
        return result;
    }

    public int transformX(int x) {
        return x + this.x;
    }
    public int transformY(int y) {
        return y + this.y;
    }

    // TODO WRONG! Should not be creating new Points!
    public Point moveFrom(Point start) {
        int x = this.getX() + start.getX();
        int y = this.getY() + start.getY();
        Point result = new Point(x, y);
        return result;
    }
}
