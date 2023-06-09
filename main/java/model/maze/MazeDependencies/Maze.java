package model.maze.MazeDependencies;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

// A class that implements how we describe the maze
public class Maze {

    private final int width;
    private final int height;
    private final int depth;
    private final Set<Cell> CellSet;
    private final boolean[][][] visited;
    private final boolean[][][] walls;

    public Maze(int width, int height,int depth) {
        this.width = width;
        this.height = height;
        this.depth= depth;
        this.CellSet = new LinkedHashSet<>();
        this.visited = new boolean[width][height][depth];
        this.walls = new boolean[width][height][depth];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {


            Arrays.fill(this.walls[i][j], false);
        }}
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++){
            Arrays.fill(this.visited[i][j], false);
        }}
        this.initialize();
    }

    public void initialize() {

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                for (int z = 0; z < depth ; z++){
                CellSet.add(new Cell(i, j , z ));
            }}
        }
    }

    public Cell getCellAtRowColZ(int row, int col, int z) {

        for (Cell Cell : CellSet) {
            if (Cell.getRow() == row && Cell.getCol() == col && Cell.getZ()==z) {
                return Cell;
            }
        }

        return new Cell();
    }

    public void markVisitedCell(int x, int y,int z) {
        visited[x][y][z] = true;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
    public int getDepth(){return this.depth;}

    public Set<Cell> getCellSet() {
        return this.CellSet;
    }

    public boolean getWalls(int i, int j, int z) {
        return walls[i][j][z];
    }

}
