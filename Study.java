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
    String setName;
    String type;
    public Study(String setCode, String userName, JFrame frame, String setName, String type) {
        this.userName = userName;
        this.setCode = setCode;
        this.setName = setName;
        this.frame = frame;
        this.type = type;
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(650,360);
        this.frame.setLayout(new FlowLayout());
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true); 
    }
    public void StartStudying() {
        JPanel panel = new JPanel();
        JLabel title = new JLabel(userName + "'s " + this.setName + " Study Set");
        title.setBounds(0, 0, 100, 20);
        this.frame.add(title);
        try {
            String q = "SELECT question, answer FROM Content WHERE username = ? and study_set = ?";
            PreparedStatement st = super.cn.prepareStatement(q);
            st.setString(1, this.userName);
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
                    // frames
                    JFrame flashFrame = new JFrame("Flashcard");
                    JPanel flashcard = new JPanel(new GridLayout(2, 0));

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
}
