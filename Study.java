import javax.swing.*;
import java.awt.event.*;

public class Study {
    JFrame frame;
    String setName;
    String userName;
    public Study(String setName, String userName, JFrame frame) {
        this.userName = userName;
        this.setName = setName;
        this.frame = frame;
    }
    public void StartStudying() {
        // JPanel flashcard = new JPanel();
        System.out.println(this.userName);
        System.out.println(this.setName);
    }
}
