import java.sql.*;

public class DBHelper {
    private static final String DB_URL = "jdbc:ucanaccess://G:/users.accdb";
    private Connection con;

    public DBHelper() {
        try {
            con = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog(null, "Database connection failed!");
        }
    }

    public boolean validateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            return rs.next(); // if user exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registerUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, username);
            pst.setString(2, password);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, "Username already exists!");
            return false;
        }
    }
}
