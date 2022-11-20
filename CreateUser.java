import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class CreateUser extends FlashStash{
    public CreateUser() {
        // do nothing
    }
    public void display() {
        // userFrame settings
        JFrame userFrame = new JFrame("Create User: ");
        userFrame.setLayout(new FlowLayout());
        Box vBox = Box.createVerticalBox();

        Box username = Box.createHorizontalBox();
        JLabel userLabel = new JLabel("Username: ");
        JTextField userInput = new JTextField();
        username.add(userLabel);
        username.add(userInput);

        Box password = Box.createHorizontalBox();
        JLabel passLabel = new JLabel("Password: ");
        JTextField passInput = new JTextField();
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
            }
        });
        vBox.add(username);
        vBox.add(password);
        vBox.add(Fname);
        vBox.add(Lname);
        vBox.add(create);
        userFrame.add(vBox);

        // userFrame settings
        userFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // this.userFrame.setLayout(new Boxlayout());
        userFrame.setSize(600,360);
        userFrame.setLocationRelativeTo(null);
        userFrame.setResizable(false);
        userFrame.setVisible(true); 


    }
}
