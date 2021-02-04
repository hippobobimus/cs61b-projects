public class LinkedListDeque<T> implements Deque<T> {
    /** Class for creating Nodes which contain an item of generic type
      * and pointers to the next and previous node in the linked list. */
    private class Node {
        public T item;
        public Node next;
        public Node prev;

        /** Node constructor. */
        public Node(T i, Node n, Node p) {
            item = i;
            next = n;
            prev = p;
        }
    }

    private Node sentinel;
    private int size;

    /** Constructs an empty LinkedListDeque. */
    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    /** Adds an item of type T to the front of the deque. */
    @Override
    public void addFirst(T item) {
        Node n = new Node(item, sentinel.next, sentinel);
        if (isEmpty())
            sentinel.prev = n;
        sentinel.next.prev = n;
        sentinel.next = n;
        size += 1;
    }

    /** Adds an item of type T to the back of the deque. */
    @Override
    public void addLast(T item) {
        Node n = new Node(item, sentinel, sentinel.prev);
        if (isEmpty())
            sentinel.next = n;
        sentinel.prev.next = n;
        sentinel.prev = n;
        size += 1;
    }

    /** Returns the number of items in the deque. */
    @Override
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last, separated by a space.
      * Once all the items have been printed, prints out a new line. */
    @Override
    public void printDeque() {
        Node p = sentinel;
        while (p.next != sentinel) {
            p = p.next;
            System.out.print(p.item + " ");
        }
        System.out.println();
    }

    /** Removes and returns the item at the front of the deque.  If no such item
      * exists, returns null. */
    @Override
    public T removeFirst() {
        T item = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size -= 1;
        return item;
    }

    /** Removes and returns the item at the back of the deque.  If no such item
      * exists, returns null. */
    @Override
    public T removeLast() {
        T item = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size -= 1;
        return item;
    }

    /** Gets the item at the given index iteratively.  If no such item exists,
      * returns null. */
    @Override
    public T get(int index) {
        Node p = sentinel;
        while (p.next != null) {
            p = p.next;
            if (index-- == 0)
                return p.item;
        }
        return null;
    }

    /** Helper method for the public getRecursive method. */
    private T getRecursive(Node p, int index) {
        if (index == 0)
            return p.item;
        else
            return getRecursive(p.next, index - 1);
    }

    /** Gets the item at the given index recursively.  If no such item exists,
      * returns null. */
    public T getRecursive(int index) {
        if (index >= size || index < 0)
            return null;
        return getRecursive(sentinel.next, index);
    }
}
