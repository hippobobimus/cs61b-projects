public class ArrayDequeSpeedTest {
    public static void main(String[] args) {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        for (int i = 0; i < 1e9; i++) {
            a.addLast(1);
        }
        for (int i = 0; i < 1e9; i++) {
            a.removeLast();
        }
    }
}
