import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String word = "";
        int cnt = 0;
        while (!StdIn.isEmpty()) {
            cnt += 1;
            String temp = StdIn.readString();
            if (StdRandom.bernoulli((double) 1 / cnt)) {
                word = temp;
            }
        }

        System.out.println(word);
    }
}
