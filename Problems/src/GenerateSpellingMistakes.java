import java.util.Random;


public class GenerateSpellingMistakes extends Spelling {

    public String generateMisspeltWord(String word){
        Random r = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            String letter = String.valueOf(word.charAt(i));
            //Give character a random case
            String changeLetter = r.nextBoolean() ? letter.toUpperCase() : letter.toLowerCase();
            //If character is a vowel, randomly change it
            changeLetter = r.nextBoolean() && isVowel(changeLetter) ? vowelList.get(r.nextInt(vowelList.size()-1)) : changeLetter;
            //randomly duplicate a letter
            changeLetter = r.nextBoolean() ? changeLetter + changeLetter : changeLetter;
            builder.append(changeLetter);
        }
        return builder.toString();
    }
    
    public static void main(String[] args) {
        GenerateSpellingMistakes g = new GenerateSpellingMistakes();
        Spellchecker sp = new Spellchecker();
        sp.loadWords();
        //Random word with spelling mistakes
        String randomWord = sp.getRandomWord();
        System.out.println("Random word from dictionary: " + randomWord);
        String misspeltWord = g.generateMisspeltWord(randomWord);
        System.out.println("Generated misspelt word: " + misspeltWord);
        String result = sp.findWord(randomWord);
        System.out.println("Result from spellchecker: " + result);
    }
}
