import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class DictionaryManagement extends Dictionary {

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
        //scanner.close();
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
        String userWord = getUserWord();
        Word word = findWordInDictionary(userWord);
        if (word == null) {
            System.out.println("Khong tim thay tu ban tra trong tu dien.");
        } else {
            word.printInfor();
        }
    }

    private String getUserWord() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhap tu ban muon tim: ");
        String word;
        word = scanner.nextLine();
        scanner.close();
        return word.toLowerCase();
    }

    private Word findWordInDictionary(String word) {
        for (Word i : words) {
            if(Objects.equals(word, i.getWord_target())) {
                return i;
            }
        }
        return null;
    }

}
