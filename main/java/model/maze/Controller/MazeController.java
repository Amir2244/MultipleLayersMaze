package model.maze.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.maze.AStarAlgorithm.AStar;
import model.maze.AStarAlgorithm.Path;
import model.maze.AStarAlgorithm.Heuristic;
import model.maze.MazeDependencies.Cell;
import model.maze.MazeDependencies.Maze;

import java.util.Set;
// A class that implements how the UI works and its properties
public class MazeController {
    private Maze maze ;
    private int  Level =0 ;
    private Set<Cell> PathCellSet = null ;
    @FXML
    public TextField mazeWidth;
    @FXML
    public TextField mazeHeight;
    @FXML
    public TextField mazeDepth;
    @FXML
    public Button createButton;
    @FXML
    public TextField startX;
    @FXML
    public TextField startY;
    @FXML
    public TextField startZ;
    @FXML
    public TextField endX;
    @FXML
    public TextField endY;
    @FXML
    public TextField endZ;
    @FXML
    public ComboBox<String> waysBox;
    @FXML
    public Pane mazePane;
    @FXML
    public TextField mazeOutputLabel;
    @FXML
    public AnchorPane menuAnchorPane;
    @FXML
    public Button solveButton;
    @FXML
    public Button Next;
    @FXML
    public Button Previous ;
// The initializer of the path finding process in the UI
    @FXML
    private void initialize() {

        String[] algorithmNames = Heuristic.algorithms;
        ObservableList<String> algorithms = FXCollections.observableArrayList(algorithmNames);
        waysBox.setItems(algorithms);
        waysBox.getSelectionModel().selectFirst();

        createButton.setOnAction((event) -> initializeMaze(Integer.parseInt(mazeWidth.getText()), Integer.parseInt(mazeHeight.getText()), Integer.parseInt(mazeDepth.getText())));
        // The ability to add walls by clicking on the maze to add them
        Next.setOnAction((event) -> nextPage()  );
        Previous.setOnAction((event)-> previousPage() );
        mazePane.setOnMouseClicked((mouseEvent -> {
            double x = mouseEvent.getX() / 30;
            double y = mouseEvent.getY() / 30;
            if (x >= 0 && y >= 0 && x < maze.getWidth() && y < maze.getHeight()) {
                boolean setTheCell = maze.getCellAtRowColZ((int) x, (int) y, Level ).isWall();
                maze.getCellAtRowColZ((int) x, (int) y , Level ).setWall(!setTheCell);
            }
            Pane pane = buildMaze(maze);
            mazePane.getChildren().add(pane);

        }));
        solveButton.setOnAction((event) -> PathFinder(waysBox.getSelectionModel().getSelectedItem()));


    }

    // The method to generate the maze in the UI
    public void initializeMaze(int width, int height, int depth) {
        Level = 0 ;

        mazePane.getChildren().clear();
        maze = new Maze(width, height,depth);
        Pane pane = buildMaze(maze);
        mazePane.getChildren().add(pane);
    }

    // When this method is triggered the algorithm will start working
    public void PathFinder(String algorithmToUse) {
        Path newPath;

        AStar finder = new AStar(maze, 100000);
        newPath = finder.solveMaze(Integer.parseInt(startX.getText()), Integer.parseInt(startY.getText()),Integer.parseInt(startZ.getText()), Integer.parseInt(endX.getText()), Integer.parseInt(endY.getText()), Integer.parseInt(endZ.getText()), algorithmToUse);
        if (newPath == null) {
            mazeOutputLabel.setText("Path not found.");
        } else {
            Pane pane = buildPath(maze, newPath);
            mazePane.getChildren().add(pane);
            mazeOutputLabel.setText("The number of Nodes :  " + newPath.getCellSet().size()
                    + ".  Runtime of A* algorithm: " + newPath.getRunTime() + " ms.");
        }
    }
// Building the path  in the UI
    private Pane buildPath(Maze maze, Path path) {

        Set<Cell> CellSet = path.getCellSet();
        double width = maze.getWidth();
        double height = maze.getHeight();

        Pane pane = new Pane();
        PathCellSet =   CellSet  ;
        buildTiles(maze, pane, width, height, CellSet, true);

        return pane;
    }

    private Pane buildMaze(Maze maze) {
        PathCellSet = null ;
        double width = maze.getWidth();
        double height = maze.getHeight();
        Set<Cell> CellSet = maze.getCellSet();

        Pane pane = new Pane();
        buildTiles(maze, pane, width, height, CellSet, false );

        return pane;
    }
// Build the tiles or the squares that describes the maze and the path
    private void buildTiles(Maze maze, Pane pane, double width, double height, Set<Cell> pathCellSet,
                            boolean solution) {
        Rectangle[][] tiles = new Rectangle[(int) width][(int) height];

        double rectangleW = 30;
        double rectangleH = 30;

        for (int i = 0; i < width; i++) {
            // Draw rows
            for (int j = 0; j < height; j++) {
                // Draw columns
                drawer(maze, pane, tiles, rectangleW, rectangleH, i, j);
            }
        }

        for (Cell Cell : maze.getCellSet()) {
            // Draw Walls
            if (Cell.isWall() && Cell.getZ() == Level) {
                tiles[Cell.getRow()][Cell.getCol()].setFill(Color.BLACK);
            }
        }
        // Printing the traversed Cells in the console
        if (solution) {
            System.out.println("The path is found : ");
            // Flag for drawing the solution on the grid
            for (Cell Cell : pathCellSet) {
                System.out.println("Cell: " + Cell.getRow() + ", " + Cell.getCol()+ ", " + Cell.getZ());

                detect(tiles, Cell);
            }
        }
    }

    private void drawer(Maze maze, Pane pane, Rectangle[][] tiles, double rectangleW, double rectangleH, int i, int j) {
        tiles[i][j] = new Rectangle();
        tiles[i][j].setX(i * rectangleW);
        tiles[i][j].setY(j * rectangleH);
        tiles[i][j].setWidth(rectangleW);
        tiles[i][j].setHeight(rectangleH);

        if (i == Integer.parseInt(startX.getText()) && j == Integer.parseInt(startY.getText())&& Level == Integer.parseInt(startZ.getText())) {
            tiles[i][j].setFill(Color.GREEN);
        } else if ((i == Integer.parseInt(endX.getText()) - 1 )&&( j == Integer.parseInt(endY.getText()) - 1)&& Level == (Integer.parseInt(endZ.getText())-1)) {
            tiles[i][j].setFill(Color.RED);
        } else if (maze.getWalls(i, j,Level)) {
            tiles[i][j].setFill(Color.BLACK);

        } else {
            tiles[i][j].setFill(Color.GRAY);
        }

        tiles[i][j].setStroke(Color.BLACK);
        pane.getChildren().add(tiles[i][j]);
    }

    public void nextPage(){
        Level ++;
        if (Level >= Integer.parseInt(mazeDepth.getText())  ){Level =Integer.parseInt(mazeDepth.getText())-1; }
        drawMazeInLevel();
    }

    public void previousPage() {
        Level--;
        if (Level < 0) {
            Level = 0;
        }
        drawMazeInLevel();
    }
    public void drawMazeInLevel(){
        mazePane.getChildren().clear();
        Pane pane = new Pane();
        mazePane.getChildren().add(pane);
        Rectangle[][] tiles = new Rectangle[(int) maze.getWidth()][(int) maze.getHeight()];

        double rectangleW = 30;
        double rectangleH = 30;
        for (int i = 0; i <  maze.getWidth(); i++) {
            // Draw rows
            for (int j = 0; j < maze.getHeight(); j++) {
                // Draw columns
                drawer(maze, pane, tiles, rectangleW, rectangleH, i, j);
            }
        }

        for (Cell Cell : maze.getCellSet()) {
            // Draw Walls
            if (Cell.isWall() && Cell.getZ() ==Level) {
                tiles[Cell.getRow()][Cell.getCol()].setFill(Color.BLACK);
            }
        }
        if (PathCellSet != null ){
            for (Cell Cell : PathCellSet) {

                detect(tiles, Cell);
            }}
    }

    private void detect(Rectangle[][] tiles, Cell Cell) {
        if ((Integer.parseInt(startX.getText()) == Cell.getRow() && Integer.parseInt(startY.getText()) == Cell.getCol() && Level == Integer.parseInt(startZ.getText()))
                || (Integer.parseInt(endX.getText())-1 == Cell.getRow()
                && Integer.parseInt(endY.getText())-1 == Cell.getCol()) && Level == Integer.parseInt(endZ.getText())-1) {
            return;
        }
        if (Level ==  Cell.getZ() ){
            tiles[Cell.getRow()][Cell.getCol()].setFill(Color.YELLOW);
        }
    }
}