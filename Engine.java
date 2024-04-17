package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.StateMachine;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.WorldGenerator;

public class Engine {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    public StateMachine stateMachine;
    public TERenderer renderer;

    public InputSource inputSource;

    public static boolean isMultiCharAction = false;
    public static String multiCharAction = "";

    public void Engine() {
        stateMachine = new StateMachine(WIDTH, HEIGHT);
        renderer = new TERenderer();
        renderer.initialize(WIDTH, HEIGHT);
        inputSource = new KeyboardInputSource();
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        while (inputSource.possibleNextInput()) {
            char c = inputSource.getNextKey();
            switch (c) {
                case 'N':
                case 'n':
                    //Starting character of multi character input
                    isMultiCharAction = true;
                    multiCharAction = "N";
                    break;
                case ':':
                    //Starting character of multi character input
                    isMultiCharAction = true;
                    multiCharAction = ":";
                    break;
                case 'S':
                case 'Q':
                    //Ending character of multi character input
                    if (isMultiCharAction) {
                        multiCharAction += c;
                        stateMachine.addStateActionFromString(multiCharAction);
                        stateMachine.processStateAction();
                        renderer.renderFrame(stateMachine.getCurrentWorldState());
                        isMultiCharAction = false;
                    }
                    break;
                default:
                    if (isMultiCharAction) {
                        multiCharAction += c;
                    }
            }

            if (!isMultiCharAction) {
                stateMachine.addStateActionFromString( "" + c);
                stateMachine.processStateAction();
                renderer.renderFrame(stateMachine.getCurrentWorldState());
            }
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        // initialize tiles
        stateMachine.addStateActionFromString(input);
        stateMachine.processStateAction();
        return stateMachine.getCurrentWorldState();
    }
}
