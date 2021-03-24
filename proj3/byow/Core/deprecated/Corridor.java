package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import static byow.Core.Constants.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class Corridor {

    public void carve(Point p) {



    }

    public boolean isTileEmpty() {
        return false;
    }


    private class GridGraph {
        private Map<Vertex, List<Vertex>> adjVertices;
        private Vertex[][] grid;

        GridGraph() {
            adjVertices = new HashMap<Vertex, List<Vertex>>();
            grid = new Vertex[WIDTH][HEIGHT];

            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    Vertex v = new Vertex(Tileset.NOTHING);
                    List<Vertex> l = new ArrayList<>();
                    adjVertices.put(v, l);
                    grid[x][y] = v;
                }
            }

        }



        private class Vertex {
            private TETile tile;
            private int x, y;

            Vertex(TETile tile) {
                this.tile = tile;
            }

            public TETile getTile() {
                return tile;
            }
        }
    }
}
