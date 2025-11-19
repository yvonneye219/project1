import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

// This is the main class for the HTML Reader GUI
public class HTMLreader implements ActionListener {
    // Main window and UI components
    private JFrame mainFrame;
    private JLabel statusLabel;
    private JPanel controlPanel;
    private JMenuBar mb;
    private JMenu file, edit, help;
    private JMenuItem cut, copy, paste, selectAll;

    // Here's text area to show links, and text fields for URL + search terms
    private JTextArea TA;
    private JTextField urlField;
    private JTextField searchField;

    private int WIDTH = 800;
    private int HEIGHT = 700;

    // Here's constructor that set up the GUI
    public HTMLreader() {
        prepareGUI();
    }

    // This is the program entry point
    public static void main(String[] args) {
        new HTMLreader();
    }

    // This is where the code builds the whole window and its components
    private void prepareGUI() {
        mainFrame = new JFrame("HTML Reader");
        mainFrame.setSize(WIDTH, HEIGHT);

        // Use BorderLayout: NORTH (top), CENTER, SOUTH (bottom)
        mainFrame.setLayout(new BorderLayout(8, 8));
        //This code builds the menu bar and adds Cut/Copy/Paste/Select All to the “Edit” menu, and makes the window respond when those items are clicked.
        cut = new JMenuItem("cut");
        copy = new JMenuItem("copy");
        paste = new JMenuItem("paste");
        selectAll = new JMenuItem("selectAll");
        cut.addActionListener(this);
        copy.addActionListener(this);
        paste.addActionListener(this);
        selectAll.addActionListener(this);

        mb = new JMenuBar();
        file = new JMenu("File");
        edit = new JMenu("Edit");
        help = new JMenu("Help");
        edit.add(cut);
        edit.add(copy);
        edit.add(paste);
        edit.add(selectAll);
        mb.add(file);
        mb.add(edit);
        mb.add(help);
        mainFrame.setJMenuBar(mb);

        //Top panel: URL field, search field, Go button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));

        // URL input with a default value
        urlField = new JTextField("https://www.milton.edu/", 35);
        urlField.setBorder(BorderFactory.createTitledBorder("URL"));
        topPanel.add(urlField);

        // Search field allows user type keywords to filter links
        searchField = new JTextField(18);
        searchField.setBorder(BorderFactory.createTitledBorder("Search term"));
        topPanel.add(searchField);

        // This part triggers the link fetch when clicked
        JButton goButton = new JButton("Go");
        goButton.setActionCommand("Go");
        goButton.addActionListener(new ButtonClickListener());
        topPanel.add(goButton);

        mainFrame.add(topPanel, BorderLayout.NORTH);
        //Center part in the panel is the text area showing the extracted links
        TA = new JTextArea();
        TA.setEditable(false); // user can't edit the links directly
        TA.setLineWrap(false);  // no wrapping, each link on its own line
        TA.setBorder(BorderFactory.createTitledBorder("Links"));
        mainFrame.add(new JScrollPane(TA), BorderLayout.CENTER);

        // Bottom part is the status bar
        controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        statusLabel = new JLabel("Ready.");
        controlPanel.add(statusLabel);
        mainFrame.add(controlPanel, BorderLayout.SOUTH);

        // When the window is closed, end the program
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        mainFrame.setVisible(true);
    }

    // Handle menu actions: Cut/Copy/Paste/Select All on the text area
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cut)
            TA.cut();
        if (e.getSource() == paste)
            TA.paste();
        if (e.getSource() == copy)
            TA.copy();
        if (e.getSource() == selectAll)
            TA.selectAll();
    }

    // Inner class that handles the "Go" button (and any other button with commands)
    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("OK")) {
                statusLabel.setText("Ok Button clicked.");
            } else if (command.equals("Submit")) {
                statusLabel.setText("Submit Button clicked.");

            } else if (command.equals("Go")) {
                statusLabel.setText("Fetching links...");
                TA.setText("");

                String urlText = urlField.getText().trim();
                String[] searchTerms = searchField.getText().trim().toLowerCase().split("\\s+");

                ArrayList<String> links = new ArrayList<>();

                try {
                    URL url = new URL(urlText);
                    URLConnection conn = url.openConnection();
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0");

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream())
                    );

                    String line;
                    while ((line = reader.readLine()) != null) {

                        int index = 0;  // where we are in this line

                        // keep looking for more "href=" in the same line
                        while ((index = line.indexOf("href=", index)) != -1) {

                            int start = index + 5;          // move past 'href='
                            if (start >= line.length()) break;

                            char quoteType = line.charAt(start);
                            start++;

                            int end;
                            if (quoteType == '"') {
                                end = line.indexOf("\"", start);
                            } else if (quoteType == '\'') {
                                end = line.indexOf("'", start);
                            } else {
                                end = line.indexOf("\"", start);
                            }

                            while (start < line.length() && line.charAt(start) == ' ')
                                start++;

                            if (end > start && end != -1) {
                                String link = line.substring(start, end).trim();

                                boolean matches = false;

// If user left search blank → match everything
                                if (searchTerms.length == 0 || (searchTerms.length == 1 && searchTerms[0].isEmpty())) {
                                    matches = true;
                                } else {
                                    // check if link contains ANY of the search terms
                                    for (String term : searchTerms) {
                                        if (link.toLowerCase().contains(term)) {
                                            matches = true;
                                            break;
                                        }
                                    }
                                }

                                if (matches) {
                                    links.add(link);
                                    TA.append(link + "\n");
                                }
                            }

                            // move index forward so we can find the next href in this line
                            if (end == -1) {
                                break;
                            } else {
                                index = end;
                            }
                        }
                    }



                    reader.close();

                    if (links.isEmpty()) {
                        String combinedTerms = String.join(" ", searchTerms);
                        TA.setText("No results found for: \"" + combinedTerms + "\"\nTry again.");
                        statusLabel.setText("No matches. Try again.");
                        return;
                    }

                    statusLabel.setText("Done. " + links.size() + " link(s) found.");

                } catch (Exception ex) {
                    statusLabel.setText("Invalid URL. Try again.");
                    TA.setText("Error: could not load the URL.\nPlease check the address and try again.");
                }
            }

        }
    }


}
