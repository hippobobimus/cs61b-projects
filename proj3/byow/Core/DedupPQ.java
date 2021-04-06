package byow.Core;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * An extension of the PriorityQueue class that prohibits duplicate entries.
 * @author Rob Masters
 */
public class DedupPQ<T> extends PriorityQueue<T> {
    private static final long serialVersionUID = 124987587318L;
    private Set<T> items;

    public DedupPQ(Comparator<? super T> comparator) {
        super(comparator);
        items = new HashSet<>();
    }

    @Override
    public boolean add(T item) {
        if (items.contains(item)) {
            return false;
        }
        return super.add(item);
    }

    @Override
    public void clear() {
        super.clear();
        items.clear();
    }

    @Override
    public boolean offer(T item) {
        if (items.contains(item)) {
            return false;
        }
        return super.offer(item);
    }

    @Override
    public T poll() {
        T item = super.poll();

        items.remove(item);

        return item;
    }

    @Override
    public boolean remove(Object o) {
        boolean result = super.remove(o);

        items.remove(o);

        return result;
    }
}
