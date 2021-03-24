package byow.Core;

import static byow.Core.Constants.*;
import byow.TileEngine.TERenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldBuilder {
    private Grid grid;
    private Random random;
    private boolean animate = false;
    private TERenderer ter;

    //private UnionFind<Region> regions;

    public WorldBuilder(Grid grid, Random random) {
        this.grid = grid;
        this.random = random;

    }

    private List<Point> getBridgePoints() {
        List<Point> result = new ArrayList<>();

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                Point p = grid.get(x, y);
                if (isBridge(p)) {
                    result.add(p);
                }
            }
        }

        return result;
    }

    private boolean isBridge(Point p) {
        int count = 0;
        List<Point> openExits = new ArrayList<>();
        for (Point exit : grid.exits(p)) {
            if (grid.isOpen(exit)) {
                count++;
                openExits.add(exit);
            }
        }

        if (count != 2) {
            return false;
        }

        if (grid.isConnected(openExits.get(0), openExits.get(1))) {
            return false;
        }

        return true;
    }

    private void openBridge(Point bridge) {
        grid.open(bridge);
        for (Point exit : grid.exits(bridge)) {
            if (grid.isOpen(exit)) {
                grid.connect(bridge, exit);
            }
        }
    }

    private void openBridges() {
        List<Point> bridges = getBridgePoints();

        for (Point bridge : bridges) {
            double r = random.nextDouble();
            if (isBridge(bridge)) {
                openBridge(bridge);
            } else if (r < 0.1) {
                openBridge(bridge);
            }
        }
    }

    public void build() {
        // Rooms
        RoomBuilder rb = new RoomBuilder(grid, random);

        for (int i = 0; i < 40; i++) {
//            int x = random.nextInt(WIDTH - w);
//            int y = random.nextInt(HEIGHT - h);
//            Point start = grid.get(x, y);
            int result = rb.build();
            System.out.println("  >>> " + (result != -1 ? "SUCCESS" : "FAILED"));
        }

        // maze
        MazeBuilder mb = new MazeBuilder(grid, random);

        Point start = grid.get(1, 1);
        mb.build(start);

        openBridges();
        //List<Point> bridges = getBridgePoints();
        //System.out.println(bridges);
    }

    public static void main(String[] args) {

        Grid g = new Grid("animate");

        Random rand = new Random(2873123);

        WorldBuilder wb = new WorldBuilder(g, rand);
        wb.build();

        g.render();

    }
}
