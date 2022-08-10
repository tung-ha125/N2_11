import com.sun.speech.freetts.VoiceManager;
import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


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
        sortDictionary();
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
        mainFrame.setLayout(new FlowLayout());
        mainFrame.setVisible(true);
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

        searchButton.setBackground(Color.CYAN);
        searchButton.setPreferredSize(new Dimension(80, 30));
        searchButton.setBounds(0, searchField.getY() + searchField.getHeight(), 80, 30);
        searchButton.setFocusable(false);

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

        spellButton.setBackground(Color.CYAN);
        spellButton.setPreferredSize(new Dimension(80, 30));
        spellButton.setBounds(0, searchField.getY() + searchField.getHeight(), 80, 30);
        spellButton.setFocusable(false);
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
        //translatePanel.setPreferredSize(new Dimension(350, 600));
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
        //subFrame.setLayout();

        JPanel subPanel = new JPanel(new GridBagLayout());
        JPanel newWordPanel = new JPanel(new GridLayout(2, 0));
        JPanel definitionPanel = new JPanel(new GridBagLayout());

        JLabel newWordLabel = new JLabel("New Word:                                                                    ");
        JLabel definitionLabel = new JLabel("Definition                                                                      ");

        JTextField newWordField = new JTextField();
        JTextArea definitionField = new JTextArea();

        JButton addButton = new JButton("Add");

        subFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        subFrame.setSize(frameWidth, frameHeight);
        subFrame.setVisible(true);
        subFrame.setLocationRelativeTo(null);

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
            if (findWordInDictionary(newWordField.getText()) != null) {
                JOptionPane.showMessageDialog(null, "Từ này đã có trong hệ thống !", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else {
                String path = "src/dictionaries.txt";
                words.add(new Word(newWordField.getText(), "<ul><li>" + definitionField.getText() + "</li></ul>"));
                ArrayList<String> S = null;
                try {
                    S = readSmallTextFile(path);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                S.add(newWordField.getText() + '\t' + definitionField.getText());
                try {
                    writeSmallTextFile(S, path);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(null, "Thêm từ thành công !", "Thông báo", JOptionPane.PLAIN_MESSAGE);
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

        subFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        subFrame.setSize(frameWidth, frameHeight);
        subFrame.setVisible(true);
        subFrame.setLocationRelativeTo(null);

        definitionField.setLineWrap(true);
        definitionField.setWrapStyleWord(true);

        JButton editedButton = new JButton("Add");

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
            Word wordInList = findWordInDictionary(editedField.getText());
            if (wordInList == null) {
                JOptionPane.showMessageDialog(null, "Từ này không có trong hệ thống !", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else {
                String path = "src/dictionaries.txt";
                Word newWord = new Word(newWordField.getText(), "<ul><li>" + definitionField.getText() + "</li></ul>"); //transform text to Word Object
                int x = editWordInFile(path, wordInList, newWord); //update word in file
                changeWordInCurrentArrayList(x, newWord); //update word in ArrayList
                JOptionPane.showMessageDialog(null, "Thao tác sửa thành công !", "Thông báo", JOptionPane.PLAIN_MESSAGE);
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
        eraseButton.addActionListener(e -> {
            Word userWord = findWordInDictionary(eraseField.getText());
            if (userWord == null) {
                JOptionPane.showMessageDialog(null, "Từ này không có trong hệ thống !", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else {
                String path = "src/dictionaries.txt";
                try {
                    eraseWordInFile(path, userWord);
                    eraseWordInCurrentArrayList(userWord);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(null, "Thao tác xóa thành công !", "Thông báo", JOptionPane.PLAIN_MESSAGE);
            }
        });

        subFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        subFrame.setSize(frameWidth, frameHeight);
        subFrame.setVisible(true);
        subFrame.setLocationRelativeTo(null);

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

        subFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        subFrame.setSize(frameWidth, frameHeight);
        subFrame.setVisible(true);
        subFrame.setLocationRelativeTo(null);

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
        searchButton.setPreferredSize(new Dimension(300, 50));
        searchButton.setFocusable(false);
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
        spellButton.setPreferredSize(new Dimension(300, 50));
        spellButton.setFocusable(false);
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

    private void search() {
        String userWord = searchField.getText();
        ArrayList<Word> tempWords = dictionarySearcher(userWord);
        if (tempWords.size() == 0) {
            translateArea.setText("Không tìm thấy");
            for (int i = 0; i < MAX_BUTTON_NUMS; i++) {
                buttons[i].setEnabled(false);
                buttons[i].setVisible(false);
            }
        } else {
            System.out.println(tempWords.size());
            for (int i = 0; i < tempWords.size(); i++) {
                buttons[i].setText(tempWords.get(i).word_target);
                int finalI1 = i;
                ActionListener[] listeners = buttons[i].getActionListeners();
                for (ActionListener l : listeners) {
                    buttons[i].removeActionListener(l);
                }
                buttons[i].addActionListener(e->{
                    Word word = tempWords.get(finalI1);
                    translateArea.setText("<html> <h1>" + word.word_target + "  " + word.word_pronounce + "</h1>" + word.word_explain + "</html>");
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

    public void eraseWordInFile(String path, Word erasedWord) throws IOException {
        ArrayList<String> S = null;
        S = readSmallTextFile(path);
        int x = S.indexOf(erasedWord.word_target + '\t' + erasedWord.word_explain);
        S.remove(x);
        writeSmallTextFile(S, path);
    }

    public void eraseWordInCurrentArrayList(Word erasedWord) throws IOException {
        int index = words.indexOf(erasedWord);
        words.remove(index);
    }

    public int editWordInFile(String path, Word wordInList, Word newWord) {
        ArrayList<String> S = null;
        try {
            S = readSmallTextFile(path);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        int x = S.indexOf(wordInList.word_target + '\t' + wordInList.word_explain);
        S.set(x, S.get(x).replace(wordInList.word_explain, newWord.word_explain));
        S.set(x, S.get(x).replace(wordInList.word_target, newWord.word_target));
        try {
            writeSmallTextFile(S, path);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return x;
    }

    public void changeWordInCurrentArrayList(int index, Word newWord) {
        words.get(index).word_target = newWord.word_target;
        words.get(index).word_explain = newWord.word_explain;
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
}
