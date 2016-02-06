import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;



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
        String randomString = scrabble.getRandomString(5);
        ArrayList<String> buchstabensalat = scrabble.powerSet(randomString);
        ArrayList<String> output = scrabble.superCheaterDeluxe(buchstabensalat);

        System.out.println("Random String: " + randomString + "\n");
        output.stream().forEach(System.out::println);
    }

    public void cheater (String letters) {
        ArrayList<String> permutations = makePermutation(letters);
        ArrayList<String> words = new ArrayList<>();
        ArrayList<String> out = new ArrayList<>();

        permutations.stream().forEach( perm ->
                words.addAll(dict.lookup(perm))
        );

        permutations.parallelStream().forEach( perm -> {
            if (words.contains(perm))
                out.add(perm);
        });

        removeDublicates(out).stream().forEach(System.out::println);
    }

    public ArrayList<String> superCheaterDeluxe (ArrayList<String> input) {
        ArrayList<String> permutations = new ArrayList<>();
        ArrayList<String> words = new ArrayList<>();
        ArrayList<String> out = new ArrayList<>();

        input.stream().forEach( in ->
            permutations.addAll(makePermutation(in))
        );

        permutations.stream().forEach( perm ->
                words.addAll(dict.lookup(perm))
        );

        permutations.parallelStream().forEach( perm -> {
            if (words.contains(perm))
                out.add(perm);
        });

        out.removeIf( s -> (s.length() < 2));
        return removeDublicates(out);

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

    private ArrayList<String> removeDublicates(ArrayList<String> input) {
        ArrayList<String> truncList = new ArrayList<>();

        input.stream().forEach( perm -> {
            if(!truncList.contains(perm))
                truncList.add(perm);
        });

        return truncList;
    }

    private ArrayList<String> removePerm(ArrayList<String> input) {
        ArrayList<String> truncList = new ArrayList<>(input);

        input.stream().forEach( word -> {
            ArrayList<String> perms = makePermutation(word);

            truncList.removeAll(perms);
            truncList.add(word);
        });

        return truncList;
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
                makePermutation(prefix + s.charAt(i), s.substring(0, i) + s.substring(i +1, N), list);
        }
    }

    public ArrayList<String> powerSet(String string) {
        ArrayList<String> set = new ArrayList<>();
        powerSet(set, string);

        set = removeDublicates(set);
        set.remove("");

        return set;
    }

    private void powerSet(ArrayList<String> list, String string) {

        list.add(string);

        for (char ch : string.toCharArray()) {
            String substring = string.replace(String.valueOf(ch), "");
            powerSet(list, substring);
        }
    }


    public ArrayList<String> binaryPowerSet(String input)
    {
        // This algorithm works only with no duplicates.
        // As workaround I need to remove wrong elements afterwards.

        int inputLength = input.length();
        int powerSetSize = (int)Math.pow(2, inputLength);
        ArrayList<String> result = new ArrayList<>();

        for (int i = 0; i < powerSetSize; i++) {
            int binaryDigits = i;
            String set = "";

            for (int j = 0; j < inputLength; j++) {
                if (binaryDigits % 2 == 1)
                    set += input.charAt(j);
                binaryDigits >>= 1;
            }

            result.add(set);
        }

        result.removeIf( s -> (s.length() < 2));
        result = removePerm(result);

        return result;
    }




}
