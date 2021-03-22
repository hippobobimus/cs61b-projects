package byow.Core;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class TestDirection {
    @Test
    public void testFromAngle() {
        assertEquals(Direction.RIGHT, Direction.from(0));
        assertEquals(Direction.UP_RIGHT, Direction.from(45));
        assertEquals(Direction.UP, Direction.from(90));
        assertEquals(Direction.UP_LEFT, Direction.from(135));
        assertEquals(Direction.LEFT, Direction.from(180));
        assertEquals(Direction.DOWN_LEFT, Direction.from(225));
        assertEquals(Direction.DOWN, Direction.from(270));
        assertEquals(Direction.DOWN_RIGHT, Direction.from(315));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromIllegalMagnitudeAngle() {
        Direction.from(40);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromIllegalNegativeAngle() {
        Direction.from(-45);
    }

    @Test
    public void testFromDeltaXY() {
        assertEquals(Direction.RIGHT, Direction.from(1, 0));
        assertEquals(Direction.UP_RIGHT, Direction.from(1, 1));
        assertEquals(Direction.UP, Direction.from(0, 1));
        assertEquals(Direction.UP_LEFT, Direction.from(-1, 1));
        assertEquals(Direction.LEFT, Direction.from(-1, 0));
        assertEquals(Direction.DOWN_LEFT, Direction.from(-1, -1));
        assertEquals(Direction.DOWN, Direction.from(0, -1));
        assertEquals(Direction.DOWN_RIGHT, Direction.from(1, -1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromDeltaXYIllegalX() {
        Direction.from(2, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromDeltaXYIllegalY() {
        Direction.from(0, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromDeltaXYIllegalNegativeX() {
        Direction.from(-2, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromDeltaXYIllegalNegativeY() {
        Direction.from(0, -2);
    }

    @Test
    public void testFromPoints() {
        Point start = new Point(3, 9);
        Point end = new Point(4, 10);

        assertEquals(Direction.UP_RIGHT, Direction.from(start, end));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromPointsIllegalArgs() {
        Point start = new Point(3, 9);
        Point end = new Point(5, 10);

        assertEquals(Direction.UP_RIGHT, Direction.from(start, end));
    }

    @Test
    public void testGetters() {
        Direction up = Direction.UP;
        Direction down_left = Direction.DOWN_LEFT;

        assertEquals(0, up.getX());
        assertEquals(1, up.getY());
        assertEquals(90, up.getAngle());

        assertEquals(-1, down_left.getX());
        assertEquals(-1, down_left.getY());
        assertEquals(225, down_left.getAngle());
    }

    @Test
    public void testListAll() {
        Set<Direction> expected = new HashSet<>();
        expected.add(Direction.RIGHT);
        expected.add(Direction.UP_RIGHT);
        expected.add(Direction.UP);
        expected.add(Direction.UP_LEFT);
        expected.add(Direction.LEFT);
        expected.add(Direction.DOWN_LEFT);
        expected.add(Direction.DOWN);
        expected.add(Direction.DOWN_RIGHT);

        Set<Direction> actual = new HashSet<>(Direction.listAll());

        assertEquals(expected, actual);
    }

    @Test
    public void testListCardinal() {
        Set<Direction> expected = new HashSet<>();
        expected.add(Direction.RIGHT);
        expected.add(Direction.UP);
        expected.add(Direction.LEFT);
        expected.add(Direction.DOWN);

        Set<Direction> actual = new HashSet<>(Direction.listCardinal());

        assertEquals(expected, actual);
    }

    @Test
    public void testListAhead() {
        // cardinal directions
        Set<Direction> upExpected = new HashSet<>();
        upExpected.add(Direction.UP);
        upExpected.add(Direction.UP_LEFT);
        upExpected.add(Direction.UP_RIGHT);
        upExpected.add(Direction.LEFT);
        upExpected.add(Direction.RIGHT);

        Set<Direction> downExpected = new HashSet<>();
        downExpected.add(Direction.DOWN);
        downExpected.add(Direction.DOWN_LEFT);
        downExpected.add(Direction.DOWN_RIGHT);
        downExpected.add(Direction.LEFT);
        downExpected.add(Direction.RIGHT);

        Set<Direction> leftExpected = new HashSet<>();
        leftExpected.add(Direction.LEFT);
        leftExpected.add(Direction.UP_LEFT);
        leftExpected.add(Direction.DOWN_LEFT);
        leftExpected.add(Direction.UP);
        leftExpected.add(Direction.DOWN);

        Set<Direction> rightExpected = new HashSet<>();
        rightExpected.add(Direction.RIGHT);
        rightExpected.add(Direction.UP_RIGHT);
        rightExpected.add(Direction.DOWN_RIGHT);
        rightExpected.add(Direction.UP);
        rightExpected.add(Direction.DOWN);

        Set<Direction> upActual = new HashSet<>(Direction.UP.listAhead());
        Set<Direction> downActual = new HashSet<>(Direction.DOWN.listAhead());
        Set<Direction> leftActual = new HashSet<>(Direction.LEFT.listAhead());
        Set<Direction> rightActual = new HashSet<>(Direction.RIGHT.listAhead());

        assertEquals(upExpected, upActual);
        assertEquals(downExpected, downActual);
        assertEquals(leftExpected, leftActual);
        assertEquals(rightExpected, rightActual);

        // cardinal directions
        Set<Direction> upRightExpected = new HashSet<>();
        upRightExpected.add(Direction.UP_RIGHT);
        upRightExpected.add(Direction.UP);
        upRightExpected.add(Direction.RIGHT);
        upRightExpected.add(Direction.UP_LEFT);
        upRightExpected.add(Direction.DOWN_RIGHT);

        Set<Direction> upRightActual = new HashSet<>(Direction.UP_RIGHT.listAhead());

        assertEquals(upRightExpected, upRightActual);
    }

    @Test
    public void testOpposite() {
        assertEquals(Direction.RIGHT, Direction.LEFT.opposite());
        assertEquals(Direction.UP_RIGHT, Direction.DOWN_LEFT.opposite());
        assertEquals(Direction.UP, Direction.DOWN.opposite());
        assertEquals(Direction.UP_LEFT, Direction.DOWN_RIGHT.opposite());
        assertEquals(Direction.LEFT, Direction.RIGHT.opposite());
        assertEquals(Direction.DOWN_LEFT, Direction.UP_RIGHT.opposite());
        assertEquals(Direction.DOWN, Direction.UP.opposite());
        assertEquals(Direction.DOWN_RIGHT, Direction.UP_LEFT.opposite());
    }

    @Test
    public void testRotate() {
        Direction d = Direction.RIGHT;

        // Test 45 degree rotations clockwise and anticlockwise.
        d = d.rotateClockwise();
        assertEquals(Direction.DOWN_RIGHT, d);

        d = d.rotateClockwise();
        assertEquals(Direction.DOWN, d);

        d = d.rotateAnticlockwise();
        assertEquals(Direction.DOWN_RIGHT, d);

        d = d.rotateAnticlockwise();
        assertEquals(Direction.RIGHT, d);

        d = d.rotateAnticlockwise();
        assertEquals(Direction.UP_RIGHT, d);

        d = d.rotateAnticlockwise();
        assertEquals(Direction.UP, d);
    }

    @Test
    public void testTransformXY() {
        Direction d1 = Direction.UP_RIGHT;

        int x1 = 27;
        int y1 = 92;

        assertEquals(28, d1.transformX(x1));
        assertEquals(93, d1.transformY(y1));

        Direction d2 = Direction.UP_LEFT;

        int x2 = 8;
        int y2 = 12;

        assertEquals(7, d2.transformX(x2));
        assertEquals(13, d2.transformY(y2));
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", TestDirection.class);
    }
}
