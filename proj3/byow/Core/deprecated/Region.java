package byow.Core;

import java.util.HashSet;
import java.util.Set;

public class Region {
    Set<Point> points;

    public Region() {
        points = new HashSet<>();
    }

    public void add(Point p) {
        points.add(p);
    }

    public boolean contains(Point p) {
        return points.contains(p);
    }
}
