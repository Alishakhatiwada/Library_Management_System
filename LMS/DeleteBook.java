package LMS;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteBook extends Application {
    private TextField txtBookID = new TextField();
    private Label lblMessage = new Label();

    @Override
    public void start(Stage primaryStage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #F4E1C1; -fx-border-radius: 10; -fx-padding: 25;");

        Label lblDeleteBook = new Label("Delete Book");
        lblDeleteBook.setStyle("-fx-font-size: 18px; -fx-text-fill: #5C3D2E; -fx-font-weight: bold;");

        txtBookID.setPromptText("Enter Book ID to delete");
        txtBookID.setStyle("-fx-border-color: #A67B5B; -fx-padding: 8; -fx-font-size: 14px;");

        lblMessage.setStyle("-fx-font-size: 14px; -fx-text-fill: red; -fx-font-weight: bold;");

        Button btnDelete = createStyledButton("Delete Book", this::deleteBook);
        Button btnBack = createStyledButton("Back", () -> new LibrarianDashboard().start(primaryStage));

        layout.getChildren().addAll(lblDeleteBook, txtBookID, btnDelete, lblMessage, btnBack);

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

    private void deleteBook() {
        String bookID = txtBookID.getText();
        if (bookID.isEmpty()) {
            lblMessage.setText("Please enter a valid Book ID.");
            return;
        }

        String query = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(bookID));
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                lblMessage.setText("Book deleted successfully.");
                txtBookID.clear();
            } else {
                lblMessage.setText("Book ID not found.");
            }
        } catch (SQLException | NumberFormatException e) {
            lblMessage.setText("Error deleting book. Please try again.");
            e.printStackTrace();
        }
    }
}
