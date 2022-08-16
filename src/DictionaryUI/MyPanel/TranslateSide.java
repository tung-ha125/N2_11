package DictionaryUI.MyPanel;

import javax.swing.*;
import java.awt.*;

public class TranslateSide extends JPanel {
    public static final JTextPane translateArea = new JTextPane();
    private final JScrollPane translateScroll = new JScrollPane(translateArea,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

    public TranslateSide() {

    }

    /**
     * renders translate panel
     */
    public void renderTranslateSide() {
        //set up for translate panel
        this.setBackground(Color.WHITE);
        this.setBounds(355, 25, 650, 600);
        this.setBorder(BorderFactory.createLineBorder(new Color(148, 143, 127)));

        //set up for translate area
        translateArea.setFont(new Font("Arial", Font.PLAIN, 30));
        translateArea.setEditable(false);
        translateArea.setContentType("text/html");
        translateScroll.setPreferredSize(new Dimension(650, 570));
        translateScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        //add to translate panel
        this.add(translateScroll);
    }
}
