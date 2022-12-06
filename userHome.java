import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.*;

public class userHome extends FlashStash{
    JFrame frame;
    String username;
    public userHome(JFrame frame, String username) {
        this.frame = frame;
        this.username = username;
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(600,360);
        this.frame.setLayout(new GridLayout(3, 0));
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true); 
    }
    public void Display() {
        // add 5 buttons
        JLabel label = new JLabel("Welcome to FlashStash " + this.username);
        label.setFont(new Font("Serif", Font.PLAIN, 24));
        this.frame.add(label);
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
                frame.remove(label);
                frame.remove(upperPanel);
                frame.remove(lowerPanel);
                frame.repaint();
                frame.revalidate();
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
                // label.setText("Browse Your Own Sets");
                frame.remove(label);
                frame.remove(upperPanel);
                frame.remove(lowerPanel);
                Browse bO = new Browse(frame, username, "Own");
                bO.Display();
                frame.repaint();
                frame.revalidate();
            }
        });
        JButton browse = new JButton("Browse Other Study Sets");
        browse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // frame.removeAll();
                // frame.repaint();
                // label.setText("Browse Other Sets");
                frame.remove(label);
                frame.remove(upperPanel);
                frame.remove(lowerPanel);
                Browse bO = new Browse(frame, username, "Other");
                bO.Display();
                frame.repaint();
                frame.revalidate();
            }
        });
        JButton saved = new JButton("Browse Your Saved Sets");
        saved.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // frame.removeAll();
                // frame.repaint();
                // label.setText("Browse Other Sets");
                frame.remove(label);
                frame.remove(upperPanel);
                frame.remove(lowerPanel);
                Browse bO = new Browse(frame, username, "Saved");
                bO.Display();
                frame.repaint();
                frame.revalidate();
            }
        });

        // logout
        JButton logout = new JButton("Logout");
        logout.addActionListener(new ActionListener() {
            // check user info user info
            public void actionPerformed(ActionEvent e) {
                // frame.removeAll();
                // frame.repaint();
                frame.remove(label);
                frame.remove(upperPanel);
                frame.remove(lowerPanel);
                FlashStash lo = new FlashStash();
                frame.dispose();
                lo.LogIn();
            }
        });

        upperPanel.add(newStudySet);
        upperPanel.add(browseOwn);
        upperPanel.add(browse);
        upperPanel.add(saved);
        

        lowerPanel.add(logout);

        this.frame.add(upperPanel);
        this.frame.add(lowerPanel);
        this.frame.setVisible(true);

    }
    
}
