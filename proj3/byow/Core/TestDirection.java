package byow.Core;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class TestDirection {

    @Test
    public void testListAhead() {
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

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", TestDirection.class);
    }
}
