/* *****************************************************************************
 *  Name:        Dale Young
 *  Date:        12/12/2022
 *  Description: A brute program that examines 4 points at a time and checks
                 whether they all lie on the same line segment, returning all
                 such line segment.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class BruteCollinearPoints {

    private List<LineSegment> lines;

    /** finds all line segment containing 4 points */
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("No null!");
        }
        lines = new ArrayList<>();
        for (int p = 0; p < points.length; p += 1) {
            pointNotNullChecker(points[p]);
            for (int q = p + 1; q < points.length; q += 1) {
                pointNotNullChecker(points[q]);
                repeatChecker(points[p], points[q]);
                for (int r = q + 1; r < points.length; r += 1) {
                    pointNotNullChecker(points[r]);
                    repeatChecker(points[r], points[p]);
                    repeatChecker(points[r], points[q]);
                    for (int s = r + 1; s < points.length; s += 1) {
                        pointNotNullChecker(points[s]);
                        repeatChecker(points[s], points[p]);
                        repeatChecker(points[s], points[q]);
                        repeatChecker(points[s], points[r]);
                        if (points[p].slopeTo(points[q]) == points[q].slopeTo(points[r])
                                && points[q].slopeTo(points[r]) == points[r].slopeTo(points[s])) {
                            Point min = min(points[p], points[q], points[r], points[s]);
                            Point max = max(points[p], points[q], points[r], points[s]);
                            lines.add(new LineSegment(min, max));
                        }
                    }
                }
            }
        }
    }

    private Point min(Point p, Point q, Point r, Point s) {
        Point min = p;
        if (min.compareTo(q) < 0) {
            min = q;
        }
        if (min.compareTo(r) < 0) {
            min = r;
        }
        if (min.compareTo(s) < 0) {
            min = s;
        }
        return min;
    }

    private Point max(Point p, Point q, Point r, Point s) {
        Point max = p;
        if (max.compareTo(q) > 0) {
            max = q;
        }
        if (max.compareTo(r) > 0) {
            max = r;
        }
        if (max.compareTo(s) > 0) {
            max = s;
        }
        return max;
    }

    private void pointNotNullChecker(Point p) {
        if (p == null) {
            throw new IllegalArgumentException("Point should not be null!");
        }
    }

    private void repeatChecker(Point p, Point q) {
        if (p.compareTo(q) == 0) {
            throw new IllegalArgumentException("Repeated point!");
        }
    }

    /** the number of line segment */
    public int numberOfSegments() {
        return lines.size();
    }

    /** the line segments */
    public LineSegment[] segments() {
        LineSegment[] lineSegments = lines.toArray(new LineSegment[0]);
        return lineSegments;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i += 1) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
