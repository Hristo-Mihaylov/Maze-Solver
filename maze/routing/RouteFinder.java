// javac -d ./bin maze/routing/*.java
// javac -d ./bin -cp .;junit-platform-console-standalone.jar tests/dev/*.java
// java -jar junit-platform-console-standalone.jar --class-path ./bin --scan-class-path --fail-if-no-tests --disable-ansi-colors

package maze.routing;

import maze.*;
import maze.Maze.Direction;
import maze.Tile.Type;

import java.lang.ClassNotFoundException;
import java.io.EOFException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * The RouteFinder class provides the core logic for solving a given Maze. The
 * RouteFinder uses a java.util.Stack to maintain state as it steps through the
 * maze from the entrance to the exit (possibly/probably via a few dead ends).
 * Only once the stack contains the complete path from entrance to exit
 * (inclusive), will the isFinished method return true.
 */

public class RouteFinder implements Serializable {

    /**
     * @param maze     - Maze
     * @param route    - Stack<Tile>
     * @param finished - boolean
     * @param visited  - List<Tile> initialised as an empty ArrayList<Tile>
     */

    private Maze maze;
    private Stack<Tile> route;
    private boolean finished;
    private List<Tile> visited = new ArrayList<Tile>();

    /**
     * The constructor. It creates a new RouteFinder object and initialises its
     * Stack of tiles as an empty one. The finished variable is set to be false.
     * 
     * @param maze
     */

    public RouteFinder(Maze maze) {
        this.maze = maze;
        route = new Stack<Tile>();
        finished = false;

    }

    /**
     * A getter for the maze.
     * 
     * @return - maze
     */

    public Maze getMaze() {
        return this.maze;
    }

    /**
     * A getter for the route parameter.
     * 
     * @return
     */

    public List<Tile> getRoute() {
        List<Tile> list = new Stack<Tile>();
        for (Tile t : route) {
            list.add(t);
        }

        return list;
    }

    /**
     * A getter for the finished variable. Type - boolean
     * 
     * @return - finished (boolean)
     */

    public boolean isFinished() {
        return this.finished;
    }

    /**
     * This method is used to load a RouteFinder object from a file, specified by
     * the user.
     * 
     * @param filename
     * @return - RouteFinder object
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws EOFException
     */

    public static RouteFinder load(String filename) throws ClassNotFoundException, IOException, EOFException {
        try (ObjectInputStream objectStream = new ObjectInputStream(new FileInputStream(filename))) {
            return (RouteFinder) objectStream.readObject();
        }

    }

    /**
     * This method is used to save a RouteFinder object to a file, specified by the
     * user.
     * 
     * @param filename
     * @throws IOException
     */

    public void save(String filename) throws IOException {
        ObjectOutputStream objectStream = new ObjectOutputStream(new FileOutputStream(filename));
        objectStream.writeObject(this);
        objectStream.close();
    }

    /**
     * This method is the core game logic.
     * 
     * The step method within RouteFinder is responsible for updating the Stack that
     * holds route-finding state. A call to the step method should make exactly one
     * move through the maze -- i.e. either adding or removing one element to/from
     * the Stack. It should not be recursive. Once a maze has been solved, further
     * calls to step should have no effect on any of the state of the RouteFinder.
     * The step method returns a boolean indicating if the maze is complete. If the
     * step method gets stuck in a circular route or otherwise finds that no route
     * to the exit is possible then it should throw a NoRouteFoundException.
     * 
     * @return - boolean - true if we reache the end, otherwise - false.
     */

    public boolean step() {
        // System.out.println(route.toString());

        if (!route.isEmpty() && route.peek().getType() == Type.EXIT) {
            return true;
        }

        if (route.isEmpty() && visited.isEmpty()) {
            route.push(maze.getEntrance());
            visited.add(maze.getEntrance());
            return false;
        }

        Tile t = route.peek();

        if (maze.getTileLocation(t).getY() < maze.getTiles().size() - 1
                && visited.contains(maze.getAdjacentTile(t, Maze.Direction.NORTH)) == false
                && maze.getAdjacentTile(t, Direction.NORTH).isNavigable()) {

            route.push(maze.getAdjacentTile(t, Direction.NORTH));
            visited.add(maze.getAdjacentTile(t, Direction.NORTH));
            t = route.peek();
            // System.out.println(t.getType().toString());

            if (t == maze.getExit()) {
                finished = true;
                return true;
            } else {
                return false;
            }

        }

        if (maze.getTileLocation(t).getX() < maze.getTiles().get(0).size() - 1
                && visited.contains(maze.getAdjacentTile(t, Direction.EAST)) == false
                && maze.getAdjacentTile(t, Direction.EAST).isNavigable()) {

            route.push(maze.getAdjacentTile(t, Direction.EAST));
            visited.add(maze.getAdjacentTile(t, Direction.EAST));
            t = route.peek();
            // System.out.println(t.getType().toString());

            if (t == maze.getExit()) {
                finished = true;
                return true;
            } else {
                return false;
            }

        }

        if (maze.getTileLocation(t).getX() > 0 && visited.contains(maze.getAdjacentTile(t, Direction.WEST)) == false
                && maze.getAdjacentTile(t, Direction.WEST).isNavigable()) {

            route.push(maze.getAdjacentTile(t, Direction.WEST));
            visited.add(maze.getAdjacentTile(t, Direction.WEST));
            t = route.peek();
            // System.out.println(t.getType().toString());

            if (t == maze.getExit()) {
                finished = true;
                return true;
            } else {
                return false;
            }

        }

        if (maze.getTileLocation(t).getY() > 0 && visited.contains(maze.getAdjacentTile(t, Direction.SOUTH)) == false
                && maze.getAdjacentTile(t, Direction.SOUTH).isNavigable()) {

            route.push(maze.getAdjacentTile(t, Direction.SOUTH));
            visited.add(maze.getAdjacentTile(t, Direction.SOUTH));
            t = route.peek();
            // System.out.println(t.getType().toString());

            if (t == maze.getExit()) {
                finished = true;
                return true;
            } else {
                return false;
            }

        } else {
            route.pop();
        }

        if (t == maze.getEntrance()) {
            throw new NoRouteFoundException("No route found!");
        }

        return false;

    }

    /**
     * The toString method returns a string that visualises the entire maze and
     * route-solving state.
     */

    public String toString() {
        String s = "";
        for (List<Tile> rows : maze.getTiles()) {
            for (Tile t : rows) {
                if (route.contains(t)) {
                    s += "* ";
                } else {
                    s += t.toString() + " ";
                }
            }
            s += '\n';
        }

        return s;
    }

    /**
     * An additional method. A getter for visited - used in MazeApplication.
     * 
     * @return - visited - List<Tile>
     */

    public List<Tile> getVisited() {
        return visited;
    }

}
