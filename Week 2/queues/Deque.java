/* *****************************************************************************
 *  Name:         Dale Young
 *  Date:         12/11/2022
 *  Description:  a queue that supports adding and removing items from either
                  the front or the back of the data structure
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private static final int REFACTOR = 2;
    private static final double MINUSAGE = 0.25;

    private Item[] items;
    private int first;
    private int last;
    private int size;

    /** construct an empty deque */
    public Deque() {
        items = (Item[]) new Object[8];
        first = 0;
        last = 1;
        size = 0;
    }

    /** is the deque empty? */
    public boolean isEmpty() {
        return size == 0;
    }

    /** return the number of items on the deque */
    public int size() {
        return size;
    }

    private int moveAhead(int index) {
        return (index - 1 + items.length) % items.length;
    }

    private int moveBackward(int index) {
        return (index + 1) % items.length;
    }

    /** add the item to the front */
    public void addFirst(Item item) {
        itemChecker(item);
        resize();
        items[first] = item;
        first = moveAhead(first);
        size += 1;
    }

    /** add the item to the back */
    public void addLast(Item item) {
        itemChecker(item);
        resize();
        items[last] = item;
        last = moveBackward(last);
        size += 1;
    }

    /** if the item to add is null, throw an IllegalArgumentException */
    private void itemChecker(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item should not be null!");
        }
    }

    /**
     * if the deque is empty when calling removing method,
     * throw a NoSuchElementException
     */
    private void emptyChecker() {
        if (isEmpty()) {
            throw new NoSuchElementException("The deque is empty now!");
        }
    }

    /** remove and return the item from the front */
    public Item removeFirst() {
        emptyChecker();
        int newFirst = moveBackward(first);
        Item temp = items[newFirst];
        items[newFirst] = null;
        first = newFirst;
        size -= 1;

        resize();
        return temp;
    }

    /**
     * call this method before adding and after removing
     * if the deque is full before adding, double the deque size
     * if the current usage < 0.25 after removing, half the deque size
     */
    private void resize() {
        if (isFull()) {
            int newSize = REFACTOR * size;
            Item[] temp = (Item[]) new Object[newSize];

            int i = moveBackward(first);
            for (int j = 0; j < size; j += 1) {
                temp[j] = items[i];
                i = moveBackward(i);
            }
            first = newSize - 1;
            last = size;
            items = temp;
        }

        while (usage() < MINUSAGE) {
            int newSize = items.length / REFACTOR;
            Item[] temp = (Item[]) new Object[newSize];

            int i = moveBackward(first);
            for (int j = 0; j < size; j += 1) {
                temp[j] = items[i];
                i = moveBackward(i);
            }
            first = newSize - 1;
            last = size;
            items = temp;
        }

    }

    private boolean isFull() {
        return size == items.length;
    }

    private double usage() {
        if (size >= 16) {
            return (double) size / items.length;
        }
        return MINUSAGE;
    }

    /** remove and return the item from the back */
    public Item removeLast() {
        emptyChecker();
        int newLast = moveAhead(last);
        Item temp = items[newLast];
        items[newLast] = null;
        last = newLast;
        size -= 1;

        resize();
        return temp;
    }

    /** returns an iterator over items in order from front to back */
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private int currentIndex = first;
        private int covered = 0;

        public boolean hasNext() {
            return covered < size;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Can't go next!");
            }
            currentIndex = moveBackward(currentIndex);
            covered += 1;
            return items[currentIndex];
        }

        public void remove() {
            throw new UnsupportedOperationException("Can't call remove() inside iterator!");
        }
    }

    /** unit testing */
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        deque.addLast("a");
        deque.addLast("b");
        deque.addFirst("c");

        for (String i : deque) {
            System.out.println(i);
        }

        System.out.println(deque.removeFirst());
        System.out.println(deque.removeLast());
        System.out.println(deque.size());
        System.out.println(deque.isEmpty());
    }
}
