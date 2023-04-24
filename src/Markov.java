import java.io.File;
import java.io.IOException;
import java.util.*;
import static java.lang.System.*;
/**
 * Author: Nima Mahanloo
 * Title: Markov
 * Date: March 19, 2023
 * This program reads a text file containing sentences,
 * then it stores its words in a data structure sequentially,
 * and creates and presents a sentence with them in the output.
 */
public class Markov {
    private static final String ENDS_IN_PUNCTUATION = "__$";
    private static final String PUNCTUATION_MARKS = ".!?";
    private String prevWord;
    private HashMap<String, ArrayList<String>> words = new HashMap<>();
    public Markov() {
        prevWord = ENDS_IN_PUNCTUATION;
        words.put(ENDS_IN_PUNCTUATION, new ArrayList<String>());
    }
    public void addFromFile(String filename) {
        Scanner scan = null;
        File file = new File(filename);
        String line = null;
        try {
            if (file.exists()) {
            }
            scan = new Scanner(file);
        } catch (IOException e) {
            System.out.println("File " + filename + " not exists!");
            exit(10); // It terminates the program with exit code 10.
        }
        while (scan != null && scan.hasNext()) {
            line = scan.nextLine();
            addLine(line);
        }
    }
    public void addLine(String line) {
        int startIndex = 0;
        String word = new String();
        line = line.replaceAll("[\s, \t, \n]", "\s");
        for (int index = 0; index < line.length(); index++) {
            while ((index < line.length()) && (line.charAt(index) == ' ')) {
                index++;
            }
            startIndex = index;
            while ((index < line.length()) && (line.charAt(index) != ' ')) {
                index++;
            }
            if ((startIndex < line.length()) && (startIndex < index)) {
                word = line.substring(startIndex, index);
                try {
                    if (word != null) {
                        addWord(word);
                    }
                } catch (Exception e) {
                    System.out.println("Line parsing error!");
                    exit(20); // It terminates the program with exit code 20.
                }
            }
        }
    }
    public static boolean endsWithPunctuation(String testWord) {
        Boolean punctuation = false;
        if (testWord.length() > 0) {
            for (int index = 0; index < PUNCTUATION_MARKS.length(); index++) {
                if (testWord.charAt(testWord.length()-1) == PUNCTUATION_MARKS.charAt(index)) {
                    punctuation = true;
                }
            }
        }
        return punctuation;
    }
    public void addWord(String word) {
        String keyword = null;
        if (endsWithPunctuation(prevWord)) {
            keyword = ENDS_IN_PUNCTUATION;
        }
        else {
            keyword = prevWord;
        }
        if (words.containsKey(keyword)) {
            if (keyword.equals(ENDS_IN_PUNCTUATION)) {
                if (!(endsWithPunctuation(word))) {
                    words.get(keyword).add(word);
                }
            }
            else {
                words.get(keyword).add(word);
            }
        }
        if ((!(words.containsKey(word))) && (!(endsWithPunctuation(word)))) {
            words.put(word, new ArrayList<String>());
        }
        prevWord = word;
    }
    public String getSentence() {
        String sentence = new String();
        prevWord = ENDS_IN_PUNCTUATION;
        while (!(endsWithPunctuation(prevWord))) {
            prevWord = randomWord(prevWord);
            sentence = sentence.concat(prevWord);
            if (!(endsWithPunctuation(prevWord))) {
                sentence = sentence.concat("\s");
            }
        }
        return sentence;
    }
    public String randomWord(String keyword) {
        int arraySize = 0;
        int index = 0;
        try {
            if (words.get(keyword).size() > 0) {
                arraySize = words.get(keyword).size();
            }
        } catch (Exception e) {
            System.out.println("Array size error in selecting output words!");
            exit(30); // It terminates the program with exit code 30.
        }
        Random random = new Random();
        index = Math.abs(random.nextInt() % arraySize);
        return words.get(keyword).get(index);
    }
    public HashMap<String, ArrayList<String>> getWords() {
        return words;
    }
    @Override
    public String toString() {
        return words.toString();
    }
}