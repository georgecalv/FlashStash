/**********************************************************************
* NAME: George Calvert
* CLASS: CPSC 321
* DATE: 12/6/22
* DESCRIPTION: displays sets and allows user to filter and select sets 
*              to study, save, and like
**********************************************************************/
import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.*;
import java.awt.GridLayout;
import javax.swing.table.*;


public class Browse extends FlashStash {
    JFrame frame;
    String type;
    String username;
    JTable table;
    String operator;
    JScrollPane scrollPane;
    DefaultTableModel tableModel = null;

    /*
    Constructor for Browse
    * @param frame, String of the username, type of browsing
    * @return Browse object initialized
    */
    public Browse(JFrame frame, String user, String type) {
        this.frame = frame;
        this.username = user;
        this.type = type;
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(600,360);
        this.frame.setLayout(new GridLayout(2, 0));
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true); 
    }
    /**
    displays page of sets for user to browse through
    *
    * @param nothing
    * @return displays gui of study sets to browse filter and select
    */
    public void Display() {
        JPanel panel = new JPanel();
        JPanel leftPanel = new JPanel();
        GridLayout layout = new GridLayout(1, 0);
        panel.setLayout(layout);
        this.frame.add(panel);
        String q = "";

        // changes query based on type of browsing
        switch(this.type) {
            case "Own":
                q = "SELECT created_by, set_name, set_subject, set_id FROM StudySet WHERE created_by = ?";
                this.operator = "=";
                break;
            case "Other":
                q = "SELECT created_by, set_name, set_subject, set_id FROM StudySet WHERE created_by != ?";
                this.operator = "!=";
                break;  
            case "Saved":
                q = "SELECT created_by, set_name, set_subject, set_id FROM Saves JOIN StudySet USING(set_id) WHERE username = ?";
        }
        // get table of sets 
        try {
            // Filter settings
            String fq = "SELECT * FROM Subjects";
            PreparedStatement subs = cn.prepareStatement(fq);
            ResultSet subRS = subs.executeQuery();
            Vector<String> subjectName = new Vector<String>();
            Vector<String> subjectCode = new Vector<String>();
            subjectName.add("No Filter");
            subjectCode.add("No Filter");

            while(subRS.next()) {
                subjectName.add(subRS.getString("subject_name"));
                subjectCode.add(subRS.getString("subject_code"));
            }

            // drop down for type of filter
            JComboBox subjectDropBox = new JComboBox(subjectName);
            Vector<String> choices = new Vector<String>(Arrays.asList("No Filter", "Most Liked", "Least Liked"));
            JComboBox likes = new JComboBox(choices);


            // make table for sets
            Vector<String> columnNames = new Vector<String>(Arrays.asList("Created By", "Set Name", "Subject"));
            Vector<Vector<String>> data = new Vector<Vector<String>>();
            Vector<String> set_ids = new Vector<String>();
            Connection cn = super.getConnection();
            PreparedStatement st = cn.prepareStatement(q);
            st.setString(1, this.username);
            ResultSet rs = st.executeQuery();
            while(rs.next()) {
                data.add(new Vector<String>(Arrays.asList(rs.getString("created_by"), rs.getString("set_name"), rs.getString("set_subject"))));
                set_ids.add(rs.getString("set_id"));
            }
            rs.close();
            st.close();
            // cn.close();
            table = new JTable(data, columnNames) {
                public boolean editCellAt(int row, int column, java.util.EventObject e) {
                   return false;
                }
             };
            this.scrollPane = new JScrollPane(table);
            frame.add(scrollPane);
            table.setFillsViewportHeight(true);
            table.setRowSelectionAllowed(true);
            table.setColumnSelectionAllowed(false);

            // filter which affects the content in scroll pane
            JButton filter = new JButton("Filter");
            filter.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        // filter scroll panel
                        String subjectFilter = subjectCode.get(subjectDropBox.getSelectedIndex());
                        String likedFilter = choices.get(likes.getSelectedIndex());
                        System.out.println(subjectFilter);
                        System.out.println(likedFilter);
                        String q = "";

                        // no filters
                        if(subjectFilter.equals("No Filter") && likedFilter.equals("No Filter")) {
                            switch(type) {
                                case "Own":
                                    q = "SELECT created_by, set_name, set_subject, set_id FROM StudySet WHERE created_by = ?";
                                    break;
                                case "Other":
                                    q = "SELECT created_by, set_name, set_subject, set_id FROM StudySet WHERE created_by != ?";
                                    break;  
                                case "Saved":
                                    q = "SELECT created_by, set_name, set_subject, set_id FROM Saves JOIN StudySet USING(set_id) WHERE username = ?";
                            }
                        }
                        // no subject filter most liked
                        else if(subjectFilter.equals("No Filter") && likedFilter.equals("Most Liked")) {
                            // for non saved sets by user
                            if(type != "Saved") {
                                q = "WITH most_liked AS(" +
                                "SELECT set_name, created_by, set_id, set_subject, COUNT(*) AS number_likes " +
                                "FROM StudySet JOIN Likes USING(set_id) " +
                                "GROUP BY set_id " +
                                "HAVING number_likes >= 1 " +
                                "ORDER BY number_likes DESC " +
                                ") " + 
                                "SELECT created_by, set_name, set_subject, set_id " +
                                "FROM most_liked " +
                                "WHERE created_by " + operator + " ?";
                            }
                            // saved sets for user
                            else {
                                q = "WITH most_liked AS ( " +
                                    "select set_name, created_by, set_id, set_subject, COUNT(*) AS number_likes " +
                                    "FROM StudySet JOIN Likes USING(set_id) " +
                                    "GROUP BY set_id HAVING number_likes >= 1 " + 
                                    "ORDER BY number_likes DESC) " +
                                "SELECT m.created_by, m.set_name, m.set_subject, m.set_id " +
                                "FROM most_liked m JOIN Saves s ON(m.set_id = s.set_id) " +
                                "WHERE s.username = ?"; 
                            }
                        }
                        // no subject filter least liked
                        else if(subjectFilter.equals("No Filter") && likedFilter.equals("Least Liked")) {
                            if(type != "Saved") {
                                q = "WITH most_liked AS(" +
                                    "SELECT set_name, created_by, set_id, set_subject, COUNT(*) AS number_likes " +
                                    "FROM StudySet JOIN Likes USING(set_id) " +
                                    "GROUP BY set_id " +
                                    "HAVING number_likes >= 1 " +
                                    "ORDER BY number_likes " +
                                    ") " + 
                                    "SELECT created_by, set_name, set_subject, set_id " +
                                    "FROM most_liked " +
                                    "WHERE created_by " + operator + " ?";
                            }
                            else {
                                q = "WITH most_liked AS ( " +
                                    "select set_name, created_by, set_id, set_subject, COUNT(*) AS number_likes " +
                                    "FROM StudySet JOIN Likes USING(set_id) " +
                                    "GROUP BY set_id HAVING number_likes >= 1 " + 
                                    "ORDER BY number_likes " +
                                    ") " +
                                "SELECT m.created_by, m.set_name, m.set_subject, m.set_id " +
                                "FROM most_liked m JOIN Saves s ON(m.set_id = s.set_id) " +
                                "WHERE s.username = ?"; 
                            }
                        }
                        //subject filter with most liked
                        else if(likedFilter.equals("Most Liked")) {
                            if(type != "Saved") {
                                q = "WITH most_liked AS(" +
                                    "SELECT set_name, created_by, set_id, set_subject, COUNT(*) AS number_likes " +
                                    "FROM StudySet JOIN Likes USING(set_id) " +
                                    "GROUP BY set_id " +
                                    "HAVING number_likes >= 1 " +
                                    "ORDER BY number_likes DESC " +
                                    ") " + 
                                    "SELECT created_by, set_name, set_subject, set_id " +
                                    "FROM most_liked " +
                                    "WHERE created_by " + operator + " ? AND set_subject = '" + subjectFilter + "'";
                            }
                            else {
                                q = "WITH most_liked AS ( " +
                                    "select set_name, created_by, set_id, set_subject, COUNT(*) AS number_likes " +
                                    "FROM StudySet JOIN Likes USING(set_id) " +
                                    "GROUP BY set_id HAVING number_likes >= 1 " + 
                                    "ORDER BY number_likes DESC) " +
                                "SELECT m.created_by, m.set_name, m.set_subject, m.set_id " +
                                "FROM most_liked m JOIN Saves s ON(m.set_id = s.set_id) " +
                                "WHERE s.username = ? AND m.set_subject = '" + subjectFilter + "'";                              
                            }
                        }
                        // subject filter least liked
                        else if(likedFilter.equals("Least Liked")) {
                            if(type != "Saved") {
                                q = "WITH most_liked AS(" +
                                    "SELECT set_name, created_by, set_id, set_subject, COUNT(*) AS number_likes " +
                                    "FROM StudySet JOIN Likes USING(set_id) " +
                                    "GROUP BY set_id " +
                                    "HAVING number_likes >= 1 " +
                                    "ORDER BY number_likes " +
                                    ") " + 
                                    "SELECT created_by, set_name, set_subject, set_id " +
                                    "FROM most_liked " +
                                    "WHERE created_by " + operator + " ? AND set_subject = '" + subjectFilter + "'";
                            }
                            else {
                                q = "WITH most_liked AS ( " +
                                    "select set_name, created_by, set_id, set_subject, COUNT(*) AS number_likes " +
                                    "FROM StudySet JOIN Likes USING(set_id) " +
                                    "GROUP BY set_id HAVING number_likes >= 1 " + 
                                    "ORDER BY number_likes) " +
                                "SELECT m.created_by, m.set_name, m.set_subject, m.set_id " +
                                "FROM most_liked m JOIN Saves s ON(m.set_id = s.set_id) " +
                                "WHERE s.username = ? AND m.set_subject = '" + subjectFilter + "'";                                    
                            }
                        }
                        // subject filter no like filter
                        else if(!subjectFilter.equals("No Filter")) {
                            if(type != "Saved") {
                                q = "SELECT created_by, set_name, set_subject, set_id " +
                                    "FROM StudySet " +
                                    "WHERE created_by " + operator + " ? AND set_subject = '" + subjectFilter + "'";
                            }
                            else {
                                q = "SELECT s.created_by, s.set_name, s.set_subject, s.set_id " +
                                    "FROM StudySet s JOIN Saves sa ON(s.set_id = sa.set_id)" +
                                    "WHERE sa.username = ? AND s.set_subject = '" + subjectFilter + "'";     
                            }
                        }

                        // do query 
                        PreparedStatement st = cn.prepareStatement(q); 
                        st.setString(1, username);
                        ResultSet rs = st.executeQuery();
                        data.clear();
                        set_ids.clear();
                        table.removeAll();
                        while(rs.next()) {
                            data.add(new Vector<String>(Arrays.asList(rs.getString("created_by"), rs.getString("set_name"), rs.getString("set_subject"))));
                            set_ids.add(rs.getString("set_id"));
                        }
                        tableModel = new DefaultTableModel(data, columnNames);
                        table.setModel(tableModel);
                        
                    }
                    catch(SQLException l) {
                        l.printStackTrace();
                    }


                    // update table
                    table.repaint();
                    table.revalidate();
                    table.doLayout();
                    frame.repaint();
                    frame.revalidate();
                }
            });
            

            // study set button
            JButton study = new JButton("Select");
            study.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int row = table.getSelectedRow();
                    if(row < 0) {
                        // do ntohing
                        System.out.println("nothing");
                        System.out.println(row);
                        System.out.println(table.getRowCount());
                    }
                    else {
                        // int column = table.getSelectedColumn();
                        System.out.println("select row");
                        String createdBy = data.get(row).get(0);
                        String setName = data.get(row).get(1);
                        String setcode = set_ids.get(row);
                        frame.remove(scrollPane);
                        frame.remove(panel);
                        frame.repaint();
                        frame.revalidate();
                        // goes to study landing page for set
                        Study st = new Study(setcode, createdBy, username, frame, setName, type);
                        st.StartStudying();
                    }

                }
            });
            // return back to userhome page
            JButton goBack = new JButton("Go Back");
            goBack.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    userHome homePg = new userHome(frame, username);
                    frame.remove(panel);
                    frame.remove(scrollPane);
                    frame.repaint();
                    frame.revalidate();
                    homePg.Display();
                }
            });
   
            // panel.add(scrollPane);
           
            // frame.add(scrollPane);
            panel.add(subjectDropBox);
            panel.add(likes);
            panel.add(filter);
            panel.add(study);
            panel.add(goBack);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        leftPanel.repaint();
        leftPanel.revalidate();
        panel.repaint();
        panel.revalidate();
        this.frame.add(panel);
        this.frame.setVisible(true);
    }
}
