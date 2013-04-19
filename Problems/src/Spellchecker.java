import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
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
        String formattedWord = formattedDictionary.get(formatWord_noDuplicates(word));
        if (formattedWord != null) {
            return formattedWord;
        } else {
            return "NO SUGGESTION";
        }
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
                String word = s2.next();
                String word_noDuplicates = formatWord_noDuplicates(word).toLowerCase();
                formattedDictionary.put(word_noDuplicates, word);
                originalDictionary.put(word.toLowerCase(), word);
            }
        }
    }

    private String formatWord_noDuplicates(String word) {
        String formattedWord = removeDuplicates(word);
        return formatVowels(formattedWord);
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

    private String removeDuplicates(String word) {
        String pattern = "(.)(?=\\1)";
        return word.replaceAll(pattern, "");
    }

    public static void main(String[] args) {
        Spellchecker spellChecker = new Spellchecker();
        spellChecker.run();
    }
}