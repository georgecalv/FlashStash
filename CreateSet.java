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
import java.awt.*;

public class CreateSet {
    JFrame frame;
    String username;
    public CreateSet(JFrame frame, String username) {
        this.frame = frame;
        this.username = username;
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(600,360);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true); 
    }
    public void Display() {
        JPanel panel = new JPanel();
        JButton goBack = new JButton("Go Back");

        goBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userHome homePg = new userHome(frame, username);
                homePg.Display();
                // frame.removeAll();
                frame.remove(panel);
                frame.repaint();
                frame.revalidate();
            }
        });
        panel.add(goBack);
        this.frame.add(panel);
        // JPanel upper = new JPanel();
        // JLabel title = new JLabel("Create Set");
        this.frame.setVisible(true);
    }
}
