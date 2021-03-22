package byow.Core;

import static byow.Core.Constants.*;
import byow.TileEngine.TERenderer;

import java.util.Random;

public class WorldBuilder {
    private Grid grid;
    private Random random;
    private boolean animate = false;
    private TERenderer ter;

    private UnionFind<Region> regions;

    public WorldBuilder(Grid grid, Random random, String animate, TERenderer ter) {
        this.grid = grid;
        this.random = random;
        this.animate = animate.equals("animate") ? true : false;
        this.ter = ter;

        if (this.animate && ter == null) {
            throw new IllegalArgumentException("When animating, a TERenderer" +
                    "must be provided.");
        }

        regions = new UnionFind<>();
    }

    public void build() {
        // Rooms
        RoomBuilder rb = new RoomBuilder(grid, random, 30, "animate", ter);

        for (int i = 0; i < 10; i++) {
            Region r = rb.build();
            regions.add(r);
            System.out.println("  >>> " + (r != null ? "SUCCESS" : "FAILED"));
        }

        // maze
        MazeBuilder mb = new MazeBuilder(grid, random, "animate", ter);

        Point start = grid.get(1, 1);
        mb.buildMaze(start);
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        Grid g = new Grid();

        Random rand = new Random(2873123);

        WorldBuilder wb = new WorldBuilder(g, rand, "animate", ter);
        wb.build();

        ter.renderFrame(g.getTiles());
    }
}
