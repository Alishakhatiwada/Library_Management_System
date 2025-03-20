package LMS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Abstract class for book operations
abstract class BookOperation {
    protected Connection conn;

    public BookOperation() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    public abstract void execute(String bookID);
}

class BorrowBookOperation extends BookOperation {
    @Override
    public void execute(String bookID) {
        if (bookID.isEmpty()) {
            System.out.println("Please enter a valid Book ID.");
            return;
        }

        if (!UserSession.isActive()) {
            System.out.println("Error: No active user session. Please log in.");
            return;
        }

        String checkQuery = "SELECT title FROM books WHERE id = ? AND available = 1";
        String borrowQuery = "INSERT INTO borrow_history (book_id, user_id, borrow_date, book_title) VALUES (?, ?, NOW(), ?)";
        String updateQuery = "UPDATE books SET available = 0 WHERE id = ?";

        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setString(1, bookID);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                String bookTitle = rs.getString("title");
                int userID = UserSession.getInstance().getUserID();

                try (PreparedStatement borrowStmt = conn.prepareStatement(borrowQuery);
                     PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {

                    borrowStmt.setString(1, bookID);
                    borrowStmt.setInt(2, userID);
                    borrowStmt.setString(3, bookTitle);
                    borrowStmt.executeUpdate();

                    updateStmt.setString(1, bookID);
                    updateStmt.executeUpdate();

                    System.out.println("Book borrowed successfully!");
                }
            } else {
                System.out.println("Book not available or does not exist.");
            }

        } catch (SQLException e) {
            System.out.println("An error occurred. Please try again.");
            e.printStackTrace();
        }
    }
}
