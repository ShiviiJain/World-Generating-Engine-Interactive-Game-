package byow;

import byow.Core.RandomUtils;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class WorldGenerator {
    TETile[][] c;
    private int WIDTH;
    private int HEIGHT;
    private  final long SEED;
    private Random RANDOM;
    public int a_x;
    public int a_y;
    public static final int a_initalx = 0;
    public static final int a_initialy = 0;
    public TETile curr_avatar_pos_tile;

    private ArrayList<Room> roomList = new ArrayList<>();
    public WorldGenerator(long input, TETile[][] world){
        SEED = input;
        RANDOM = new Random(SEED);
        c = world;
        WIDTH = c.length;
        HEIGHT = c[0].length;
        int rooms = RANDOM.nextInt(3,17);
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        randomRooms(rooms, world);
        int index1 = 0;
        int index2 = 1;
        while (index2 < rooms) {
            drawPath(roomList.get(index1).getCenter(), roomList.get(index2).getCenter(), world);
            index1 += 1;
            index2 += 1;
        }
        //for (Position p : roomCenters) {
        //    drawPath(roomCenters.get(0), p, world);
        //}
        for(int x = 3; x < WIDTH - 3; x++) {
            for(int y = 3; y < HEIGHT - 3; y++) {
                if (world[x][y].equals(Tileset.FLOOR)) {
                    addWall(x, y , world);
                }
            }
        }
        boolean exit = false;
        while (!exit) {
            int doorCoin = RANDOM.nextInt(rooms);
            exit = roomList.get(doorCoin).addDoor(world);
        }
    }
    private class Room {
        Position start;
        Position center;
        int width;
        int height;
        Position doorN;
        Position doorS;
        Position doorW;
        Position doorE;
        Room(Position start, int width, int height) {
            this.start = start;
            this.width = width;
            this.height = height;
            this.center = new Position(start.x + (width / 2), start.y + (height / 2));
            doorN = new Position(center.x, start.y + height);
            doorS = new Position(center.x, start.y - 1);
            doorW = new Position(start.x - 1, center.y);
            doorE = new Position(start.x + width, center.y);
        }
        Position getStart() {
            return new Position(start.x, start.y);
        }
        Position getCenter() {
            return new Position(center.x, center.y);
        }
        int getWidth() {
            return width;
        }
        int getHeight() {
            return height;
        }

        boolean addDoor(TETile[][] world) {
            boolean trigeer = false;
            int coin = RANDOM.nextInt(4);
            switch (coin) {
                case 0:
                    if (world[center.x][start.y - 1].equals(Tileset.FLOOR)) {
                        break;
                    }
                    world[center.x][start.y - 1] = Tileset.LOCKED_DOOR;
                    trigeer = true;
                    break;
                case 1:
                    if (world[center.x][start.y + height].equals(Tileset.FLOOR)) {
                        break;
                    }
                    world[center.x][start.y + height] = Tileset.LOCKED_DOOR;
                    trigeer = true;
                    break;
                case 2:
                    if (world[start.x - 1][center.y].equals(Tileset.FLOOR)) {
                        break;
                    }
                    world[start.x - 1][center.y] = Tileset.LOCKED_DOOR;
                    trigeer = true;
                    break;
                case 3:
                    if (world[start.x + width][center.y].equals(Tileset.FLOOR)) {
                        break;
                    }
                    world[start.x + width][center.y] = Tileset.LOCKED_DOOR;
                    trigeer = true;
                    break;
            }
            return trigeer;
        }
    }

    private class Position {
        int x;
        int y;
        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Position shift(int dx, int dy) {
            return new Position(x + dx, y + dy);
        }
    }
    /**
     * Randomly place random size rooms
     * */
    public void randomRooms(int rooms, TETile[][] world) {
        int index = 0;
        while (index < rooms) {
            boolean overlap = false;
            int x = RANDOM.nextInt(3, WIDTH - 3);
            int y = RANDOM.nextInt(3, HEIGHT - 3);
            int width = RANDOM.nextInt(2, WIDTH / 5);
            int height = RANDOM.nextInt(2, WIDTH / 5);
            if (x + width >= WIDTH - 2|| y + height >= HEIGHT - 2) {
                continue;
            }
            for (int i = x; i < x + width; i++) {
                for (int j = y; j < y + height; j++) {
                    if (!world[i][j].equals(Tileset.NOTHING)) {
                        overlap = true;
                    }
                }
            }
            if (overlap) {
                continue;
            }
            addRoom(x, y, width, height, world);
            index++;
        }
    }


    /**
     * Draw room
     * */
    public void addRoom(int x, int y, int width, int height, TETile[][] world) {
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                world[i][j] = Tileset.FLOOR;
            }
        }
        Position start = new Position(x, y);
        roomList.add(new Room(start, width, height));
    }

    public void drawPath(Position start, Position end, TETile[][] world) {
        int randX;
        int randY;
        if (start.x == end.x) {
            randX = start.x;
        } else {
            randX = RANDOM.nextInt(Math.min(start.x, end.x), Math.max(start.x, end.x));
        }
        if (start.y == end.y) {
            randY = start.y;
        } else {
            randY = RANDOM.nextInt(Math.min(start.y, end.y), Math.max(start.y, end.y));
        }
        Position randPoint = new Position(randX, randY);
        int coin = RANDOM.nextInt(2);
        switch (coin) {
              case 0:
                  addPathHor(start, randPoint, world);
                  addPathVer(randPoint, start, world);
                  break;
              case 1:
                  addPathHor(randPoint, start, world);
                  addPathVer(start, randPoint, world);
                  break;
          }
          int coin2 = RANDOM.nextInt(2);
          switch (coin2) {
              case 0:
                  addPathHor(end, randPoint, world);
                  addPathVer(randPoint, end, world);
                  break;
              case 1:
                  addPathHor(randPoint, end, world);
                  addPathVer(end, randPoint, world);
                  break;
          }
    }
    /**
     * Draw horizontal hull
     * */
    public void addPathHor(Position start, Position end, TETile[][] world) {
        world[start.x][start.y] = Tileset.FLOOR;
        if (start.x == end.x) {
            return;
        }
        if (start.x <= end.x) {
            addPathHor(start.shift(1, 0), end, world);
        } else {
            addPathHor(start.shift(-1, 0), end, world);
        }
    }
    /**
     * Draw vertical hull
     * */
    public void addPathVer(Position start, Position end, TETile[][] world) {
        world[start.x][start.y] = Tileset.FLOOR;
        if (start.y == end.y) {
            return;
        }
        if (start.y <= end.y) {
            addPathVer(start.shift(0, 1), end, world);
        } else {
            addPathVer(start.shift(0, -1), end, world);
        }
    }

    public void addWall(int x, int y, TETile[][] world) {
        if (world[x - 1][y - 1].equals(Tileset.NOTHING)) {
            world[x - 1][y - 1] = Tileset.WALL;
        }
        if (world[x - 1][y].equals(Tileset.NOTHING)) {
            world[x - 1][y] = Tileset.WALL;
        }
        if (world[x - 1][y + 1].equals(Tileset.NOTHING)) {
            world[x - 1][y + 1] = Tileset.WALL;
        }
        if (world[x + 1][y - 1].equals(Tileset.NOTHING)) {
            world[x + 1][y - 1] = Tileset.WALL;
        }
        if (world[x + 1][y].equals(Tileset.NOTHING)) {
            world[x + 1][y] = Tileset.WALL;
        }
        if (world[x + 1][y + 1].equals(Tileset.NOTHING)) {
            world[x + 1][y + 1] = Tileset.WALL;
        }
        if (world[x][y + 1].equals(Tileset.NOTHING)) {
            world[x][y + 1] = Tileset.WALL;
        }
        if (world[x][y - 1].equals(Tileset.NOTHING)) {
            world[x][y - 1] = Tileset.WALL;
        }
    }

    public void avatar_position(TETile[][] avatar_tile) {
        boolean is_position_same = true;
        while (is_position_same) {
            a_x = RandomUtils.uniform(RANDOM, 17);
            a_y = RandomUtils.uniform(RANDOM, 17);
            if (avatar_tile[a_x][a_y] == Tileset.FLOOR) {
                curr_avatar_pos_tile = avatar_tile[a_x][a_y];
                avatar_tile[a_x][a_y] = Tileset.AVATAR;
                is_position_same = false;
            }
        }

    }
}
