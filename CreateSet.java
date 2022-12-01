import javax.swing.*;
// import java.awt.BorderLayout;
// import java.awt.Dimension;
import java.awt.event.*;
// import java.sql.Connection;
// import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
// import java.sql.SQLException;
import java.sql.Statement;
// import java.util.Properties;
import java.sql.PreparedStatement;
import java.awt.*;
import java.util.*;
import javax.swing.text.Document;

public class CreateSet extends FlashStash{
    JFrame frame;
    String username;
    int size;
    ArrayList<String> set;
    ArrayList<String> questions;
    ArrayList<String> answers;
    JPanel full;
    JPanel content;
    JPanel options;
    Box settings;
    Box nameSet;
    JLabel title;
    JTextField name;

    public CreateSet(JFrame frame, String username) {
        this.frame = frame;
        this.username = username;
        this.size = 5;
        this.set = new ArrayList<String>();
        this.questions = new ArrayList<String>();
        this.answers = new ArrayList<String>();
        this.full = new JPanel();
        full.setLayout(new GridLayout(2,0));
        this.content = new JPanel();
        this.options = new JPanel();

        // title for set
        this.settings = Box.createVerticalBox();
        this.nameSet = Box.createHorizontalBox();
        this.title = new JLabel("StudySet Name: ");
        this.name = new JTextField("Enter Text");
        nameSet.add(title);
        nameSet.add(name);
        settings.add(nameSet);

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(600,360);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true); 
    }
    public void Display() {
        try {
            String fq = "SELECT * FROM Subjects";
            PreparedStatement subs = cn.prepareStatement(fq);
            ResultSet subRS = subs.executeQuery();
            Vector<String> subjectName = new Vector<String>();
            Vector<String> subjectCode = new Vector<String>();
            subjectName.add("Subject");
            while(subRS.next()) {
                subjectName.add(subRS.getString("subject_name"));
                subjectCode.add(subRS.getString("subject_code"));
            }
            JComboBox subjectDropBox = new JComboBox(subjectName);
            // content.add(subjectDropBox);
            Box subjects = Box.createHorizontalBox();
            subjects.add(new JLabel("Subject: "));
            subjects.add(subjectDropBox);
            settings.add(subjects);
            content.add(settings);
            // submit button
            JButton select = new JButton("Select");
            select.addActionListener(new ActionListener() {
                int times = 0;
                public void actionPerformed(ActionEvent e) {
                    // add selected things to arraylist
                    if(times == 0) {
                        set.add(name.getText());
                        set.add(subjectCode.get(subjectDropBox.getSelectedIndex()));
                    }
                    times += 1;
                    // text for question and then answer
                    title.setText("Question: ");
                    name.setText("Enter Text");
                    subjects.removeAll();
                    JLabel answerLabel = new JLabel("Answer: ");
                    JTextField answer = new JTextField("Enter Text");
                    subjects.add(answerLabel);
                    subjects.add(answer);
                    options.remove(select);
                    // finsihed with data
                    JButton done = new JButton("Done");
                    done.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            // create study set and then add questions and answers
                        }
                    });
                    options.add(done);

                    options.repaint();
                    options.revalidate();
                    // get questions and answers

                    getQuestions(subjectDropBox, subjects, subjectName, subjectCode);
                }
            });
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        JButton goBack = new JButton("Go Back");
        goBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(full);
                userHome homePg = new userHome(frame, username);
                frame.dispose();
                homePg.Display(); 
            }
        });
        options.add(goBack);
        full.add(content);
        full.add(options);
        this.frame.add(full);
        // JPanel upper = new JPanel();
        // JLabel title = new JLabel("Create Set");
        this.frame.setVisible(true);

    }
    public void getQuestions(JComboBox subjectDropBox, Box subjects, Vector<String> subjectName, Vector<String> subjectCode) {

            // submit question and Answer
        //     JButton submit = new JButton("Submit");
        //     submit.addActionListener(new ActionListener() {
        //         public void actionPerformed(ActionEvent e) {
        //             questions.add(name.getText());
        //             answers.add(answer.getText());
        //             options.remove(select);
        //             options.remove(done);
        //             options.repaint();
        //             options.revalidate();
        //             content.repaint();
        //             content.revalidate();
        //             getQuestions(subjectDropBox, subjects, subjectName, subjectCode);
        //             // options.repaint();
        //             // options.revalidate();
        //             // content.repaint();
        //             // content.revalidate();
        //         }
        //     });
        // options.add(submit);
        // options.repaint();
        // options.revalidate();
        // content.repaint();
        // content.revalidate();
        // options.add(select);
    }
}

