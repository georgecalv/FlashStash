// import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;
// import java.awt.Image;
// import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
// import java.sql.Statement;
import java.util.Properties;
import java.sql.PreparedStatement;
// import java.awt.Container;
// import java.awt.*;

public class FlashStash {
    JFrame frame;
    Connection cn;
    JPanel login;

    // TODO: admin stats object, how everything looks(BRANCH), fix filter for saved browsing

    public FlashStash() {
        this.frame = new JFrame("FlashStash");
        try {
            this.cn = getConnection();
        }
        catch(SQLException e) {
            System.out.println(e.getStackTrace());
        }

        // // frame settings
        // this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // // this.frame.setLayout(new Boxlayout());
        // this.frame.setSize(600,360);
        // this.frame.setLocationRelativeTo(null);
        // this.frame.setResizable(false);
        // this.frame.setVisible(true); 
    }
    // log into FlashStash
    public void LogIn() {
        login  = new JPanel();
        login.setAlignmentY(SwingConstants.CENTER);
        this.frame.add(login);
        Box input = Box.createVerticalBox();
        // JLabel userLabel = new JLabel("Username: ");
        // JLabel passLabel = new JLabel("Password: ");
        JFormattedTextField user = new JFormattedTextField();
        user.setEditable(true);
        JPasswordField pass = new JPasswordField();
        pass.setEditable(true);
        input.add(user);
        input.add(pass);
        login.add(input);

        // insert button
        JButton go = new JButton("Log in");
        go.addActionListener(new ActionListener() {
            // check user info user info
            public void actionPerformed(ActionEvent e) {
                // is a user
                if(checkUsername(user.getText(), pass.getPassword())) {
                    login.removeAll();
                    login.revalidate();
                    login.repaint();
                    if(user.getText().equals("admin") && pass.getPassword()[0] == 'a') {
                        adminUserHome adminHomePg = new adminUserHome(frame, user.getText());
                        adminHomePg.display();
                        frame.remove(login);
                        frame.repaint();
                        frame.revalidate();

                    }
                    else {
                        userHome homePg = new userHome(frame, user.getText());
                        homePg.Display();
                        frame.remove(login);
                        frame.repaint();
                        frame.revalidate();
                    }
                }
                // not a user
                else {
                    // retry logging in
                    JLabel retry = new JLabel("Invalid Login please try again");
                    user.setText("");
                    pass.setText("");
                    login.add(retry);
                    login.revalidate();
                    login.repaint();
                    frame.revalidate();
                    frame.repaint();
                }
            }
        });
        JButton newUser = new JButton("Create New User");
        newUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login.removeAll();
                login.revalidate();
                login.repaint();
                CreateUser user = new CreateUser();
                frame.dispose();
                user.display(); 
                frame.remove(login);
                frame.repaint();
                frame.revalidate();   
            }
        });
        input.add(go);
        input.add(newUser);
        input.setAlignmentX(SwingConstants.CENTER);
        login.revalidate();
        login.repaint();

        // frame settings
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // this.frame.setLayout(new Boxlayout());
        this.frame.setSize(600,360);
        this.frame.setLocationRelativeTo(null);
        this.frame.setResizable(false);
        this.frame.setVisible(true); 

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
    private boolean checkUsername(String username, char[] password) {
        try {
            String q = "SELECT username FROM User WHERE username = ?";
            PreparedStatement st = cn.prepareStatement(q);
            st.setString(1, username);
            // check if user is a real user
            ResultSet rs = st.executeQuery();
            // is a user
            if (rs.next()) {
                String tmp = "";
                for(int i = 0; i < password.length; i++) {
                    tmp += password[i];
                }
                rs.close();
                st.close();
                q = "SELECT pass FROM User WHERE username = ?";
                st = cn.prepareStatement(q);
                st.setString(1, username);
                rs = st.executeQuery();
                if(rs.next()) {
                    String actual = rs.getString("pass");
                    if(actual.equals(tmp)) {
                        return true;
                    }
                }
                rs.close();
                st.close();
                this.cn.close();
                return false;
            }
            // is ot a user
            else{
                return false;
            }
        }
        catch(SQLException e) {
            e.getStackTrace();
            System.out.println("Prepared Statement failed");
            return false;
        }
    }
    public boolean checkUsername(String username) throws SQLException {
        String q = "SELECT username FROM User WHERE username = ?";
        PreparedStatement st = cn.prepareStatement(q);
        st.setString(1, username);
        // check if user is a real user
        ResultSet rs = st.executeQuery();
        // is a user
        if (rs.next() || username.equals("")) {
            return true;
        }
        return false;
    }
    public boolean checkPassword(char[] password) {
        String check = "";
        for(int i = 0; i < password.length; i++) {
            check += password[i];
        }
        if(check.equals("")) {
            return true;
        }
        return false;
    }
}
