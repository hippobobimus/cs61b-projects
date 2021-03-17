package byow.Core;

import java.util.function.Predicate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Representation of a direction in 2D tilespace.
 * @author Rob Masters
 */
public enum Direction {
    RIGHT (1, 0, 0),
    UP_RIGHT (1, 1, 45),
    UP (0, 1, 90),
    UP_LEFT (-1, 1, 135),
    LEFT (-1, 0, 180),
    DOWN_LEFT (-1, -1, 225),
    DOWN (0, -1, 270),
    DOWN_RIGHT (1, -1, 315)
    ;

    private final int dx, dy, angle;

    /* CONSTRUCTOR -----------------------------------------------------------*/

    /**
     * Constructor for enum takes x,y-coordinates and an angle in degrees
     * measured anticlockwise from the positive y-axis.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param angle angle
     */
    private Direction(int dx, int dy, int angle) {
        this.dx = dx;
        this.dy = dy;
        this.angle = angle;
    }

    /* STATIC METHODS --------------------------------------------------------*/

    /**
     * Returns the direction corresponding to the given angle (measured
     * anticlockwise from RIGHT direction). The angle must be a 45 degree
     * increment between 0 and 315 degrees (inclusive).
     * @param angle angle in degrees 
     */
    public static Direction from(int angle) {
        if (angle % 45 != 0 || angle < 0) {
            throw new IllegalArgumentException(
                    "from(): The angle must be a non-negative multiple of 45" +
                    "degrees. Given: " + angle + ".");
        }
        Direction result = stream()
            .filter(dir -> dir.getAngle() == angle)
            .findFirst()
            .orElse(null);
        return result;
    }

    /**
     * Returns the direction corresponding to the given deltas in x and y
     * axes. The delta values must be integers between -1 and 1 inclusive.
     * @param dx delta in x-axis 
     * @param dy delta in y-axis 
     */
    public static Direction from(int dx, int dy) {
        if (dx < -1 || dy < -1 || dx > 1 || dy > 1) {
            throw new IllegalArgumentException(
                    "from(): The values of dx and dy must between -1 and 1 " +
                    "inclusive. Given: " + dx + ", " + dy + ".");
        }
        Direction result = stream()
            .filter(dir -> dir.getX() == dx && dir.getY() == dy)
            .findFirst()
            .orElse(null);
        return result;
    }

    /**
     *
     */
    public static Direction from(Point start, Point end) {
        int dx = end.getX() - start.getX();
        int dy = end.getY() - start.getY();
        return from(dx, dy);
    }

    /* GETTERS ---------------------------------------------------------------*/

    public int getX() {
        return dx;
    }

    public int getY() {
        return dy;
    }

    public int getAngle() {
        return angle;
    }

    /* DIRECTION LISTS -------------------------------*/

    public static List<Direction> listAll() {
        List<Direction> result = stream()
                                    .collect(Collectors.toList());
        return result;
    }

    public static List<Direction> listCardinal() {
        Predicate<Direction> exclude = d -> Math.abs(d.getX()) != Math.abs(d.getY());
        List<Direction> result = stream()
                                    .filter(exclude)
                                    .collect(Collectors.toList());
        return result;
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
                                    .collect(Collectors.toList());
        return result;
    }

    /* DIRECTION TRANSFORMATIONS ---------------------------------------------*/

    public Direction opposite() {
        return from(-this.getX(), -this.getY());
    }

    public Direction rotateAnticlockwise() {
        return rotateAnticlockwise(45);
    }

    public Direction rotateClockwise() {
        return rotateAnticlockwise(315);
    }

    public int transformX(int x) {
        return x + getX();
    }
    public int transformY(int y) {
        return y + getY();
    }

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

    /** Transforms the direction by rotating it by 45 degrees. The argument
      * supplied determines whether the rotation is anticlockwise (1) or 
      * clockwise (-1). */
    private Direction rotateAnticlockwise(int degrees) {
        int newAngle = (this.getAngle() + degrees) % 360;
        Direction result = from(newAngle);
        return result;
    }

    private static Stream<Direction> stream() {
        return Stream.of(Direction.values());
    }
}
