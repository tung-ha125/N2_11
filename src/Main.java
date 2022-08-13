import javax.xml.crypto.Data;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException {
//        DictionaryCommandline a = new DictionaryCommandline();
//        a.dictionaryBasic();
//        a.dictionarySearcher();
        Database.connectToDatabase();
        Database.getAllWord();
        DictionaryApplication dic = new DictionaryApplication();
        dic.runApplication();
        dic.dictionaryAdvance();

    }


}
