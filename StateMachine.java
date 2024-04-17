package byow;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class StateMachine {

    private  Random RANDOM;

    private int WIDTH;
    private int  HEIGHT;

    private long SEED;

    private int positionX;

    private int positionY;

    private int prevPositionX;

    private int prevPositionY;

    private Queue<StateAction> stateActions;

    private TETile[][] world;

    public StateMachine(int width, int height) {
        WIDTH = width;
        HEIGHT = height;
        world = new TETile[WIDTH][HEIGHT];
        stateActions =  new LinkedList<>();
    }

    public void setRandomSeed(long seed) {
        SEED = seed;
        RANDOM = new Random(SEED);
        setRandomWorld();
        setInitialRandomAvatarPosition();
    }

    public void setInitialRandomAvatarPosition() {
        int coinX = RANDOM.nextInt(WIDTH);
        int coinY = RANDOM.nextInt(HEIGHT);

        TETile current = world[coinX][coinY];
        if(current.equals(Tileset.FLOOR)){
            positionX = coinX;
            positionY = coinY;
            prevPositionX = coinX;
            prevPositionY = coinY;
        } else {
            setInitialRandomAvatarPosition();
        }
    }

    public void setRandomWorld() {
        WorldGenerator newWorld = new WorldGenerator(SEED, world);
    }

    public TETile[][] getCurrentWorldState() {
        return  world;
    }

    public void addStateActionFromString(String input) {
        for(int i = 0; i < input.length(); i++) {
            switch (input.charAt(i)) {
                case 'W':
                case 'w':
                    stateActions.add(StateAction.MOVE_UP);
                    break;
                case 'S':
                case 's':
                    stateActions.add(StateAction.MOVE_DOWN);
                    break;
                case 'A':
                case 'a':
                    stateActions.add(StateAction.MOVE_LEFT);
                    break;
                case 'D':
                case 'd':
                    stateActions.add(StateAction.MOVE_RIGHT);
                    break;
                case ':':
                    stateActions.add(StateAction.SAVE_AND_CLOSE);
                    i++;
                    break;
                case 'N':
                    String s = "";
                    while(input.charAt(i) != 'S' || input.charAt(i) != 's') {
                        s += input.charAt(i);
                    }
                    setRandomSeed(Long.parseLong(s));
                    break;
            }
        }
    }

    public void processStateAction() {
        while (!stateActions.isEmpty()) {
            switch (stateActions.peek()) {
                case MOVE_UP:
                    moveUp();
                    break;
                case MOVE_DOWN:
                    moveDown();
                    break;
                case MOVE_LEFT:
                    moveLeft();
                    break;
                case MOVE_RIGHT:
                    moveRight();
                    break;
                case SAVE_AND_CLOSE:
                    saveAndClose();
                    break;
            }
            stateActions.poll();
        }
    }

    public void moveUp() {
        if(isValidLocation(positionX, positionY + 1)) {
            prevPositionY = positionY;
            positionY++;
            moveAvatar();
        }
    }

    public void moveDown() {
        if(isValidLocation(positionX, positionY - 1)) {
            prevPositionY = positionY;
            positionY--;
            moveAvatar();
        }
    }

    public void moveLeft() {
        if(isValidLocation(positionX - 1, positionY)) {
            prevPositionX = positionX;
            positionX--;
            moveAvatar();
        }
    }

    public void moveRight() {
        if(isValidLocation(positionX + 1, positionY)) {
            prevPositionX = positionX;
            positionX++;
            moveAvatar();
        }
    }

    public void moveAvatar() {
        world[positionX][positionY] = Tileset.AVATAR;
        world[prevPositionX][prevPositionY] = Tileset.FLOOR;
    }

    public boolean isValidLocation(int x, int y) {
        if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
            return false;
        }

        if(world[x][y].equals(Tileset.FLOOR)) {
            return true;
        }

        return false;
    }

    public void saveAndClose() {

    }

}
