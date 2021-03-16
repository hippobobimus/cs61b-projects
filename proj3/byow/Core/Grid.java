package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import static byow.Core.Constants.*;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Grid {
    private int width, height;
    private Point[][] grid;
    private TETile[][] tiles;

    // TODO
    private Map<Point, Edge> edgeTo;

    Grid(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new Point[width][height];
        tiles = new TETile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Point(x, y);
                tiles[x][y] = Tileset.NOTHING;
            }
        }
        edgeTo = new HashMap<>();
    }

    Grid() {
        this(WIDTH, HEIGHT);
    }

    public TETile getTile(Point p) {
        int x = p.getX();
        int y = p.getY();
        return tiles[x][y];
    }

    public void setTile(Point p, TETile tile) {
        int x = p.getX();
        int y = p.getY();
        tiles[x][y] = tile;
        p.setTile(tile);
    }

    public TETile[][] getTiles() {
        return tiles;
    }

    /** Returns the Point at the given grid coordinates. */
    public Point get(int x, int y) {
        Point result;
        try {
            result = grid[x][y];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(
                    "get(): The given grid coordinates are outside the bounds " +
                    "of the Grid.");
        }
        return grid[x][y];
    }

    /** Checks whether the given Point is within the boundaries of the Grid. */
    private boolean validPoint(Point p) {
        int x = p.getX();
        int y = p.getY();
        if (x >= width || x < 0) {
            return false;
        }
        if (y >= height || y < 0) {
            return false;
        }
        return true;
    }

    /** Returns a List of Points surrounding the given Point in the given
      * directions. */
    private List<Point> neighbours(Point p, List<Direction> dirs) {
        List<Point> result = new ArrayList<>();

        for (Direction dir : dirs) {
            int neighbourX = dir.transformX(p.getX());
            int neighbourY = dir.transformY(p.getY());
            Point neighbour;
            //Point neighbour = dir.moveFrom(p);
            //TODO Refactor
            // TODO handle invalid arg exception
            try {
                neighbour = get(neighbourX, neighbourY);
            } catch (IllegalArgumentException e) {
                continue;
            }
            result.add(neighbour);
            //if (validPoint(neighbour)) {
            //    result.add(neighbour);
            //}
        }
        return result;
    }

    /** Returns 4 Points directly above, below and to the left and right of the
      * given Point, if they are within the Grid boundary. */
    public List<Point> exits(Point p) {
        List<Direction> dirs = Direction.listCardinal();
//        Direction[] dirs = new Direction[] {
//            Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT
//        };
        return neighbours(p, dirs);
    }

    /** Returns the 8 Points directly surrounding a given Point, if they are 
     * within the Grid boundary. */
    public List<Point> surrounding(Point p) {
        List<Direction> dirs = Direction.listAll();
        //Direction[] dirs = new Direction[] {
       //     Direction.UP_LEFT, Direction.UP, Direction.UP_RIGHT,
       //     Direction.LEFT, Direction.RIGHT,
       //     Direction.DOWN_LEFT, Direction.DOWN, Direction.DOWN_RIGHT
       // };
        return neighbours(p, dirs);
    }

    /** Returns the 5 Points directly ahead and to the sides of the given Point
      * relative to the Direction of travel, if they are within the Grid
      * boundary. */
    public List<Point> ahead(Point p, Direction d) {
        List<Direction> dirs = d.listAhead();
//        int dx = d.getX();
//        int dy = d.getY();
//        List<Direction> dirs = new ArrayList<>();
//        for (Direction dir : Direction.values()) {
//            if (dx != 0 && dir.getX() == -dx) {
//                continue;
//            }
//            if (dy != 0 && dir.getY() == -dy) {
//                continue;
//            }
//            dirs.add(dir);
//        }
        //switch(d) {
        //    case UP:
        //        dirs = new Direction[] {
        //            Direction.UP_LEFT, Direction.UP, Direction.UP_RIGHT,
        //            Direction.LEFT, Direction.RIGHT
        //        };
        //        break;
        //    case DOWN:
        //        dirs = new Direction[] {
        //            Direction.DOWN_LEFT, Direction.DOWN, Direction.DOWN_RIGHT,
        //            Direction.LEFT, Direction.RIGHT
        //        };
        //        break;
        //    case LEFT:
        //        dirs = new Direction[] {
        //            Direction.UP_LEFT, Direction.LEFT, Direction.DOWN_LEFT,
        //            Direction.UP, Direction.DOWN
        //        };
        //        break;
        //    case RIGHT:
        //        dirs = new Direction[] {
        //            Direction.UP_RIGHT, Direction.RIGHT, Direction.DOWN_RIGHT,
        //            Direction.UP, Direction.DOWN
        //        };
        //        break;
        //    default:
        //        throw new IllegalArgumentException(
        //                "ahead(): The given direction must be UP, DOWN, LEFT " +
        //                "or RIGHT. Given '" + d + "'.");
        //}
        return neighbours(p, dirs);
    }

    /** Returns true if the 5 Points ahead of the given point fall within the
     * Grid boundary and are not already navigable. */
    private boolean canExtendPath(Point p, Direction d) {
        List<Point> pointsAhead = ahead(p, d);
        // Any Points outside Grid?
        if (pointsAhead.size() < 5) {
            return false;
        }
        for (Point a : pointsAhead) {
            if (a.navigable()) {
                return false;
            }
        }
        return true;
    }


/***************************************/


//    public List<DirectedPoint> surrounding(DirectedPoint p) {
//        int x = p.getX();
//        int y = p.getY();
//
//        ArrayList<DirectedPoint> l = new ArrayList<>();
//
//        for (int i = -1; i < 2; i++) {
//            for (int j = -1; j < 2; j++) {
//                if (i == 0 && j == 0) {
//                    continue;
//                }
//                DirectedPoint s = grid[x + i][y + j];
//                l.add(s);
//            }
//        }
//
//        return l;
//    }
//
//    public List<DirectedPoint> neighbours(DirectedPoint p) {
//        int x = p.getX();
//        int y = p.getY();
//
//        ArrayList<DirectedPoint> l = new ArrayList<>();
//
//        if (y < HEIGHT - 1) {
//            DirectedPoint up = grid[x][y + 1];
//            up.setDirection(Direction.UP);
//            l.add(up);
//        }
//        if (y > 0) {
//            DirectedPoint down = grid[x][y - 1];
//            //System.out.println(down);
//            down.setDirection(Direction.DOWN);
//            l.add(down);
//        }
//        if (x > 0) {
//            DirectedPoint left = grid[x - 1][y];
//            left.setDirection(Direction.LEFT);
//            l.add(left);
//        }
//        if (x < WIDTH - 1) {
//            DirectedPoint right = grid[x + 1][y];
//            right.setDirection(Direction.RIGHT);
//            l.add(right);
//        }
//
//        return l;
//    }
//
//    public List<DirectedPoint> pointsAhead(DirectedPoint p) {
//        int x = p.getX();
//        int y = p.getY();
//        Direction d = p.getDirection();
//
//        ArrayList<DirectedPoint> l = new ArrayList<>();
//
//        DirectedPoint a1, a2, a3, a4, a5;
//
//        switch(d) {
//            case UP:
//                if (y == HEIGHT - 1) {
//                    return null;
//                }
//                // in front
//                a1 = grid[x - 1][y + 1];
//                a2 = grid[x][y + 1];
//                a3 = grid[x + 1][y + 1];
//                // sides
//                a4 = grid[x - 1][y];
//                a5 = grid[x + 1][y];
//                l.add(a1);
//                l.add(a2);
//                l.add(a3);
//                l.add(a4);
//                l.add(a5);
//                break;
//            case DOWN:
//                if (y == 0) {
//                    return null;
//                }
//                a1 = grid[x - 1][y - 1];
//                a2 = grid[x][y - 1];
//                a3 = grid[x + 1][y - 1];
//                a4 = grid[x - 1][y];
//                a5 = grid[x + 1][y];
//                l.add(a1);
//                l.add(a2);
//                l.add(a3);
//                l.add(a4);
//                l.add(a5);
//                break;
//            case LEFT:
//                if (x == 0) {
//                    return null;
//                }
//                a1 = grid[x - 1][y + 1];
//                a2 = grid[x - 1][y];
//                a3 = grid[x - 1][y - 1];
//                a4 = grid[x][y - 1];
//                a5 = grid[x][y + 1];
//                l.add(a1);
//                l.add(a2);
//                l.add(a3);
//                l.add(a4);
//                l.add(a5);
//                break;
//            case RIGHT:
//                if (x == WIDTH - 1) {
//                    return null;
//                }
//                a1 = grid[x + 1][y + 1];
//                a2 = grid[x + 1][y];
//                a3 = grid[x + 1][y - 1];
//                a4 = grid[x][y - 1];
//                a5 = grid[x][y + 1];
//                l.add(a1);
//                l.add(a2);
//                l.add(a3);
//                l.add(a4);
//                l.add(a5);
//                break;
//        }
//
//        return l;
//    }
}
