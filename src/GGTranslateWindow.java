import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GGTranslateWindow extends JFrame {
    private final int frameWidth = 670;
    private final int frameHeight = 670;

    private final JPanel subPanel = new JPanel(new GridLayout());
    private final JPanel searchPanel = new JPanel(new FlowLayout());
    private final JPanel translatePanel = new JPanel(new FlowLayout());
    private final JTextArea searchArea = new JTextArea();
    private final JTextArea translateArea = new JTextArea();
    private final JScrollPane searchScroll = new JScrollPane(searchArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private final JScrollPane translateScroll = new JScrollPane(translateArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private final JButton searchButton = new JButton("Search Text");
    private final JButton spellButton = new JButton("Spell");

    public GGTranslateWindow() {

    }

    public void renderWindow() {
        this.setTitle("GG Translate");
        DictionaryApplication.setFrame(this, frameWidth, frameHeight);

        //set up for search site
        searchPanel.setBorder(BorderFactory.createEtchedBorder());
        DictionaryApplication.setField(searchArea);
        searchArea.setFont(new Font("Arial", Font.PLAIN, 20));
        searchArea.setEditable(true);
        searchScroll.setPreferredSize(new Dimension(300, 550));
        searchScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        DictionaryApplication.setButton(searchButton, 300, 50);
        searchButton.setHorizontalTextPosition(JButton.CENTER);
        searchButton.setVerticalTextPosition(JButton.CENTER);
        searchButton.addActionListener(e -> {
            try {
                translateArea.setText(DictionaryApplication.translate("en", "vi", searchArea.getText()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        searchPanel.add(searchScroll);
        searchPanel.add(searchButton);

        //set up for translate side
        DictionaryApplication.setField(translateArea);
        translateArea.setFont(new Font("Arial", Font.PLAIN, 20));
        translateArea.setEditable(false);
        translateScroll.setPreferredSize(new Dimension(300, 550));
        translateScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        DictionaryApplication.setButton(spellButton, 300, 50);
        spellButton.setHorizontalTextPosition(JButton.CENTER);
        spellButton.setVerticalTextPosition(JButton.CENTER);
        spellButton.addActionListener(e -> DictionaryApplication.speech(searchArea.getText()));
        translatePanel.add(translateScroll);
        translatePanel.add(spellButton);

        //Add search and translate side to sub panel
        subPanel.add(searchPanel);
        subPanel.add(translatePanel);

        //add sub panel to GGTranslate frame
        this.add(subPanel);
    }
}
