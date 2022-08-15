package Trie;

import java.util.ArrayList;
import java.util.List;

public class Trie {
    public static Node root = new Node();

    /**
     * Add a word to trie and it's index.
     * @param Word the word add to trie
     * @param index index of word in List<Word>
     */
    public static void addWord(String Word,int index) {
        Node it = root;
        for (int i = 0; i < Word.length(); ++i) {
            Character CharAtI = Word.charAt(i);
            if (it.next.get(CharAtI) == null) {
                it.next.put(CharAtI, new Node());
            }
            it = it.next.get(CharAtI);
            it.prefixOf.add(index);
        }
        if (it.endOfAWord == -1) {
            it.endOfAWord = index;
        }
    }

    /**
     * Search a word on trie.
     * @param Word word to search
     * @return index of word in List<Word>
     */
    public static int searchAWord(String Word) {
        Node it = root;
        for (int i = 0; i < Word.length(); ++i) {
            Character CharAtI = Word.charAt(i);
            if (it.next.get(CharAtI) == null) {
                System.out.println("Tu ban tim khong ton tai!");
                return -1;
            }
            it = it.next.get(CharAtI);
        }
        if (it.endOfAWord == -1) {
            System.out.println("Tu ban tim khong ton tai!");
        }
        return it.endOfAWord;
    }

    /**
     * Search prefix of word on trie.
     * @param Word prefix of word to search
     * @param limit count of word to return
     * @return List<Integer> of word's indexes in List<Word>
     */
    public static List<Integer> searchPrefixOfWord(String Word, int limit) {
        Node it = root;
        for (int i = 0; i < Word.length(); ++i) {
            Character CharAtI = Word.charAt(i);
            if (it.next.get(CharAtI) == null) {
                return new ArrayList<Integer>();
            }
            it = it.next.get(CharAtI);
        }
        List<Integer> ans = new ArrayList<Integer>();
        for (int it2 = 0; it2 < Math.min(limit, it.prefixOf.size()); ++it2) {
            ans.add(it.prefixOf.get(it2));
        }
        return ans;
    }

    /**
     * Delete a word on trie.
     * @param Word word to delete
     */
    public static void deleteAWord(String Word) {
        int index = searchAWord(Word);
        if (index == -1) {
            System.out.println("Tu ban muon xoa khong ton tai");
        }
        Node it = root;
        for (int i = 0; i < Word.length(); ++i) {
            Character CharAtI = Word.charAt(i);
            it = it.next.get(CharAtI);
            it.prefixOf.remove((Integer) index);
        }
        it.endOfAWord = -1;
    }
}
