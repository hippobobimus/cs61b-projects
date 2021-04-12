package byow.Core;

import static byow.Core.Constants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * A world extension that adds functionality for bridging unconnected regions of
 * pathway.
 * @author Rob Masters
 */
public class BridgedWorld extends MazeWorld {

    /* CONSTRUCTORS ----------------------------------------------------------*/

    /**
     * Full constructor.
     * @param width width
     * @param height height
     * @param random pseudorandom number generator
     * @param animate animation/no animation
     */
    public BridgedWorld(int width, int height, Random random, String animate) {
        super(width, height, random, animate);
    }

    /* PUBLIC METHODS --------------------------------------------------------*/

    /**
     * Iterates through all viable bridge points between unconnected regions,
     * connecting all regions into one unified region. Additional connections
     * are opened between already connected regions with the given probability.
     * Throws an exception if the probability given is not between 0 and 1.
     * @param probability probability of additional connections
     */
    public void bridgeRegions(double probability) {
        if (probability < 0 || probability > 1) {
            throw new IllegalArgumentException(
                    "The probability must be between 0 and 1. Given: " +
                    probability);
        }

        List<Point> bridges = listAllBridges();

        for (Point bridge : bridges) {
            double r = random.nextDouble();

            if (isBridge(bridge)) {
                openPath(bridge);
                animate();
            } else if (r < probability) {
                openPath(bridge);
                animate();
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

        for (Point p : listAllPoints()) {
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
        if (isPath(p)) {
            return false;
        }

        List<Point> pathNbrs = listCardinalNeighbours(p).stream()
            .filter(nbr -> isPath(nbr))
            .collect(Collectors.toList());

        if (pathNbrs.size() != 2) {
            return false;
        }

        Point nbr0 = pathNbrs.get(0);
        Point nbr1 = pathNbrs.get(1);

        if (isConnected(nbr0, nbr1)) {
            return false;
        }

        return true;
    }

    /* MAIN METHOD -----------------------------------------------------------*/

    /**
     * Adds rooms, fills available screen area with maze(s) and then bridges
     * unconnected regions; animating the process to screen.
     */
    public static void main(String[] args) {
        Random rand = new Random(2873123);

        BridgedWorld bw = new BridgedWorld(WINDOW_WIDTH, WINDOW_HEIGHT, rand, "animate");

        bw.buildRooms(40);
        bw.mazeFill(-1);
        bw.bridgeRegions(0.1);
        bw.reduceDeadEnds(20);
        bw.render();
    }
}
