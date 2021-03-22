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
        validateAngleParameter(angle);

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
        validateDeltaParameters(dx, dy);

        Direction result = stream()
            .filter(dir -> dir.getX() == dx && dir.getY() == dy)
            .findFirst()
            .orElse(null);

        return result;
    }

    /**
     * Returns the direction corresponding to the given start and end points in.
     * The points must be within an increment of 1 in both the x and y axes.
     * @param start start point
     * @param end end point
     */
    public static Direction from(Point start, Point end) {
        int dx = end.getX() - start.getX();
        int dy = end.getY() - start.getY();

        return from(dx, dy);
    }

    /* GETTERS ---------------------------------------------------------------*/

    /**
     * Delta value in x direction.
     * @return delta x
     */
    public int getX() {
        return dx;
    }

    /**
     * Delta value in y direction.
     * @return delta y
     */
    public int getY() {
        return dy;
    }

    /**
     * Angle of the direction in degrees.
     * @return angle
     */
    public int getAngle() {
        return angle;
    }

    /* DIRECTION LISTS -------------------------------*/

    /**
     * Returns a list of all directions.
     * @return list of all directions
     */
    public static List<Direction> listAll() {
        List<Direction> result = stream()
                                    .collect(Collectors.toList());
        return result;
    }

    /**
     * Returns a list of the cardinal directions (UP, DOWN, LEFT, RIGHT).
     * @return list of cardinal directions
     */
    public static List<Direction> listCardinal() {
        Predicate<Direction> exclude = d -> Math.abs(d.getX()) != Math.abs(d.getY());
        List<Direction> result = stream()
                                    .filter(exclude)
                                    .collect(Collectors.toList());
        return result;
    }

    /**
     * Returns a subset of the list of all directions, excluding any in a
     * backward direction from the current direction. Specifically, it excludes
     * the opposite direction and the two directions found by rotating clockwise
     * and anticlockwise from the opposite direction.
     * @return list of directions 'ahead'
     */
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

    /**
     * Returns the opposite direction to the current direction.
     * @return opposite direction
     */
    public Direction opposite() {
        return from(-this.getX(), -this.getY());
    }

    /**
     * Returns the direction that corresponds to rotating the current direction
     * anticlockwise by 45 degrees.
     * @return direction 45 degrees anticlockwise
     */
    public Direction rotateAnticlockwise() {
        return rotateAnticlockwise(45);
    }

    /**
     * Returns the direction that corresponds to rotating the current direction
     * clockwise by 45 degrees.
     * @return direction 45 degrees clockwise
     */
    public Direction rotateClockwise() {
        return rotateAnticlockwise(315);
    }

    /**
     * Takes an x coordinate and returns the x coordinate reached by adding the
     * delta x value of the direction.
     * @return x + delta-x of direction
     */
    public int transformX(int x) {
        return x + getX();
    }

    /**
     * Takes an y coordinate and returns the y coordinate reached by adding the
     * delta y value of the direction.
     * @return y + delta-y of direction
     */
    public int transformY(int y) {
        return y + getY();
    }

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

    /**
     * Returns a stream comprised of all possible directions.
     * @return stream of all directions
     */
    private static Stream<Direction> stream() {
        return Stream.of(Direction.values());
    }

    /**
     * Checks that the given angle is a positive multiple of 45 and, if not,
     * throws an exception.
     * @param angle angle
     */
    private static void validateAngleParameter(int angle) {
        if (angle % 45 != 0 || angle < 0) {
            throw new IllegalArgumentException(
                    "from(): The angle must be a non-negative multiple of 45" +
                    "degrees. Given: " + angle + ".");
        }
    }

    /**
     * Checks that the given delta-x and delta-y are between -1 and 1 inclusive
     * and, if not, throws an exception.
     * @param dx delta-x
     * @param dy delta-y
     */
    private static void validateDeltaParameters(int dx, int dy) {
        if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
            throw new IllegalArgumentException(
                    "from(): The values of dx and dy must between -1 and 1 " +
                    "inclusive. Given: " + dx + ", " + dy + ".");
        }
    }

    /**
     * Gets the new direction that corresponds to rotating the current direction
     * anticlockwise by the specified number of degrees. The number of degrees
     * must be a positive multiple of 45.
     * @param degrees rotation in degrees
     */
    private Direction rotateAnticlockwise(int degrees) {
        validateAngleParameter(degrees);
        int newAngle = (this.getAngle() + degrees) % 360;
        Direction result = from(newAngle);
        return result;
    }
}
