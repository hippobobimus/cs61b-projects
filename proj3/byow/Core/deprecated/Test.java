package byow.Core;

import java.util.PriorityQueue;

public class Test {

    public static void main(String[] args) {
        Test prog = new Test();
        prog.run();
    }

    private PriorityQueue<Node> pq;
    private Node[] arr;

    public void run() {
        pq = new PriorityQueue<Node>((a, b)->b.priority()-a.priority());
        arr = new Node[1];

        Node n = new Node(1, 2, 0);

        System.out.println(n);

        pq.add(n);
        arr[0] = n;

        newMethod(n);
        //n.visit();
        System.out.println(n);
        System.out.println(pq);
        System.out.println(arr[0]);

    }

    private void newMethod(Node n) {
        n.visit();
    }

    private class Node {
        private int x;
        private int y;
        private boolean visited;
        private int priority;

        Node(int x, int y, int priority) {
            this.x = x;
            this.y = y;
            this.priority = priority;
            visited = false;
        }

        public void visit() {
            visited = true;
        }

        public boolean visited() {
            return visited;
        }

        @Override
        public String toString() {
            return "{" + x + ", " + y + ", priority=" + priority + ", visited=" + visited + "}";
        }

        public int priority() {
            return priority;
        }
    }
}
