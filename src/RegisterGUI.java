import javax.swing.*;

public class RegisterGUI extends JFrame{
        JLabel userLabel, passLabel;
        JTextField userField;
        JPasswordField passField;
        JButton registerBtn;
        JLabel registerLabel;
        DBHelper db;
        public RegisterGUI(){
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
            registerBtn = new JButton("Register");
            registerBtn.setBounds(100, 80, 80, 25);
            registerBtn.addActionListener(e -> {
                String username = userField.getText();
                String password = new String(passField.getPassword());
                if (db.registerUser(username, password)) {
                    JOptionPane.showMessageDialog(this, "User registered successfully!");
                    new TaskManagerGUI(username).setVisible(true);
                    dispose();
                }
            });
            add(registerBtn);
        }

    }
