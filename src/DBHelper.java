import java.sql.*;

public class DBHelper {
    private static final String DB_URL = "jdbc:ucanaccess://G:/users.accdb";
    private Connection con;

    public DBHelper() {
        try {
            con = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean validateUser(User currentUser) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, currentUser.getUsername());
            pst.setString(2, currentUser.getPassword());
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registerUser(User currentUser) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, currentUser.getUsername());
            pst.setString(2, currentUser.getPassword());
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
