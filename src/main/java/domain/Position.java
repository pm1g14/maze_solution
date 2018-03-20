package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to replicate the node behaviour in a tree data structure - it denotes the current position in the maze.
 */
public class Position {

    private int coordX;
    private int coordY;
    private String positionValue;
    private List<Position> neightboringPositions = new ArrayList<>();
    private Position parentPosition;
    private boolean isVisited;


    public Position(int coordX, int coordY, Maze maze, Position parentPosition) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.positionValue = (isValidPosition(maze)) ? maze.getMazeAs2DArray()[coordX][coordY] : null;
        this.parentPosition = parentPosition;
        this.isVisited = false;
    }

    public Position(int coordX, int coordY, Maze maze) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.positionValue = (isValidPosition(maze)) ? maze.getMazeAs2DArray()[coordX][coordY] : null;
        this.isVisited = false;
    }

    public int getPositionX() {
        return coordX;
    }

    public int getPositionY() {
        return coordY;
    }

    public String getPositionValue() {
        return positionValue;
    }

    public Position getParentPosition() {
        return parentPosition;
    }

    public void setParentPosition(Position parent) {
        this.parentPosition = parent;
    }

    public List<Position> getNeighboringPositions() {
        return neightboringPositions;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setIsVisited(boolean visited) {
        isVisited = visited;
    }

    public boolean equals(Position position) {
        if (position == null) {
            return false;
        }

        return (coordX == position.coordX && coordY == position.coordY);
    }

    private boolean isValidPosition(Maze maze) {
        if (maze == null) {
            return false;
        }

        return (coordX >= 0 && coordY >= 0 && coordX < maze.getMazeWidth() && coordY < maze.getMazeHeight());
    }
}
