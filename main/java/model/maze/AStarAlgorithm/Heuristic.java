package model.maze.AStarAlgorithm;

// A class that implements the Manhattan heuristics to implement it in AStarAlgorithm
public class Heuristic {
    public static String[] algorithms = {"Manhattan", "Manhattan With DiagMovement"};

    public static float ManhattanDistanceCost(int x, int y,int z, int targetX, int targetY , int targetZ ) {
        float newX = Math.abs(x - targetX);
        float newY = Math.abs(y - targetY);
        float newZ = Math.abs(z - targetZ);
        return (newX + newY + newZ);
    }
}
