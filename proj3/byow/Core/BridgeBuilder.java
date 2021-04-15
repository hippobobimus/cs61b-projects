package byow.Core;

import static byow.Core.Constants.*;

import java.util.ArrayList;
import java.util.List;
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
     * Iterates through all viable bridge points between unconnected regions,
     * connecting all regions into one unified region. Additional connections
     * are opened between already connected regions with the given probability.
     * Throws an exception if the probability given is not between 0 and 1.
     * @param probability probability of additional connections
     */
    public void build(double probability) {
        if (probability < 0 || probability > 1) {
            throw new IllegalArgumentException(
                    "The probability must be between 0 and 1. Given: " +
                    probability);
        }

        List<Point> bridges = listAllBridges();

        for (Point bridge : bridges) {
            double r = random.nextDouble();

            if (isBridge(bridge) || r < probability) {
                ntg.openPath(bridge);
                //animate();
            }
        }
    }

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

    /**
     * Scans all points in the world and returns a list of all bridging points.
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
