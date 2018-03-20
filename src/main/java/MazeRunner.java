import domain.Maze;
import domain.MazeTree;
import domain.Position;
import exceptions.NonValidMazePathException;
import parsers.InputParser;
import parsers.OutputParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MazeRunner {

    private static String filePath = null;
    private static String[] mazeDimensions = null;
    private static String[] startPositionCoordinates = null;
    private static String[] endPositionCoordinates = null;
    private static List<String> mazeLines = Collections.emptyList();
    private static Position mazeStartPosition;
    private static Position mazeExitPosition;
    private static Maze maze;


    public static void main(String[] args) {

        if (args != null) {
            filePath = args[0];
        }

        getDataFromFile();
        initMaze();
        solveMaze();
    }


    /**
     * Gets the maze dimensions, start and exit positions, and the maze lines from the filepath.
     */
    private static void getDataFromFile() {
        try {
            mazeDimensions           = InputParser.getLine(Files.lines(Paths.get(filePath)), 0);
            startPositionCoordinates = InputParser.getLine(Files.lines(Paths.get(filePath)), 1);
            endPositionCoordinates   = InputParser.getLine(Files.lines(Paths.get(filePath)), 2);
            mazeLines                = InputParser.getMazeLines(Files.lines(Paths.get(filePath)), 3);

        } catch(IOException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }


    /**
     * Finds the path from start to finish and returns the path positions in a list.
     *
     * @param mazeStartPosition  The maze start position.
     * @param mazeExitPosition   The maze exit position.
     *
     * @return List.
     */
    private static List<Position> findMazePath(Position mazeStartPosition, Position mazeExitPosition) {
        if (mazeStartPosition == null || mazeExitPosition == null) {
            return Collections.emptyList();
        }

        List<Position> mazePath = new ArrayList<>();

        Position currentPosition = mazeExitPosition;
        mazePath.add(currentPosition);

        while(!currentPosition.equals(mazeStartPosition)) {
            currentPosition = currentPosition.getParentPosition();
            mazePath.add(currentPosition);
        }
        mazePath.add(mazeStartPosition);
        return mazePath;
    }


    /**
     * Draws the maze in the output as per the requirements.
     *
     * @param maze      The maze object for the output.
     * @param mazePath  The path from start to exit.
     */
    private static void drawOutput(Maze maze, List<Position> mazePath) {
        List<String> outputMaze = OutputParser.getOutput(maze, mazePath);

        for (String mazeLine : outputMaze) {
            System.out.println(mazeLine);
        }
    }


    /**
     * Initializes the maze, the start and exit positions.
     */
    private static void initMaze() {
        maze = Maze.initMaze(Integer.parseInt(mazeDimensions[0]),  Integer.parseInt(mazeDimensions[1]), mazeLines);

        mazeStartPosition = new Position(Integer.parseInt(startPositionCoordinates[1]), Integer.parseInt(startPositionCoordinates[0]), maze);
        mazeExitPosition = new Position(Integer.parseInt(endPositionCoordinates[1]), Integer.parseInt(endPositionCoordinates[0]), maze);
    }


    /**
     * Solves the maze and produces the output.
     **/
    private static void solveMaze() {
        MazeTree tree = MazeTree.getMazeTree(mazeStartPosition, mazeExitPosition, maze);

        if (tree != null) {
            tree.createTree();

            try {
                produceMazeOutput(tree);
            } catch (NonValidMazePathException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }


    /**
     * Produces the desired output or throws an exception if the maze cannot be solved.
     *
     * @param tree  The maze tree.
     *
     * @throws NonValidMazePathException
     */
    private static void produceMazeOutput(MazeTree tree) throws NonValidMazePathException {
        if (tree == null) {
            return;
        }
        List<Position> mazePath;

        if (maze.isSolvable()) {
            mazePath = findMazePath(tree.getRootNode(), tree.getLastNode());
            drawOutput(maze, mazePath);

        } else {
            throw new NonValidMazePathException("The provided maze cannot be solved - there is no valid path between start and exit");
        }
    }
}
