package maze;

/**
 * An exception that has to be thrown if there is no entrance.
 */

public class NoEntranceException extends InvalidMazeException {

    public NoEntranceException(){
        super("This is a no entrance exception!");
    }
}