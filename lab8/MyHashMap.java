import java.lang.UnsupportedOperationException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MyHashMap<K, V> implements Map61B<K, V>, Iterable<K> {
    private List<Entry>[] buckets;
    private Set<K> keys;
    private double loadFactor;
    private int size;

    private class Entry {
        K key;
        V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    /** Generate and reduce a hash code from a given key. */
    private int hash(K key) {
        int h = key.hashCode();
        h = Math.floorMod(h, buckets.length);
        return h;
    }

    /** Resizes (increase size) the HashMap bucket arrays. */
    private void resize(int factor) {
        List<Entry>[] oldBuckets = buckets;
        buckets = (List<Entry>[]) new ArrayList[factor * buckets.length];
        keys.clear();
        size = 0;
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new ArrayList<Entry>();
        }
        for (List<Entry> chain : oldBuckets) {
            for (Entry e : chain) {
                put(e.key, e.value);
            }
        }
    }

    /** Constructors. */
    public MyHashMap() {
        this(16, 0.75);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, 0.75);
    }

    public MyHashMap(int initialSize, double loadFactor) {
        buckets = (List<Entry>[]) new ArrayList[initialSize];
        for (int i = 0; i < initialSize; i++) {
            buckets[i] = new ArrayList<Entry>();
        }
        keys = new HashSet<K>();
        this.loadFactor = loadFactor;
        size = 0;
    }

    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        for (List<Entry> chain : buckets) {
            chain.clear();
        }
        size = 0;
        keys.clear();
    }

    /** Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        List<Entry> chain = buckets[hash(key)];
        for (Entry entry : chain) {
            if (entry.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        List<Entry> chain = buckets[hash(key)];
        for (Entry entry : chain) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    /** Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    @Override
    public void put(K key, V value) {
        List<Entry> chain = buckets[hash(key)];
        for (Entry entry : chain) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
        chain.add(new Entry(key, value));
        keys.add(key);
        size++;
        if (size > 0.75 * buckets.length) {
            resize(2);
        }
    }

    /** Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        return keys;
    }

    /** Returns an iterator over all the keys in the HashMap. */
    public Iterator<K> iterator() {
        return keys.iterator();
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException("remove() unsupported.");
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.
     */
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("remove() unsupported.");
    }
}
