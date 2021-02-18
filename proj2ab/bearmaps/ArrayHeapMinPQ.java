package bearmaps;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.lang.IllegalArgumentException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    private ArrayList<PriorityNode> items;
    private int size;

    public ArrayHeapMinPQ() {
        items = new ArrayList<PriorityNode>();
        size = 0;
    }

    /** Returns the root node. */
    private PriorityNode root() {
        return items.get(0);
    }

    /** Throws an exception if the key is not in the valid range. */
    private void validateKey(int key) {
        if (key < 0 || key >= size) {
            throw new IllegalArgumentException(
                    "Invalid key. Must be between 0 and " + (size() - 1)
                    + ". Given: " + key + ".");
        }
    }

    /** Returns the key of the parent node. */
    private int parent(int key) {
        validateKey(key);
        if (key == 0) {
            return 0;
        }
        return (key - 1) / 2;
    }

    /** Returns the key of the left child node, or -1 if no child exists. */
    private int leftChild(int key) {
        validateKey(key);
        int l = 2 * key + 1;
        if (l >= size) {
            return -1;
        }
        return l;
    }

    /** Returns the key of the right child node, or -1 if no child exists. */
    private int rightChild(int key) {
        validateKey(key);
        int r = 2 * key + 2;
        if (r >= size) {
            return -1;
        }
        return r;
    }

    /** Replaces the node at k1 with the node at k2. */
    private void replace(int k1, int k2) {
        validateKey(k1);
        validateKey(k2);
        items.set(k1, items.get(k2));
        items.remove(k2);
        size--;
    }

    /** Swaps the positions of two nodes. */
    private void swap(int k1, int k2) {
        validateKey(k1);
        validateKey(k2);
        PriorityNode temp = items.get(k1);
        items.set(k1, items.get(k2));
        items.set(k2, temp);
    }

    /** Lifts a node up to the appropriate position based on priority. */
    private void swim(int key) {
        validateKey(key);
        int p = parent(key);
        if (items.get(key).compareTo(items.get(p)) < 0) {
            swap(key, p);
            swim(p);
        }
    }

    /** Drops a node down to the appropriate position based on priority. */
    private void sink(int key) {
        validateKey(key);
        int lKey = leftChild(key);
        int rKey = rightChild(key);
        int smallestChild;
        if (lKey < 0 && rKey < 0) {
            return;
        } else if (lKey < 0) {
            smallestChild = rKey;
        } else if (rKey < 0) {
            smallestChild = lKey;
        } else {
            PriorityNode lNode = items.get(lKey);
            PriorityNode rNode = items.get(rKey);
            smallestChild = lNode.compareTo(rNode) < 0 ? lKey : rKey;
        }
        if (items.get(key).compareTo(items.get(smallestChild)) > 0) {
            swap(key, smallestChild);
            sink(smallestChild);
        }
    }

    /** Returns the key of the last Node (bottom right). */
    private int lastKey() {
        return size() - 1;
    }

    /** Adds a new item to the PQ with associated priority. */
    @Override
    public void add(T item, double priority) {
        items.add(new PriorityNode(item, priority));
        size++;
        swim(lastKey());
    }

    /* Returns true if the PQ contains the given item. */
    @Override
    public boolean contains(T item) {
        // TODO
        return false;
    }

    /* Returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    @Override
    public T getSmallest() {
        if (items.isEmpty()) {
            throw new NoSuchElementException("The PQ is empty.");
        }
        return root().getItem();
    }

    /* Removes and returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    @Override
    public T removeSmallest() {
        if (items.isEmpty()) {
            throw new NoSuchElementException("The PQ is empty.");
        }
        T smallestItem = root().getItem();
        replace(0, lastKey());
        sink(0);
        return smallestItem;
    }

    /* Returns the number of items in the PQ. */
    @Override
    public int size() {
        return size;
    }

    /* Changes the priority of the given item. Throws NoSuchElementException if the item 
     * doesn't exist. */
    @Override
    public void changePriority(T item, double priority) {
        // TODO
    }

    private class PriorityNode implements Comparable<PriorityNode> {
        private T item;
        private double priority;

        public PriorityNode(T item, double priority) {
            this.item = item;
            this.priority = priority;
        }

        T getItem() {
            return item;
        }

        double getPriority() {
            return priority;
        }

        void setPriority(double priority) {
            this.priority = priority;
        }

        @Override
        public int compareTo(PriorityNode other) {
            if (other == null) {
                return -1;
            }
            return Double.compare(this.getPriority(), other.getPriority());
        }
    }
}
