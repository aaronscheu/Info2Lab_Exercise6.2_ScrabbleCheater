import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by amaridev on 22/01/16.
 * Package: PACKAGE_NAME for Exercise6.1_ScrabbleCheater.
 */
public class Scrabble {

    Dictionary dict;

    public Scrabble() {
        dict = new Dictionary("/Users/amaridev/Documents/IdeaProjects/Info2/" +
                "Exercise6.1_ScrabbleCheater/src/dictionary.txt");
    }

    public static void main(String[] args) {
        Scrabble scrabble = new Scrabble();
        scrabble.cheater("nora");
//        scrabble.dict.maxCollision();
    }

    public void cheater (String letters) {
        ArrayList<String> permutations = makePermutation(letters);
        ArrayList<String> words = dict.lookup(letters);

        words.parallelStream().forEach( lookedUp ->

            permutations.parallelStream().forEach( (permutation) -> {
                if (lookedUp.equals(permutation))
                    System.out.println(permutation);
            })
        );
    }


    @NotNull
    private String getRandomString(int length){
        Random random = new Random();
        return random.ints(97,122)
                .mapToObj(i -> (char) i)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }


    private boolean isPermutation(String word, String compare){
        ArrayList<String> permutations = makePermutation(word);

        for (String string : permutations) {
            if (string.equals(compare))
                return true;
        }
        return false;
    }


    private ArrayList<String> makePermutation(String s) {
        ArrayList<String> output = new ArrayList<>();
        makePermutation("", s, output);
        return output;
    }

    private void makePermutation(String prefix, String s, ArrayList<String> list) {
        int N = s.length();
        if (N == 0) list.add(prefix);
        else {
            for (int i = 0; i < N; i++)
                makePermutation(prefix + s.charAt(i), s.substring(0, i) + s.substring(i+1, N), list);
        }
    }

}
