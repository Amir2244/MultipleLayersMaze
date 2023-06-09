package model.maze.AStarAlgorithm;


import model.maze.MazeDependencies.Cell;
import model.maze.MazeDependencies.Maze;

import java.util.LinkedHashSet;
import java.util.Set;

// A class that implements how we process the result path
public class Path {

    private final Set<Cell> Cells;
    private long runTime;

    public Path() {
        this.Cells = new LinkedHashSet<>();
        this.runTime = 0;
    }

    // A method that build the step that we go on to reach the target
    public void buildStep(Maze maze, int x, int y,int z) {
        Cell Cell = maze.getCellAtRowColZ(x, y, z);
        Cells.add(Cell);
    }

    // Setters and getters
    public void setRuntime(long runTime) {
        this.runTime = runTime;
    }

    public long getRunTime() {
        return runTime;
    }

    public Set<Cell> getCellSet() {
        return this.Cells;
    }

}
