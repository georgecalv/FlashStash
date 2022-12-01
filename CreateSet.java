import javax.swing.*;
// import java.awt.BorderLayout;
// import java.awt.Dimension;
import java.awt.event.*;
// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.sql.Statement;
// import java.util.Properties;
// import java.sql.PreparedStatement;
import java.awt.*;
import java.util.*;
import javax.swing.text.Document;

public class CreateSet {
    JFrame frame;
    String username;
    int size;
    public CreateSet(JFrame frame, String username) {
        this.frame = frame;
        this.username = username;
        this.size = 5;
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(600,360);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true); 
    }
    public void Display() {
        JPanel full = new JPanel();
        full.setLayout(new GridLayout(3,0));
        JPanel title = new JPanel();
        JPanel content = new JPanel();
        JPanel options = new JPanel();

        // title for set
        JTextField name = new JTextField("Title of StudySet");
        title.add(name);

        // make table
        Vector<String> columnNames = new Vector<String>(Arrays.asList("Question", "Answer"));
        Vector<Vector<String>> data = new Vector<Vector<String>>();
        for(int i = 0; i < this.size; i++) {
            data.add(i, new Vector<String>(Arrays.asList("", "")));
        }
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        content.add(scrollPane);
        // JButton plus = new JButton("+");
        // plus.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         content.remove(scrollPane);
        //         data.add(size, new Vector<String>(Arrays.asList("", "")));
        //         size += 1;
        //         JTable t = new JTable(data, columnNames);
        //         t.setFillsViewportHeight(true);
        //         t.setRowSelectionAllowed(true);
        //         t.setColumnSelectionAllowed(false);
        //         JScrollPane sp = new JScrollPane(t);
        //         content.add(sp);
        //         content.repaint();
        //         content.revalidate();
        //     }
        // });
        // options.add(plus);

        JButton goBack = new JButton("Go Back");
        goBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(full);
                userHome homePg = new userHome(frame, username);
                frame.dispose();
                homePg.Display(); 
            }
        });
        options.add(goBack);
        full.add(title);
        full.add(content);
        full.add(options);
        this.frame.add(full);
        // JPanel upper = new JPanel();
        // JLabel title = new JLabel("Create Set");
        this.frame.setVisible(true);
    }
}
