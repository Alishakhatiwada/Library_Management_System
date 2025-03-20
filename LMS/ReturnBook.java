package LMS;

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

public class ReturnBook extends Application {
    private TextField txtReturnBookID = new TextField();
    private DatePicker dpReturnDate = new DatePicker();
    private Label lblMessage = new Label();
    private int userid;  

    private ReturnBookSubject returnSubject = new ReturnBookSubject();

    public ReturnBook() {
        returnSubject.addObserver(new ReturnNotification());
        returnSubject.addObserver(new ReturnLogger());
    }

    @Override
    public void start(Stage primaryStage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #F4E1C1; -fx-border-radius: 10; -fx-padding: 25;");

        Label lblReturnBook = new Label("Return Book");
        lblReturnBook.setStyle("-fx-font-size: 18px; -fx-text-fill: #5C3D2E; -fx-font-weight: bold;");

        txtReturnBookID.setPromptText("Enter Book ID");
        txtReturnBookID.setStyle("-fx-border-color: #A67B5B; -fx-padding: 8; -fx-font-size: 14px;");

        Label lblReturnDate = new Label("Select Return Date:");
        lblReturnDate.setStyle("-fx-font-size: 14px; -fx-text-fill: #5C3D2E;");

        dpReturnDate.setPromptText("Return Date");
        dpReturnDate.setStyle("-fx-border-color: #A67B5B; -fx-padding: 8;");

        lblMessage.setStyle("-fx-font-size: 14px; -fx-text-fill: red; -fx-font-weight: bold;");

        Button btnReturn = createStyledButton("Return Book", this::returnBook);
        Button btnBack = createStyledButton("Back", () -> new UserDashboard(userid).start(primaryStage));

        layout.getChildren().addAll(lblReturnBook, txtReturnBookID, lblReturnDate, dpReturnDate, btnReturn, lblMessage, btnBack);

        Scene scene = new Scene(layout, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @SuppressWarnings("unused")
	private Button createStyledButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #D4A76A; -fx-text-fill: black; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 5; -fx-padding: 10 20;");
        button.setOnAction(e -> action.run());
        return button;
    }

    private void returnBook() {
        String bookID = txtReturnBookID.getText().trim();
        LocalDate returnDate = dpReturnDate.getValue();

        if (bookID.isEmpty()) {
            lblMessage.setText("Please enter a valid Book ID.");
            return;
        }

        if (returnDate == null) {
            lblMessage.setText("Please select a return date.");
            return;
        }

        String checkQuery = "SELECT * FROM borrowing WHERE book_id = ? AND return_date IS NULL";
        String updateHistoryQuery = "UPDATE borrowing SET return_date = ? WHERE book_id = ? AND return_date IS NULL";
        String updateBookQuery = "UPDATE books SET available = 1 WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {

            checkStmt.setString(1, bookID);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) { 
                try (PreparedStatement updateHistoryStmt = conn.prepareStatement(updateHistoryQuery);
                     PreparedStatement updateBookStmt = conn.prepareStatement(updateBookQuery)) {

                    updateHistoryStmt.setDate(1, java.sql.Date.valueOf(returnDate));
                    updateHistoryStmt.setString(2, bookID);
                    updateHistoryStmt.executeUpdate();

                    updateBookStmt.setString(1, bookID);
                    updateBookStmt.executeUpdate();

                    lblMessage.setText("Book returned successfully!");
                    txtReturnBookID.clear();
                    dpReturnDate.setValue(null);  

                    returnSubject.notifyObservers(bookID); // Notify all observers
                }
            } else {
                lblMessage.setText("Book not found in borrowed records or already returned.");
            }

        } catch (SQLException e) {
            lblMessage.setText("Error returning book. Please try again.");
            e.printStackTrace();
        }
    }

    public void setUserId(int userid) {
        this.userid = userid;
    }
}
