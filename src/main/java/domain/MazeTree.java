package domain;

import constants.MazeConstants;

import java.util.ArrayList;
import java.util.List;


/**
 * The MazeTree class to construct the tree from the maze.
 */
public class MazeTree {

    private Position rootNode;
    private Position lastNode;
    private Maze maze;


    /**
     * Factory constructor - constructs the MazeTree after performing the null checks.
     *
     * @param rootNode  The root node of the tree (start position).
     * @param lastNode  The last node of the tree (exit position).
     * @param maze      The maze object.
     *
     * @return MazeTree.
     */
    public static MazeTree getMazeTree(Position rootNode, Position lastNode, Maze maze) {
        return (rootNode != null && lastNode != null && maze != null) ? new MazeTree(rootNode, lastNode, maze) : null;
    }


    /**
     * Creates the tree by looping through the maze elements - calculates the child nodes of the nodes with their value set to 0.
     */
    public void createTree() {
        List<Position> childNodes = new ArrayList<>();
        Position currentNodePosition = rootNode;
        currentNodePosition.setIsVisited(true);
        populateChildNodesForNode(rootNode, childNodes);

        do {
            List<Position> childNodePositions = currentNodePosition.getNeighboringPositions();

            int invalidPositionsToMoveTo = 0;
            for (Position childNodePosition : childNodePositions) {

                if (isValidNodePosition(childNodePosition)) {
                    currentNodePosition = childNodePosition;
                    childNodePosition.setIsVisited(true);
                    populateChildNodesForNode(childNodePosition, childNodes);
                    break;

                } else {
                    ++invalidPositionsToMoveTo;
                }
            }

            if (isReturnedToRootNodePosition(currentNodePosition, childNodePositions, invalidPositionsToMoveTo)){
                maze.setIsSolvable(false);
                return;
            }

            if (noValidMovesForward(childNodePositions, invalidPositionsToMoveTo)) {
                currentNodePosition = currentNodePosition.getParentPosition();
            }

        } while (!currentNodePosition.equals(lastNode));

        if (currentNodePosition.equals(lastNode)) {
            lastNode.setParentPosition(currentNodePosition.getParentPosition());
        }
    }


    /**
     * Returns the root node of the tree.
     *
     * @return Position.
     */
    public Position getRootNode() {
        return rootNode;
    }


    /**
     * Returns the exit node of the maze.
     *
     * @return Position.
     */
    public Position getLastNode() {
        return lastNode;
    }


    /**
     * Checks if the passed node object is in the list of visited nodes (has been populated).
     *
     * @param node                  The given node to check.
     * @param visitedNodePositions  The visited nodes list.
     *
     * @return boolean.
     */
    public static boolean isInNodesList(Position node, List<Position> visitedNodePositions) {
        if (node == null || visitedNodePositions == null) {
            return false;
        }

        for (Position visitedNode : visitedNodePositions) {

            if (visitedNode.equals(node)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Checks if the passed node is a valid node to visit.
     *
     * @param node  The node position object.
     *
     * @return boolean.
     */
    private boolean isValidNodePosition(Position node) {
        return MazeConstants.MAZE_CLEAR_PATH.equals(node.getPositionValue()) && !node.isVisited();
    }


    /**
     * Checks if the current position is the start position and there are no valid routes left, so the maze is not solvable.
     *
     * @param currentNodePosition       The current node position to check.
     * @param possiblePositions         The possible nearby positions list.
     * @param invalidPositionsToMoveTo  The number of invalid positions to move to.
     *
     * @return boolean.
     */
    private boolean isReturnedToRootNodePosition(Position currentNodePosition, List<Position> possiblePositions, int invalidPositionsToMoveTo) {
        return currentNodePosition.getParentPosition() == null && noValidMovesForward(possiblePositions, invalidPositionsToMoveTo);
    }


    /**
     * Private constructor.
     *
     * @param rootNode  The root node of the tree (start position).
     * @param lastNode  The last node of the tree (exit position).
     * @param maze      The maze object.
     */
    private MazeTree(Position rootNode, Position lastNode, Maze maze) {
        this.rootNode = rootNode;
        this.lastNode = lastNode;
        this.maze = maze;
    }


    /**
     * Populates the child nodes given the current node.
     * If there is a wall north or south, first the horizontal positions (east and west) are populated since the program looks for the first valid route.
     * In all other cases the vertical positions (south and north) are populated since the program looks for the first valid route.
     *
     * @param parentNodePosition  The node object.
     * @param childNodePositions  The nodes that already have a parent node.
     */
    private void populateChildNodesForNode(Position parentNodePosition, List<Position> childNodePositions) {
        int nodeCoordX = parentNodePosition.getPositionX();
        int nodeCoordY = parentNodePosition.getPositionY();

        Position childNodePositionNorth = new Position(nodeCoordX - 1, nodeCoordY, maze, parentNodePosition);
        Position childNodePositionSouth = new Position(nodeCoordX + 1, nodeCoordY, maze, parentNodePosition);
        Position childNodePositionEast = new Position(nodeCoordX, nodeCoordY + 1, maze, parentNodePosition);
        Position childNodePositionWest = new Position(nodeCoordX, nodeCoordY - 1, maze, parentNodePosition);

        if (MazeConstants.MAZE_WALL.equals(childNodePositionSouth.getPositionValue()) || MazeConstants.MAZE_WALL.equals(childNodePositionNorth.getPositionValue())) {
            addChildNodeToList(parentNodePosition, childNodePositionEast, childNodePositions);
            addChildNodeToList(parentNodePosition, childNodePositionWest, childNodePositions);
            addChildNodeToList(parentNodePosition, childNodePositionSouth, childNodePositions);
            addChildNodeToList(parentNodePosition, childNodePositionNorth, childNodePositions);
            return;
        }
        addChildNodeToList(parentNodePosition, childNodePositionSouth, childNodePositions);
        addChildNodeToList(parentNodePosition, childNodePositionNorth, childNodePositions);
        addChildNodeToList(parentNodePosition, childNodePositionEast, childNodePositions);
        addChildNodeToList(parentNodePosition, childNodePositionWest, childNodePositions);
    }


    /**
     * Adds the given child node to the given parent node after checking the validity of the child node.
     *
     * @param parentNodePosition    The parent node for which to add the child.
     * @param childNodePosition     The child node to add to the parent.
     * @param childNodes            The nodes that already have a parent.
     */
    private void addChildNodeToList(Position parentNodePosition, Position childNodePosition, List<Position> childNodes) {

        if (parentNodePosition == null || childNodePosition == null) {
            return;
        }

        if (childNodePosition.getPositionValue() != null && !childNodePosition.equals(parentNodePosition.getParentPosition()) && !MazeTree.isInNodesList(childNodePosition, childNodes)) {
            childNodes.add(childNodePosition);
            parentNodePosition.getNeighboringPositions().add(childNodePosition);
        }
    }


    /**
     * Checks if the current node position has valid node positions to move to.
     *
     * @param possiblePositions         The possible positions to move to.
     * @param invalidPositionsToMoveTo  The number of invalid positions nearby.
     *
     * @return boolean.
     */
    private static boolean noValidMovesForward(List<Position> possiblePositions, int invalidPositionsToMoveTo) {
        return (possiblePositions != null && invalidPositionsToMoveTo == possiblePositions.size());
    }
}
