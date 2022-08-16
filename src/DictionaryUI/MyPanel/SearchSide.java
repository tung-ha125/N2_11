package DictionaryUI.MyPanel;
import DictionaryConsole.Dictionary;
import DictionaryUI.DictionaryApplication;
import DictionaryConsole.Word;
import Trie.Trie;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class SearchSide extends JPanel {
    private final JButton searchButton = new JButton("Search");
    private final JButton spellButton = new JButton("Spell");
    private final JTextField searchField = new JTextField();
    private final JScrollPane suggestionScroll = new JScrollPane();
    private final JPanel suggestionPanel = new JPanel(new FlowLayout());
    private final int MAX_BUTTON_NUMS = 20;
    private final JButton[] buttons = new JButton[MAX_BUTTON_NUMS];
    private final int SEARCH_SIDE_WIDTH = 350;
    private final int SEARCH_SIDE_HEIGHT = 600;

    public SearchSide() {
        //do nothing
    }

    /**
     * set up for render search side
     */
    public void renderSearchSide() {
        //set up for panel
        this.setBackground(Color.WHITE);
        this.setBounds(0, 25, SEARCH_SIDE_WIDTH, SEARCH_SIDE_HEIGHT);
        this.setLayout(new FlowLayout());
        this.setBorder(BorderFactory.createLineBorder(new Color(127, 138, 148)));

        //set up for search part
        searchField.setPreferredSize(new Dimension(SEARCH_SIDE_WIDTH, 50));
        searchField.setBounds(0, 0, SEARCH_SIDE_WIDTH, 50);
        searchField.setBorder(BorderFactory.createLineBorder(new Color(127, 138, 148)));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchByPreFix();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchByPreFix();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchByPreFix();
            }
        });
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    search();
                }
            }
        });

        //set up search button, button will be under search field
        DictionaryApplication.setButton(searchButton, 80, 30);
        searchButton.setBounds(0, searchField.getY() + searchField.getHeight(), 80, 30);
        searchButton.addActionListener(e -> search());

        //suggestion scroll will be under the search button
        suggestionScroll.setPreferredSize(new Dimension(SEARCH_SIDE_WIDTH, 500));
        suggestionScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        for (int i = 0; i < MAX_BUTTON_NUMS; i++) {
            buttons[i] = new JButton();
            DictionaryApplication.setButton(buttons[i], this.getWidth(), 50);
            buttons[i].setVisible(false);
            buttons[i].setBackground(Color.WHITE);
            buttons[i].setBorder(BorderFactory.createEtchedBorder());
            suggestionPanel.add(buttons[i]);
        }
        suggestionPanel.setPreferredSize(new Dimension(suggestionScroll.getWidth(), 1000));
        suggestionScroll.setViewportView(suggestionPanel);

        //set up spell button, spell button will be on the same line as search button
        DictionaryApplication.setButton(spellButton, 80, 30);
        spellButton.setBounds(0, searchField.getY() + searchField.getHeight(), 80, 30);
        spellButton.addActionListener(e -> DictionaryApplication.speech(searchField.getText()));

        //add all opponents to the search panel
        this.add(searchField);
        this.add(searchButton);
        this.add(spellButton);
        this.add(suggestionScroll);
    }

    /**
     * use for suggestion features (which is rendered below search part) to
     * suggest some words can find in dictionary, user can click the suggestion to
     * access the definition directly
     * Warn: it is 20 suggestions maximum
     */
    private void searchByPreFix() {
        //get word from the search field
        String userWord = searchField.getText().trim();

        //find the word in dictionary and add it to a temporary list
        List<Integer> tmp = Trie.searchPrefixOfWord(userWord.toLowerCase(), MAX_BUTTON_NUMS);
        ArrayList<Word> tempWords = new ArrayList<Word>();
        for (Integer it : tmp) {
            tempWords.add(Dictionary.getWordAt(it));
        }

        //logic for render
        if (tempWords.size() == 0) {
            TranslateSide.translateArea.setText("Không tìm thấy");
            for (int i = 0; i < MAX_BUTTON_NUMS; i++) {
                buttons[i].setEnabled(false);
                buttons[i].setVisible(false);
            }
        } else {
            for (int i = 0; i < Math.min(tempWords.size(), MAX_BUTTON_NUMS); i++) {
                buttons[i].setText(tempWords.get(i).word_target);
                int finalI1 = i;
                ActionListener[] listeners = buttons[i].getActionListeners();
                for (ActionListener l : listeners) {
                    buttons[i].removeActionListener(l);
                }
                buttons[i].addActionListener(e->{
                    Word word = tempWords.get(finalI1);
                    TranslateSide.translateArea.setText("<html> <h1 color=purple>" + word.word_target + "</h1>"  + "<u>Phát âm</u>: " + word.word_pronounce + word.word_explain + "</html>");
                });
                buttons[i].setVisible(true);
                buttons[i].setEnabled(true);
            }

            for (int i = tempWords.size(); i < MAX_BUTTON_NUMS; i++) {
                buttons[i].setEnabled(false);
                buttons[i].setVisible(false);
            }
        }
    }

    /**
     * to search the word while we click search button
     * it will be used for add action listener of the search button or click Enter
     */
    private void search() {
        String userWord = searchField.getText().trim();
        Word word = Dictionary.getWordAt(Trie.searchAWord(userWord.toLowerCase()));
        if (word == null) {
            TranslateSide.translateArea.setText("Không tìm thấy");
            for (int i = 0; i < MAX_BUTTON_NUMS; i++) {
                buttons[i].setEnabled(false);
                buttons[i].setVisible(false);
            }
        } else {
            TranslateSide.translateArea.setText("<html> <h1 color=purple>" + word.word_target + "</h1>"  + "<u>Phát âm</u>: " + word.word_pronounce + word.word_explain + "</html>");
        }
    }
}
