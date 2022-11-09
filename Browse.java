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
        try {
            JLabel title = new JLabel("Browse " + this.type + " Sets:");
            leftPanel.add(title);
            Vector<String> columnNames = new Vector<String>(Arrays.asList("User", "Set Name", "Subject"));
            Vector<Vector<String>> data = new Vector<Vector<String>>();
            Connection cn = super.getConnection();
            PreparedStatement st = cn.prepareStatement(q);
            st.setString(1, this.username);
            ResultSet rs = st.executeQuery();
            if(rs.next()) {
                while(rs.next()) {
                    data.add(new Vector<String>(Arrays.asList(rs.getString("user"), rs.getString("set_name"), rs.getString("set_subject"))));
                }
            }
            JTable table  = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            table.setFillsViewportHeight(true);
            leftPanel.add(scrollPane);
        }
        catch(SQLException e) {
            e.getStackTrace();
            System.out.println("Prepared Statement Error");
        }
        leftPanel.repaint();
        leftPanel.revalidate();
        this.frame.add(leftPanel);
        this.frame.setVisible(true);
    }
}
