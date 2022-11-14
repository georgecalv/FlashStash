import javax.swing.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;
import java.util.*;

public class Study extends FlashStash{
    JFrame frame;
    String setCode;
    String userName;
    public Study(String setCode, String userName, JFrame frame) {
        this.userName = userName;
        this.setCode = setCode;
        this.frame = frame;
    }
    public void StartStudying() {
        // JPanel flashcard = new JPanel();
        try {
            String q = "SELECT question, answer FROM Content WHERE username = ? and study_set = ?";
            PreparedStatement st = super.cn.prepareStatement(q);
            st.setString(1, this.userName);
            st.setString(2, this.setCode);
            ResultSet rs = st.executeQuery();
            Vector<String> questions = new Vector<String>();
            Vector<String> answers = new Vector<String>();
            while(rs.next()) {
                questions.add(rs.getString("question"));
                answers.add(rs.getString("answer"));
            }
            for(int i = 0; i < answers.size(); i++) {
                System.out.println(questions.get(i));
                System.out.println(answers.get(i));
            }
        }
        catch(SQLException e) {
            System.out.println(e.getStackTrace());
        }


    }
}
