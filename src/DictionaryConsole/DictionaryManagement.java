package DictionaryConsole;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class DictionaryManagement extends Dictionary {

    Scanner scanner = new Scanner(System.in);
    DictionaryManagement() throws IOException {
        insertFromFile();
    }

    public void insertFromCommandline() {
        int n;
        String s;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhap so tu: ");
        n = scanner.nextInt();
        s = scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String target;
            String explain;
            Word temp;
            System.out.print("Anh: ");
            target = scanner.nextLine();
            System.out.print("Viet: ");
            explain = scanner.nextLine();
            temp = new Word(target, explain);

            words.add(temp);
        }
    }

    public void insertFromFile() throws FileNotFoundException {
        String path = "src/dictionaries.txt";
        File file = new File(path);
        FileReader reader = new FileReader(file);
        BufferedReader in = new BufferedReader(reader);
        try {
            String line = in.readLine();
            while (line != null) {
                words.add(handleLine(line));
                line = in.readLine();
            }
        } catch (IOException e) {
            System.out.println("IOException.");
        } finally {
            try {
                in.close();
                reader.close();
            } catch (Exception e) {

            }
        }
    }

    private Word handleLine(String line) {
        String word_target = "";
        String word_explain = "";
        int index = line.indexOf('\t');
        //Get word_target
        for (int i = 0; i < index; i++) {
            word_target += line.charAt(i);
        }
        //Get word_explain
        for (int i = index + 1; i < line.length(); i++) {
            word_explain += line.charAt(i);
        }
        return new Word(word_target,word_explain);
    }

    public void dictionaryLookup() {
        String userWord = getUserWord("find");
        Word word = findWordInDictionary(userWord);
        if (word == null) {
            System.out.println("Khong tim thay tu cua ban trong tu dien.");
        } else {
            word.printInfor();
        }
    }

    public String getUserWord(String s) {
        if (s.equals("find")) {
            System.out.print("Nhap tu ban muon tim: ");
        } else if (s.equals("add")) {
            System.out.print("Nhap tu ban muon them: ");
        } else if (s.equals("edit")) {
            System.out.print("Nhap tu ban muon sua: ");
        } else if (s.equals("erase")) {
            System.out.print("Nhap tu ban muon xoa: ");
        } else if (s.equals("confirm")) {
            System.out.print("Xac nhan thao tac (yes/no): ");
        } else if (s.equals("try again")) {
            System.out.print("Thuc hien lai thao tac (yes/no): ");
        } else {
            System.out.print("Thao tac khong ton tai!!!");
            return "";
        }
        String word;
        word = scanner.nextLine();
        return word.toLowerCase();
    }

    protected Word findWordInDictionary(String word) {
        for (Word i : words) {
            if(Objects.equals(word, i.getWord_target().toLowerCase())) {
                return i;
            }
        }
        return null;
    }

    public ArrayList<Word> dictionarySearcher(String s) {
        ArrayList<Word> a1 = new ArrayList<Word>();
        ArrayList<Word> a2 = new ArrayList<Word>();
        ArrayList<Word> a3 = new ArrayList<Word>();
        if ("".equals(s.trim())) {
            return a1;
        }
        for (Word i : words) {
            String S = i.word_target;
            if (s.length() > S.length()) continue;
            if (S.substring(0, s.length()).toLowerCase().equals(s)) {
                a1.add(i);
            } else if (S.toLowerCase().contains(s)) {
                a2.add(i);
            } else {
                int k = 0;
                for (int j = 0; j < S.length(); ++j) {
                    if (Character.toLowerCase(S.charAt(j)) == s.charAt(k)) {
                        ++k;
                    }
                    if (k == s.length()) break;
                }
                if (k == s.length()) a3.add(i);
            }
        }
        a1.addAll(a2);
        a1.addAll(a3);
        return a1;
    }

    public void add() throws IOException {
        String newTarget = getUserWord("add");
        if (findWordInDictionary(newTarget) != null) {
            System.out.println("Tu nay da co trong he thong !!!");
            String cf = getUserWord("try again");
            if (cf.equals("yes")) {
                add();
            }
            return;
        }
        System.out.print("Nghia cua tu '" + newTarget + "' la: ");
        String newExplain = scanner.nextLine();
        String path = "src/dictionaries.txt";
        ArrayList<String> S = readSmallTextFile(path);
        S.add(newTarget + '\t' + newExplain);
        writeSmallTextFile(S, path);
        System.out.println(("Thao tac them thanh cong!!!"));
    }

    public void edit() throws IOException {
        Word userWord = findWordInDictionary(getUserWord("edit"));
        if (userWord == null) {
            System.out.println("Tu nay khong co trong he thong!!!");
            return;
        }
        System.out.print("Tu cua ban dang muon sua la: ");
        userWord.printInfor();
        System.out.print("Ban muon sua nghia cua tu '" + userWord.word_target + "' thanh: ");
        String newWord;
        newWord = scanner.nextLine();
        String path = "src/dictionaries.txt";
        ArrayList<String> S = readSmallTextFile(path);
        int x = S.indexOf(userWord.word_target + '\t' + userWord.word_explain);
        S.set(x, S.get(x).replace(userWord.word_explain, newWord));
        writeSmallTextFile(S, path);
        System.out.println(("Thao tac sua thanh cong!!!"));
    }

    public void erase() throws IOException {
        Word userWord = findWordInDictionary(getUserWord("erase"));
        if (userWord == null) {
            System.out.println("Tu nay khong co trong he thong!!!");
            return;
        }
        System.out.print("Tu cua ban dang muon xoa la: ");
        userWord.printInfor();
        String cf = getUserWord("confirm");
        if (!cf.equals("yes")) {
            System.out.println("Da huy thao tac xoa!!!");
            return;
        }
        String path = "src/dictionaries.txt";
        ArrayList<String> S = readSmallTextFile(path);
        int x = S.indexOf(userWord.word_target + '\t' + userWord.word_explain);
        S.remove(x);
        writeSmallTextFile(S, path);
        System.out.println(("Thao tac xoa thanh cong!!!"));
    }

    private final static Charset ENCODING = StandardCharsets.UTF_8;

    ArrayList<String> readSmallTextFile(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        return new ArrayList<String>(Files.readAllLines(path, ENCODING));
    }

    public void writeSmallTextFile(ArrayList<String> lines, String fileName) throws IOException {
        Path path = Paths.get(fileName);
        Files.write(path, lines, ENCODING);
    }

    public void dictionaryExportToFile() throws IOException{
        String path = "src/dictionaries.txt";
        ArrayList<String> S = new ArrayList<>();
        for (Word i : words) {
            S.add(i.word_target + '\t' + i.word_explain);
        }
        writeSmallTextFile(S, path);
        System.out.println("Successfully export to src/dictionaries.txt");
    }
}

