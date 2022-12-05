import javax.swing.*;

import com.mysql.cj.protocol.Resultset;

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
    public void StartStudying() {
        JPanel panel = new JPanel();
        JLabel title = new JLabel(this.createdBy + "'s " + this.setName + " Study Set");
        title.setBounds(0, 0, 100, 20);
        this.frame.add(title);
        try {
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
            JTable table = new JTable(content, labels) {
                public boolean editCellAt(int row, int column, java.util.EventObject e) {
                   return false;
                }
             };
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
                        createFlashCards();
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
                            displayFlashCards(0);
                        }
                    }
                    catch(SQLException l) {
                        l.printStackTrace();
                    }
                }
            });
            JButton goBack = new JButton("Go Back");
            goBack.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Browse b = new Browse(frame, userName, type);
                    frame.remove(panel);
                    frame.remove(title);
                    // frame.setLayout(new GridLayout(2, 0));
                    frame.repaint();
                    frame.revalidate();
                    b.Display();
                }
            });
            panel.add(study);
            panel.add(goBack);
         
        }
        catch(SQLException e) {
            System.out.println(e.getStackTrace());
        }
        panel.repaint();
        panel.revalidate();
        this.frame.add(panel);
        this.frame.setVisible(true);
    }
    public void createFlashCards() throws SQLException{
        // query
        this.question = new ArrayList<String>();
        this.answer = new ArrayList<String>();
        String q = "SELECT * FROM Content WHERE study_set = ?";
        PreparedStatement st = cn.prepareStatement(q);
        st.setString(1, setCode);
        ResultSet rs = st.executeQuery();
        while(rs.next()) {
            this.question.add(rs.getString("question"));
            this.answer.add(rs.getString("answer"));
        }  
    }
    public void displayFlashCards(int start) {
        JLabel tmp = new JLabel(this.question.get(start));
        tmp.setFont((new Font("Serif", Font.PLAIN, 16)));
        this.flashcard.add(tmp);
        JButton show = new JButton("Display Answer");
        show.addActionListener(new ActionListener() {
            int time = 1;
            public void actionPerformed(ActionEvent e) {
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
        JButton next = new JButton("Next Question");
        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flashcard.removeAll();
                options.removeAll();
                if(start == question.size() - 1) {
                    displayFlashCards(0);
                }
                else {
                    displayFlashCards(start + 1);
                }
            }
        });
        options.add(next);
        JButton goBack = new JButton("Go Back");
        goBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Browse b = new Browse(frame, userName, type);
                flashcard.removeAll();
                options.removeAll();
                // frame.setLayout(new GridLayout(2, 0));
                frame.repaint();
                frame.revalidate();
                b.Display();
            }
        });
        // this.flashcard.setBackground(Color.GRAY);
        this.flashcard.setBounds(0,0,600,180);
        this.options.add(show);
        this.options.add(goBack);
        this.flashFrame.add(this.flashcard);
        this.flashFrame.add(this.options);
        // this.flashFrame.add(this.back);
        this.flashFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.flashFrame.setSize(600,360);
        this.flashFrame.setLocationRelativeTo(null);
        this.flashFrame.setResizable(false);
        this.flashFrame.setVisible(true);
    }
}
