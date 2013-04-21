import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Spelling {
    
    public Spelling() {
       createCollections();
    }
    protected Map<String, String> originalDictionary;
    protected ArrayList<String> vowelList;
    
     void createCollections() {
        originalDictionary = new HashMap<String, String>();
        vowelList = new ArrayList<String>();
        vowelList.add("a");
        vowelList.add("e");
        vowelList.add("i");
        vowelList.add("o");
        vowelList.add("u");
    }

    public boolean isVowel(String letter) {
        return vowelList.contains(letter);
    }
}
