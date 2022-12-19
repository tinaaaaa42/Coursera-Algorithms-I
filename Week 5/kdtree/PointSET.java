/* *****************************************************************************
 *  Name:        Dale Young
 *  Date:        12/18/2022
 *  Description: A mutable data type that represents a set of points in the unit
                 square.
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.Set;
import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> set;

    /** construct an empty set of points */
    public PointSET() {
        set = new TreeSet<>();
    }

    /** is the set empty? */
    public boolean isEmpty() {
        return set.isEmpty();
    }

    /** number of points in the set */
    public int size() {
        return set.size();
    }

    private void nullChecker(Object p) {
        if (p == null) {
            throw new IllegalArgumentException("No null!");
        }
    }

    /** add the point to the set (if it is not already in the set) */
    public void insert(Point2D p) {
        nullChecker(p);
        if (!contains(p)) {
            set.add(p);
        }
    }

    /** does the set contain point p? */
    public boolean contains(Point2D p) {
        nullChecker(p);
        return set.contains(p);
    }

    /** draw all points to standard draw */
    public void draw() {
        for (Point2D p : set) {
            p.draw();
        }
    }

    /** all points that are inside the rectangle (or on the boundary) */
    public Iterable<Point2D> range(RectHV rect) {
        nullChecker(rect);
        Set<Point2D> inside = new TreeSet<>();
        for (Point2D p : set) {
            if (rect.contains(p)) {
                inside.add(p);
            }
        }
        return inside;
    }

    /** a nearest neighbor in the set to point p; null if the set is empty */
    public Point2D nearest(Point2D p) {
        nullChecker(p);
        Point2D nearest = null;
        double distance = Double.POSITIVE_INFINITY;

        for (Point2D point : set) {
            if (point.distanceSquaredTo(p) < distance) {
                nearest = point;
            }
        }
        return nearest;
    }

    /** unit testing of methods */
    public static void main(String[] args) {
        PointSET pointSET = new PointSET();
        Point2D p1 = new Point2D(100, 100);
        Point2D p2 = new Point2D(200, 200);
        pointSET.insert(p1);
        pointSET.insert(p2);
        pointSET.draw();
    }
}
