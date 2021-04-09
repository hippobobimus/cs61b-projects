package byow.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    private static final String imgDir = "/byow/TileEngine/tile_images/";

    // Images.
    private static final String wallImg = imgDir + "wall.png";
    private static final String floorImg = imgDir + "floor.png";
    private static final String grassImg = imgDir + "grass.png";
    private static final String avatarDefaultImg = imgDir + "chicken_down.png";
    private static final String avatarUpImg = imgDir + "chicken_up.png";
    private static final String avatarDownImg = imgDir + "chicken_down.png";
    private static final String avatarLeftImg = imgDir + "chicken_left.png";
    private static final String avatarRightImg = imgDir + "chicken_right.png";

    // Avatar.
    public static final TETile AVATAR_DEFAULT = new TETile(
            '@', Color.white, Color.black, "you", avatarDefaultImg);
    public static final TETile AVATAR_UP = new TETile(
            '@', Color.white, Color.black, "you", avatarUpImg);
    public static final TETile AVATAR_DOWN = new TETile(
            '@', Color.white, Color.black, "you", avatarDownImg);
    public static final TETile AVATAR_LEFT = new TETile(
            '@', Color.white, Color.black, "you", avatarLeftImg);
    public static final TETile AVATAR_RIGHT = new TETile(
            '@', Color.white, Color.black, "you", avatarRightImg);

    // Terrain.
    public static final TETile WALL = new TETile(
            '#', new Color(216, 128, 128), Color.darkGray, "wall", wallImg);
    public static final TETile FLOOR = new TETile(
            '·', new Color(128, 192, 128), Color.black, "floor", floorImg);
    public static final TETile GRASS = new TETile(
            '"', Color.green, Color.black, "grass", grassImg);

    // Currently unused.
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
}


