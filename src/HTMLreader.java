import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HTMLreader implements ActionListener {
    private JFrame mainFrame;
    private JLabel statusLabel;
    private JPanel controlPanel;
    private JMenuBar mb;
    private JMenu file, edit, help;
    private JMenuItem cut, copy, paste, selectAll;

    private JTextArea TA;
    private JTextField urlField;
    private JTextField searchField;

    private int WIDTH = 800;
    private int HEIGHT = 700;

    public HTMLreader() {
        prepareGUI();
    }

    public static void main(String[] args) {
        HTMLreader swingControlDemo = new HTMLreader();
        swingControlDemo.showEventDemo();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("HTML Reader");
        mainFrame.setSize(WIDTH, HEIGHT);

        mainFrame.setLayout(new BorderLayout(8, 8));

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

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));

        urlField = new JTextField("https://www.milton.edu/", 35);
        urlField.setBorder(BorderFactory.createTitledBorder("URL"));
        topPanel.add(urlField);

        searchField = new JTextField(18);
        searchField.setBorder(BorderFactory.createTitledBorder("Search term"));
        topPanel.add(searchField);

        JButton goButton = new JButton("Go");
        goButton.setActionCommand("Go");
        goButton.addActionListener(new ButtonClickListener());
        topPanel.add(goButton);

        mainFrame.add(topPanel, BorderLayout.NORTH);

        TA = new JTextArea();
        TA.setEditable(false);
        TA.setLineWrap(false);
        TA.setBorder(BorderFactory.createTitledBorder("Links"));
        mainFrame.add(new JScrollPane(TA), BorderLayout.CENTER);

        controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        statusLabel = new JLabel("Ready.");
        controlPanel.add(statusLabel);
        mainFrame.add(controlPanel, BorderLayout.SOUTH);

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        mainFrame.setVisible(true);
    }

    private void showEventDemo() {
        JButton goButton = new JButton("Go");                 // >>> ADD

        goButton.setActionCommand("Go");

        goButton.addActionListener(new ButtonClickListener());

        controlPanel.add(goButton);

        mainFrame.setVisible(true);
    }


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

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("OK")) {
                statusLabel.setText("Ok Button clicked.");
            } else if (command.equals("Submit")) {
                statusLabel.setText("Submit Button clicked.");
            } else if (command.equals("Go")) {
                statusLabel.setText("Go Button clicked.");
            } else {
                statusLabel.setText("Cancel Button clicked.");
            }
        }
    }
}
