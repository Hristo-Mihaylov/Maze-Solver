package maze;

/**
 * An exception that has to be thrown if the maze does not have rows of equal size.
 */

public class RaggedMazeException extends InvalidMazeException {
    
    public RaggedMazeException(){
        super("This is a ragged maze exception!");
    }

}