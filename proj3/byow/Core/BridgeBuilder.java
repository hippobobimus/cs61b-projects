package byow.Core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Offers a build method for bridging unconnected regions of the world.
 * @author Rob Masters
 */
public class BridgeBuilder {
    private World world;
    private Random random;

    /* CONSTRUCTORS ----------------------------------------------------------*/

    /**
     * Full constructor. Creates an object with associated build methods for
     * bridging regions in 2D tilespace.
     * @param world world
     */
    public BridgeBuilder(World world) {
        this.world = world;
        this.random = world.getRandom();
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

            if (isBridge(bridge)) {
                openBridge(bridge);
            } else if (r < probability) {
                openBridge(bridge);
            }
        }
    }

    /* PRIVATE HELPER METHODS ------------------------------------------------*/

    /**
     * TODO
     * Surrounding open points.
     */
    private List<Point> surroundingOpenCardinalPoints(Point p) {
        List<Point> result = new ArrayList<>();

        for (Point s : world.exits(p)) {
            if (world.isOpen(s)) {
                result.add(s);
            }
        }

        return result;
    }

    /**
     * Opens the given point and connects it to any open exits.
     * @param bridge bridge point
     */
    private void openBridge(Point bridge) {
        world.open(bridge);
        world.render();
        //System.out.println(world.pathway.get(bridge));
        //System.out.println(world.pathway);

        for (Point exit : world.listOpenExits(bridge)) {
            world.connect(bridge, exit);
        }

        world.animate();
    }

    /**
     * Scans all points in the world and returns a list of all bridging points.
     * @return bridge points
     */
    private List<Point> listAllBridges() {
        List<Point> result = new ArrayList<>();

        for (Point p : world.listAllPoints()) {
            if (isBridge(p)) {
                result.add(p);
            }
        }

        return result;
    }

    /**
     * Determines whether the given point is a bridge point. To be a bridge
     * point it must have 2 unconnected open exits.
     * @param p point
     * @return bridge?
     */
    private boolean isBridge(Point p) {
        List<Point> openExits = surroundingOpenCardinalPoints(p);

        if (openExits.size() != 2) {
            return false;
        }

        Point exit0 = openExits.get(0);
        Point exit1 = openExits.get(1);

        if (world.isConnected(exit0, exit1)) {
            return false;
        }

        return true;
    }
}
