import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Spellchecker {

    private static final String VOWEL_DELIMITER = "?";
    private static final String DICTIONARY_WORDS_PATH = "/usr/share/dict/words";
    private Map<String, String> formattedDictionary;
    private Map<String, String> originalDictionary;
    private ArrayList<String> vowelList;

    private void run() {
        createCollections();
        loadWords();
        enterWord();
    }

    private void createCollections() {
        formattedDictionary = new HashMap<String, String>();
        originalDictionary = new HashMap<String, String>();
        vowelList = new ArrayList<String>();
        vowelList.add("a");
        vowelList.add("e");
        vowelList.add("i");
        vowelList.add("o");
        vowelList.add("u");
    }

    private void enterWord() {
        System.out.println("Enter a word to spellcheck. Enter an empty string to exit");
        while (true) {
            Scanner in = new Scanner(System.in);
            String word = in.nextLine().toLowerCase();
            if (word.length() == 0) {
                System.out.println("No word entered ... exiting");
                break;
            }
            System.out.println(findWord(word));
        }
    }

    private String findWord(String word) {
        if (originalDictionary.get(word) != null) {
            return word;
        }
        ArrayList<String> words = returnPossibleWordsFromRepeatedLetters(word);
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

    private void loadWords() {
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

    private String formatVowels(String word) {
        String formattedWords = "";
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            if (isVowel(letter)) {
                formattedWords += VOWEL_DELIMITER;
            } else {
                formattedWords += letter;
            }
        }
        return formattedWords;
    }

    private boolean isVowel(char letter) {
        return vowelList.contains(String.valueOf(letter));
    }

    private ArrayList<String> returnPossibleWordsFromRepeatedLetters(String word) {
        String pattern = "(.)(?=\\1{2})";
        String maxTwoRepetitions = word.replaceAll(pattern, "");
        ArrayList<String> possibleWords = new ArrayList<String>();
        possibleWords.add(maxTwoRepetitions);
        words("", maxTwoRepetitions, possibleWords);
        return possibleWords;
    }

    private List<String> words(String start, String ending, List<String> possibleWords) {
        if (ending.length() <= 1) {
            possibleWords.add(start + ending);
            return possibleWords;
        }
        String prev = "";
        for (int i = 0; i < ending.length(); i++) {
            String letter = String.valueOf(ending.charAt(i));
            // its a double letter
            if (letter.equals(prev)) {
                words(start + ending.substring(0, i), ending.substring(i + 1), possibleWords);
                words(start + ending.substring(0, i + 1), ending.substring(i + 1), possibleWords);
            }
            prev = letter;
        }
        return null;
    }

    public static void main(String[] args) {
        Spellchecker sp = new Spellchecker();
        sp.run();
    }
}