package kalendario;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:sample.db");
    }
}