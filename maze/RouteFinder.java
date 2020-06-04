package maze.routing;

import maze.*;
import maze.Maze.Direction;
import maze.routing.NoRouteFoundException;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class RouteFinder implements Serializable {
    private Maze maze;
    private Stack<Tile> route;
    private boolean finished;

    public RouteFinder(Maze maze) {
        this.maze = maze;
        Stack<Tile> route = new Stack<Tile>();
        finished = false;
    }

    public Maze getMaze() {
        return this.maze;
    }

    public List<Tile> getRoute() {
        List<Tile> list = new Stack<Tile>();
        for (Tile t : route) {
            list.add(t);
        }

        return list;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public static RouteFinder load(String filename) {

        try (ObjectInputStream objectStream = new ObjectInputStream(new FileInputStream(filename + ".obj"));) {
            return (RouteFinder) objectStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not read " + filename + ".obj");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: problem when reading " + filename + ".obj");
        } finally {
            System.out.println("** Finally **");
            throw new RuntimeException("This is not the error you are looking for.");
        }

    }

    public void save(String filename) {
        ObjectOutputStream objectStream = null;
        try {
            objectStream = new ObjectOutputStream(new FileOutputStream(filename + ".obj"));
            objectStream.writeObject(this);
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not open " + filename + ".obj for writing.");
        } catch (IOException e) {
            System.out.println("Error: IOException when writing " + filename + ".obj");
        } finally {
            try {
                objectStream.close();
            } catch (IOException e) {
                System.out.println("Error: IOException when closing " + filename + ".obj");
            }
        }

    }

    // Game logic here!!!

    public boolean step() {
        boolean complete = false;
        List<Tile> visited = new ArrayList<Tile>();

        while (complete == false) {

            if (visited.isEmpty() && route.isEmpty()) {
                route.add(maze.getEntrance());
                visited.add(maze.getEntrance());
            }

            Tile t = route.lastElement();

            if (visited.contains(maze.getAdjacentTile(t, Direction.EAST)) == false
                    && maze.getAdjacentTile(t, Direction.EAST).isNavigable()) {

                route.add(maze.getAdjacentTile(t, Direction.EAST));
                visited.add(maze.getAdjacentTile(t, Direction.EAST));

            }

            if (visited.contains(maze.getAdjacentTile(t, Direction.NORTH)) == false
                    && maze.getAdjacentTile(t, Direction.NORTH).isNavigable()) {

                route.add(maze.getAdjacentTile(t, Direction.NORTH));
                visited.add(maze.getAdjacentTile(t, Direction.NORTH));

            }

            if (visited.contains(maze.getAdjacentTile(t, Direction.WEST)) == false
                    && maze.getAdjacentTile(t, Direction.WEST).isNavigable()) {

                route.add(maze.getAdjacentTile(t, Direction.WEST));
                visited.add(maze.getAdjacentTile(t, Direction.WEST));

            }

            if (visited.contains(maze.getAdjacentTile(t, Direction.SOUTH)) == false
                    && maze.getAdjacentTile(t, Direction.SOUTH).isNavigable()) {

                route.add(maze.getAdjacentTile(t, Direction.SOUTH));
                visited.add(maze.getAdjacentTile(t, Direction.SOUTH));

            }
            if (t == maze.getEntrance()) {
                throw new NoRouteFoundException("No route found!");
            }

            if (t == maze.getExit()) {
                complete = true;
            }

            else {
                route.pop();
            }

        }

        return complete;
    }

    public String toString() {
        String s = "";
        return s;
    }

}