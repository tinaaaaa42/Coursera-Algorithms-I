/* *****************************************************************************
 *  Name:        Dale Young
 *  Date:        12/14/2022
 *  Description: Implement A* search to solve n-by-n slider puzzles.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private SearchNode solution;
    private boolean isSolvable;
    private Stack<Board> solutionStack;

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moveToThis;
        private SearchNode prev;

        SearchNode(Board b, int lastMove, SearchNode previous) {
            board = b;
            moveToThis = lastMove;
            prev = previous;
        }

        public Board getBoard() {
            return board;
        }

        public SearchNode getPrev() {
            return prev;
        }

        public int getMoveToThis() {
            return moveToThis;
        }

        private int priority() {
            return board.manhattan() + moveToThis;
        }

        public int compareTo(SearchNode that) {
            return this.priority() - that.priority();
        }
    }

    /** find a solution to the initial board (using A* algorithm) */
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("NOo null!");
        }
        
        solution = null;
        MinPQ<SearchNode> minPQ = new MinPQ<>();
        solutionStack = new Stack<>();
        minPQ.insert(new SearchNode(initial, 0, null));

        while (!minPQ.isEmpty()) {
            SearchNode currentNode = minPQ.delMin();
            Board currentBoard = currentNode.getBoard();

            if (currentBoard.isGoal()) {
                solution = currentNode;
                isSolvable = true;
                break;
            }
            if (currentBoard.hamming() == 2 && currentBoard.twin().isGoal()) {
                isSolvable = false;
                break;
            }

            int moves = currentNode.getMoveToThis();
            Board prevBoard = moves > 0 ? currentNode.getPrev().getBoard() : null;

            for (Board nextBoard : currentBoard.neighbors()) {
                if (currentNode.getPrev() == null || !nextBoard.equals(prevBoard)) {
                    minPQ.insert(new SearchNode(nextBoard, moves + 1, currentNode));
                }

            }

        }
        for (SearchNode s = solution; s != null; s = s.getPrev()) {
            solutionStack.push(s.getBoard());
        }

    }

    /** is the initial board solvable? */
    public boolean isSolvable() {
        return isSolvable;
    }

    /** min number of moves to solve initial board; -1 if unsolvable */
    public int moves() {
        if (isSolvable) {
            return solution.getMoveToThis();
        }
        return -1;
    }

    /** sequence of boards in the shortest solution; null if unsolvable */
    public Iterable<Board> solution() {
        if (isSolvable) {
            return solutionStack;
        }
        return null;
    }

    /** test client */
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
