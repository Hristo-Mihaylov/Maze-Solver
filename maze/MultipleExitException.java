package maze;

/**
 * An exception that has to be thrown if there is more than one exit.
 */

public class MultipleExitException extends InvalidMazeException {

    public MultipleExitException(){
        super("This is a multiple exit exception!");
    }

}