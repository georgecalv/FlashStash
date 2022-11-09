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

public class Browse {
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
                q = "SELECT * FROM StudySet WHERE username = ?";
                break;
            case "Other":
                q = "SELECT * FROM StudySet WHERE username != ?";
                break;  
        }
        JLabel title = new JLabel("Browse");
        leftPanel.add(title);
        leftPanel.repaint();
        leftPanel.revalidate();
        this.frame.add(leftPanel);
        this.frame.setVisible(true);
    }
}
