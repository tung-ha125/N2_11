import Database.Database;
import DictionaryUI.DictionaryApplication;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Database.connectToDatabase();
        Database.getAllWord();
        DictionaryApplication dic = new DictionaryApplication();
        dic.runApplication();
        dic.dictionaryAdvance();
    }
}
