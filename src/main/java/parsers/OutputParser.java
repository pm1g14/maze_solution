package parsers;

import constants.MazeConstants;
import domain.Maze;
import domain.MazeTree;
import domain.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OutputParser {

    private static final String WALL_REPRESENTATION = "#";
    private static final String CLEAR_PATH_REPRESENTATION = " ";
    private static final String START_POSITION_REPRESENTATION = "S";
    private static final String EXIT_POSITION_REPRESENTATION = "E";
    private static final String MAZE_PATH_REPRESENTATION = "X";
    private static final String EMPTY_STRING = "";


    /**
     * Returns the output in the desired format by replacing the maze walls, clear paths, start, exit, and chosen path with the appropriate signs.
     *
     * @param maze      The maze object.
     * @param mazePath  The path from start to exit.
     *
     * @return List.
     */
    public static List<String> getOutput(Maze maze, List<Position> mazePath) {
        if (maze == null || mazePath == null) {
            return null;
        }

        Position startPosition = null;
        Position exitPosition = null;

        if (!mazePath.isEmpty()) {
            exitPosition = mazePath.get(0);
            startPosition = mazePath.get(mazePath.size() - 1);
        }

        return parseOutputAsList(maze, mazePath, startPosition, exitPosition);
    }


    /**
     * Populates and returns the output as a list of maze lines.
     *
     * @param maze           The maze object.
     * @param mazePath       The maze path.
     * @param startPosition  The start position in the maze.
     * @param exitPosition   The exit position in the maze.
     *
     * @return List.
     */
    private static List<String> parseOutputAsList(Maze maze, List<Position> mazePath, Position startPosition, Position exitPosition) {
        List<String> output = new ArrayList<>();
        String[][] mazeAsArray = maze.getMazeAs2DArray();

        for (int i = 0; i < maze.getMazeWidth(); ++i) {
            String mazeLine;

            for (int j = 0; j < maze.getMazeHeight(); ++j) {
                Position position = new Position(i, j, maze);
                replaceMazeElementsWithOutputValues(mazePath, startPosition, exitPosition, mazeAsArray, position);
            }
            mazeLine = getLineAsString(mazeAsArray[i]);
            output.add(mazeLine);
        }
        return output;
    }


    /**
     * Replaces the original elements with the desired output values.
     *
     * @param mazePath       The maze path from start to exit.
     * @param startPosition  The maze starting position to be marked as S.
     * @param exitPosition   The maze exit position to be marked as E.
     * @param mazeAsArray    The maze array object.
     * @param position       The current position.
     */
    private static void replaceMazeElementsWithOutputValues(List<Position> mazePath, Position startPosition, Position exitPosition, String[][] mazeAsArray, Position position) {
        if (position == null) {
            return;
        }

        int coordX = position.getPositionX();
        int coordY = position.getPositionY();

        if (isMazeWall(position)) {
            mazeAsArray[coordX][coordY] = WALL_REPRESENTATION;

        } else if (position.equals(startPosition)) {
            mazeAsArray[coordX][coordY] = START_POSITION_REPRESENTATION;

        } else if (position.equals(exitPosition)) {
            mazeAsArray[coordX][coordY] = EXIT_POSITION_REPRESENTATION;

        } else if (isPartOfTheMazePath(mazePath, startPosition, exitPosition, position)) {
            mazeAsArray[coordX][coordY] = MAZE_PATH_REPRESENTATION;

        } else {
            mazeAsArray[coordX][coordY] = CLEAR_PATH_REPRESENTATION;
        }
    }


    /**
     * Checks if the given position is part of the maze path excluding the start and exit positions - positions in the maze path are marked with X.
     *
     * @param mazePath       The maze path.
     * @param startPosition  The start position.
     * @param exitPosition   The exit position.
     * @param position       The position to check.
     *
     * @return boolean.
     */
    private static boolean isPartOfTheMazePath(List<Position> mazePath, Position startPosition, Position exitPosition, Position position) {
        return MazeTree.isInNodesList(position, mazePath) && (!position.equals(startPosition) || position.equals(exitPosition));
    }


    /**
     * Checks if the given position is a maze wall - maze walls are marked with #.
     *
     * @param position  The position to check.
     *
     * @return boolean.
     */
    private static boolean isMazeWall(Position position) {
        return position != null && MazeConstants.MAZE_WALL.equals(position.getPositionValue());
    }


    /**
     * Converts the given maze line array to a String after stripping it from unnecessary characters for the output.
     *
     * @param mazeLineArray  The given maze line as array.
     *
     * @return String.
     */
    private static String getLineAsString(String[] mazeLineArray) {
        if (mazeLineArray == null) {
            return EMPTY_STRING;
        }

        String mazeLine = Arrays.toString(mazeLineArray);
        return mazeLine.replaceAll("[,\\[\\]]", EMPTY_STRING);
    }
}
