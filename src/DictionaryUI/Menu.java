package DictionaryUI;

import SmallWindow.AddWindow;
import SmallWindow.EditedWindow;
import SmallWindow.ErasedWindow;
import SmallWindow.GGTranslateWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JMenuBar implements ActionListener {
    private final JMenu featuresMenu = new JMenu("Features");
    private final JMenuItem addMenu = new JMenuItem("Add");
    private final JMenuItem editMenu = new JMenuItem("Edit");
    private final JMenuItem eraseMenu = new JMenuItem("Erase");
    private final JMenuItem GGTranslateMenu = new JMenuItem("GG Translate");
    private final AddWindow addWindow = new AddWindow();
    private final EditedWindow editedWindow = new EditedWindow();
    private final ErasedWindow erasedWindow = new ErasedWindow();
    private final GGTranslateWindow ggTranslateWindow = new GGTranslateWindow();

    public Menu() {
        //Add action listener to all items
        addMenu.addActionListener(this);
        editMenu.addActionListener(this);
        eraseMenu.addActionListener(this);
        GGTranslateMenu.addActionListener(this);

        //Add all items to menu
        featuresMenu.add(addMenu);
        featuresMenu.add(editMenu);
        featuresMenu.add(eraseMenu);
        featuresMenu.add(GGTranslateMenu);

        //Add menu to menu bar
        this.add(featuresMenu);
    }

    /**
     * open a window when one of the items in features menu is clicked.
     * @param e action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addMenu) {
            addWindow.renderWindow();
        }
        if (e.getSource() == editMenu) {
            editedWindow.renderWindow();
        }
        if(e.getSource() == eraseMenu) {
            erasedWindow.renderWindow();
        }
        if (e.getSource() == GGTranslateMenu) {
            ggTranslateWindow.renderWindow();
        }
    }
}
