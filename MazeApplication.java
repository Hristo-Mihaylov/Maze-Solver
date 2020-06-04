// javac --module-path ../javafx/lib/ --add-modules=javafx.controls MazeApplication.java
// java --module-path ../javafx/lib/ --add-modules=javafx.controls MazeApplication

import maze.*;
import maze.Tile.Type;
import maze.routing.RouteFinder;
import maze.routing.*;

import java.lang.ClassNotFoundException;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.util.List;
import javafx.scene.shape.Rectangle;
import java.io.Console;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javafx.scene.control.TextField;

/**
 * The MazeApplication is a JavaFX application that allows users to specify a
 * text file containing a maze representation. The graphical representation of
 * the maze is displayed to the user. The user will also be able to: (i) step
 * through the solving process with the visualisation updating to the current
 * state, (ii) save the current route-solving state to a file, and (iii) load
 * route-solving state from a file.
 */

public class MazeApplication extends Application {
    /**
     * @param filename
     * @param counter
     * @param colour
     */

    private String filename;
    private int counter = 1;
    private int colour = 1;

    /**
     * In the init function, we will ask the user to enter a filename until they
     * enter a valid filename.
     * 
     * @param bool    - boolean
     * @param console - Console
     */

    public void init() {
        // Files are: ../mazes/maze1.txt and ../mazes/maze2.txt

        Console console = System.console();
        boolean bool = false;
        System.out.println("----------> M A Z E   S O L V E R <----------");
        do {
            try {
                System.out.println("Enter a filename: ");
                filename = console.readLine();

                BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
                bool = true;

            } catch (FileNotFoundException e) {
                System.out.println("Invalid filename!");
                bool = false;
            }
        } while (bool == false);

    }

    /**
     * The start method is used to create the GUI with all functionalities and
     * interactivity after the user has specified a correct file path.
     * 
     * @param stage
     * @throws IOException
     */

    @Override
    public void start(Stage stage) throws IOException {

        // Top content

        HBox buttons1 = new HBox(10);
        // Button loadMap = new Button ("Load Map");
        Button step = new Button("Step");
        Button changeBackground = new Button("Background");
        Button design = new Button("Design");

        buttons1.getChildren().addAll(step, changeBackground, design);
        buttons1.setAlignment(Pos.CENTER);
        buttons1.setMaxHeight(50);

        // Middle content

        VBox middle = new VBox(1);

        RouteFinder routeFinder = new RouteFinder(Maze.fromTxt(filename));
        int rowsLength = routeFinder.getMaze().getTiles().get(0).size();
        int colsize = routeFinder.getMaze().getTiles().size();
        for (List<Tile> row : routeFinder.getMaze().getTiles()) {
            HBox hb = new HBox(1);
            for (Tile t : row) {
                Rectangle r = new Rectangle(35, 35);
                if (t.getType() == Type.CORRIDOR) {
                    r.setFill(Color.MOCCASIN);
                }
                if (t.getType() == Type.ENTRANCE) {
                    r.setFill(Color.GOLD);
                }
                if (t.getType() == Type.WALL) {
                    r.setFill(Color.FORESTGREEN);
                }
                if (t.getType() == Type.EXIT) {
                    r.setFill(Color.RED);
                }

                hb.getChildren().add(r);
            }
            hb.setAlignment(Pos.CENTER);
            middle.getChildren().add(hb);
        }

        // Bottom content

        HBox buttons2 = new HBox(10);
        Button loadRoute = new Button("Load Route");
        Button saveRoute = new Button("Save Route");
        buttons2.getChildren().addAll(loadRoute, saveRoute);
        buttons2.setAlignment(Pos.CENTER);
        buttons2.setMaxHeight(50);

        VBox root = new VBox(10);
        root.setBackground(Background.EMPTY);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(buttons1, middle, buttons2);

        Scene scene = new Scene(root, rowsLength * 35 + 50, colsize * 36 + 120, Color.AZURE);

        changeBackground.setOnAction(e -> {
            if (counter % 3 == 1) {
                scene.setFill(Color.BLANCHEDALMOND);
            }
            if (counter % 3 == 0) {
                scene.setFill(Color.AZURE);
            } else if (counter % 3 == 2) {
                scene.setFill(Color.BLACK);
            }
            counter++;
        });

        design.setOnAction(e -> {
            middle.getChildren().clear();
            if (colour % 2 == 0) {
                for (List<Tile> row : routeFinder.getMaze().getTiles()) {
                    HBox hb = new HBox(1);
                    for (Tile t : row) {
                        Rectangle r = new Rectangle(35, 35);
                        if (t.getType() == Type.ENTRANCE && !routeFinder.getRoute().contains(t)) {
                            r.setFill(Color.GOLD);
                        }
                        if (routeFinder.getRoute().contains(t)) {
                            r.setFill(Color.DEEPSKYBLUE);// DEEPSKYBLUE
                        }
                        if (t.getType() == Type.CORRIDOR && !routeFinder.getRoute().contains(t)) {
                            r.setFill(Color.MOCCASIN);
                        }
                        if (routeFinder.getVisited().contains(t) && !routeFinder.getRoute().contains(t)) {
                            r.setFill(Color.STEELBLUE);
                        }
                        if (t.getType() == Type.WALL) {
                            r.setFill(Color.FORESTGREEN);
                        }
                        if (t.getType() == Type.EXIT && !routeFinder.getRoute().contains(t)) {
                            r.setFill(Color.RED);
                        }

                        hb.getChildren().add(r);
                    }
                    hb.setAlignment(Pos.CENTER);
                    middle.getChildren().add(hb);
                }
            } else {
                for (List<Tile> row : routeFinder.getMaze().getTiles()) {
                    HBox hb = new HBox(1);
                    for (Tile t : row) {
                        Rectangle r = new Rectangle(35, 35);
                        if (t.getType() == Type.ENTRANCE && !routeFinder.getRoute().contains(t)) {
                            r.setFill(Color.GOLD);
                        }
                        if (routeFinder.getRoute().contains(t)) {
                            r.setFill(Color.MOCCASIN);
                        }
                        if (t.getType() == Type.CORRIDOR && !routeFinder.getRoute().contains(t)) {
                            r.setFill(Color.WHITE);
                        }
                        if (routeFinder.getVisited().contains(t) && !routeFinder.getRoute().contains(t)) {
                            r.setFill(Color.STEELBLUE);
                        }
                        if (t.getType() == Type.WALL) {
                            r.setFill(Color.GRAY);// MEDIUMSPRINGGREEN
                        }
                        if (t.getType() == Type.EXIT && !routeFinder.getRoute().contains(t)) {
                            r.setFill(Color.RED);
                        }

                        hb.getChildren().add(r);
                    }
                    hb.setAlignment(Pos.CENTER);
                    middle.getChildren().add(hb);
                }
            }
            colour++;
        });

        step.setOnAction(e -> {
            middle.getChildren().clear();
            routeFinder.step();
            if (colour % 2 == 1) {
                for (List<Tile> row : routeFinder.getMaze().getTiles()) {
                    HBox hb = new HBox(1);
                    for (Tile t : row) {
                        Rectangle r = new Rectangle(35, 35);
                        if (t.getType() == Type.ENTRANCE && !routeFinder.getRoute().contains(t)) {
                            r.setFill(Color.GOLD);
                        }
                        if (routeFinder.getRoute().contains(t)) {
                            r.setFill(Color.DEEPSKYBLUE);// DEEPSKYBLUE
                        }
                        if (t.getType() == Type.CORRIDOR && !routeFinder.getRoute().contains(t)) {
                            r.setFill(Color.MOCCASIN);
                        }
                        if (routeFinder.getVisited().contains(t) && !routeFinder.getRoute().contains(t)) {
                            r.setFill(Color.STEELBLUE);
                        }
                        if (t.getType() == Type.WALL) {
                            r.setFill(Color.FORESTGREEN);// MEDIUMSPRINGGREEN
                        }
                        if (t.getType() == Type.EXIT && !routeFinder.getRoute().contains(t)) {
                            r.setFill(Color.RED);
                        }

                        hb.getChildren().add(r);
                    }
                    hb.setAlignment(Pos.CENTER);
                    middle.getChildren().add(hb);
                }
            } else {
                for (List<Tile> row : routeFinder.getMaze().getTiles()) {
                    HBox hb = new HBox(1);
                    for (Tile t : row) {
                        Rectangle r = new Rectangle(35, 35);
                        if (t.getType() == Type.ENTRANCE && !routeFinder.getRoute().contains(t)) {
                            r.setFill(Color.GOLD);
                        }
                        if (routeFinder.getRoute().contains(t)) {
                            r.setFill(Color.MOCCASIN);
                        }
                        if (t.getType() == Type.CORRIDOR && !routeFinder.getRoute().contains(t)) {
                            r.setFill(Color.WHITE);
                        }
                        if (routeFinder.getVisited().contains(t) && !routeFinder.getRoute().contains(t)) {
                            r.setFill(Color.STEELBLUE);
                        }
                        if (t.getType() == Type.WALL) {
                            r.setFill(Color.GRAY);
                        }
                        if (t.getType() == Type.EXIT && !routeFinder.getRoute().contains(t)) {
                            r.setFill(Color.RED);
                        }

                        hb.getChildren().add(r);
                    }
                    hb.setAlignment(Pos.CENTER);
                    middle.getChildren().add(hb);
                }
            }
        });

        saveRoute.setOnAction(e -> {
            middle.getChildren().clear();
            TextField tf = new TextField("Enter a filename to save and then press <ENTER>");
            middle.getChildren().add(tf);
            tf.setOnKeyPressed(ke -> {
                switch (ke.getCode()) {
                    case ENTER:
                        try {
                            String saveFile = tf.getText();
                            routeFinder.save(saveFile);
                            tf.clear();
                            tf.setText("Saved");
                            break;
                        } catch (IOException ioe) {
                            tf.clear();
                            tf.setText("IOException!");
                        }
                    default:
                }
            });
        });

        loadRoute.setOnAction(e -> {
            middle.getChildren().clear();
            TextField tf = new TextField("Enter a filename to load and then press <ENTER>");
            middle.getChildren().add(tf);
            tf.setOnKeyPressed(ke -> {
                switch (ke.getCode()) {
                    case ENTER:
                        try {
                            String loadF = tf.getText();
                            RouteFinder loaded = routeFinder.load(loadF);
                            tf.clear();
                            middle.getChildren().clear();
                            for (List<Tile> row : loaded.getMaze().getTiles()) {
                                HBox hb = new HBox(1);
                                for (Tile t : row) {
                                    Rectangle r = new Rectangle(30, 30);
                                    if (loaded.getRoute().contains(t)) {
                                        r.setFill(Color.ORANGE);
                                    }
                                    if (t.getType() == Type.CORRIDOR && !loaded.getRoute().contains(t)) {
                                        r.setFill(Color.WHITE);
                                    }
                                    if (loaded.getVisited().contains(t) && !loaded.getRoute().contains(t)) {
                                        r.setFill(Color.GRAY);
                                    }
                                    if (t.getType() == Type.WALL) {
                                        r.setFill(Color.BLACK);
                                    }
                                    if (t.getType() == Type.EXIT && !loaded.getRoute().contains(t)) {
                                        r.setFill(Color.RED);
                                    }
                                    if (t.getType() == Type.ENTRANCE && !loaded.getRoute().contains(t)) {
                                        r.setFill(Color.GOLD);
                                    }

                                    hb.getChildren().add(r);
                                }
                                hb.setAlignment(Pos.CENTER);
                                middle.getChildren().add(hb);
                            }
                            break;
                        } catch (ClassNotFoundException cnfe) {

                        } catch (IOException ioe) {

                        }
                    default:
                }
            });
        });

        stage.setScene(scene);
        stage.setTitle("Maze Solver");
        stage.show();

    }

    /**
     * The main method of the MazeApplication class. It displays the GUI.
     * 
     * @param args
     */

    public static void main(String[] args) {
        launch(args);
    }
}
