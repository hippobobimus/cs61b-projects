package byow.Core;

import java.util.HashMap;
import java.util.Map;

public class UnionFind<T> {
    Map<T, T> items;  // maps item to parent.
    Map<T, Integer> sizes;  // maps item to size of subset.

    /* CONSTRUCTOR METHODS ---------------------------------------------------*/

    /**
     * Constructor that produces an empty UnionFind data structure.
     */
    public UnionFind() {
        items = new HashMap<>();
        sizes = new HashMap<>();
    }

    /* PUBLIC METHODS --------------------------------------------------------*/

    /**
     * Adds an unconnected item to the UnionFind.
     * @param item item
     */
    public void add(T item) {
        // item is its own parent (i.e. unconnected).
        items.put(item, item);
        // no other items in subset.
        sizes.put(item, 1);
    }

    /** 
     * Connects two items together. The items must be contained within the 
     * UnionFind and a union-by-size heuristic is used. 
     * @param item1 first item
     * @param item2 second item
     */
    public void connect(T item1, T item2) {
        validate(item1);
        validate(item2);

        T root1 = find(item1);
        T root2 = find(item2);
        int s1 = sizeOf(root1);
        int s2 = sizeOf(root2);

        if (s1 > s2) {
            // set parent of root2 to root1.
            items.put(root2, root1);
            // increase size of root1's tree.
            sizes.put(root1, s1 + s2);
        } else {
            // set parent of root1 to root2.
            items.put(root1, root2);
            // increase size of root2's tree.
            sizes.put(root2, s1 + s2);
        }
    }

    /**
     * Determines whether the two given items are connected. The two items must
     * be members of the UnionFind.
     * @param item1 first item
     * @param item2 second item
     * @return whether the items are connected
     */
    public boolean isConnected(T item1, T item2) {
        validate(item1);
        validate(item2);

        T root1 = find(item1);
        T root2 = find(item2);

        // connected if they share a common root.
        return root1.equals(root2);
    }

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

    /**
     * Determines whether the given item is valid. To be valid it must be a
     * member of the UnionFind.
     * @param item item
     */
    private void validate(T item) {
        if (!items.containsKey(item)) {
            throw new IllegalArgumentException(
                    "Given item not found in UnionFind.");
        }
    }

    /**
     * Returns the parent of the given item.
     * @param item item
     * @return parent
     */
    private T parent(T item) {
        return items.get(item);
    }

    /**
     * Finds the root of the set the given item belongs to.
     * @param item item
     * @return root
     */
    private T find(T item) {
        if (parent(item).equals(item)) {
            return item;
        } else {
            items.put(item, find(parent(item)));  // Path compression.
            return parent(item);
        }
    }

    /**
     * Returns the size of the subset of items rooted at the given item.
     * @param item
     * @return size of subset
     */
    private int sizeOf(T item) {
        return sizes.get(item);
    }

    /* OVERRIDDEN METHODS ----------------------------------------------------*/

    /**
     * Returns a String representation of the UnionFind.
     * @return String representation
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("{");
        for (Map.Entry<T, T> entry : items.entrySet()) {
            result.append("[");
            result.append(entry.getKey());
            result.append(", parent=");
            result.append(entry.getValue());
            result.append("], ");
        }
        result.append("}");
        return result.toString();
    }
}
