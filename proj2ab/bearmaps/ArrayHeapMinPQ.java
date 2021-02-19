package bearmaps;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.NoSuchElementException;
import java.lang.IllegalArgumentException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    private ArrayList<PriorityNode> heap;
    private TreeMap<T, PriorityNode> items;
    private int size;

    public ArrayHeapMinPQ() {
        heap = new ArrayList<PriorityNode>();
        size = 0;

        items = new TreeMap<T, PriorityNode>();
    }

    /** Adds a new item to the PQ with associated priority. */
    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("Item already present.");
        }
        PriorityNode node = new PriorityNode(item, priority, size);
        heap.add(node);
        items.put(item, node);
        size++;
        swim(last());
    }

    /* Returns true if the PQ contains the given item. */
    @Override
    public boolean contains(T item) {
        return items.containsKey(item);
    }

    /* Returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    @Override
    public T getSmallest() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("The PQ is empty.");
        }
        return root().getItem();
    }

    /* Removes and returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    @Override
    public T removeSmallest() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("The PQ is empty.");
        }
        T smallestItem = root().getItem();
        replaceRootWithLast();
        if (size() > 0) {
            sink(root());
        }
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
        PriorityNode node = items.get(item);
        if (node == null) {
            throw new NoSuchElementException("Item not found.");
        }
        node.setPriority(priority);
        swim(node);
        sink(node);
    }

    @Override
    public String toString() {
        int depth = ((int) (Math.log(size()) / Math.log(2)));
        int level = 0;
        int itemsUntilNext = 0;
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < Math.pow(2, depth) - 1; j++) {
                result.append("     ");
            }
            result.append(heap.get(i));
            for (int j = 0; j < Math.pow(2, depth) - 1; j++) {
                result.append("     ");
            }
            if (i == itemsUntilNext) {
                result.append("\n");
                level++;
                itemsUntilNext += Math.pow(2, level);
                depth--;
            }
        }
        result.append("\n");
        return result.toString();
    }

    /** HELPER METHODS  */

    /** Returns the root node. */
    private PriorityNode root() {
        return heap.get(0);
    }

    /** Returns the last node. */
    private PriorityNode last() {
        return heap.get(size - 1);
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

    private void removeLast() {
        PriorityNode l = last();
        items.remove(l.getItem());
        heap.remove(l.getKey());
        size--;
    }

    /** Replaces the root node with the last node. */
    private void replaceRootWithLast() {
        swap(root(), last());
        removeLast();
    }

    /** Swaps the positions of two nodes. */
    private void swap(int k1, int k2) {
        validateKey(k1);
        validateKey(k2);
        if (k1 == k2) {
            return;
        }
        PriorityNode node1 = heap.get(k1);
        PriorityNode node2 = heap.get(k2);
        heap.set(k1, node2);
        node2.setKey(k1);
        heap.set(k2, node1);
        node1.setKey(k2);
    }

    private void swap(PriorityNode node1, PriorityNode node2) {
        swap(node1.getKey(), node2.getKey());
    }

    private void swim(int key) {
        validateKey(key);
        int p = parent(key);
        if (heap.get(key).compareTo(heap.get(p)) < 0) {
            swap(key, p);
            swim(p);
        }
    }

    /** Lifts a node up to the appropriate position based on priority. */
    private void swim(PriorityNode node) {
        swim(node.getKey());
    }

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
            PriorityNode lNode = heap.get(lKey);
            PriorityNode rNode = heap.get(rKey);
            smallestChild = lNode.compareTo(rNode) < 0 ? lKey : rKey;
        }
        if (heap.get(key).compareTo(heap.get(smallestChild)) > 0) {
            swap(key, smallestChild);
            sink(smallestChild);
        }
    }

    /** Drops a node down to the appropriate position based on priority. */
    private void sink(PriorityNode node) {
        sink(node.getKey());
    }

    private class PriorityNode implements Comparable<PriorityNode> {
        private T item;
        private double priority;
        private int key;

        public PriorityNode(T item, double priority, int key) {
            this.item = item;
            this.priority = priority;
            this.key = key;
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

        int getKey() {
            return key;
        }

        void setKey(int key) {
            this.key = key;
        }

        @Override
        public int compareTo(PriorityNode other) {
            if (other == null) {
                return -1;
            }
            return Double.compare(this.getPriority(), other.getPriority());
        }

        @Override
        public String toString() {
            return "[" + item + ", " + priority + "]";
        }
    }
}
