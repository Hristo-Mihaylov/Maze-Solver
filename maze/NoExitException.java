package maze;

/**
 * An exception that has to be thrown if there is no exit.
 */

public class NoExitException extends InvalidMazeException {

    public NoExitException(){
        super("This is a no exit exception!");
    }
}