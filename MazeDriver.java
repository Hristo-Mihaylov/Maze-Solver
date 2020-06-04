import java.io.IOException;

import maze.routing.*;
import maze.Maze;

public class MazeDriver {

    public static void main(String[] args) throws IOException {

        RouteFinder r = new RouteFinder(Maze.fromTxt("../mazes/maze2.txt"));
        r.step();
        System.out.println(r.toString());

    }

}