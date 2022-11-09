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

public class CreateSet {
    JFrame frame;
    String username;
    public CreateSet(JFrame frame, String username) {
        this.frame = frame;
        this.username = username;
    }
    public void Display() {
        JPanel upper = new JPanel();
        JLabel title = new JLabel("Create Set");
        upper.add(title);
        this.frame.add(upper);
        this.frame.setVisible(true);
    }
}
