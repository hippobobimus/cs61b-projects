package byow.Core.tests;

import byow.Core.*;
import static byow.Core.Constants.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestInputProcessor {
    @Test
    public void test() {
        Game g = new Game();
        InputSource is = new StringInputDevice("N123S");

        InputProcessor ip = new InputProcessor(g);
        ip.initialize(is);

        assertEquals(GameState.MAIN_MENU, g.getState());

        // N
        ip.process();
        assertEquals(GameState.SEED_ENTRY, g.getState());

        // 1
        ip.process();
        assertEquals(GameState.SEED_ENTRY, g.getState());

        // 2
        ip.process();
        assertEquals(GameState.SEED_ENTRY, g.getState());

        // 3
        ip.process();
        assertEquals(GameState.SEED_ENTRY, g.getState());

        // S
        ip.process();
        assertEquals(GameState.BUILDING_LEVEL, g.getState());

    }
}
