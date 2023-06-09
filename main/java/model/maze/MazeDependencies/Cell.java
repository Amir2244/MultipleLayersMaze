package model.maze.MazeDependencies;

import org.jetbrains.annotations.NotNull;
// A class that implements the maze's cells
public class Cell implements Comparable<Cell> {
    private final int row;
    private final int col;
    private final int z ;
    private boolean isWall;
    public float cost;
    public Cell parent;
    public float heuristic;
    public int level;

    public Cell(int row, int col , int z) {
        this.row = row;
        this.col = col;
        this.z = z;
    }

    public Cell() {
        this.z=-1;
        this.row = -1;
        this.col = -1;
        this.isWall = true;
    }

    public int setParent(Cell parent) {
        level = parent.level + 1;
        this.parent = parent;

        return level;
    }

    public int compareTo(@NotNull Cell other) {

        float first = heuristic + cost;
        float others = other.heuristic + other.cost;

        return Float.compare(first, others);
    }
    public int priority() {
        return (int)heuristic +(int) cost;
    }


    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }
    public int getZ(){return z ;}

    public boolean isWall() {
        return isWall;
    }

    public void setWall(boolean isWall) {
        this.isWall = isWall;
    }

}
