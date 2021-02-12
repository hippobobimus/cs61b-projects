package es.datastructur.synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(10);

        assertTrue(arb.isEmpty());
        assertFalse(arb.isFull());
        assertEquals(0, arb.fillCount());

        arb.enqueue(1);
        assertFalse(arb.isEmpty());
        assertEquals(1, arb.fillCount());

        assertEquals(1, (int) arb.peek());
        assertFalse(arb.isEmpty());
        assertEquals(1, arb.fillCount());

        arb.dequeue();
        assertTrue(arb.isEmpty());
        assertEquals(0, arb.fillCount());

        for (int i = 0; i < 10; i++) {
            arb.enqueue(i);
            assertEquals(i + 1, arb.fillCount());
        }

        assertFalse(arb.isEmpty());
        assertTrue(arb.isFull());

        arb.dequeue();
        assertFalse(arb.isFull());
    }

    @Test
    public void printTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(10);

        for (int i = 0; i < 10; i++) {
            arb.enqueue(i);
        }
        System.out.print("Printing ARB: ");
        System.out.println(arb);
        assertEquals("{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, }", arb.toString());
    }

    @Test
    public void equalsTest() {
        ArrayRingBuffer<Integer> arb1 = new ArrayRingBuffer<>(10);
        ArrayRingBuffer<Integer> arb2 = new ArrayRingBuffer<>(10);
        ArrayRingBuffer<Integer> arb3 = new ArrayRingBuffer<>(10);

        for (int i = 0; i < 10; i++) {
            arb1.enqueue(i);
            arb2.enqueue(i);
            arb3.enqueue(i + 1);
        }
        assertTrue(arb1.equals(arb2));
        assertFalse(arb1.equals(arb3));
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", TestArrayRingBuffer.class);
    }
}
