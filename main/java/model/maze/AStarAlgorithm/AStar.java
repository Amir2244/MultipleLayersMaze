package model.maze.AStarAlgorithm;

import model.maze.MazeDependencies.Cell;
import model.maze.MazeDependencies.Maze;

import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;

// A class that describes how we implement the algorithm
public class AStar {
    // List of Maze Cells to store the visited ones

    private final PriorityQueue<Cell> open;
    private final Maze maze;

    private final int maxSearchDistance;
    private final Cell[][][] Cells;

    // Constructor
    public AStar(Maze maze, int maxSearchDistance) {
        this.maze = maze;
        this.maxSearchDistance = maxSearchDistance;

        // Initialize the Cells
        Cells = new Cell[maze.getWidth()][maze.getHeight()][maze.getDepth()];

        for (int x = 0; x < maze.getWidth(); x++) {
            for (int y = 0; y < maze.getHeight(); y++) {
                for (int z =0 ; z< maze.getDepth(); z++){
                Cells[x][y][z] = new Cell(x, y,z);
                }
            }
        }
        open = new PriorityQueue<>(Comparator.comparingInt(Cell::priority));
    }

    /**
     * @param startX : The X coordinate of the starting point
     * @param startY : The Y coordinate of the starting point
     * @param width : The width of the maze
     * @param height : The height of the maze
     * @param algorithmToUse : The algorithm to use to calculate the path
     * @return : The shortest  path
     */
    public Path solveMaze(int startX, int startY,int startZ, int width, int height,int depth, String algorithmToUse) {
        long startTime = System.nanoTime();
        Path path = findPath(startX, startY,startZ, width, height ,depth , algorithmToUse);

        if (path != null) {
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / (1000000); // divide to get ms
            path.setRuntime(duration);
        }

        return path;
    }

 // The description of this method is same as solveMaze method
    public Path findPath(int startX, int startY,int startZ, int targetX, int targetY,int targetZ, String algorithmToUse) {
        // Decrement for zero based indices
        targetX = targetX - 1;
        targetY = targetY - 1;
        targetZ = targetZ - 1;
        // In the initial state The closed list is empty
        // Adding the starting Cell to the open list
        Cells[startX][startY][startZ].cost = 0;
        Cells[startX][startY][startZ].level = 0;
        open.add(Cells[startX][startY][startZ]);

        Cells[targetX][targetY][startZ].parent = null;
        int maxDepth = 0;
        int perfectPath = -1 ;
        // The main work of the algorithm
        while ((maxDepth < maxSearchDistance) && (open.size() != 0)) {
            // fetch the first cell and process it
            Cell current = open.poll();
            if (perfectPath !=-1 && current.priority() > perfectPath){
                break;
            }
            if (current == Cells[targetX][targetY][targetZ]) {
               if (perfectPath ==-1 || current.priority()< perfectPath){
                   perfectPath = current.priority() ;}
            }
            // Knowing the neighbors of the current cell
            for (int x = -1; x < 2; x++) {
                for (int y = -1; y < 2; y++) {
                    for (int z = -1 ; z<2 ; z++){
                        if ((x == 0) && (y == 0) && (z == 0)) {
                            // Skip not a neighbor
                            continue;
                        }
                        else if (((x!=0 && y!=0) ||z!=0 && y!=0 ||(x!=0 && z!=0) )  && (Objects.equals(algorithmToUse, "Manhattan"))) {
                            continue;
                        }

                        // Get where the neighbor is located
                    int newX = x + current.getRow();
                    int newY = y + current.getCol();
                    int newZ = z + current.getZ();
                    // verify that if the neighbor is not a wall or outside the maze
                    if (isValidLocation(newX, newY,newZ)) {

                        // Add the base Cell to the visited list and get neighbors
                        float nextStepCost;
                        // Allowing diagonal movement if we choose DiagonalMovement (cost is 2)
                        if ( ( z==0 && y==0    )||( x==0 && z==0 )) {
                            nextStepCost = current.cost + getNormalMovementCost() ;
                        }else if ( x==0 && y==0    )   {
                            nextStepCost = (float) (current.cost + getNormalMovementCost() + 0.5);
                        } else if ( z==0 )   {
                            nextStepCost = (float) (current.cost + getNormalMovementCost() + 0.5);
                        }else{
                            nextStepCost = (current.cost + getNormalMovementCost() +1 );
                        }
                        Cell neighbour = Cells[newX][newY][newZ];
                        // Mark the neighbour as visited
                        maze.markVisitedCell(newX, newY , newZ);

                        if (neighbour.cost == 0 || (nextStepCost < neighbour.cost) ) {
                            neighbour.cost = nextStepCost;
                            neighbour.heuristic = getHeuristicCost(newX, newY,newZ, targetX, targetY,targetZ);
                            maxDepth = Math.max(maxDepth, neighbour.setParent(current));
                            open.add(neighbour);
                        }}
                    }
                }
            }
        }

        if (Cells[targetX][targetY][targetZ].parent == null) {
            return null;
        }
        Path path = new Path();
        Cell target = Cells[targetX][targetY][targetZ];
        // Building the path if exists
        while (target != Cells[startX][startY][startZ]) {
            path.buildStep(maze, target.getRow(), target.getCol(),target.getZ());
            target = target.parent;
        }
        path.buildStep(maze, startX, startY,startZ);
        return path;
    }


    protected boolean isValidLocation(int width, int height , int depth) {

        if ((width < 0) || (height < 0)|| (depth < 0) || (width >= maze.getWidth()) || (height >= maze.getHeight())|| (depth>= maze.getDepth())) {
            return false;
        } else return !maze.getCellAtRowColZ(width, height,depth).isWall();
    }

    public float getNormalMovementCost() {
        return 1;
    }

    public float getHeuristicCost(int x, int y,int z, int endX, int endY, int endZ) {
        return Heuristic.ManhattanDistanceCost(x, y, z, endX, endY,endZ);
    }
}
