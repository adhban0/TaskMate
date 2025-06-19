import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RegisterGUI extends JFrame{
        JLabel userLabel, passLabel;
        JTextField userField;
        JPasswordField passField;
        JButton registerBtn;
        JLabel registerLabel;
        DBHelper db;
        public RegisterGUI(){
            db = new DBHelper();
            setTitle("TaskMate Registration");
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
            registerBtn = new JButton("Register");
            registerBtn.setBounds(100, 80, 100, 25);
            registerBtn.addActionListener(e -> {
                String username = userField.getText();
                String hashedPassword = PasswordUtil.hashPassword(passField.getText().toString());
                if (db.registerUser(username, hashedPassword)) {
                    JOptionPane.showMessageDialog(this, "User registered successfully!");
                    new TaskManagerGUI(username).setVisible(true);
                    dispose();
                }
            });
            add(registerBtn);
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    int option = JOptionPane.showConfirmDialog(RegisterGUI.this, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);

                    if (option == JOptionPane.NO_OPTION || option == JOptionPane.CLOSED_OPTION) {
                        return; // Do nothing, user canceled
                    }

                    System.exit(0); // Close the app manually
                }
            });
            getRootPane().setDefaultButton(registerBtn);
        }

    }
