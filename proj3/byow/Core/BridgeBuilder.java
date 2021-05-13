package byow.Core;

import static byow.Core.Constants.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * A class that offers methods for bridging unconnected regions of pathway
 * within a navigable tile grid.
 * @author Rob Masters
 */
public class BridgeBuilder {
    private NavigableTileGrid ntg;
    private Random random;
    private List<Point> candidates;

    /* CONSTRUCTORS ----------------------------------------------------------*/

    /**
     * Full constructor. Creates an object with associated methods for bridging
     * regions in a navigable tile grid.
     * @param ntg navigable tile grid
     * @param random pseudorandom number generator
     */
    public BridgeBuilder(NavigableTileGrid ntg, Random random) {
        this.ntg = ntg;
        this.random = random;
    }

    /* PUBLIC METHODS --------------------------------------------------------*/

    /**
     * Incrementally iterates through all viable bridge points between
     * unconnected regions, connecting all regions into one unified region.
     * Additional connections are opened between already connected regions with
     * the probability set in the Constants class.
     */
    public boolean build() {
        if (candidates == null) {
            candidates = listAllBridges();
            shuffle(candidates);
        }

        if (candidates.isEmpty()) {
            return true;
        }

        Point candidate = candidates.remove(0);

        double r = random.nextDouble();

        if (isBridge(candidate) || r < EXTRA_BRIDGE_PROBABILITY) {
            ntg.openPath(candidate);
            return false;
        } else {
            build();
        }
        return false;
    }

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

    /**
     * Shuffles a given list using the Fisher-Yates/Durstenfeld algorithm.
     * @param list list to be shuffled in place
     */
    private void shuffle(List<Point> list) {
        for (int i = list.size() - 1; i > 1; i--) {
            int r = random.nextInt(i + 1);
            Point p = list.get(r);
            list.set(r, list.get(i));
            list.set(i, p);
        }
    }

    /**
     * Scans all points in the world and returns a list of all potential
     * bridging points.
     * @return bridge points
     */
    private List<Point> listAllBridges() {
        List<Point> result = new ArrayList<>();

        for (Point p : ntg.listAllPoints()) {
            if (isBridge(p)) {
                result.add(p);
            }
        }

        return result;
    }

    /**
     * Determines whether the given point is a bridge candidate point. To be a
     * bridge point it must have 2 unconnected cardinal neighbours on the
     * pathway and not be on the pathway itself.
     * @param p point
     * @return bridge?
     */
    private boolean isBridge(Point p) {
        if (ntg.isPath(p)) {
            return false;
        }

        List<Point> pathNbrs = ntg.listCardinalNeighbours(p).stream()
            .filter(nbr -> ntg.isPath(nbr))
            .collect(Collectors.toList());

        if (pathNbrs.size() != 2) {
            return false;
        }

        Point nbr0 = pathNbrs.get(0);
        Point nbr1 = pathNbrs.get(1);

        if (ntg.isConnected(nbr0, nbr1)) {
            return false;
        }

        return true;
    }
}
