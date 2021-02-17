import static org.junit.Assert.*;
import org.junit.Test;

public class TestUnionFind {

    @Test
    public void test() {
        UnionFind uf = new UnionFind(10);

        uf.connect(0, 1);
        uf.connect(0, 2);
        uf.connect(0, 3);
        uf.connect(0, 4);
        uf.connect(0, 5);
        uf.connect(6, 7);
        uf.connect(6, 8);
        uf.connect(9, 8);

        System.out.println(uf);

        assertEquals(1, uf.find(3));
        assertEquals(6, uf.sizeOf(3));
        assertEquals(7, uf.find(9));
        assertEquals(4, uf.sizeOf(8));

        assertTrue(uf.isConnected(2, 5));
        assertTrue(uf.isConnected(5, 2));
        assertTrue(uf.isConnected(3, 3));
        assertTrue(uf.isConnected(0, 3));
        assertFalse(uf.isConnected(0, 8));
        assertFalse(uf.isConnected(9, 4));

        uf.connect(3, 8);

        System.out.println(uf);

        assertEquals(1, uf.find(3));
        assertEquals(10, uf.sizeOf(3));
        assertEquals(1, uf.find(9));

        assertTrue(uf.isConnected(3, 8));
        assertTrue(uf.isConnected(8, 3));
        assertTrue(uf.isConnected(2, 6));

        System.out.println("After path compression: ");
        System.out.println(uf);
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", TestUnionFind.class);
    }
}
