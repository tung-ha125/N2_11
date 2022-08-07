import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;


public class DictionaryApplication extends DictionaryCommandline implements ActionListener {

    private final JFrame mainFrame = new JFrame("Dictionary");

    private final JPanel subPanel = new JPanel(new GridBagLayout());
    private final JPanel searchPanel = new JPanel();
    private final JPanel translatePanel = new JPanel();

    private final JMenuBar menuBar = new JMenuBar();

    private final JMenu featuresMenu = new JMenu("Features");
    private final JMenuItem addMenu = new JMenuItem("Add");
    private final JMenuItem editMenu = new JMenuItem("Edit");
    private final JMenuItem eraseMenu = new JMenuItem("Erase");

    private final JButton searchButton = new JButton("Search");

    private JTextField searchField = new JTextField();
    private JTextPane translateArea = new JTextPane();

    private JScrollPane translateScroll = new JScrollPane(translateArea);


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
        mainFrame.setSize(705, 650);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(new FlowLayout());
        mainFrame.setVisible(true);
        mainFrame.setBackground(Color.LIGHT_GRAY);

        subPanel.setLayout(null);
        subPanel.setPreferredSize(new Dimension(705, 650));
        subPanel.setBackground(Color.LIGHT_GRAY);
        mainFrame.add(subPanel);
        mainFrame.pack();
    }

    /**
     * render phần search
     */
    private void renderSearchSide() {
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBounds(0, 25, 350, 600);
        searchPanel.setLayout(new FlowLayout());
        searchPanel.setBorder(BorderFactory.createLineBorder(new Color(127, 138, 148)));

        searchField.setPreferredSize(new Dimension(350, 50));
        searchField.setBounds(0, 0, 350, 50);
        searchField.setBorder(BorderFactory.createLineBorder(new Color(127, 138, 148)));

        searchButton.setBackground(Color.CYAN);
        searchButton.setPreferredSize(new Dimension(80, 30));
        searchButton.setBounds(0, searchField.getY() + searchField.getHeight(), 80, 30);
        searchButton.setFocusable(false);

        searchButton.addActionListener(e -> search());

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        subPanel.add(searchPanel);
    }

    /**
     * render phần translate.
     */
    private void renderTranslateSide() {
        translatePanel.setBackground(Color.WHITE);
        translatePanel.setPreferredSize(new Dimension(350, 600));
        translatePanel.setBounds(355, 25, 350, 600);
        translatePanel.setBorder(BorderFactory.createLineBorder(new Color(148, 143, 127)));

//        translateArea.setLineWrap(true);
//        translateArea.setWrapStyleWord(true);
        translateArea.setFont(new Font("Arial", Font.PLAIN, 30));
        translateArea.setEditable(false);
        translateArea.setContentType("text/html");

        translateScroll.setPreferredSize(new Dimension(350, 500));
        translateScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        translatePanel.add(translateScroll);
        subPanel.add(translatePanel);
    }

    private void renderMenuBar() {
        addMenu.addActionListener(this);
        editMenu.addActionListener(this);
        eraseMenu.addActionListener(this);

        featuresMenu.add(addMenu);
        featuresMenu.add(editMenu);
        featuresMenu.add(eraseMenu);

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
                words.add(new Word(newWordField.getText(), definitionField.getText()));
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
                Word newWord = new Word(newWordField.getText(), definitionField.getText()); //transform text to Word Object
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

    private void search() {
        String userWord = searchField.getText();
        Word word = findWordInDictionary(userWord);
        if (word == null) {
            translateArea.setText("Không tìm thấy");
        } else {
            translateArea.setText("<html> <h1>" + word.word_target + "</h1>" + word.word_explain + "</html>");
        }
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
