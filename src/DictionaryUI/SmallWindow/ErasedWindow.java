package DictionaryUI.SmallWindow;

import DictionaryConsole.Dictionary;
import DictionaryUI.DictionaryApplication;
import Trie.Trie;
import Database.Database;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ErasedWindow extends JFrame implements ActionListener {
    private final int frameWidth = 300;
    private final int frameHeight = 300;
    private final JPanel mainPanel = new JPanel(new FlowLayout());
    private final JPanel erasePanel = new JPanel(new GridLayout(2, 0));
    private final JLabel eraseLabel = new JLabel("DictionaryConsole.Word to erase:                                                                  ");
    private final JTextArea eraseField = new JTextArea();
    private final JButton eraseButton = new JButton("Erase");

    public ErasedWindow() {

    }

    /**
     * render erased window.
     */
    public void renderWindow() {
        this.setTitle("Erase");
        DictionaryApplication.setFrame(this, frameWidth, frameHeight);

        //set up for button
        eraseButton.setFocusable(false);
        eraseButton.addActionListener(this);

        //set up for erase side
        eraseField.setBorder(BorderFactory.createLineBorder(new Color(127, 138, 148)));
        erasePanel.add(eraseLabel);
        erasePanel.add(eraseField);

        //add erase side to main panel
        mainPanel.add(erasePanel);
        mainPanel.add(eraseButton);

        //add panel to frame
        this.add(mainPanel);
    }

    /**
     * do erased action when erased button is clicked.
     * @param e action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String userWord = eraseField.getText();
        int index = Trie.searchAWord(userWord);
        if (index == -1) {
            JOptionPane.showMessageDialog(null, "Từ này không có trong hệ thống !", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            Database.deleteWord(userWord);
            Trie.deleteAWord(userWord);
            Dictionary.words.remove(index);
            JOptionPane.showMessageDialog(null, "Thao tác xóa thành công !", "Thông báo", JOptionPane.PLAIN_MESSAGE);
        }
    }
}
