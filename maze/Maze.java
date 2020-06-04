//javac -d ./bin maze/routing/*.java
//javac -d ./bin -cp .;junit-platform-console-standalone.jar tests/dev/*.java
//java -jar junit-platform-console-standalone.jar --class-path ./bin --scan-class-path --fail-if-no-tests --disable-ansi-colors
package maze;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

/**
 * A Maze is an object that represents the maze to be solved. Each Maze contains
 * a two-dimensional ArrayList of Tile objects. Each Maze contains exactly one
 * entrance, one exit, and all rows of tiles must be the same length. If any of
 * these constraints are not met, an appropriate subclass of
 * InvalidMazeException should be thrown. The fromTxt method allows a Maze
 * object to be created by reading in a txt file (the file path is passed as a
 * String parameter), and the toString method should return a string that
 * visualises the entire maze.
 */

public class Maze implements Serializable {

    /**
     * @param entrance - Tile
     * @param exit     - Tile
     * @param tiles    - List<List<Tile>>
     */

    private Tile entrance;
    private Tile exit;
    private List<List<Tile>> tiles;

    /**
     * The constructor. It creates a maze and initialises an empty
     * ArrayList<List<Tile>>
     */

    private Maze() {
        tiles = new ArrayList<List<Tile>>();
    }

    /**
     * This method is used to create a maze from a text file. It reads through a
     * text file and creates a two-dimensional ArrayList of Tile objects.
     * 
     * @param filename
     * @return maze
     * @throws IOException
     * @throws FileNotFoundException
     * @throws RaggedMazeException
     * @throws NoEntranceException
     * @throws NoExitException
     * @throws InvalidMazeException
     */

    public static Maze fromTxt(String filename) throws IOException, FileNotFoundException {
        Maze m = new Maze();
        m.tiles = new ArrayList<List<Tile>>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        String line = bufferedReader.readLine();
        // List<Tile> rows = new ArrayList<Tile>();
        boolean noEntrance = true;
        boolean noExit = true;
        boolean raggedMaze = true;
        boolean invalidMaze = false;
        List<Integer> rowsLength = new ArrayList<Integer>();
        m.tiles.add(new ArrayList<Tile>());
        int promenliva = m.tiles.size() - 1;
        while (line != null) {
            char[] c = line.toCharArray();
            for (int i = 0; i < c.length; i++) {
                Tile gadno = Tile.fromChar(c[i]);
                // rows.add(Tile.fromChar(c[i]));
                m.tiles.get(promenliva).add(gadno);

                if (c[i] == 'e') {
                    noEntrance = false;
                    m.setEntrance(gadno);
                }
                if (c[i] == 'x') {
                    noExit = false;
                    m.setExit(gadno);
                }
                if (c[i] != '.' && c[i] != '#' && c[i] != 'e' && c[i] != 'x') {
                    invalidMaze = true;
                }

            }
            rowsLength.add(c.length);
            // System.out.println(rows);
            // m.tiles.add(rows);
            // rows.clear();

            promenliva += 1;
            line = bufferedReader.readLine();
            m.tiles.add(new ArrayList<Tile>());

        }
        m.tiles.remove(m.tiles.size() - 1);
        // System.out.println(m.tiles);
        int first = rowsLength.get(0);
        for (Integer item : rowsLength) {
            if (item != first) {
                raggedMaze = false;
                break;
            }
        }

        if (raggedMaze == false) {
            throw new RaggedMazeException();
        }
        if (noEntrance == true) {
            throw new NoEntranceException();
        }
        if (noExit == true) {
            throw new NoExitException();
        }
        if (invalidMaze == true) {
            throw new InvalidMazeException("This is an invalid maze exception!");
        }

        return m;
    }

    /**
     * This method is used to find an adjacent tile to a specific one in a given
     * direction.
     * 
     * @param t
     * @param d
     * @return Tile
     */

    public Tile getAdjacentTile(Tile t, Direction d) {
        int xDiff = 0;
        int yDiff = 0;
        int x = 0;
        int y = 0;
        if (d == Direction.NORTH) {
            yDiff = 1;
        }
        if (d == Direction.SOUTH) {
            yDiff = -1;
        }
        if (d == Direction.EAST) {
            xDiff = 1;
        }
        if (d == Direction.WEST) {
            xDiff = -1;
        }
        for (List<Tile> rowsList : tiles) {
            for (Tile wanted : rowsList) {
                if (wanted == t) {
                    x = rowsList.indexOf(wanted);
                    y = tiles.indexOf(rowsList);
                    break;
                }
            }
        }

        return tiles.get(y - yDiff).get(x + xDiff);
    }

    /**
     * A getter for the paramether of type Tile entrance.
     * 
     * @return Tile
     */

    public Tile getEntrance() {
        return entrance;
    }

    /**
     * A getter for the paramether of type Tile exit.
     * 
     * @return Tile
     */

    public Tile getExit() {
        return exit;
    }

    /**
     * A method that uses an object of type Coordinate to access and return an
     * object of type Tile at specific coordinates.
     * 
     * @param c
     * @return Tile
     */

    public Tile getTileAtLocation(Coordinate c) {
        int x = c.getX();
        int y = c.getY();
        int cSize = tiles.size();

        return tiles.get(cSize - y - 1).get(x);
    }

    /**
     * A method that returns an object of Type Coordinate given the coordinates of a
     * given tile in the maze.
     * 
     * @param t
     * @return
     */

    public Coordinate getTileLocation(Tile t) {
        int x = 0;
        int y = 0;
        for (List<Tile> rowsList : tiles) {
            for (Tile wanted : rowsList) {
                if (t == wanted) {
                    x = rowsList.indexOf(wanted);
                    y = tiles.indexOf(rowsList);
                    break;
                }
            }
        }
        int yNew = tiles.size() - y - 1;
        return new Coordinate(x, yNew);
    }

    /**
     * A getter for the parameter of type List<List<Tile>> tiles.
     * 
     * @return
     */

    public List<List<Tile>> getTiles() {
        return tiles;
    }

    /**
     * This method checks if entrance is empty, and if empty, checks if the tile we
     * want to set as an entrance is in the maze. If it is in the maze, we set it to
     * be the entrance.
     * 
     * @param e
     * @throws MultipleEntranceException
     */

    private void setEntrance(Tile e) {

        if (entrance != null) {
            throw new MultipleEntranceException();
        }

        boolean inMaze = false;
        for (List<Tile> row : tiles) {
            for (Tile t : row) {
                if (e == t) {
                    inMaze = true;
                    break;
                }
            }
        }

        if (inMaze) {
            if (entrance == null) {
                entrance = e;
            }
        }
    }

    /**
     * This method checks if exit is empty, and if empty, checks if the tile we want
     * to set as an exit is in the maze. If it is in the maze, we set it to be the
     * exit.
     * 
     * @param x
     * @throws MultipleExitException
     */

    private void setExit(Tile x) {
        if (exit != null) {
            throw new MultipleExitException();
        }

        boolean inMaze = false;
        for (List<Tile> row : tiles) {
            for (Tile t : row) {
                if (x == t) {
                    inMaze = true;
                    break;
                }
            }
        }

        if (inMaze) {
            if (exit == null) {
                exit = x;
            }
        }
    }

    /**
     * This method is used to represent the whole maze as a String object.
     * 
     * @return string
     */

    public String toString() {
        String s = "";
        for (int i = 0; i < this.tiles.size(); i++) {
            for (int j = 0; j < this.tiles.get(0).size(); j++) {
                s += this.tiles.get(i).get(j).toString() + " ";
            }
            // System.out.println();
            s += "\n";
        }
        return s;
    }

    /**
     * Each position within the Maze can be represented as a Coordinate
     */

    public class Coordinate {

        /**
         * @param x - integer
         * @param y - integer
         */
        private int x;
        private int y;

        /**
         * This is the constructor. It creates a Coordinate object with its parameters.
         * 
         * @param x
         * @param y
         */
        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * A getter for the x variable - the column index
         * 
         * @return - x
         */

        public int getX() {
            return x;
        }

        /**
         * A getter for the y variable - the row index (bottom row is 0)
         * 
         * @return - y
         */
        public int getY() {
            return y;
        }

        /**
         * The toString method returns "(x, y)" where x represents the column number
         * (indexed from 0, where 0 is the left of the maze) and y represents the row
         * number (indexed from 0, where 0 is the bottom of the maze).
         * 
         * @return - string s
         */

        public String toString() {
            String s = "";
            s += "(";
            s += String.valueOf(getX());
            s += ", ";
            s += String.valueOf(getY());
            s += ")";
            return s;
        }
    }

    /**
     * An enumeration class. The Direction values are used for navigation through
     * the maze.
     */

    public enum Direction {
        NORTH, SOUTH, EAST, WEST;
    }

}
