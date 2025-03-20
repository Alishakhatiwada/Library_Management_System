package LMS;

import java.sql.Connection;

public class TestDB {
    public static void main(String[] args) {
        Connection conn = DatabaseConnection.getInstance().getConnection();
		if (conn != null) {
		    System.out.println("Database connected successfully!");
		}
    }
}

