import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
public class Browse {
    JFrame frame;
    String type;
    String username;

    public Browse(JFrame frame, String user, String type) {
        this.frame = frame;
        this.username = user;
        this.type = type;
    }
    public void Display() {
        String q = "";
        switch(this.type) {
            case "Own":
                q = "SELECT * FROM StudySet WHERE username = ?";
                break;
            case "Other":
                q = "SELECT * FROM StudySet WHERE username != ?";
                break;  
        }
    }
}
