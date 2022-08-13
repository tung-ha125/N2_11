import com.sun.speech.freetts.VoiceManager;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import Trie.Trie;


public class DictionaryApplication extends DictionaryCommandline implements ActionListener {

    private final JFrame mainFrame = new JFrame("Dictionary");

    private final JPanel subPanel = new JPanel(new GridBagLayout());
    private final JPanel searchPanel = new JPanel(new GridLayout());
    private final JPanel translatePanel = new JPanel();

    private final JMenuBar menuBar = new JMenuBar();

    private final JMenu featuresMenu = new JMenu("Features");
    private final JMenuItem addMenu = new JMenuItem("Add");
    private final JMenuItem editMenu = new JMenuItem("Edit");
    private final JMenuItem eraseMenu = new JMenuItem("Erase");
    private final JMenuItem GGTranslateMenu = new JMenuItem("GG Translate");

    private final JButton searchButton = new JButton("Search");

    private final JButton spellButton = new JButton("Spell");

    private JTextField searchField = new JTextField();
    private JTextPane translateArea = new JTextPane();

    private JScrollPane translateScroll = new JScrollPane(translateArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    JScrollPane suggestionScroll = new JScrollPane();
    JPanel suggestionPanel = new JPanel(new FlowLayout());
    private final int MAX_BUTTON_NUMS = 20;
    JButton[] buttons = new JButton[MAX_BUTTON_NUMS];

    private int FRAME_WIDTH = 1000;
    private int FRAME_HEIGHT = 650;

    private int SEARCH_SIDE_WIDTH = 350;
    private int SEARCH_SIDE_HEIGHT = 600;
    DictionaryApplication() throws IOException {
        dictionaryBasic();
    }

    public void runApplication() {
        renderSearchSide();
        renderTranslateSide();
        renderMenuBar();
        setUpMainFrame();
    }

    /**
     * app có chia ra làm 2 phía, phần search và phần translate
     *
     */
    private void setUpMainFrame() {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        mainFrame.setLayout(new FlowLayout());
        mainFrame.setBackground(Color.LIGHT_GRAY);

        subPanel.setLayout(null);
        subPanel.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        subPanel.setBackground(Color.LIGHT_GRAY);
        mainFrame.add(subPanel);
        mainFrame.pack();
    }

    /**
     * render phần search
     */
    private void renderSearchSide() {
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBounds(0, 25, SEARCH_SIDE_WIDTH, SEARCH_SIDE_HEIGHT);
        searchPanel.setLayout(new FlowLayout());
        searchPanel.setBorder(BorderFactory.createLineBorder(new Color(127, 138, 148)));

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

        setButton(searchButton, 80, 30);
        searchButton.setBounds(0, searchField.getY() + searchField.getHeight(), 80, 30);
        searchButton.addActionListener(e -> search());

        suggestionScroll.setPreferredSize(new Dimension(SEARCH_SIDE_WIDTH, 500));
        suggestionScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        for (int i = 0; i < MAX_BUTTON_NUMS; i++) {
            buttons[i] = new JButton();
            buttons[i].setVisible(false);
            buttons[i].setFocusable(false);
            buttons[i].setPreferredSize(new Dimension(searchPanel.getWidth(), 50));
            buttons[i].setBackground(Color.WHITE);
            buttons[i].setBorder(BorderFactory.createEtchedBorder());
            suggestionPanel.add(buttons[i]);
        }
        suggestionPanel.setPreferredSize(new Dimension(suggestionScroll.getWidth(), 1000));
        suggestionScroll.setViewportView(suggestionPanel);

        setButton(spellButton, 80, 30);
        spellButton.setBounds(0, searchField.getY() + searchField.getHeight(), 80, 30);
        spellButton.addActionListener(e -> speech(searchField.getText()));

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(spellButton);
        searchPanel.add(suggestionScroll);
        subPanel.add(searchPanel);
    }

    /**
     * render phần translate.
     */
    private void renderTranslateSide() {
        translatePanel.setBackground(Color.WHITE);
        translatePanel.setBounds(355, 25, 650, 600);
        translatePanel.setBorder(BorderFactory.createLineBorder(new Color(148, 143, 127)));

        translateArea.setFont(new Font("Arial", Font.PLAIN, 30));
        translateArea.setEditable(false);
        translateArea.setContentType("text/html");

        translateScroll.setPreferredSize(new Dimension(650, 570));
        translateScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        translatePanel.add(translateScroll);
        subPanel.add(translatePanel);
    }

    private void renderMenuBar() {
        addMenu.addActionListener(this);
        editMenu.addActionListener(this);
        eraseMenu.addActionListener(this);
        GGTranslateMenu.addActionListener(this);

        featuresMenu.add(addMenu);
        featuresMenu.add(editMenu);
        featuresMenu.add(eraseMenu);
        featuresMenu.add(GGTranslateMenu);

        menuBar.add(featuresMenu);

        mainFrame.setJMenuBar(menuBar);
    }

    /**
     * Mở 1 cửa sổ nhỏ để thê từ
     * @throws IOException
     */
    @Override
    public void add() throws IOException {
        final int frameWidth = 300;
        final int frameHeight = 300;

        JFrame subFrame = new JFrame("Add");

        JPanel subPanel = new JPanel(new GridBagLayout());
        JPanel newWordPanel = new JPanel(new GridLayout(2, 0));
        JPanel definitionPanel = new JPanel(new GridBagLayout());

        JLabel newWordLabel = new JLabel("New Word:                                                                    ");
        JLabel definitionLabel = new JLabel("Definition                                                                      ");

        JTextField newWordField = new JTextField();
        JTextArea definitionField = new JTextArea();

        JButton addButton = new JButton("Add");
        addButton.setFocusable(false);

        subFrame.setSize(frameWidth, frameHeight);
        subFrame.setResizable(false);
        subFrame.setLocationRelativeTo(null);
        subFrame.setVisible(true);

        definitionField.setLineWrap(true);
        definitionField.setWrapStyleWord(true);

        newWordPanel.add(newWordLabel);
        newWordPanel.add(newWordField);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        changeGridBag(gbc, 0, 2, 0, 0);
        subPanel.add(addButton, gbc);

        changeGridBag(gbc, 0, 1, 1, 1);
        newWordPanel.add(newWordField, gbc);

        changeGridBag(gbc, 0, 0, 1, 0);
        subPanel.add(newWordPanel, gbc);

        changeGridBag(gbc, 0, 0, 1, 0);
        definitionPanel.add(definitionLabel, gbc);

        changeGridBag(gbc, 0, 1, 1, 1);
        definitionPanel.add(definitionField, gbc);

        changeGridBag(gbc, 0, 1, 1, 1);
        subPanel.add(definitionPanel, gbc);

        addButton.addActionListener(e-> {
            String userWord = newWordField.getText();
            int index = Trie.searchAWord(userWord);
            if (index > -1) {
                JOptionPane.showMessageDialog(null, "Từ này đã có trong hệ thống !", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else {
                Trie.addWord(userWord, words.size());
                Database.insertWord(userWord,"", definitionField.getText());
                words.add(new Word(userWord, "<ul><li>" + definitionField.getText() + "</li></ul>"));
                JOptionPane.showMessageDialog(null, "Thêm từ thành công !", "Thông báo", JOptionPane.PLAIN_MESSAGE);
            }
        });

        newWordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    definitionField.requestFocus();
                }
            }
        });

        subFrame.add(subPanel);
    }

    /**
     * Mở 1 cửa sổ nhỏ để sửa từ
     * @throws IOException
     */
    @Override
    public void edit() throws IOException {
        final int frameWidth = 300;
        final int frameHeight = 300;

        JFrame subFrame = new JFrame("Edit");
        //subFrame.setLayout();

        JPanel subPanel = new JPanel(new GridBagLayout());
        JPanel editedWordPanel = new JPanel(new GridLayout(2, 0));
        JPanel newWordPanel = new JPanel(new GridLayout(2, 0));

        JPanel definitionPanel = new JPanel(new GridBagLayout());

        JLabel editedWordLabel = new JLabel("Edited Word :                                                                      ");
        JLabel newWordLabel = new JLabel("English :                                                                    ");
        JLabel definitionLabel = new JLabel("Vietnamese :                                                                      ");

        JTextField editedField = new JTextField();
        JTextField newWordField = new JTextField();
        JTextArea definitionField = new JTextArea();

        subFrame.setSize(frameWidth, frameHeight);
        subFrame.setResizable(false);
        subFrame.setLocationRelativeTo(null);
        subFrame.setVisible(true);

        definitionField.setLineWrap(true);
        definitionField.setWrapStyleWord(true);

        JButton editedButton = new JButton("Add");
        editedButton.setFocusable(false);

        editedWordPanel.add(editedWordLabel);
        editedWordPanel.add(editedField);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        changeGridBag(gbc, 0, 3, 0, 0);
        subPanel.add(editedButton, gbc);

        changeGridBag(gbc, 0, 1, 1, 1);
        editedWordPanel.add(editedField, gbc);

        changeGridBag(gbc, 0, 0, 1, 0);
        subPanel.add(editedWordPanel, gbc);

        newWordPanel.add(newWordLabel);
        newWordPanel.add(newWordField);

        changeGridBag(gbc, 0, 1, 1, 0);
        subPanel.add(newWordPanel, gbc);

        changeGridBag(gbc, 0, 0, 1, 0);
        definitionPanel.add(definitionLabel, gbc);

        changeGridBag(gbc, 0, 1, 1, 1);
        definitionPanel.add(definitionField, gbc);

        changeGridBag(gbc, 0, 2, 1, 1);
        subPanel.add(definitionPanel, gbc);

        editedButton.addActionListener(e -> {
            String wordInList = editedField.getText();
            int index = Trie.searchAWord(wordInList);
            if (index == -1) {
                JOptionPane.showMessageDialog(null, "Từ này không có trong hệ thống !", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else if (Trie.searchAWord(newWordField.getText()) > -1) {
                JOptionPane.showMessageDialog(null, "Từ muốn đổi đã có trong hệ thống !", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else {
                Database.deleteWord(wordInList);
                Database.insertWord(newWordField.getText(), "", definitionField.getText());
                Trie.deleteAWord(wordInList);
                Trie.addWord(newWordField.getText(), index);
                Word newWord = new Word(newWordField.getText(), "<ul><li>" + definitionField.getText() + "</li></ul>"); //transform text to Word Object
                words.set(index, newWord);
                JOptionPane.showMessageDialog(null, "Thao tác sửa thành công !", "Thông báo", JOptionPane.PLAIN_MESSAGE);
            }
        });

        editedField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    newWordField.requestFocus();
                }
            }
        });

        newWordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    definitionField.requestFocus();
                }
            }
        });

        subFrame.add(subPanel);
    }

    /**
     * Mở 1 cửa sổ nhỏ để xóa từ
     * @throws IOException
     */
    @Override
    public void erase() throws IOException {
        final int frameWidth = 300;
        final int frameHeight = 300;

        JFrame subFrame = new JFrame("Erase");
        JPanel subPanel = new JPanel(new FlowLayout());

        JPanel erasePanel = new JPanel(new GridLayout(2, 0));
        JLabel eraseLabel = new JLabel("Word to erase:                                                                  ");
        JTextArea eraseField = new JTextArea();
        eraseField.setBorder(BorderFactory.createLineBorder(new Color(127, 138, 148)));

        JButton eraseButton = new JButton("Erase");
        eraseButton.setFocusable(false);
        eraseButton.addActionListener(e -> {
            String userWord = eraseField.getText();
            int index = Trie.searchAWord(userWord);
            if (index == -1) {
                JOptionPane.showMessageDialog(null, "Từ này không có trong hệ thống !", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else {
                    Database.deleteWord(userWord);
                    Trie.deleteAWord(userWord);
                    words.remove(index);
                JOptionPane.showMessageDialog(null, "Thao tác xóa thành công !", "Thông báo", JOptionPane.PLAIN_MESSAGE);
            }
        });

        subFrame.setSize(frameWidth, frameHeight);
        subFrame.setResizable(false);
        subFrame.setLocationRelativeTo(null);
        subFrame.setVisible(true);

        erasePanel.add(eraseLabel);
        erasePanel.add(eraseField);

        subPanel.add(erasePanel);
        subPanel.add(eraseButton);

        subFrame.add(subPanel);
    }

    private void GGTranslateMenu() {
        final int frameWidth = 670;
        final int frameHeight = 670;
        JFrame subFrame = new JFrame("GG Translate");

        subFrame.setSize(frameWidth, frameHeight);
        subFrame.setResizable(false);
        subFrame.setLocationRelativeTo(null);
        subFrame.setVisible(true);

        JPanel subPanel = new JPanel(new GridLayout());
        JPanel searchPanel = new JPanel(new FlowLayout());
        JPanel translatePanel = new JPanel(new FlowLayout());
        searchPanel.setBorder(BorderFactory.createEtchedBorder());

        final JTextArea searchArea = new JTextArea();
        searchArea.setFont(new Font("Arial", Font.PLAIN, 20));
        searchArea.setEditable(true);
        searchArea.setLineWrap(true);
        searchArea.setWrapStyleWord(true);

        final JScrollPane searchScroll = new JScrollPane(searchArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        searchScroll.setPreferredSize(new Dimension(300, 550));
        searchScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        final JButton searchButton = new JButton("Search Text");
        setButton(searchButton, 300, 50);
        searchButton.setHorizontalTextPosition(JButton.CENTER);
        searchButton.setVerticalTextPosition(JButton.CENTER);

        final JTextArea translateArea = new JTextArea();
        translateArea.setFont(new Font("Arial", Font.PLAIN, 20));
        translateArea.setEditable(false);
        translateArea.setLineWrap(true);
        translateArea.setWrapStyleWord(true);

        final JScrollPane translateScroll = new JScrollPane(translateArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        translateScroll.setPreferredSize(new Dimension(300, 550));
        translateScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        final JButton spellButton = new JButton("Spell");
        setButton(spellButton, 300, 50);
        spellButton.setHorizontalTextPosition(JButton.CENTER);
        spellButton.setVerticalTextPosition(JButton.CENTER);
        spellButton.addActionListener(e -> speech(searchArea.getText()));

        searchButton.addActionListener(e -> {
            try {
                translateArea.setText(translate("en", "vi", searchArea.getText()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        searchPanel.add(searchScroll);
        searchPanel.add(searchButton);

        translatePanel.add(translateScroll);
        translatePanel.add(spellButton);

        subPanel.add(searchPanel);
        subPanel.add(translatePanel);

        subFrame.add(subPanel);
    }

    private void searchByPreFix() {
        String userWord = searchField.getText().trim();
        List<Integer> tmp = Trie.searchPrefixOfWord(userWord.toLowerCase(), MAX_BUTTON_NUMS);
        ArrayList<Word> tempWords = new ArrayList<Word>();
        for (Integer it : tmp) {
            tempWords.add(Dictionary.getWordAt(it));
        }
        if (tempWords.size() == 0) {
            translateArea.setText("Không tìm thấy");
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
                    translateArea.setText("<html> <h1 color=purple>" + word.word_target + "</h1>"  + "<u>Phát âm</u>: " + word.word_pronounce + word.word_explain + "</html>");
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

    private void search() {
        String userWord = searchField.getText().trim();
        Word word = Dictionary.getWordAt(Trie.searchAWord(userWord.toLowerCase()));
        if (word == null) {
            translateArea.setText("Không tìm thấy");
            for (int i = 0; i < MAX_BUTTON_NUMS; i++) {
                buttons[i].setEnabled(false);
                buttons[i].setVisible(false);
            }
        } else {
            translateArea.setText("<html> <h1 color=purple>" + word.word_target + "</h1>"  + "<u>Phát âm</u>: " + word.word_pronounce + word.word_explain + "</html>");
        }
    }

    private void speech(String speechText) {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        VoiceManager voiceManager = VoiceManager.getInstance();
        com.sun.speech.freetts.Voice syntheticVoice = voiceManager.getVoice("kevin16");
        syntheticVoice.allocate();
        syntheticVoice.speak(speechText);
        syntheticVoice.deallocate();
    }

    private static String translate(String langFrom, String langTo, String text) throws IOException {
        // INSERT YOU URL HERE
        String urlStr = "https://script.google.com/macros/s/AKfycbxLvaPEKedcmyfKsKNn6fXKNri8nWowekZF8uNAWKY0lM7JJL9E-BGj9T31lxrn0cRGfQ/exec" +
                "?q=" + URLEncoder.encode(text, "UTF-8") +
                "&target=" + langTo +
                "&source=" + langFrom;
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addMenu) {
            try {
                add();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (e.getSource() == editMenu) {
            try {
                edit();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if(e.getSource() == eraseMenu) {
            try {
                erase();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (e.getSource() == GGTranslateMenu) {
            GGTranslateMenu();
        }
    }

    void changeGridBag(GridBagConstraints c, int gx, int gy, int wx, int wy) {
        c.gridx = gx;
        c.gridy = gy;
        c.weightx = wx;
        c.weighty = wy;
    }

    public static final String html2text(String html) {
        EditorKit kit = new HTMLEditorKit();
        Document doc = kit.createDefaultDocument();
        doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
        try {
            Reader reader = new StringReader(html);
            kit.read(reader, doc, 0);
            return doc.getText(0, doc.getLength());
        } catch (Exception e) {
            return "";
        }
    }

    private void setFrame(JFrame frame, int width, int height) {
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    private void setButton(JButton button, int width, int height) {
        button.setPreferredSize(new Dimension(width, height));
        button.setFocusable(false);
    }
}
