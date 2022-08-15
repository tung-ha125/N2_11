import Trie.Trie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AddWindow extends JFrame implements ActionListener {
    private final int frameWidth = 300;
    private final int frameHeight = 300;

    private final JPanel mainPanel = new JPanel(new GridBagLayout());
    private final JPanel newWordPanel = new JPanel(new GridLayout(2, 0));
    private final JPanel definitionPanel = new JPanel(new GridBagLayout());
    private final JLabel newWordLabel = new JLabel("New Word:                                                                    ");
    private final JLabel definitionLabel = new JLabel("Definition                                                                      ");

    private final JTextField newWordField = new JTextField();
    private final JTextArea definitionField = new JTextArea();
    private final JButton addButton = new JButton("Add");
    private GridBagConstraints gbc = new GridBagConstraints();

    public AddWindow() {
        gbc.fill = GridBagConstraints.BOTH;
    }

    public void renderWindow() {
        this.setTitle("Add");
        DictionaryApplication.setFrame(this, frameWidth, frameHeight);

        addButton.setFocusable(false);

        //Add opponents to newWordPanel
        newWordPanel.add(newWordLabel);
        newWordPanel.add(newWordField);

        //Add button first
        DictionaryApplication.changeGridBag(gbc, 0, 2, 0, 0);
        mainPanel.add(addButton, gbc);
        addButton.addActionListener(this);

        //Add new word
        DictionaryApplication.changeGridBag(gbc, 0, 1, 1, 1);
        newWordPanel.add(newWordField, gbc);
        DictionaryApplication.changeGridBag(gbc, 0, 0, 1, 0);
        mainPanel.add(newWordPanel, gbc);
        newWordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    definitionField.requestFocus();
                }
            }
        });

        //Add definition
        DictionaryApplication.setField(definitionField);
        DictionaryApplication.changeGridBag(gbc, 0, 0, 1, 0);
        definitionPanel.add(definitionLabel, gbc);
        DictionaryApplication.changeGridBag(gbc, 0, 1, 1, 1);
        definitionPanel.add(definitionField, gbc);
        DictionaryApplication.changeGridBag(gbc, 0, 1, 1, 1);
        mainPanel.add(definitionPanel, gbc);

        //Add panel to frame
        this.add(mainPanel);
    }

    /**
     * action while clicks add button
     * @param e action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            String userWord = newWordField.getText();
            int index = Trie.searchAWord(userWord);
            if (index > -1) {
                JOptionPane.showMessageDialog(null, "Từ này đã có trong hệ thống !", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else {
                Trie.addWord(userWord, Dictionary.words.size());
                Database.insertWord(userWord,"", definitionField.getText());
                Dictionary.words.add(new Word(userWord, "<ul><li>" + definitionField.getText() + "</li></ul>"));
                JOptionPane.showMessageDialog(null, "Thêm từ thành công !", "Thông báo", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }
}
