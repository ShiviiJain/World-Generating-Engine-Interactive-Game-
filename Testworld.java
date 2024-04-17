package byow;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import javax.swing.text.Position;

public class Testworld {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 50;

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        WorldGenerator test = new WorldGenerator(1144, world);
        //test.randomRooms(7, world);


        // draws the world to the screen
        ter.renderFrame(world);
    }
}
/**
 *         switch (coin) {
 *             case 0:
 *                 addPathHor(start, midpoint, world);
 *                 addPathVer(start, midpoint, world);
 *             case 1:
 *                 addPathHor(start, midpoint, world);
 *                 addPathVer(start, midpoint, world);
 *         }
 *         int coin2 = RANDOM.nextInt(2);
 *         switch (coin2) {
 *             case 0:
 *                 addPathHor(end, midpoint, world);
 *                 addPathVer(end, midpoint, world);
 *             case 1:
 *                 addPathHor(end, midpoint, world);
 *                 addPathVer(end, midpoint, world);
 *         }
 *         */