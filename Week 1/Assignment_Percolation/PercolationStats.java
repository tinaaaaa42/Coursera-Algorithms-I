/* *****************************************************************************
 *  Name:              Dale Young
 *  Coursera User ID:  ?
 *  Last modified:     12/8/2022
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double MAGIC = 1.96;
    private int totalTimes;
    private double[] experiment;

    /** perform independent trials on an n-by-n grid */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("positive param!");
        }

        totalTimes = trials;
        experiment = new double[trials];

        for (int i = 0; i < trials; i += 1) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int row = StdRandom.uniformInt(n) + 1;
                int col = StdRandom.uniformInt(n) + 1;
                p.open(row, col);
            }
            experiment[i] = (double) p.numberOfOpenSites() / (n * n);
        }

    }

    /** sample mean of percolation threshold */
    public double mean() {
        return StdStats.mean(experiment);
    }

    /** sample standard deviation of percolation threshold */
    public double stddev() {
        return StdStats.stddev(experiment);
    }

    /** low endpoint of 95% confidence interval */
    public double confidenceLo() {
        return mean() - MAGIC * stddev() / Math.sqrt(totalTimes);
    }

    /** high endpoint of 95% confidence interval */
    public double confidenceHi() {
        return mean() + MAGIC * stddev() / Math.sqrt(totalTimes);
    }

    /** test client */
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);

        System.out.println("mean                     = " + ps.mean());
        System.out.println("stddev                   = " + ps.stddev());
        System.out.println("95% confidence interval = [" + ps.confidenceLo()
                                   + ", " + ps.confidenceHi() + "]");
    }
}
