/* *****************************************************************************
 *  Name:              Dale Young
 *  Coursera User ID:  ?
 *  Last modified:     12/8/2022
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int sizeN;
    private final int top;
    private final int bottom;
    private WeightedQuickUnionUF allPercolation;
    private WeightedQuickUnionUF percolationWithNoBottom;
    private int openSites = 0;
    private boolean[] site;

    /** creates n-by-n grid, with all sites initially blocked */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N must be positive!");
        }

        sizeN = n;
        top = n * n;
        bottom = n * n + 1;
        allPercolation = new WeightedQuickUnionUF(n * n + 2);
        percolationWithNoBottom = new WeightedQuickUnionUF(n * n + 1);
        site = new boolean[n * n];

        for (int i = 0; i < n * n; i += 1) {
            site[i] = false;
        }
    }

    /**
     * change the 2D index to 1D
     * the 2D index ranges from (1, 1) to (n, n)
     * the 1D index ranges from 0 to n * n - 1
     */
    private int indexChanger(int row, int col) {
        return (row - 1) * sizeN + col - 1;
    }

    /** check whether the given 2D index is valid */
    private void indexChecker(int row, int col) {
        if (row < 1 || row > sizeN || col < 1 || col > sizeN) {
            throw new IllegalArgumentException("invalid index!");
        }
    }

    /** cope with the adjacent sites when opening a new site */
    private void adjacentOpener(int row, int col) {
        int currentIndex = indexChanger(row, col);
        // upside
        if (row > 1 && isOpen(row - 1, col)) {
            int upside = indexChanger(row - 1, col);
            connect(currentIndex, upside);
        }

        // downside
        if (row < sizeN && isOpen(row + 1, col)) {
            int downside = indexChanger(row + 1, col);
            connect(currentIndex, downside);
        }

        // left side
        if (col > 1 && isOpen(row, col - 1)) {
            int left = indexChanger(row, col - 1);
            connect(currentIndex, left);
        }

        // right side
        if (col < sizeN && isOpen(row, col + 1)) {
            int right = indexChanger(row, col + 1);
            connect(currentIndex, right);
        }
    }

    /** union index1 and index2 in both QU */
    private void connect(int index1, int index2) {
        allPercolation.union(index1, index2);
        percolationWithNoBottom.union(index1, index2);
    }

    /** opens the site (row, col) if it is not open already */
    public void open(int row, int col) {
        indexChecker(row, col);

        if (!isOpen(row, col)) {
            int index = indexChanger(row, col);
            site[index] = true;
            openSites += 1;

            if (row == 1) {
                connect(index, top);
            }
            if (row == sizeN) {
                allPercolation.union(index, bottom);
            }
            adjacentOpener(row, col);
        }
    }

    /** is the site (row, col) open? */
    public boolean isOpen(int row, int col) {
        indexChecker(row, col);
        return site[indexChanger(row, col)];
    }

    /** is the site (row, col) full? */
    public boolean isFull(int row, int col) {
        indexChecker(row, col);
        int i = indexChanger(row, col);
        return percolationWithNoBottom.find(top) == percolationWithNoBottom.find(i);
    }

    /** returns the number of open sites */
    public int numberOfOpenSites() {
        return openSites;
    }

    /** does the system percolate? */
    public boolean percolates() {
        return allPercolation.find(top) == allPercolation.find(bottom);
    }

    /** test client */
    public static void main(String[] args) {
        Percolation p = new Percolation(2);
        p.open(1, 1);
        System.out.println(p.percolates());
        p.open(2, 2);
        System.out.println(p.percolates());
        p.open(2, 1);
        System.out.println(p.percolates());
    }
}
