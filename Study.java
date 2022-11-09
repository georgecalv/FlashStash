import javax.swing.*;
import java.awt.event.*;

public class Study {
    JFrame frame;
    String setName;
    String userName;
    Study(String setName, String userName, JFrame frame) {
        this.userName = userName;
        this.setName = setName;
        this.frame = frame;
    }
}
