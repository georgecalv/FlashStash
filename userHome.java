import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;

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

        // new study set
        JButton newStudySet = new JButton("Create New Study Set");
        newStudySet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // frame.removeAll();
                // frame.repaint();
                frame.remove(upperPanel);
                frame.remove(lowerPanel);
                CreateSet cs = new CreateSet(frame, username);
                cs.Display();   
            }
        });

        // browsing
        JButton browseOwn = new JButton("Browse Your Own Study Sets");
        browseOwn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // frame.removeAll();
                // frame.repaint();
                frame.remove(upperPanel);
                frame.remove(lowerPanel);
                Browse bO = new Browse(frame, username, "Own");
                bO.Display();
            }
        });
        JButton browse = new JButton("Browse Other Study Sets");
        browse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // frame.removeAll();
                // frame.repaint();
                frame.remove(upperPanel);
                frame.remove(lowerPanel);
                Browse bO = new Browse(frame, username, "Other");
                bO.Display();
            }
        });

        // logout
        JButton logout = new JButton("Logout");
        logout.addActionListener(new ActionListener() {
            // check user info user info
            public void actionPerformed(ActionEvent e) {
                // frame.removeAll();
                // frame.repaint();
                frame.remove(upperPanel);
                frame.remove(lowerPanel);
                FlashStash lo = new FlashStash();
                lo.LogIn();
            }
        });

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
