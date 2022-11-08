import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class userHome {
    JFrame frame;
    String username;
    public userHome(JFrame frame, String username) {
        this.frame = frame;
        this.username = username;
    }
    public void Display() {
        // add 5 buttons
        JPanel upperPanel = new JPanel();
        JPanel lowerPanel = new JPanel();
        upperPanel.setBounds(20, 20, 560, 200);
        lowerPanel.setBounds(0, 0, 560, 90);


        // Box upperButtons = Box.createHorizontalBox();
        JButton newStudySet = new JButton("Create New Study Set");
        JButton browseOwn = new JButton("Browse Your Own Study Sets");
        JButton browse = new JButton("Browse Other Study Sets");
        // Box lowerButtons = Box.createHorizontalBox();
        JButton logout = new JButton("Logout");

        // lowerButtons.add(logout);
        upperPanel.add(newStudySet);
        upperPanel.add(browseOwn);
        upperPanel.add(browse);
        

        lowerPanel.add(logout);


        this.frame.add(upperPanel);
        this.frame.add(lowerPanel);
        this.frame.setVisible(true);

    }
    
}
