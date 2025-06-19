import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
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
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(LoginGUI.this, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);

                if (option == JOptionPane.NO_OPTION|| option == JOptionPane.CLOSED_OPTION) {
                    return; // Do nothing, user canceled
                }

                System.exit(0); // Close the app manually
            }
        });
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
