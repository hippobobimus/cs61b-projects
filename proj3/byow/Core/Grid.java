package byow.Core;

import static byow.Core.Constants.*;

import java.util.List;
import java.util.ArrayList;

public class Grid {
    private DirectedPoint[][] grid;

    Grid(int width, int height) {
        grid = new DirectedPoint[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new DirectedPoint(x, y, null, -1);
            }
        }
    }

    public DirectedPoint get(int x, int y) {
        return grid[x][y];
    }

    public List<DirectedPoint> surrounding(DirectedPoint p) {
        int x = p.getX();
        int y = p.getY();

        ArrayList<DirectedPoint> l = new ArrayList<>();

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                DirectedPoint s = grid[x + i][y + j];
                l.add(s);
            }
        }

        return l;
    }

    public List<DirectedPoint> neighbours(DirectedPoint p) {
        int x = p.getX();
        int y = p.getY();

        ArrayList<DirectedPoint> l = new ArrayList<>();

        if (y < HEIGHT - 1) {
            DirectedPoint up = grid[x][y + 1];
            up.setDirection(Direction.UP);
            l.add(up);
        }
        if (y > 0) {
            DirectedPoint down = grid[x][y - 1];
            //System.out.println(down);
            down.setDirection(Direction.DOWN);
            l.add(down);
        }
        if (x > 0) {
            DirectedPoint left = grid[x - 1][y];
            left.setDirection(Direction.LEFT);
            l.add(left);
        }
        if (x < WIDTH - 1) {
            DirectedPoint right = grid[x + 1][y];
            right.setDirection(Direction.RIGHT);
            l.add(right);
        }

        return l;
    }

    public List<DirectedPoint> pointsAhead(DirectedPoint p) {
        int x = p.getX();
        int y = p.getY();
        Direction d = p.getDirection();

        ArrayList<DirectedPoint> l = new ArrayList<>();

        DirectedPoint a1, a2, a3, a4, a5;

        switch(d) {
            case UP:
                if (y == HEIGHT - 1) {
                    return null;
                }
                // in front
                a1 = grid[x - 1][y + 1];
                a2 = grid[x][y + 1];
                a3 = grid[x + 1][y + 1];
                // sides
                a4 = grid[x - 1][y];
                a5 = grid[x + 1][y];
                l.add(a1);
                l.add(a2);
                l.add(a3);
                l.add(a4);
                l.add(a5);
                break;
            case DOWN:
                if (y == 0) {
                    return null;
                }
                a1 = grid[x - 1][y - 1];
                a2 = grid[x][y - 1];
                a3 = grid[x + 1][y - 1];
                a4 = grid[x - 1][y];
                a5 = grid[x + 1][y];
                l.add(a1);
                l.add(a2);
                l.add(a3);
                l.add(a4);
                l.add(a5);
                break;
            case LEFT:
                if (x == 0) {
                    return null;
                }
                a1 = grid[x - 1][y + 1];
                a2 = grid[x - 1][y];
                a3 = grid[x - 1][y - 1];
                a4 = grid[x][y - 1];
                a5 = grid[x][y + 1];
                l.add(a1);
                l.add(a2);
                l.add(a3);
                l.add(a4);
                l.add(a5);
                break;
            case RIGHT:
                if (x == WIDTH - 1) {
                    return null;
                }
                a1 = grid[x + 1][y + 1];
                a2 = grid[x + 1][y];
                a3 = grid[x + 1][y - 1];
                a4 = grid[x][y - 1];
                a5 = grid[x][y + 1];
                l.add(a1);
                l.add(a2);
                l.add(a3);
                l.add(a4);
                l.add(a5);
                break;
        }

        return l;
    }
}
