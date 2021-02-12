import java.lang.UnsupportedOperationException;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private Node root;
    private int size;

    private class Node {
        private K key;
        private V value;
        private Node left, right;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public BSTMap() {
        root = null;
        size = 0;
    }

    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        return !(get(key, root) == null);
    }

    private V get(K key, Node node) {
        if (node == null) {
            return null;
        }
        if (key.compareTo(node.key) < 0) {
            return get(key, node.left);
        } else if (key.compareTo(node.key) > 0) {
            return get(key, node.right);
        }
        return node.value;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return get(key, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    private Node put(K key, V value, Node node) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }
        if (key.compareTo(node.key) < 0) {
            node.left = put(key, value, node.left);
        } else if (key.compareTo(node.key) > 0) {
            node.right = put(key, value, node.right);
        } else {
            node.value = value;
        }
        return node;
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        root = put(key, value, root);
    }

    private void printInOrder(Node node) {
        if (node == null) {
            return;
        }
        printInOrder(node.left);
        System.out.print("[" + node.key + ", " + node.value + "], ");
        printInOrder(node.right);
    }

    /** Prints out the BSTMap in order of increasing key. */
    public void printInOrder() {
        System.out.print("{");
        printInOrder(root);
        System.out.println("}");
    }

    /* Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException. */
    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException(
                "BSTMap does not support the keySet() method.");
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException(
                "BSTMap does not support the remove() method.");
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 7. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException(
                "BSTMap does not support the remove() method.");
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException(
                "BSTMap does not support the iterator() method.");
    }
}
