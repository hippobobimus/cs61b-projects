public class ArrayDeque<T> {
    private T[] items;
    private int nextFirst;
    private int nextLast;
    private int size;

    /** Constructs an empty ArrayDeque with an array size of 8. */
    public ArrayDeque() {
        items = (T[]) new Object[8];
        nextFirst = 0;
        nextLast = 1;
        size = 0;
    }

    /** Increments nextFirst. */
    private void incNextFirst() {
        if (--nextFirst < 0)
            nextFirst += items.length;
    }

    /** Decrements nextFirst. */
    private void decNextFirst() {
        if (++nextFirst >= items.length)
            nextFirst -= items.length;
    }

    /** Increments nextLast. */
    private void incNextLast() {
        if (++nextLast >= items.length)
            nextLast -= items.length;
    }

    /** Decrements nextLast. */
    private void decNextLast() {
        if (--nextLast < 0)
            nextLast += items.length;
    }

    /** Returns true if the items array is full, otherwise returns false. */
    private boolean isFull() {
        return items.length == size;
    }

    /** Resizes the items array. */
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        System.arraycopy(items, 0, a, 0, nextLast);
        System.arraycopy(items, nextFirst + 1, a, 0,
                items.length - nextFirst + 1);
        nextFirst += capacity - items.length;
        items = a;
    }

    /** Adds an item of type T to the front of the deque. */
    public void addFirst(T item) {
        items[nextFirst] = item;
        incNextFirst();
        size += 1;
    }

    /** Adds an item of type T to the back of the deque. */
    public void addLast(T item) {
        items[nextLast] = item;
        incNextLast();
        size += 1;
    }

    /** Returns true if the deque is empty, false otherwise. */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns the number of items in the deque. */
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last, separated by a space.
      * Once all the items have been printed, prints out a new line. */
    public void printDeque() {
        for (int i = 0; i < size; i++)
            System.out.print(items[(i + nextFirst + 1) % items.length] + " ");
        System.out.println();
    }

    /** Removes and returns the item at the front of the deque.  If no such item
      * exists, returns null. */
    public T removeFirst() {
        size -= 1;
        decNextFirst();
        return items[nextFirst];
    }

    /** Removes and returns the item at the back of the deque.  If no such item
      * exists, returns null. */
    public T removeLast() {
        size -= 1;
        decNextLast();
        return items[nextLast];
    }

    /** Gets the item at the given index.  If no such item exists, returns
      * null. */
    public T get(int index) {
        if (index >= size || index < 0)
            return null;
        return items[(nextFirst + index + 1) % items.length];
    }
}
