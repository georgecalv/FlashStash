/**********************************************************************
* NAME: George Calvert
* CLASS: CPSC 321
* DATE: 12/6/22
* DESCRIPTION: allows a user to create a study set and add it to the database 
**********************************************************************/
import javax.swing.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.awt.*;
import java.util.*;

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

    /*
    Constructor for CreateSet
    * @param frame, String of the username
    * @return userHome object initialized
    */
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
    /**
    displays page for creating a study set and adds it to database
    *
    * @param nothing
    * @return displays gui for creating a set with options for what to do
    */
    public void Display() {
        try {
            // make drop down for selecting subject fo set
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

            // submit button for subject and set name
            JButton select = new JButton("Select");
            select.addActionListener(new ActionListener() {
                int times = 0;
                public void actionPerformed(ActionEvent e) {
                    // add selected things to arraylist
                    if(times == 0) {
                        set.add(name.getText());
                        set.add(subjectCode.get(subjectDropBox.getSelectedIndex() - 1));
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
                    // get questions and answers

                    getQuestions(subjectDropBox, subjects, subjectName, subjectCode, answer, answerLabel);
                }
            });
            options.add(select);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        // go back to user home page
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
        this.frame.setVisible(true);

    }
    /**
    gets the questions and answers for study set and adds to database
    *
    * @param JComboBox, Box, Vector<String>, Vector<String>, JTextField, Jlabel
    * @return adds question and answers to arraylist then when done submits them to database
    */
    public void getQuestions(JComboBox subjectDropBox, Box subjects, Vector<String> subjectName, Vector<String> subjectCode, JTextField answer, JLabel answerLabel) {
        // reset text for text field
        title.setText("Question: ");
        name.setText("Enter Text");
        subjects.removeAll();
        answerLabel.setText("Answer: ");
        answer.setText("Enter Text");
        subjects.add(answerLabel);
        subjects.add(answer);

        // button for when you want to submit all questions and answers
        JButton done = new JButton("Done");
        done.addActionListener(new ActionListener() {
            int times = 0;
            public void actionPerformed(ActionEvent e) {
                // create study set and then add questions and answers with sql
                times++;
                if(times <= 1){
                    try {
                        // add study set to database
                        String q = "INSERT INTO StudySet (created_by, set_name, set_subject) VALUES (?,?,?)";
                        PreparedStatement ps = cn.prepareStatement(q);
                        ps.setString(1, username);
                        ps.setString(2, set.get(0));
                        ps.setString(3, set.get(1));
                        ps.execute();
                        
                        // check what number is the id of the set
                        q = "SELECT * FROM StudySet";
                        ps = cn.prepareStatement(q);
                        ResultSet rs = ps.executeQuery();
                        int num = 0;
                        while(rs.next()) {
                            num++;
                        }
    
                        // add questions and answers to dataset
                        q = "INSERT INTO Content (question, answer, username, study_set) VALUES (?,?,?,?)";
                        ps = cn.prepareStatement(q);
                        for(int i = 0; i < questions.size(); i++) {
                            ps.setString(1, questions.get(i));
                            ps.setString(2, answers.get(i));
                            ps.setString(3, username);
                            ps.setInt(4, num);
                            ps.execute();
                        }
                    }
                    catch(SQLException l) {
                        l.printStackTrace();
                    }
                }  
            }
        });
        options.add(done);
        options.repaint();
        options.revalidate();

        // submit question and Answer and add another 
        JButton submit = new JButton("Submit");
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                questions.add(name.getText());
                answers.add(answer.getText());
                options.remove(done);
                options.remove(submit);
                options.repaint();
                options.revalidate();
                content.repaint();
                content.revalidate();
                // add more questions and answers
                getQuestions(subjectDropBox, subjects, subjectName, subjectCode, answer, answerLabel);
            }
        });
        // update panel
        options.add(submit);
        options.repaint();
        options.revalidate();
        content.repaint();
        content.revalidate();
    }
}

