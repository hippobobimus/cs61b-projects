package byow.Core;

import byow.TileEngine.TETile;

public interface Element {
    public int getWidth();
    public int getHeight();
    public TETile[][] tiles();
}
