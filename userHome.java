/**********************************************************************
* NAME: George Calvert
* CLASS: CPSC 321
* DATE: 12/6/22
* DESCRIPTION: Landing page for when a user signs in
**********************************************************************/
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class userHome extends FlashStash{
    JFrame frame;
    String username;

    /*
    Constructor for userHome
    * @param frame, String of the username
    * @return userHome object initialized
    */
    public userHome(JFrame frame, String username) {
        this.frame = frame;
        this.username = username;
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(600,360);
        this.frame.setLayout(new GridLayout(3, 0));
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true); 
    }
    /**
    displays landing page for a user
    *
    * @param nothing
    * @return displays gui of the landing page with options for what to do
    */
    public void Display() {
        // add buttons
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
                frame.remove(label);
                frame.remove(upperPanel);
                frame.remove(lowerPanel);
                frame.repaint();
                frame.revalidate();
                // display create set page
                CreateSet cs = new CreateSet(frame, username);
                cs.Display();   
            }
        });

        // browsing own sets button
        JButton browseOwn = new JButton("Browse Your Own Study Sets");
        browseOwn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(label);
                frame.remove(upperPanel);
                frame.remove(lowerPanel);
                // browse with own sets
                Browse bO = new Browse(frame, username, "Own");
                bO.Display();
                frame.repaint();
                frame.revalidate();
            }
        });
        // brows other sets
        JButton browse = new JButton("Browse Other Study Sets");
        browse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(label);
                frame.remove(upperPanel);
                frame.remove(lowerPanel);
                // go to display sets owned by others
                Browse bO = new Browse(frame, username, "Other");
                bO.Display();
                frame.repaint();
                frame.revalidate();
            }
        });
        // go to saved sets
        JButton saved = new JButton("Browse Your Saved Sets");
        saved.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(label);
                frame.remove(upperPanel);
                frame.remove(lowerPanel);
                // browse saved sets
                Browse bO = new Browse(frame, username, "Saved");
                bO.Display();
                frame.repaint();
                frame.revalidate();
            }
        });

        // logout button
        JButton logout = new JButton("Logout");
        logout.addActionListener(new ActionListener() {
            // check user info user info
            public void actionPerformed(ActionEvent e) {
                frame.remove(label);
                frame.remove(upperPanel);
                frame.remove(lowerPanel);
                // go back to loginpage
                FlashStash lo = new FlashStash();
                frame.dispose();
                lo.LogIn();
            }
        });
        // add buttons
        upperPanel.add(newStudySet);
        upperPanel.add(browseOwn);
        upperPanel.add(browse);
        upperPanel.add(saved);
        
        // add button
        lowerPanel.add(logout);

        // add to frame
        this.frame.add(upperPanel);
        this.frame.add(lowerPanel);
        this.frame.setVisible(true);

    }
    
}
