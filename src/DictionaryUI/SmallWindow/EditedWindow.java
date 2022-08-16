package DictionaryUI.SmallWindow;

import DictionaryConsole.Dictionary;
import DictionaryConsole.Word;
import DictionaryUI.DictionaryApplication;
import Database.Database;
import Trie.Trie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EditedWindow extends JFrame implements ActionListener {
    private final int frameWidth = 300;
    private final int frameHeight = 300;
    private final JPanel mainPanel = new JPanel(new GridBagLayout());
    private final JPanel editedWordPanel = new JPanel(new GridLayout(2, 0));
    private final JPanel definitionPanel = new JPanel(new GridBagLayout());
    private final JLabel editedWordLabel = new JLabel("Edited DictionaryConsole.Word :                                                                    ");
    private final JLabel definitionLabel = new JLabel("Vietnamese :                                                                    ");
    private final JTextField editedField = new JTextField();
    private final JTextArea definitionField = new JTextArea();
    private final JButton editedButton = new JButton("Add");
    private GridBagConstraints gbc = new GridBagConstraints();

    public EditedWindow() {
        gbc.fill = GridBagConstraints.BOTH;
    }

    /**
     * render edited window.
     */
    public void renderWindow() {
        //set up for main frame
        this.setTitle("Edit");
        DictionaryApplication.setFrame(this, frameWidth, frameHeight);

        //set up for edited site
        editedButton.setFocusable(false);
        editedButton.addActionListener(this);
        editedWordPanel.add(editedWordLabel);
        editedWordPanel.add(editedField);
        DictionaryApplication.changeGridBag(gbc, 0, 3, 0, 0);
        mainPanel.add(editedButton, gbc);
        DictionaryApplication.changeGridBag(gbc, 0, 1, 1, 1);
        editedWordPanel.add(editedField, gbc);
        DictionaryApplication.changeGridBag(gbc, 0, 0, 1, 0);
        mainPanel.add(editedWordPanel, gbc);

        //set up for definition site
        DictionaryApplication.setField(definitionField);
        DictionaryApplication.changeGridBag(gbc, 0, 0, 1, 0);
        definitionPanel.add(definitionLabel, gbc);
        DictionaryApplication.changeGridBag(gbc, 0, 1, 1, 1);
        definitionPanel.add(definitionField, gbc);
        DictionaryApplication.changeGridBag(gbc, 0, 2, 1, 1);
        mainPanel.add(definitionPanel, gbc);

        editedField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    definitionField.requestFocus();
                }
            }
        });

        this.add(mainPanel);
    }

    /**
     * do edit action when edited button is clicked.
     * @param e action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == editedButton) {
            String wordInList = editedField.getText();
            int index = Trie.searchAWord(wordInList);
            if (index == -1) {
                JOptionPane.showMessageDialog(null, "Từ này không có trong hệ thống !", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }  else {
                Database.updateWord(wordInList, definitionField.getText());
                Dictionary.words.get(index).word_explain = definitionField.getText();
                JOptionPane.showMessageDialog(null, "Thao tác sửa thành công !", "Thông báo", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }
}
