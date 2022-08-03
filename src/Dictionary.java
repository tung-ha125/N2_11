import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Dictionary {
    public static ArrayList<Word> words;
    public Dictionary() {
        words = new ArrayList<Word>();
    }

    public ArrayList<Word> getDic() {
        return words;
    }
    public void sortDictionary() {
        Collections.sort(words, new cmpWord());
    }
}


class cmpWord implements Comparator<Word> {
    public int compare(Word w1, Word w2) {
        return w1.getWord_target().compareTo(w2.getWord_target());
    }
}