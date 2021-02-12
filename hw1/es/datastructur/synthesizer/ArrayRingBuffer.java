package es.datastructur.synthesizer;
import java.util.Iterator;

public class ArrayRingBuffer<T> implements BoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;
    /* Index for the next enqueue. */
    private int last;
    /* Variable for the fillCount. */
    private int fillCount;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        rb = (T[]) new Object[capacity];
        first = 0;
        last = 0;
        fillCount = 0;
    }

    /** Return size of the buffer. */
    @Override
    public int capacity() {
        return rb.length;
    }

    /** Return number of items currently in the buffer. */
    @Override
    public int fillCount() {
        return fillCount;

    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow").
     */
    @Override
    public void enqueue(T x) {
        if (isFull()) {
            throw new RuntimeException("Ring Buffer overflow");
        }
        rb[last] = x;
        last = (last + 1) % capacity();
        fillCount++;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Ring Buffer underflow");
        }
        T item = rb[first];
        first = (first + 1) % capacity();
        fillCount--;
        return item;
    }

    /**
     * Return oldest item, but don't remove it. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Ring Buffer underflow");
        }
        return rb[first];
    }

    // TODO: When you get to part 4, implement the needed code to support
    //       iteration and equals.
    private class ArrayRingBufferIterator implements Iterator<T> {
        private int pos;

        /** Constructor. */
        public ArrayRingBufferIterator() {
            pos = 0;
        }

        public boolean hasNext() {
            return pos < fillCount();
        }

        public T next() {
            T item = rb[(first + pos++) % capacity()];
            return item;
        }
    }

    /** Return an Iterator over items in the ArrayRingBuffer. */
    public Iterator<T> iterator() {
        return new ArrayRingBufferIterator();
    }

    /** Returns a String representation of the ArrayRingBuffer. */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");
        for (T item : rb) {
            result.append(item);
            result.append(", ");
        }
        result.append("}");
        return result.toString();
    }


    /** Returns true only if the other object is an ArrayRingBuffer with the
      * exact same values. */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        ArrayRingBuffer<T> other = (ArrayRingBuffer<T>) o;
        if (this.fillCount() != other.fillCount()) {
            return false;
        }
        Iterator<T> iter = this.iterator();
        Iterator<T> iter_other = other.iterator();
        while (iter.hasNext()) {
            if (iter.next() != iter_other.next()) {
                return false;
            }
        }
        return true;
    }
}
