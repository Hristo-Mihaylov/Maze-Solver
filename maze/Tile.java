package maze;

import java.io.Serializable;

/**
 * A Tile is an object that represents one space within a maze.
 */
public class Tile implements Serializable{

    /**
     * @param type
     */

    private Type type;

    /**
     * The constructor.
     * @param type
     */

    private Tile(Type type) {
        this.type = type;
    }

    /**
     * The fromChar method creates a new Tile from its text representation (supplied as a char).
     * @param a
     * @return - Tile object
     */

    protected static Tile fromChar(char a) {

        Tile tile = new Tile(Type.CORRIDOR);
        if (a == 'e') {
            tile.type = Type.ENTRANCE;
        }
        if (a == 'x') {
            tile.type = Type.EXIT;
        }
        if (a == '.') {
            tile.type = Type.CORRIDOR;
        }
        if (a == '#') {
            tile.type = Type.WALL;
        }

        return tile;
    }

    /**
     * A getter for the type variable.
     * @return - Type object
     */

    public Type getType(){
        return type;
    }

    /**
     * A Tile of Type WALL cannot be navigated through; all other Tile types can be.
     * @return
     */

    public boolean isNavigable(){
        if (type == Type.WALL){
            return false;
        }

        return true;
    }

    /**
     * The toString method returns the the string representation used in text files.
     * @return - String object
     */

    public String toString(){
        String s = "";
        if (type == Type.CORRIDOR){
            s += ".";
        }
        if (type == Type.ENTRANCE){
            s += "e";
        }
        if (type == Type.EXIT){
            s += "x";
        }
        if (type == Type.WALL){
            s += "#";
        }
        return s;
    }

    /**
     * An enumeration class. Used for the 'type' of the tiles.
     */
    
    public enum Type {
        CORRIDOR, ENTRANCE, EXIT, WALL;
    }
    
}