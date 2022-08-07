import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Dictionary {
    public static ArrayList<Word> words = new ArrayList<>();
    public Dictionary() {

    }

    public ArrayList<Word> getDic() {
        return words;
    }

    public static void setWords(ArrayList<Word> words) {
        Dictionary.words = words;
    }

    public static ArrayList<Word> getWords() {
        return words;
    }

    public static void addWord(Word newWord) {
        words.add(newWord);
    }

    public void removeWord(Word word) {
        //find word
        int index = words.indexOf(word);
        if (index > 0) {
            words.remove(index);
        }
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