import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class DictionaryCommandline extends DictionaryManagement {

    DictionaryCommandline() throws IOException {

    }

    public static void showAllWords() {
        System.out.printf("%-15s|%-35s|%s\n", "No", "English", "Vietnamese");
        for (int i = 0; i < words.size(); i++) {
            System.out.printf("%-15s|%-35s|%s\n", i + 1,
                    words.get(i).getWord_target(), words.get(i).getWord_explain());
        }
    }

    public static void showAllWords(ArrayList<Word> words) {
        System.out.printf("%-15s|%-35s|%s\n", "No", "English", "Vietnamese");
        for (int i = 0; i < words.size(); i++) {
            System.out.printf("%-15s|%-35s|%s\n", i + 1,
                    words.get(i).getWord_target(), words.get(i).getWord_explain());
        }
    }

    public void dictionaryBasic() throws FileNotFoundException, IOException {
        insertFromFile();
    }

    public void dictionaryAdvance() throws FileNotFoundException, IOException {
        insertFromFile();
        showAllWords();
        dictionaryLookup();
    }

}
