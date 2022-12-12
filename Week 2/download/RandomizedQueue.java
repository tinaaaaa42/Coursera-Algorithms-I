/* *****************************************************************************
 *  Name:         Dale Young
 *  Date:         12/11/2022
 *  Description:  a queue that remove items by choosing uniformly at random
                  among items
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private static final int INITIALSIZE = 8;
    private static final int REFACTOR = 2;
    private static final double MINUSAGE = 0.25;

    private Item[] items;
    private int size;

    /** construct an empty randomized queue */
    public RandomizedQueue() {
        items = (Item[]) new Object[INITIALSIZE];
        size = 0;
    }

    /** is the randomized queue empty? */
    public boolean isEmpty() {
        return size == 0;
    }

    /** return the number of items on the randomized queue */
    public int size() {
        return size;
    }

    private boolean isFull() {
        return size == items.length;
    }

    private double usage() {
        if (items.length >= 16) {
            return (double) size / items.length;
        }
        return MINUSAGE;
    }

    private void resize() {
        if (isFull()) {
            Item[] temp = (Item[]) new Object[REFACTOR * size];
            for (int i = 0; i < size; i += 1) {
                temp[i] = items[i];
            }
            items = temp;
        }

        while (usage() < MINUSAGE) {
            Item[] temp = (Item[]) new Object[items.length / REFACTOR];
            for (int i = 0; i < size; i += 1) {
                temp[i] = items[i];
            }
            items = temp;
        }

    }

    /** add the item */
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("No null item!");
        }

        resize();
        items[size] = item;
        size += 1;
    }

    private void emptyChecker() {
        if (isEmpty()) {
            throw new NoSuchElementException("No item!");
        }
    }

    /** remove and return a random item */
    public Item dequeue() {
        emptyChecker();
        int index = StdRandom.uniformInt(size);
        Item temp = items[index];
        items[index] = items[size - 1];
        
        size -= 1;
        resize();
        return temp;
    }

    /** return a random item (but not remove it) */
    public Item sample() {
        emptyChecker();
        int index = StdRandom.uniformInt(size);
        return items[index];
    }

    /** return an independent iterator over items in random order */
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int tempSize = size;
        private Item[] temp = items;

        public boolean hasNext() {
            return tempSize > 0;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more!");
            }
            int index = StdRandom.uniformInt(tempSize);
            Item chosenItem = temp[index];
            temp[index] = temp[tempSize - 1];

            tempSize -= 1;
            return chosenItem;
        }

        public void remove() {
            throw new UnsupportedOperationException("Can't call remove()!");
        }
    }

    /** unit testing */
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        rq.enqueue("a");
        rq.enqueue("b");
        rq.enqueue("c");
        for (String s : rq) {
            System.out.println(s);
        }
        System.out.println(rq.dequeue());
        System.out.println(rq.sample());
        System.out.println(rq.size());
        System.out.println(rq.isEmpty());

    }
}
