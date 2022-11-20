import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.*;
import java.awt.*;

public class CreateUser extends FlashStash{
    public CreateUser() {
        // do nothing
    }
    public void display() {
        // userFrame settings
        JFrame userFrame = new JFrame("Create User: ");
        JPanel panel = new JPanel(); 
        userFrame.setLayout(new FlowLayout());
        Box vBox = Box.createVerticalBox();

        Box username = Box.createHorizontalBox();
        JLabel userLabel = new JLabel("Username: ");
        JTextField userInput = new JTextField();
        username.add(userLabel);
        username.add(userInput);

        Box password = Box.createHorizontalBox();
        JLabel passLabel = new JLabel("Password: ");
        JPasswordField passInput = new JPasswordField();
        password.add(passLabel);
        password.add(passInput);

        Box Fname = Box.createHorizontalBox();
        JLabel fNameLabel = new JLabel("First Name: ");
        JTextField fNameInput = new JTextField();
        Fname.add(fNameLabel);
        Fname.add(fNameInput);

        Box Lname = Box.createHorizontalBox();
        JLabel lNameLabel = new JLabel("Last Name: ");
        JTextField lNameInput = new JTextField();
        Lname.add(lNameLabel);
        Lname.add(lNameInput);

        JButton create = new JButton("Create Account: ");
        create.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // check if username exists and all textfields meet requirements
                try {
                    if(checkUsername(userInput.getText())) {
                        JLabel fail = new JLabel("This username already exists Please try again");
                        vBox.add(fail);
                        userFrame.revalidate();
                        userFrame.repaint();
                    }
                    else {
                        String q = "INSERT INTO User Values(?,?,?,?)";
                        PreparedStatement ps = cn.prepareStatement(q);
                        ps.setString(1, userInput.getText());
                        String pass = "";
                        for(int i = 0; i < passInput.getPassword().length; i++) {
                            pass += passInput.getPassword()[i];
                        }
                        ps.setString(2, pass);
                        ps.setString(3, fNameInput.getText());
                        ps.setString(4, lNameInput.getText());
                        ps.execute();

                        panel.removeAll();
                        panel.revalidate();
                        panel.repaint();
                        userHome homePg = new userHome(userFrame, userInput.getText());
                        homePg.Display();
                        userFrame.remove(panel);
                        userFrame.repaint();
                        userFrame.revalidate();
                    }
                }
                catch(SQLException l) {
                    l.printStackTrace();
                }

            }
        });
        vBox.add(username);
        vBox.add(password);
        vBox.add(Fname);
        vBox.add(Lname);
        vBox.add(create);
        panel.add(vBox);
        userFrame.add(panel);
        // userFrame settings
        userFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // this.userFrame.setLayout(new Boxlayout());
        userFrame.setSize(600,360);
        userFrame.setLocationRelativeTo(null);
        userFrame.setResizable(false);
        userFrame.setVisible(true); 


    }
}
