import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by amaridev on 22/01/16.
 * Package: PACKAGE_NAME for Exercise6.1_ScrabbleCheater.
 */

public class Dictionary {

    List<String> wordlist = new ArrayList<>();
    ArrayList<String>[] map;

    public Dictionary(String filename) {
        readFile(filename);
        int mapsize = getNextPrime((int) (.75 * wordlist.size()));
        map = new ArrayList[mapsize];

        for (int i = 0; i < map.length; i++)
            map[i] = new ArrayList<>();

        for (String string : wordlist) {
            int index = (int) (getHash(string) % map.length);
            map[index].add(string.toLowerCase());
        }
    }


    public long getHash(String word) {
        word = word.toLowerCase();
        int length = word.length();
        long hash = 0;

        for (int i = 0; i < length; i++) {
            char ch = word.charAt(i);
            hash += (int) Math.pow((ch * 31), (length - i + 1));
        }
        return hash;
    }


    public ArrayList<String> lookup(String word) {
        int index = (int) (getHash(word) % map.length);
        ArrayList<String> out = new ArrayList<>();

        map[index].stream().forEach(out::add);

        return out;
    }

    public void maxCollision() {
        int collision = 0;

        for (int i = 0; i < map.length; i++) {
            if (map[i].size() > collision) {
                collision = map[i].size();
            }

            if (map[i].size() > 1)
                System.out.println("Position of Collision: " + i);
        }

        System.out.println("Max. Collisions: " + collision);
    }


    private void readFile(String filename) {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filename))) {

            //br returns as stream and convert it into a List
            wordlist = br.lines().collect(Collectors.toList());
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean isPrime(int n) {
        //check if n is a multiple of 2
        if (n % 2 == 0) return false;
        //if not, then just check the odds
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }


    private int getNextPrime(int previous) {
        if (previous < 2) {
            return 2;
        }
        if (previous == 2) {
            return 3;
        }
        int next = 0;
        int increment = 0;
        switch (previous % 6) {
            case 0:
                next = previous + 1;
                increment = 4;
                break;
            case 1:
                next = previous + 4;
                increment = 2;
                break;
            case 2:
                next = previous + 3;
                increment = 2;
                break;
            case 3:
                next = previous + 2;
                increment = 2;
                break;
            case 4:
                next = previous + 1;
                increment = 2;
                break;
            case 5:
                next = previous + 2;
                increment = 4;
                break;
        }
        while (!isPrime(next)) {
            next += increment;
            increment = 6 - increment;   // 2, 4 alternating
        }
        return next;
    }
}

