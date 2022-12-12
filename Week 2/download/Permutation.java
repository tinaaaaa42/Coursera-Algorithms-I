/* *****************************************************************************
 *  Name:         Dale Young
 *  Date:         12/11/2022
 *  Description:  A client program that takes an integer k as a command-line
                  argument; reads a sequence of strings from standard input
                  using StdIn.readString(); and prints exactly k of them,
                  uniformly at random.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            rq.enqueue(StdIn.readString());
        }

        for (int i = 0; i < Integer.parseInt(args[0]); i += 1) {
            System.out.println(rq.dequeue());
        }
    }
}
