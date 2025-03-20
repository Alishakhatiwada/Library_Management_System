package LMS;

import LMS.GlobalVar;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@SuppressWarnings("unused")
public class BorrowBook extends Application {
    private TextField txtBookID = new TextField();
    private DatePicker dpBorrowDate = new DatePicker();
    private Label lblMessage = new Label();
    private int userId; 

    // Constructor to pass userId
    public BorrowBook(int userId) {
        this.userId = userId;
    }

    @Override
    public void start(Stage primaryStage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(25));
        layout.setStyle("-fx-background-color: #F4E1C1; -fx-border-radius: 10; -fx-padding: 20;");

        Label lblBookID = new Label("Book ID:");
        lblBookID.setStyle("-fx-font-size: 16px; -fx-text-fill: #5C3D2E; -fx-font-weight: bold;");
        
        Label lblBorrowDate = new Label("Borrow Date:");
        lblBorrowDate.setStyle("-fx-font-size: 16px; -fx-text-fill: #5C3D2E; -fx-font-weight: bold;");

        txtBookID.setPromptText("Enter Book ID");
        txtBookID.setStyle("-fx-background-color: #D4A76A; -fx-text-fill: black; -fx-border-radius: 8; -fx-padding: 8; -fx-font-size: 14px;");
        
        dpBorrowDate.setStyle("-fx-background-color: #D4A76A; -fx-text-fill: black; -fx-border-radius: 8; -fx-padding: 8; -fx-font-size: 14px;");

        Button btnSubmit = new Button("Borrow Book");
        btnSubmit.setStyle("-fx-background-color: #A67B5B; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 10; -fx-padding: 10 25; -fx-font-size: 14px;");
        btnSubmit.setOnAction(e -> borrowBook());

        // Back button to return to previous screen
        Button btnBack = new Button("Back");
        btnBack.setStyle("-fx-background-color: #A67B5B; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 10; -fx-padding: 10 25; -fx-font-size: 14px;");
        btnBack.setOnAction(e -> new UserDashboard(userId).start(primaryStage));
        
        lblMessage.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-font-weight: bold;");

        layout.getChildren().addAll(lblBookID, txtBookID, lblBorrowDate, dpBorrowDate, btnSubmit, btnBack, lblMessage);

        Scene scene = new Scene(layout, 420, 320);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Borrow Book");
        primaryStage.show();
    }

    private void borrowBook() {
        String bookID = txtBookID.getText();
        LocalDate borrowDate = dpBorrowDate.getValue();

        if (bookID.isEmpty() || borrowDate == null) {
            lblMessage.setText("Please fill all fields.");
            return;
        }

        String checkUserQuery = "SELECT id FROM users WHERE id = ?";
        String checkBookQuery = "SELECT id FROM books WHERE id = ?";
        String insertBorrowingQuery = "INSERT INTO Borrowing (Book_ID, User_ID, Borrow_Date) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {

            // Check if the user exists
            try (PreparedStatement checkUserStmt = conn.prepareStatement(checkUserQuery)) {
                checkUserStmt.setInt(1, userId);
                ResultSet userRs = checkUserStmt.executeQuery();

                if (!userRs.next()) {
                    lblMessage.setText("User ID does not exist.");
                    return;
                }

                // Check if the book exists
                try (PreparedStatement checkBookStmt = conn.prepareStatement(checkBookQuery)) {
                    checkBookStmt.setInt(1, Integer.parseInt(bookID));
                    ResultSet bookRs = checkBookStmt.executeQuery();

                    if (bookRs.next()) {
                        // Insert the borrowing record
                        try (PreparedStatement stmt = conn.prepareStatement(insertBorrowingQuery)) {
                            stmt.setInt(1, Integer.parseInt(bookID));
                            stmt.setInt(2, GlobalVar.id);
                            stmt.setDate(3, java.sql.Date.valueOf(borrowDate));

                            int rowsInserted = stmt.executeUpdate();
                            if (rowsInserted > 0) {
                                lblMessage.setText("Book borrowed successfully!");
                            }
                        }
                    } else {
                        lblMessage.setText("Book does not exist.");
                    }
                }
            }
        } catch (SQLException e) {
            lblMessage.setText("Error borrowing book.");
            e.printStackTrace();
        }
    }
}