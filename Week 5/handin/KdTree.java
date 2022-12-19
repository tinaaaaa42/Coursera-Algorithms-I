/* *****************************************************************************
 *  Name:        Dale Young
 *  Date:        12/18/2022
 *  Description: A mutable data type that uses 2d-tree to implement the same API
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.LinkedList;
import java.util.List;

public class KdTree {

    private Node root;
    private int size;

    private class Node {
        private double x;
        private double y;
        private int level;
        private Node left, right;
        private RectHV rect;

        Node(double px, double py, int setLevel, RectHV rectHV) {
            x = px;
            y = py;
            level = setLevel;
            left = null;
            right = null;
            rect = rectHV;
        }

        public RectHV rectLeftBottom() {
            if (compareX(level)) {
                return new RectHV(rect.xmin(), rect.ymin(), x, rect.ymax());
            }
            else {
                return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), y);
            }
        }

        public RectHV rectRightTop() {
            if (compareX(level)) {
                return new RectHV(x, rect.ymin(), rect.xmax(), rect.ymax());
            }
            else {
                return new RectHV(rect.xmin(), y, rect.xmax(), rect.ymax());
            }
        }
    }

    /** construct an empty set of points */
    public KdTree() {
        root = null;
        size = 0;
    }

    /** is the set empty? */
    public boolean isEmpty() {
        return size == 0;
    }

    /** number of points in the set */
    public int size() {
        return size;
    }

    /**
     * the connection between the level and the comparing way:
     * if the level is odd, compare x-coordinate
     * if the level is even, compare y-coordinate
     */
    private boolean compareX(int currrentLevel) {
        return currrentLevel % 2 == 1;
    }

    private Node nextNode(Node current, Point2D p) {
        if (compareX(current.level)) {
            if (p.x() < current.x) {
                return current.left;
            }
            else {
                return current.right;
            }
        }
        else {
            if (p.y() < current.y) {
                return current.left;
            }
            else {
                return current.right;
            }
        }
    }

    /** return the previous node if point p is not in th set; else null */
    private Node dive(Point2D p) {
        Node current = root;
        Node prev = null;

        while (current != null) {
            if (current.x == p.x() && current.y == p.y()) {
                return null;
            }

            prev = current;
            current = nextNode(current, p);
        }
        return prev;
    }

    /** add the point to the set (if it is not already in the set) */
    public void insert(Point2D p) {
        nullChecker(p);
        if (size == 0) {
            root = new Node(p.x(), p.y(), 1, new RectHV(0, 0, 1, 1));
            size += 1;
        }
        else {
            Node prev = dive(p);
            if (prev != null) {
                if (compareX(prev.level)) {
                    if (p.x() < prev.x) {
                        prev.left = new Node(p.x(), p.y(), prev.level + 1,
                                             prev.rectLeftBottom());
                    }
                    else {
                        prev.right = new Node(p.x(), p.y(), prev.level + 1,
                                              prev.rectRightTop());
                    }
                }
                else {
                    if (p.y() < prev.y) {
                        prev.left = new Node(p.x(), p.y(), prev.level + 1,
                                             prev.rectLeftBottom());
                    }
                    else {
                        prev.right = new Node(p.x(), p.y(), prev.level + 1,
                                              prev.rectRightTop());
                    }
                }
                size += 1;
            }

        }
    }

    /** does the set contain point p */
    public boolean contains(Point2D p) {
        nullChecker(p);
        return dive(p) == null;
    }

    /** draw all points to standard draw */
    public void draw() {
        draw(root);
    }

    private void draw(Node node) {
        if (node != null) {
            Point2D current = new Point2D(node.x, node.y);
            current.draw();
            draw(node.left);
            draw(node.right);
        }
    }

    /** all points that are inside the rectangle (or on the boundary) */
    public Iterable<Point2D> range(RectHV rect) {
        nullChecker(rect);
        List<Point2D> point2DSet = new LinkedList<>();
        range(rect, root, point2DSet);
        return point2DSet;
    }

    private void range(RectHV rect, Node subTree, List<Point2D> set) {
        if (subTree == null) {
            return;
        }
        Point2D currentPoint = new Point2D(subTree.x, subTree.y);
        if (rect.contains(currentPoint)) {
            set.add(currentPoint);
        }
        if (compareX(subTree.level)) {
            if (subTree.x > rect.xmin()) {
                range(rect, subTree.left, set);
            }
            if (subTree.x <= rect.xmax()) {
                range(rect, subTree.right, set);
            }
        }
        else {
            if (subTree.y > rect.ymin()) {
                range(rect, subTree.left, set);
            }
            if (subTree.y <= rect.ymax()) {
                range(rect, subTree.right, set);
            }
        }
    }

    /** a nearest neighbor in the set to point p; null if the set is empty */
    public Point2D nearest(Point2D p) {
        nullChecker(p);
        if (isEmpty()) {
            return null;
        }
        return nearest(p, new Point2D(root.x, root.y), root);
    }

    private Point2D nearest(Point2D p, Point2D near, Node subTree) {
        if (subTree == null) {
            return near;
        }

        double distance = p.distanceSquaredTo(near);
        if (subTree.rect.distanceSquaredTo(p) < distance) {
            double nodeDis = p.distanceSquaredTo(new Point2D(subTree.x, subTree.y));
            if (nodeDis < distance) {
                near = new Point2D(subTree.x, subTree.y);
            }

            if ((compareX(subTree.level) && p.x() < subTree.x)
                    || (!compareX(subTree.level) && p.y() < subTree.y)) {
                near = nearest(p, near, subTree.left);
                near = nearest(p, near, subTree.right);
            }
            else {
                near = nearest(p, near, subTree.right);
                near = nearest(p, near, subTree.left);
            }
        }
        return near;
    }

    private void nullChecker(Object p) {
        if (p == null) {
            throw new IllegalArgumentException("No null!");
        }
    }

    /** unit testing of the methods */
    public static void main(String[] args) {

    }
}
