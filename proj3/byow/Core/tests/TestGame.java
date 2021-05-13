package byow.Core.tests;

import byow.Core.*;
import static byow.Core.Constants.*;
import byow.TileEngine.TETile;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestGame {
    @Test
    public void testConsistentLevelBuild() {
        Game g0 = new Game();
        Game g1 = new Game();

        g0.initialize(123L);
        g1.initialize(123L);

        TETile[][] f0 = g0.getFrame();
        TETile[][] f1 = g1.getFrame();

        // check tile frames are the same for both builds.
        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                if (!f0[x][y].equals(f1[x][y])) {
                    System.out.println("FRAMES NOT EQUAL!");
                    System.out.println("x-coord=" + x + ", y-coord=" + y);
                    System.out.println("tiles=" + f0[x][y] + ", " + f1[x][y]);
                }
                assertEquals(f0[x][y], f1[x][y]);
            }
        }

    }

    @Test
    public void testGameState() {
        Game g = new Game();

        assertEquals(GameState.MAIN_MENU, g.getState());

        g.setState(GameState.IN_PLAY);
        assertEquals(GameState.MAIN_MENU, g.getPreviousState());
        assertEquals(GameState.IN_PLAY, g.getState());

        g.setState(GameState.BUILDING_LEVEL);
        assertEquals(GameState.IN_PLAY, g.getPreviousState());
        assertEquals(GameState.BUILDING_LEVEL, g.getState());
    }

    @Test
    public void testAnimationSetting() {
        Game g = new Game();

        assertEquals(BUILD_ANIMATION_DEFAULT, g.getAnimationSetting());

        g.disableBuildAnimation();
        assertFalse(g.getAnimationSetting());

        g.enableBuildAnimation();
        assertTrue(g.getAnimationSetting());

        g.toggleBuildAnimation();
        assertFalse(g.getAnimationSetting());
    }
}
