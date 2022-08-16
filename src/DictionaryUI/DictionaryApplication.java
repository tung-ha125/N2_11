package DictionaryUI;

import DictionaryConsole.DictionaryCommandline;
import DictionaryUI.MyPanel.SearchSide;
import DictionaryUI.MyPanel.TranslateSide;
import com.sun.speech.freetts.VoiceManager;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;



public class DictionaryApplication extends DictionaryCommandline {
    private final JFrame mainFrame = new JFrame("DictionaryConsole.Dictionary");
    private final JPanel subPanel = new JPanel(new GridBagLayout());
    private final SearchSide searchPanel = new SearchSide();
    private final TranslateSide translatePanel = new TranslateSide();
    private final Menu menuBar = new Menu();
    private int FRAME_WIDTH = 1000;
    private int FRAME_HEIGHT = 650;

    /**
     * constructor.
     * @throws IOException throws exception
     */
    public DictionaryApplication() throws IOException {
        dictionaryBasic();
    }

    /**
     * run application.
     */
    public void runApplication() {
        renderSearchSide();
        renderTranslateSide();
        renderMenuBar();
        setUpMainFrame();
    }

    /**
     * app có chia ra làm 2 phía, phần search và phần translate
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
     * render search side
     */
    private void renderSearchSide() {
        searchPanel.renderSearchSide();
        subPanel.add(searchPanel);
    }

    /**
     * render translate side
     */
    private void renderTranslateSide() {
        translatePanel.renderTranslateSide();
        subPanel.add(translatePanel);
    }

    /**
     * render menu bar
     */
    private void renderMenuBar() {
        mainFrame.setJMenuBar(menuBar);
    }

    /**
     * speech text.
     * @param speechText text to speech
     */
    public static void speech(String speechText) {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        VoiceManager voiceManager = VoiceManager.getInstance();
        com.sun.speech.freetts.Voice syntheticVoice = voiceManager.getVoice("kevin16");
        syntheticVoice.allocate();
        syntheticVoice.speak(speechText);
        syntheticVoice.deallocate();
    }

    /**
     * translate a text use API.
     * @param langFrom language type
     * @param langTo language type translate to
     * @param text text
     * @return
     * @throws IOException
     */
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

    /**
     * change the columns and rows the opponent has.
     * @param c GridBagConstraints
     * @param gx x position
     * @param gy y position
     * @param wx horizontal length of the opponent
     * @param wy vertical length of the opponent
     */
    public static void changeGridBag(GridBagConstraints c, int gx, int gy, int wx, int wy) {
        c.gridx = gx;
        c.gridy = gy;
        c.weightx = wx;
        c.weighty = wy;
    }

    /**
     * set up some data for frame.
     * @param frame the frame to set up
     * @param width frame width
     * @param height frame height
     */
    public static void setFrame(JFrame frame, int width, int height) {
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * set up JTextArea
     * @param tx JTextArea
     */
    public static void setField(JTextArea tx) {
        tx.setLineWrap(true);
        tx.setWrapStyleWord(true);
    }

    /**
     * set up button.
     * @param button button to set up
     * @param width button width
     * @param height button height
     */
    public static void setButton(JButton button, int width, int height) {
        button.setPreferredSize(new Dimension(width, height));
        button.setFocusable(false);
    }
}
