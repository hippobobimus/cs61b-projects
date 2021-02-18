package bearmaps;

import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ArrayHeapMinPQTest {

    private int getRandomPriority(Random rnd) {
        return rnd.nextInt() * 1_000_000;
    }

    @Test
    public void testConstruction() {
        ArrayHeapMinPQ<String> pq = new ArrayHeapMinPQ<>();
        NaiveMinPQ<String> nn = new NaiveMinPQ<>();

        long seed = 956;
        Random rnd = new Random(seed);

        for (int i = 0; i < 100; i++) {
            int priority = getRandomPriority(rnd);
            pq.add("hi" + i, priority);
            nn.add("hi" + i, priority);
        }

        assertEquals(nn.size(), pq.size());
        assertEquals(nn.getSmallest(), pq.getSmallest());

        assertEquals(nn.removeSmallest(), pq.removeSmallest());
        assertEquals(nn.size(), pq.size());
        assertEquals(nn.getSmallest(), pq.getSmallest());

    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", ArrayHeapMinPQTest.class);
    }
}
