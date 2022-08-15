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


public class DictionaryApplication extends DictionaryCommandline {

    private final JFrame mainFrame = new JFrame("Dictionary");

    private final JPanel subPanel = new JPanel(new GridBagLayout());
    private final JPanel searchPanel = new JPanel(new GridLayout());
    private final JPanel translatePanel = new JPanel();

    private final JButton searchButton = new JButton("Search");

    private final JButton spellButton = new JButton("Spell");

    private final JTextField searchField = new JTextField();
    private final JTextPane translateArea = new JTextPane();

    private final JScrollPane translateScroll = new JScrollPane(translateArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    JScrollPane suggestionScroll = new JScrollPane();
    private final JPanel suggestionPanel = new JPanel(new FlowLayout());
    private final int MAX_BUTTON_NUMS = 20;
    private final JButton[] buttons = new JButton[MAX_BUTTON_NUMS];
    private final Menu menuBar = new Menu();

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
        mainFrame.setJMenuBar(menuBar);
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

    public static void speech(String speechText) {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        VoiceManager voiceManager = VoiceManager.getInstance();
        com.sun.speech.freetts.Voice syntheticVoice = voiceManager.getVoice("kevin16");
        syntheticVoice.allocate();
        syntheticVoice.speak(speechText);
        syntheticVoice.deallocate();
    }

    public static String translate(String langFrom, String langTo, String text) throws IOException {
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
    

    public static void changeGridBag(GridBagConstraints c, int gx, int gy, int wx, int wy) {
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

    public static void setFrame(JFrame frame, int width, int height) {
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void setField(JTextArea tx) {
        tx.setLineWrap(true);
        tx.setWrapStyleWord(true);
    }

    public static void setButton(JButton button, int width, int height) {
        button.setPreferredSize(new Dimension(width, height));
        button.setFocusable(false);
    }
}
