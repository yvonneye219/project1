import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HTMLreader implements ActionListener {
    private JFrame mainFrame;
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

        mainFrame.setLayout(new GridLayout(3, 1, 8, 8));

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

        urlField = new JTextField("https://www.milton.edu/");
        urlField.setBorder(BorderFactory.createTitledBorder("URL"));
        mainFrame.add(urlField);

        searchField = new JTextField();
        searchField.setBorder(BorderFactory.createTitledBorder("Search term"));
        mainFrame.add(searchField);

        TA = new JTextArea();
        TA.setEditable(false);
        TA.setLineWrap(false);
        TA.setBorder(BorderFactory.createTitledBorder("Links"));


        controlPanel = new JPanel(new FlowLayout());

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        mainFrame.setVisible(true);
    }

    private void showEventDemo() {
        JButton okButton = new JButton("OK");
        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");

        okButton.setActionCommand("OK");
        submitButton.setActionCommand("Submit");
        cancelButton.setActionCommand("Cancel");

        okButton.addActionListener(new ButtonClickListener());
        submitButton.addActionListener(new ButtonClickListener());
        cancelButton.addActionListener(new ButtonClickListener());


        controlPanel.add(okButton);
        controlPanel.add(submitButton);
        controlPanel.add(cancelButton);

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
            JOptionPane.showMessageDialog(mainFrame, command + " Button clicked.");
        }
    }
}
