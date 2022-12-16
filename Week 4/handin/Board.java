/* *****************************************************************************
 *  Name:        Dale Young
 *  Date:        12/14/2022
 *  Description: A data type that models an n-by-n board with sliding tiles.
 **************************************************************************** */

import java.util.LinkedList;
import java.util.List;

public class Board {
    private int sizeN;
    private int[][] board;
    private int blankRow;
    private int blankCol;

    /**
     * create a board from an n-by-n array of tiles
     * where tiles[row][col] = tile at (row, col)
     */
    public Board(int[][] tiles) {
        sizeN = tiles.length;
        board = new int[sizeN][sizeN];
        for (int i = 0; i < sizeN; i += 1) {
            for (int j = 0; j < sizeN; j += 1) {
                if (tiles[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                }
                board[i][j] = tiles[i][j];
            }
        }
    }

    /** string representation of this board */
    public String toString() {
        StringBuilder sb = new StringBuilder(String.valueOf(sizeN) + "\n");
        for (int row = 0; row < sizeN; row += 1) {
            for (int col = 0; col < sizeN; col += 1) {
                sb.append(String.format(" %2d", board[row][col]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /** board dimension n */
    public int dimension() {
        return sizeN;
    }

    /** number of tiles out of place */
    public int hamming() {
        int cnt = 0;
        for (int row = 0; row < sizeN; row += 1) {
            for (int col = 0; col < sizeN; col += 1) {
                if (row == blankRow && col == blankCol) {
                    continue;
                }
                int expected = col + row * sizeN + 1;
                if (board[row][col] != expected) {
                    cnt += 1;
                }
            }
        }
        return cnt;
    }

    /** sum of Manhattan distances between tiles and goal */
    public int manhattan() {
        int sum = 0;
        for (int row = 0; row < sizeN; row += 1) {
            for (int col = 0; col < sizeN; col += 1) {
                if (row == blankRow && col == blankCol) {
                    continue;
                }
                int tile = board[row][col];
                int goalRow = (tile - 1) / sizeN;
                int goalCol = (tile - 1) % sizeN;
                int man = Math.abs(row - goalRow) + Math.abs(col - goalCol);
                sum += man;
            }
        }
        return sum;
    }

    /** is this board the goal board? */
    public boolean isGoal() {
        return hamming() == 0;
    }

    /** does this board equal y? */
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        else if (y.getClass() != this.getClass()) {
            return false;
        }
        else if (y == this) {
            return true;
        }
        else if (((Board) y).dimension() != this.dimension()) {
            return false;
        }

        for (int i = 0; i < sizeN; i += 1) {
            for (int j = 0; j < sizeN; j += 1) {
                if (board[i][j] != ((Board) y).board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /** all neighboring boards */
    public Iterable<Board> neighbors() {
        List<Board> adj = new LinkedList<>();
        // upside
        if (blankRow > 0) {
            int[][] copy = copyOfBoard(board);
            swap(copy, blankRow, blankCol, blankRow - 1, blankCol);
            adj.add(new Board(copy));
        }
        // downside
        if (blankRow < sizeN - 1) {
            int[][] copy = copyOfBoard(board);
            swap(copy, blankRow, blankCol, blankRow + 1, blankCol);
            adj.add(new Board(copy));
        }
        // left side
        if (blankCol > 0) {
            int[][] copy = copyOfBoard(board);
            swap(copy, blankRow, blankCol, blankRow, blankCol - 1);
            adj.add(new Board(copy));
        }
        // right side
        if (blankCol < sizeN - 1) {
            int[][] copy = copyOfBoard(board);
            swap(copy, blankRow, blankCol, blankRow, blankCol + 1);
            adj.add(new Board(copy));
        }
        return adj;
    }

    private int[][] copyOfBoard(int[][] source) {
        int[][] temp = new int[source.length][source.length];
        for (int i = 0; i < source.length; i += 1) {
            for (int j = 0; j < source.length; j += 1) {
                temp[i][j] = source[i][j];
            }
        }
        return temp;
    }

    private void swap(int[][] tiles, int row1, int col1, int row2, int col2) {
        int temp = tiles[row1][col1];
        tiles[row1][col1] = tiles[row2][col2];
        tiles[row2][col2] = temp;
    }

    /** a board that is obtained by exchanging any pair of tiles */
    public Board twin() {
        int[][] clone = copyOfBoard(board);
        if (blankRow != 0) {
            swap(clone, 0, 0, 0, 1);
        }
        else {
            swap(clone, 1, 0, 1, 1);
        }
        return new Board(clone);
    }

    /** unit testing */
    public static void main(String[] args) {
        int[][] test = new int[3][3];
        for (int i = 0; i < 3; i += 1) {
            for (int j = 0; j < 3; j += 1) {
                test[i][j] = i + j * 3;
            }
        }
        Board boardTest = new Board(test);
        System.out.println(boardTest);
        System.out.println(boardTest.hamming());
        System.out.println(boardTest.manhattan());
    }
}
