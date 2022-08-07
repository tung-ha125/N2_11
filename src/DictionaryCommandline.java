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

    private void searchByPreFix() {
        String s = getUserWord("find");
        ArrayList<Word> list = dictionarySearcher(s);
        showAllWords(list);
    }

    public void dictionaryAdvance() throws FileNotFoundException, IOException {
        boolean quitFlag = false;
        while(!quitFlag) {
            System.out.println("Danh sách chức năng: \n"
                                + "\t0: Lấy từ từ file txt\n"
                                + "\t1: Bảng danh sách từ có trong từ điển\n"
                                + "\t2: Tìm từ\n"
                                + "\t3: Tìm từ bằng pre-fix\n"
                                + "\t4: Xóa từ\n"
                                + "\t5: Thêm từ\n"
                                + "\t6: Sủa từ\n"
                                + "\t7: Thoát\n"
                                + "Nhập chức năng bạn muốn dùng: "
            );
            int num = scanner.nextInt();
            switch (num) {
                case 0:
                    insertFromFile();
                    break;
                case 1:
                    showAllWords();
                    break;
                case 2:
                    dictionaryLookup();
                    break;
                case 3:
                    searchByPreFix();
                    break;
                case 4:
                    erase();
                    break;
                case 5:
                    add();
                    break;
                case 6:
                    edit();
                    break;
                case 7:
                    quitFlag = true;
                    break;

            }
        }
    }
}
