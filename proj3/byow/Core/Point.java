package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Point {
    private int x;
    private int y;
    private TETile tile;
    private boolean visited;
    private boolean navigable;
    private int priority;

    Point(int x, int y) {
        this(x, y, Tileset.NOTHING, 0);
    }
    Point(int x, int y, TETile tile) {
        this(x, y, tile, 0);
    }
    Point(int x, int y, TETile tile, int priority) {
        this.x = x;
        this.y = y;
        this.visited = false;
        this.priority = priority;
        setTile(tile);
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
    public TETile getTile() {
        return tile;
    }
    public void setTile(TETile tile) {
        this.tile = tile;
        if (tile.equals(Tileset.FLOOR)) {
            this.navigable = true;
        }
    }
    public boolean visited() {
        return visited;
    }
    public void visit() {
        visited = true;
    }
    public boolean navigable() {
        return navigable;
    }
}

