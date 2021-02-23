package bearmaps;

import java.util.Random;
import java.util.Iterator;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayHeapMinPQTest {

    @Test
    public void testAddRemoveSizeSingleItem() {
        ArrayHeapMinPQ<String> pq = new ArrayHeapMinPQ<>();
        NaiveMinPQ<String> nn = new NaiveMinPQ<>();

        assertEquals(nn.size(), pq.size());

        pq.add("hi", 1);
        nn.add("hi", 1);

        assertEquals(nn.size(), pq.size());
        assertEquals(nn.getSmallest(), pq.getSmallest());
        assertEquals(nn.size(), pq.size());
        assertEquals(nn.removeSmallest(), pq.removeSmallest());
        assertEquals(nn.size(), pq.size());
    }

    @Test
    public void testAddRemoveSize() {
        ArrayHeapMinPQ<String> pq = new ArrayHeapMinPQ<>();
        NaiveMinPQ<String> nn = new NaiveMinPQ<>();

        long seed = 956;
        Random rnd = new Random(seed);
        Iterator<Integer> rndInts = rnd.ints(100, 0, 1_000_000).distinct().iterator();

        for (int i = 0; i < 100; i++) {
            //int priority = rnd.nextInt(100);
            int priority = rndInts.next();
            pq.add("hi" + i, priority);
            nn.add("hi" + i, priority);
        }

        for (int i = 0; i < 100; i++) {
            assertEquals(nn.size(), pq.size());
            assertEquals(nn.getSmallest(), pq.getSmallest());
            assertEquals(nn.removeSmallest(), pq.removeSmallest());
            assertEquals(nn.size(), pq.size());
        }
    }

    @Test
    public void testChangePriority() {
        ArrayHeapMinPQ<String> pq = new ArrayHeapMinPQ<>();
        NaiveMinPQ<String> nn = new NaiveMinPQ<>();

        long seed = 956;
        Random rnd = new Random(seed);

        for (int i = 0; i < 100; i++) {
            int priority = rnd.nextInt(100);
            pq.add("hi" + i, priority);
            nn.add("hi" + i, priority);
        }

        assertEquals(nn.getSmallest(), pq.getSmallest());

        int priority = rnd.nextInt(100);
        pq.changePriority(pq.getSmallest(), priority);
        nn.changePriority(nn.getSmallest(), priority);

        assertEquals(nn.getSmallest(), pq.getSmallest());
    }

    @Test
    public void testExample() {
        ArrayHeapMinPQ<String> pq = new ArrayHeapMinPQ<>();
        NaiveMinPQ<String> nn = new NaiveMinPQ<>();

        String[] items = new String[]{"0th", "1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th"};
        int[] priorities = new int[]{1, 9, 5, 4, 8, 2, 3, 0, 7, 6};

        for (int i = 0; i < items.length; i++) {
            pq.add(items[i], priorities[i]);
            nn.add(items[i], priorities[i]);
            //System.out.println("Inserting [" + items[i] + ", " + priorities[i] + "]...");
            //System.out.println(pq);
        }

        System.out.println(pq);

        assertEquals(10, pq.size());
        assertEquals("7th", pq.getSmallest());

        assertEquals("7th", pq.removeSmallest());
        assertEquals(9, pq.size());
        assertEquals("0th", pq.getSmallest());

        assertEquals("0th", pq.removeSmallest());
        assertEquals("5th", pq.removeSmallest());
        assertEquals("6th", pq.removeSmallest());

        assertTrue(pq.contains("3rd"));
        assertFalse(pq.contains("11th"));

        assertEquals("3rd", pq.getSmallest());
        pq.changePriority("3rd", 8);
        assertEquals("2nd", pq.getSmallest());

    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", ArrayHeapMinPQTest.class);
    }
}
