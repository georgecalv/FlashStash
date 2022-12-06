import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class adminUserHome {
    JFrame frame;
    String username;
    JScrollPane info;

    public adminUserHome(JFrame frame, String username) {
        this.frame = frame;
        this.frame.setLayout(new GridLayout(2, 0));
        this.username = username;
    }
    public void display() {
        displayInfo();
        JPanel panel = new JPanel();
        JPanel buttons = new JPanel(new GridLayout(5, 0));

        // logout
        JButton logout = new JButton("Logout");
        logout.addActionListener(new ActionListener() {
            // check user info user info
            public void actionPerformed(ActionEvent e) {
                frame.removeAll();
                // frame.repaint();
                // frame.remove(panel);
                FlashStash lo = new FlashStash();
                frame.dispose();
                lo.LogIn();
            }
        });
        JButton activeUsers = new JButton("Display Active Users");
        activeUsers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(info);
                Vector<String> columnNames = new Vector<String>(Arrays.asList("Rank", "Username", "Number_Sets"));
                Vector<Vector<String>> data = new Vector<Vector<String>>();
                try {
                    String q = "SELECT username, COUNT(*) AS number_sets, DENSE_RANK() OVER (ORDER BY number_sets DESC) AS rank " +
                                "FROM User JOIN StudySet ON(created_by = username) " +
                                "GROUP BY username " +
                                "HAVING number_sets >= 1 " +
                                "ORDER BY number_sets DESC";
                    Connection cn = getConnection();
                    PreparedStatement st = cn.prepareStatement(q);
                    ResultSet rs = st.executeQuery(q);
                    while(rs.next()) {
                        data.add(new Vector<String>(Arrays.asList(rs.getString("rank"), rs.getString("username"),rs.getString("number_sets"))));
                    }
                    JTable table = new JTable(data, columnNames) {
                        public boolean editCellAt(int row, int column, java.util.EventObject e) {
                           return false;
                        }
                     };
                    JScrollPane scrollPane = new JScrollPane(table);
                    table.setFillsViewportHeight(true);
                    table.setRowSelectionAllowed(true);
                    table.setColumnSelectionAllowed(false);
                    
                    panel.remove(buttons);
                    frame.remove(panel);
                    frame.add(scrollPane);
                    frame.add(panel);

                    JButton goBack = new JButton("Go Back");
                    goBack.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            frame.remove(panel);
                            frame.remove(scrollPane);
                            frame.remove(info);
                            display();
                            frame.repaint();
                            frame.revalidate();
                        }
                    });
                    panel.add(goBack);
                    panel.repaint();
                    panel.revalidate();
                    frame.repaint();
                    frame.revalidate();
                    
                }
                catch(SQLException l) {
                    l.printStackTrace();
                }

            }
        });
        JButton popSets = new JButton("Most Popular Sets");
        popSets.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(info);
                Vector<String> columnNames = new Vector<String>(Arrays.asList("Set Name", "Created By", "Number Saves"));
                Vector<Vector<String>> data = new Vector<Vector<String>>();
                String q = "SELECT set_name, created_by, COUNT(*) AS number_saves " +
                            "FROM StudySet JOIN Saves USING(set_id) " +
                            "GROUP BY set_id " +
                            "HAVING number_saves >= 2 " +
                            "ORDER BY number_saves DESC";
                try {
                    Connection cn = getConnection();
                    PreparedStatement st = cn.prepareStatement(q);
                    ResultSet rs = st.executeQuery(q);
                    while(rs.next()) {
                        data.add(new Vector<String>(Arrays.asList(rs.getString("set_name"), rs.getString("created_by"),rs.getString("number_saves"))));
                    }
                } 
                catch(SQLException l) {
                    l.printStackTrace();
                }  
                JTable table = new JTable(data, columnNames) {
                    public boolean editCellAt(int row, int column, java.util.EventObject e) {
                        return false;
                    }
                };
                JScrollPane scrollPane = new JScrollPane(table);
                table.setFillsViewportHeight(true);
                table.setRowSelectionAllowed(true);
                table.setColumnSelectionAllowed(false);
                
                panel.remove(buttons);
                frame.remove(panel);
                frame.add(scrollPane);
                frame.add(panel);

                JButton goBack = new JButton("Go Back");
                goBack.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        frame.remove(panel);
                        frame.remove(scrollPane);
                        frame.remove(info);
                        display();
                        frame.repaint();
                        frame.revalidate();
                    }
                });
                panel.add(goBack);
                panel.repaint();
                panel.revalidate();
                frame.repaint();
                frame.revalidate();
            }
        });
        JButton mostLikes = new JButton("Users who like the most");
        mostLikes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(info);
                Vector<String> columnNames = new Vector<String>(Arrays.asList("Username", "Number of Sets Liked"));
                Vector<Vector<String>> data = new Vector<Vector<String>>();
                String q = "SELECT username, COUNT(*) AS number_likes " +
                            "FROM Likes " +
                            "GROUP BY username " +
                            "HAVING number_likes >= ALL (SELECT COUNT(*) FROM Likes GROUP BY username)";
                try {
                    Connection cn = getConnection();
                    PreparedStatement st = cn.prepareStatement(q);
                    ResultSet rs = st.executeQuery(q);
                    while(rs.next()) {
                        data.add(new Vector<String>(Arrays.asList(rs.getString("username"), rs.getString("number_likes"))));
                    }
                } 
                catch(SQLException l) {
                    l.printStackTrace();
                }  
                JTable table = new JTable(data, columnNames) {
                    public boolean editCellAt(int row, int column, java.util.EventObject e) {
                        return false;
                    }
                };
                JScrollPane scrollPane = new JScrollPane(table);
                table.setFillsViewportHeight(true);
                table.setRowSelectionAllowed(false);
                table.setColumnSelectionAllowed(false);
                
                panel.remove(buttons);
                frame.remove(panel);
                frame.add(scrollPane);
                frame.add(panel);

                JButton goBack = new JButton("Go Back");
                goBack.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        frame.remove(panel);
                        frame.remove(scrollPane);
                        frame.remove(info);
                        display();
                        frame.repaint();
                        frame.revalidate();
                    }
                });
                panel.add(goBack);
                panel.repaint();
                panel.revalidate();
                frame.repaint();
                frame.revalidate();
            }
        });
        JButton displayUsers = new JButton("Display User Info");
        displayUsers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.remove(info);
                Vector<String> columnNames = new Vector<String>(Arrays.asList("Username", "Password", "First Name", "Last Name"));
                Vector<Vector<String>> data = new Vector<Vector<String>>();
                String q = "SELECT * FROM User";
                try {
                    Connection cn = getConnection();
                    PreparedStatement st = cn.prepareStatement(q);
                    ResultSet rs = st.executeQuery(q);
                    while(rs.next()) {
                        data.add(new Vector<String>(Arrays.asList(rs.getString("username"), rs.getString("pass"), rs.getString("fname"), rs.getString("lname"))));
                    }
                } 
                catch(SQLException l) {
                    l.printStackTrace();
                }  
                JTable table = new JTable(data, columnNames) {
                    public boolean editCellAt(int row, int column, java.util.EventObject e) {
                        return false;
                    }
                };
                JScrollPane scrollPane = new JScrollPane(table);
                table.setFillsViewportHeight(true);
                table.setRowSelectionAllowed(false);
                table.setColumnSelectionAllowed(false);
                
                panel.remove(buttons);
                frame.remove(panel);
                frame.add(scrollPane);
                frame.add(panel);

                JButton goBack = new JButton("Go Back");
                goBack.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        frame.remove(panel);
                        frame.remove(scrollPane);
                        frame.remove(info);
                        display();
                        frame.repaint();
                        frame.revalidate();
                    }
                });
                panel.add(goBack);
                panel.repaint();
                panel.revalidate();
                frame.repaint();
                frame.revalidate();
            }
        });
        buttons.add(displayUsers);
        buttons.add(activeUsers);
        buttons.add(popSets);
        buttons.add(mostLikes);
        buttons.add(logout);
        panel.add(buttons);
        this.frame.add(panel);
        this.frame.repaint();
        this.frame.revalidate();


        // frame settings
        // this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // this.frame.setSize(600,360);
        // this.frame.setLayout(new GridLayout(3, 0));
        // this.frame.setLocationRelativeTo(null);
        // this.frame.setVisible(true); 
    }
    public void displayInfo() {
        Vector<String> columnNames = new Vector<String>(Arrays.asList("Number Users", "Number Sets", "Number Questions/Answers"));
        Vector<Vector<Integer>> data = new Vector<Vector<Integer>>();

        // get num users
        String q = "SELECT COUNT(*) AS count FROM User";
        try {
            Connection cn = getConnection();
            PreparedStatement st = cn.prepareStatement(q);
            ResultSet rs = st.executeQuery(q);
            rs.next();
            int numUser = rs.getInt("count");

            // get num sets
            q = "SELECT COUNT(*) AS number_sets FROM StudySet";
            rs = st.executeQuery(q);
            rs.next();
            int numSets = rs.getInt("number_sets");

            // get numquestion
            q = "SELECT COUNT(*) AS number_q FROM Content";
            rs = st.executeQuery(q);
            rs.next();
            int numQ = rs.getInt("number_q");

            // table
            data.add(new Vector<Integer>(Arrays.asList(numUser, numSets, numQ)));
            JTable table = new JTable(data, columnNames) {
                public boolean editCellAt(int row, int column, java.util.EventObject e) {
                    return false;
                }
            };
            this.info = new JScrollPane(table);
            table.setFillsViewportHeight(true);
            table.setRowSelectionAllowed(false);
            table.setColumnSelectionAllowed(false);
            frame.add(this.info);
        }
        catch(SQLException l) {
            l.printStackTrace();
        }


    }
    // get connection
    public Connection getConnection() throws SQLException {
        String[] connection = getUrl(); 
        Connection cn = DriverManager.getConnection(connection[4], connection[1], connection[2]);
        return cn;
    } 

    // get url
    public String[] getUrl() {
            String hst, usr, pwd, dab, url;
            String result[] = new String[5];
            try {
                Properties prop = new Properties();
                FileInputStream in = new FileInputStream("config.properties");
                try {
                    prop.load(in);
                    in.close();
                }
                catch(IOException e) {
                    e.getStackTrace();
                }
                        
                // connect to datbase
                hst = prop.getProperty("host");
                usr = prop.getProperty("user");
                pwd = prop.getProperty("password");
                dab = "gcalvert_DB";
                url = "jdbc:mysql://" + hst + "/" + dab;
    
                // adding to return array
                result[0] = hst;
                result[1] = usr;
                result[2] = pwd;
                result[3] = dab;
                result[4] = url;  
            }
            catch(FileNotFoundException ex) {
                ex.printStackTrace();
            }
            return result;
    }

}
