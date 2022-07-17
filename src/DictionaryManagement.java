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
        scanner.close();
    }
}
