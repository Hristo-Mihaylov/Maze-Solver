package maze;

/**
 * An exception that is thrown if there is more than one entrance.
 */

public class MultipleEntranceException extends InvalidMazeException {

    public MultipleEntranceException(){
        super("This is a multiple entrance exception!");
    }
}