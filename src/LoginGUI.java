import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginGUI extends JFrame implements ActionListener {
    JLabel userLabel, passLabel;
    JTextField userField;
    JPasswordField passField;
    JButton loginBtn;
    JLabel registerLabel;
    DBHelper db;
    public LoginGUI(){
        db = new DBHelper();
        setTitle("TaskMate Login");
        setSize(300,200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        userLabel = new JLabel("Username:");
        userLabel.setBounds(10,10,80,25);
        add(userLabel);
        userField = new JTextField();
        userField.setBounds(100, 10, 160, 25);
        add(userField);
        passLabel = new JLabel("Password:");
        passLabel.setBounds(10, 40, 80, 25);
        add(passLabel);
        passField = new JPasswordField();
        passField.setBounds(100, 40, 160, 25);
        add(passField);
        loginBtn = new JButton("Login");
        loginBtn.setBounds(100, 80, 80, 25);
        loginBtn.addActionListener(this);
        add(loginBtn);
        registerLabel = new JLabel("<HTML><U>Don't have an account? Register here</U></HTML>");
        registerLabel.setForeground(Color.BLUE);
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLabel.setBounds(40, 115, 220, 25); // adjust as needed
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose(); // close the login window
                new RegisterGUI().setVisible(true); // open registration window
            }
        });
        add(registerLabel);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String username = userField.getText();
        String password = passField.getText().toString();
        if (db.validateUser(username,password)){
            JOptionPane.showMessageDialog(this,"Login Successful!\nWelcome back "+userField.getText());
            new TaskManagerGUI(username).setVisible(true);
            dispose();
        }
        else{
            JOptionPane.showMessageDialog(this,"Wrong username or password. Try again","Wrong Credentials",JOptionPane.WARNING_MESSAGE);
        }
    }
}
