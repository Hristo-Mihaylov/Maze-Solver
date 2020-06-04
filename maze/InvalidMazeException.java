package maze;

/**
 * An exception that has to be thrown if the maze has invalid tiles.
 */

public class InvalidMazeException extends RuntimeException {

    public InvalidMazeException(String s){
        super(s);
    }

}