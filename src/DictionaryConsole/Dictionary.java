package DictionaryConsole;

import java.util.ArrayList;

public class Dictionary {
    public static ArrayList<Word> words = new ArrayList<>();
    public Dictionary() {

    }

    public static ArrayList<Word> getDic() {
        return words;
    }

    public static void setWords(ArrayList<Word> words) {
        Dictionary.words = words;
    }

    public static ArrayList<Word> getWords() {
        return words;
    }

    public static Word getWordAt(int index) {
        if (index < 0 || index >= words.size()) {
            return null;
        }
        return words.get(index);
    }

    public static void addWord(Word newWord) {
        words.add(newWord);
    }

    public static void removeWord(Word word) {
        //find word
        int index = words.indexOf(word);
        if (index > 0) {
            words.remove(index);
        }
    }
}