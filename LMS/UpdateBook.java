package LMS;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateBook extends Application {
    private TextField txtUpdateBookID = new TextField();
    private TextField txtUpdateBookTitle = new TextField();
    private Label lblMessage = new Label();

    @SuppressWarnings("unused")
	@Override
    public void start(Stage primaryStage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #F4E1C1;");

        Label lblUpdateBook = new Label("Update Book Details");
        lblUpdateBook.setFont(new Font("Arial", 24));
        lblUpdateBook.setTextFill(Color.web("#5C3D2E"));

        styleTextField(txtUpdateBookID, "Enter Book ID");
        styleTextField(txtUpdateBookTitle, "Enter New Title");

        Button btnUpdate = new Button("Update Book");
        Button btnBack = new Button("Back");
        styleButton(btnUpdate);
        styleButton(btnBack);

        btnUpdate.setOnAction(e -> updateBook());
        btnBack.setOnAction(e -> new LibrarianDashboard().start(primaryStage));

        lblMessage.setTextFill(Color.web("#5C3D2E"));
        
        layout.getChildren().addAll(lblUpdateBook, txtUpdateBookID, txtUpdateBookTitle, btnUpdate, lblMessage, btnBack);

        Scene scene = new Scene(layout, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Update Book");
        primaryStage.show();
    }

    private void styleTextField(TextField textField, String promptText) {
        textField.setPromptText(promptText);
        textField.setStyle("-fx-background-color: #D4A76A; -fx-text-fill: black; -fx-font-size: 14px; -fx-padding: 10px; -fx-border-radius: 10px;");
    }

    private void styleButton(Button button) {
        button.setStyle("-fx-background-color: #A67B5B; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-border-radius: 10px;");
    }

    private void updateBook() {
        String bookID = txtUpdateBookID.getText().trim();
        String newTitle = txtUpdateBookTitle.getText().trim();

        if (bookID.isEmpty() || newTitle.isEmpty()) {
            lblMessage.setText("Please enter both Book ID and New Title.");
            return;
        }

        String query = "UPDATE books SET title = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newTitle);
            stmt.setString(2, bookID);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                lblMessage.setText("Book updated successfully.");
                txtUpdateBookID.clear();
                txtUpdateBookTitle.clear();
            } else {
                lblMessage.setText("Book ID not found.");
            }
        } catch (SQLException e) {
            lblMessage.setText("Error updating book. Please try again.");
            e.printStackTrace();
        }
    }
}