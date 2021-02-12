package es.datastructur.synthesizer;
import java.util.Iterator;

public interface BoundedQueue<T> extends Iterable<T> {
    /** Return size of the buffer. */
    int capacity();

    /** Return number of items currently in the buffer. */
    int fillCount();

    /** Add item x to the end. */
    void enqueue(T x);

    /** Delete and return item from the front. */
    T dequeue();

    /** Return (but do not delete) item from the front. */
    T peek();

    /** Returns true if the buffer is empty (fillCount equals zero). */
    default boolean isEmpty() {
        return fillCount() == 0;
    }

    /** Returns true if the buffer is full (fillCount same as capacity). */
    default boolean isFull() {
        return fillCount() == capacity();
    }

    /** Returns an Iterator over items in the BoundedQueue. */
    Iterator<T> iterator();
}
