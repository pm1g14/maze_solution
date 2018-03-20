package domain;

import java.util.List;

public class Maze {

    private String[][] mazeRepresentation;
    private List<String> mazeLines;
    private boolean isSolvable;

    public static Maze initMaze(int width, int height, List<String> mazeLines) {
        return (mazeLines != null && width >= 0 && height >= 0) ? new Maze(width, height, mazeLines) : null;
    }

    public String[][] getMazeAs2DArray() {
        return this.mazeRepresentation;
    }

    public int getMazeWidth() {
        return mazeRepresentation.length;
    }

    public int getMazeHeight() {
        return mazeRepresentation[0].length;
    }

    public boolean isSolvable() {
        return isSolvable;
    }

    public void setIsSolvable(boolean isSolvable) {
        this.isSolvable = isSolvable;
    }

    private Maze(int width, int height, List<String> mazeLines) {
        this.mazeRepresentation = new String[height][width];
        this.mazeLines = mazeLines;
        this.isSolvable = true;
        populateMazeRepresentation();
    }

    private void populateMazeRepresentation() {

        int currentMazeLine = 0;
        for(String mazeLine : mazeLines) {

            if (mazeLine != null) {
                String[] elements = mazeLine.trim().split(" ");
                mazeRepresentation[currentMazeLine] = elements;
            }
            currentMazeLine++;
        }
    }
}
