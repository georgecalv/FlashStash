import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.*;


public class Browse extends FlashStash {
    JFrame frame;
    String type;
    String username;

    public Browse(JFrame frame, String user, String type) {
        this.frame = frame;
        this.username = user;
        this.type = type;
        
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(600,360);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true); 
    }
    public void Display() {
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        // rightPanel.setBounds(400, 0, 100, 150);
        // leftPanel.setBounds(0, 0, 300, 360);
        // rightPanel.setAlignmentX(SwingConstants.RIGHT);
        // leftPanel.setAlignmentX(SwingConstants.LEFT);
        this.frame.add(leftPanel);
        String q = "";

        switch(this.type) {
            case "Own":
                q = "SELECT username, set_name, set_subject FROM StudySet WHERE username = ?";
                break;
            case "Other":
                q = "SELECT username, set_name, set_subject FROM StudySet WHERE username != ?";
                break;  
        }
        // get table of sets 
        try {
            // JLabel title = new JLabel("Browse " + this.type + " Sets:");
            // leftPanel.add(title);
            Vector<String> columnNames = new Vector<String>(Arrays.asList("User", "Set Name", "Subject"));
            Vector<Vector<String>> data = new Vector<Vector<String>>();
            Connection cn = super.getConnection();
            PreparedStatement st = cn.prepareStatement(q);
            st.setString(1, this.username);
            Vector<String> choice = new Vector<String>();
            Vector<String> subject = new Vector<String>();
            ResultSet rs = st.executeQuery();
            while(rs.next()) {
                data.add(new Vector<String>(Arrays.asList(rs.getString("username"), rs.getString("set_name"), rs.getString("set_subject"))));
                choice.add(rs.getString("set_name") + " | " + rs.getString("username"));
                subject.add(rs.getString("set_subject"));
            }
            rs.close();
            st.close();
            cn.close();
            JComboBox setDropBox = new JComboBox(choice);
            JTable table  = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            // table.setBounds(0, 0, 300, 360);
            table.setFillsViewportHeight(true);

            // study set button
            JButton study = new JButton("Study");
            study.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String selected = choice.get(setDropBox.getSelectedIndex());
                    String[] sep = selected.split(" ");
                    Study set = new Study(sep[0], sep[2], frame);

                }
            });
            leftPanel.add(setDropBox);
            leftPanel.add(study);
            leftPanel.add(scrollPane);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        // rightPanel.repaint();
        // rightPanel.revalidate();
        leftPanel.repaint();
        leftPanel.revalidate();
        this.frame.add(leftPanel);
        // this.frame.add(rightPanel);
        this.frame.setVisible(true);
    }
}
