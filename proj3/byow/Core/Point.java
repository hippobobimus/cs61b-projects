package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Point {
    private int x;
    private int y;
    private TETile tile;
    private boolean visited;

    Point(int x, int y) {
        this(x, y, Tileset.NOTHING);
    }
    Point(int x, int y, TETile tile) {
        this.x = x;
        this.y = y;
        this.tile = tile;
        this.visited = false;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public TETile getTile() {
        return tile;
    }
    public void setTile(TETile tile) {
        this.tile = tile;
    }
    public boolean visited() {
        return visited;
    }
    public void visit() {
        visited = true;
    }
}

