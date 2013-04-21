import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Spellchecker extends Spelling{

    private static final String VOWEL_DELIMITER = "?";
    private static final String DICTIONARY_WORDS_PATH = "/usr/share/dict/words";
    private Map<String, String> formattedDictionary;

    public Spellchecker() {
        super();
        formattedDictionary = new HashMap<String, String>();

    }

    public void loadWords() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(DICTIONARY_WORDS_PATH));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNextLine()) {
            Scanner s2 = new Scanner(scanner.nextLine());
            while (s2.hasNext()) {
                String word = s2.next().toLowerCase();
                String wildcardVowels = formatVowels(word);
                formattedDictionary.put(wildcardVowels, word);
                originalDictionary.put(word, word);
            }
        }
    }

    public void enterWord() {
        System.out.println("Enter a word to spellcheck. Enter an empty string to exit");
        while (true) {
            Scanner in = new Scanner(System.in);
            String word = in.nextLine();
            if (word.length() == 0) {
                System.out.println("No word entered ... exiting");
                break;
            }
            System.out.println(findWord(word));
        }
    }

    public String findWord(String word) {
        if (originalDictionary.get(word.toLowerCase()) != null) {
            return word.toLowerCase();
        }
        ArrayList<String> words = repeatedLettersPermutations(word.toLowerCase());
        Collections.sort(words);
        String foundWord = "";
        for (int i = 0; i < words.size(); i++) {
            foundWord = formattedDictionary.get(formatVowels(words.get(i)));
            if (foundWord != null) {
                return foundWord;
            }

        }
        return "NO SUGGESTION";
    }

    private String formatVowels(String word) {
        String formattedWords = "";
        for (int i = 0; i < word.length(); i++) {
            String letter = String.valueOf(word.charAt(i));
            if (isVowel(letter)) {
                formattedWords += VOWEL_DELIMITER;
            } else {
                formattedWords += letter;
            }
        }
        return formattedWords;
    }

    private ArrayList<String> repeatedLettersPermutations(String word) {
        String pattern = "(.)(?=\\1{2})";
        String maxTwoRepetitions = word.replaceAll(pattern, "");
        ArrayList<String> possibleWords = new ArrayList<String>();
        possibleWords.add(maxTwoRepetitions);
        doubleLetterPermute("", maxTwoRepetitions, possibleWords);
        return possibleWords;
    }

    private List<String> doubleLetterPermute(String start, String ending, List<String> possibleWords) {
        String prev = "";
        for (int i = 0; i < ending.length(); i++) {
            String letter = String.valueOf(ending.charAt(i));
            // its a double letter
            if (letter.equals(prev)) {
                doubleLetterPermute(start + ending.substring(0, i), ending.substring(i + 1), possibleWords);
                doubleLetterPermute(start + ending.substring(0, i + 1), ending.substring(i + 1), possibleWords);
            }
            prev = letter;
        }
        possibleWords.add(start + ending);
        return possibleWords;
    }
    
    public String getRandomWord(){
        Random r = new Random();
        List<String> keys = new ArrayList<String>(originalDictionary.keySet());
        return originalDictionary.get(keys.get(r.nextInt(keys.size())));
    }
    
    public static void main(String[] args) {
        Spellchecker sp = new Spellchecker();
        sp.loadWords();
        sp.enterWord();
    }
}