package Trie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node {
    List<Integer> prefixOf = new ArrayList<Integer>();
    Map<Character, Node> next = new HashMap<Character, Node>();
    int endOfAWord = -1;
}
