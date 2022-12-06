/**********************************************************************
* NAME: George Calvert
* CLASS: CPSC 321
* DATE: 12/6/22
* DESCRIPTION: Allows a user to study a certain study set
**********************************************************************/
import javax.swing.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;
import java.util.*;
import java.awt.*;

public class Study extends FlashStash{
    JFrame frame;
    String setCode;
    String userName;
    String createdBy;
    String setName;
    String type;
    ArrayList<String> question;
    ArrayList<String> answer;
    JFrame flashFrame;
    JPanel flashcard;
    JPanel options;
    JPanel back;

    /*
    Constructor for Browse
    * @param String, String, String, JFrame, String, String
    * @return Study object initialized
    */
    public Study(String setCode, String createdBy, String userName, JFrame frame, String setName, String type) {
        this.userName = userName;
        this.createdBy = createdBy;
        this.setCode = setCode;
        this.setName = setName;
        this.frame = frame;
        this.type = type;
        this.flashFrame = new JFrame("Flashcard");
        this.flashFrame.setLayout(new GridLayout(3, 0));
        this.flashcard = new JPanel();
        this.options = new JPanel();
        this.back = new JPanel();
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(650,360);
        this.frame.setLayout(new FlowLayout());
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true); 
    }
    /**
    Displays question and answer from study set selected
    *
    * @param nothing
    * @return displays gui of questions and answers for user to select what to do
    */
    public void StartStudying() {
        // paanels
        JPanel panel = new JPanel();
        JLabel title = new JLabel(this.createdBy + "'s " + this.setName + " Study Set");
        title.setBounds(0, 0, 100, 20);
        this.frame.add(title);
        try {
            // query to get questions for selected study set
            String q = "SELECT question, answer FROM Content WHERE username = ? and study_set = ?";
            PreparedStatement st = super.cn.prepareStatement(q);
            st.setString(1, this.createdBy);
            st.setString(2, this.setCode);
            ResultSet rs = st.executeQuery();
            Vector<Vector<String>> content = new Vector<Vector<String>>();
            Vector<String> labels = new Vector<String>(Arrays.asList("Question", "Answer"));
            while(rs.next()) {
                content.add(new Vector<String>(Arrays.asList(rs.getString("question"), rs.getString("answer"))));
            }
            // puts questions and answers into table
            JTable table = new JTable(content, labels) {
                public boolean editCellAt(int row, int column, java.util.EventObject e) {
                   return false;
                }
             };
            //  add to frame
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBounds(0, 0, 400, 200);
            table.setFillsViewportHeight(true);
            table.setRowSelectionAllowed(true);
            table.setColumnSelectionAllowed(false);
            panel.add(scrollPane);

            // study set button
            JButton study = new JButton("Study");
            study.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        frame.remove(panel);
                        frame.remove(title);
                        // create list of questions and answers
                        createFlashCards();
                        // checks if set has content
                        if(question.size() == 0 || answer.size() == 0) {
                            JFrame sorry = new JFrame("Invalid Set");
                            JPanel p = new JPanel();
                            p.setLayout(new GridLayout(2, 0));
                            JLabel l = new JLabel("Sorry this set doesnt contain any content");
                            p.add(l);
                            JButton ok = new JButton("Okay");
                            ok.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    frame.remove(panel);
                                    frame.remove(title);
                                    Browse bO = new Browse(frame, userName, type);
                                    bO.Display();
                                    frame.repaint();
                                    frame.revalidate();
                                }
                            });
                            p.add(ok);
                            sorry.add(p);

                            sorry.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            sorry.setSize(300,100);
                            sorry.setLocationRelativeTo(frame);
                            sorry.setResizable(false);
                            sorry.setVisible(true);
                            
                        }
                        else {
                            // displays flashcards
                            displayFlashCards(0);
                        }
                    }
                    catch(SQLException l) {
                        l.printStackTrace();
                    }
                }
            });
            // return back to browsing page
            JButton goBack = new JButton("Go Back");
            goBack.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Browse b = new Browse(frame, userName, type);
                    frame.remove(panel);
                    frame.remove(title);
                    frame.repaint();
                    frame.revalidate();
                    b.Display();
                }
            });

            // save set button
            JButton save = new JButton("Save Set");
            save.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        // check if already saved
                        if(checkAlreadyDone("Save")) {
                            // insert that user saved set to database
                            String q = "INSERT INTO Saves VALUES (?, ?)";
                            PreparedStatement st = cn.prepareStatement(q);
                            st.setString(1, setCode);
                            st.setString(2, userName);
                            st.execute();
                        }
                        // already saved
                        else {
                            JFrame sorry = new JFrame("Issue");
                            JPanel p = new JPanel(new GridLayout(2, 0));
                            p.add(new JLabel("Sorry you have already Saved this set"));
                            JButton close = new JButton("Close");
                            close.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    sorry.dispose();
                                }
                            });
                            p.add(close);
                            sorry.add(p);
                            sorry.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            sorry.setSize(300,100);
                            sorry.setLocationRelativeTo(frame);
                            sorry.setResizable(false);
                            sorry.setVisible(true);
                        }
                    }
                    catch(SQLException l) {
                        l.printStackTrace();
                    }
                }
            });
            // like a set button
            JButton like = new JButton("Like");
            like.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        // check if already liked
                        if(checkAlreadyDone("Like")) {
                            String q = "INSERT INTO Likes VALUES (?, ?)";
                            PreparedStatement st = cn.prepareStatement(q);
                            st.setString(1, setCode);
                            st.setString(2, userName);
                            st.execute();
       
                        }
                        // display frame that already liked
                        else {
                            JFrame sorry = new JFrame("Issue");
                            JPanel p = new JPanel(new GridLayout(2, 0));
                            p.add(new JLabel("Sorry you have already Liked this set"));
                            JButton close = new JButton("Close");
                            close.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    sorry.dispose();
                                }
                            });
                            p.add(close);
                            sorry.add(p);
                            sorry.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            sorry.setSize(300,100);
                            sorry.setLocationRelativeTo(frame);
                            sorry.setResizable(false);
                            sorry.setVisible(true);
                        }
                    }
                    catch(SQLException l) {
                        l.printStackTrace();
                    }
                    
        
                }
            });
            // add buttons
            Box b = Box.createVerticalBox();
            b.add(study);
            b.add(save);
            Box b2 = Box.createVerticalBox();
            b2.add(like);
            b2.add(goBack);
            panel.add(b);
            panel.add(b2);  
        }
        catch(SQLException e) {
            System.out.println(e.getStackTrace());
        }
        panel.repaint();
        panel.revalidate();
        this.frame.add(panel);
        this.frame.setVisible(true);
    }
    /**
    Checks if smomething is already in databse
    *
    * @param String of whther it is being saved or liked
    * @return boolean of if it is used or not
    */
    public boolean checkAlreadyDone(String type) throws SQLException {
        String q = "";
        switch(type) {
            case "Like":
                q = "SELECT * FROM Likes WHERE set_id = ? AND username = ?";
                break;
            case "Save":
                q = "SELECT * FROM Saves WHERE set_id = ? AND username = ?";
                break;
        }
        // execute query
        PreparedStatement st = cn.prepareStatement(q);
        st.setString(1, this.setCode);
        st.setString(2, this.userName);
        ResultSet rs = st.executeQuery();
        if(rs.next()) {
            return false;
        }
        return true;
    }
    /**
    Gets all content from set and adds it to arraylists
    *
    * @param nothing
    * @return arraylists of questions and answers are initialized and filled
    */
    public void createFlashCards() throws SQLException{
        this.question = new ArrayList<String>();
        this.answer = new ArrayList<String>();

        // query
        String q = "SELECT * FROM Content WHERE study_set = ?";
        PreparedStatement st = cn.prepareStatement(q);
        st.setString(1, setCode);
        ResultSet rs = st.executeQuery();
        while(rs.next()) {
            this.question.add(rs.getString("question"));
            this.answer.add(rs.getString("answer"));
        }  
    }
    /**
    displays flashcards for user to go through
    *
    * @param int of the index the user is at
    * @return displays flashcards to user in GUI
    */
    public void displayFlashCards(int start) {
        // gets question
        JLabel tmp = new JLabel(this.question.get(start));
        tmp.setFont((new Font("Serif", Font.PLAIN, 16)));
        this.flashcard.add(tmp);
        // show answer or question
        JButton show = new JButton("Display Answer");
        show.addActionListener(new ActionListener() {
            int time = 1;
            public void actionPerformed(ActionEvent e) {
                // swithces from display answer to display question for index
                if(time % 2 == 0) {
                    tmp.setText(question.get(start));
                    show.setText("Display Answer");

                }
                else {
                    tmp.setText(answer.get(start));
                    show.setText("Display Question");
                }
                time += 1;
                flashcard.repaint();
                flashcard.revalidate();
            }
        });
        // goes to next question
        JButton next = new JButton("Next Question");
        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flashcard.removeAll();
                options.removeAll();
                // checks if at end of arraylist
                if(start == question.size() - 1) {
                    displayFlashCards(0);
                }
                else {
                    displayFlashCards(start + 1);
                }
            }
        });
        options.add(next);

        // go back to browsing page
        JButton goBack = new JButton("Go Back");
        goBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Browse b = new Browse(frame, userName, type);
                flashcard.removeAll();
                options.removeAll();;
                frame.repaint();
                frame.revalidate();
                b.Display();
            }
        });

        // frame settings
        this.flashcard.setBounds(0,0,600,180);
        this.options.add(show);
        this.options.add(goBack);
        this.flashFrame.add(this.flashcard);
        this.flashFrame.add(this.options);
        this.flashFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.flashFrame.setSize(600,360);
        this.flashFrame.setLocationRelativeTo(null);
        this.flashFrame.setResizable(false);
        this.flashFrame.setVisible(true);
    }
}
