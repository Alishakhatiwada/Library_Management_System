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
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchBook extends Application {
    private TextField txtSearchBook = new TextField();
    private ListView<String> searchResults = new ListView<>();
    private Label lblMessage = new Label();
    private int userid;

    @SuppressWarnings("unused")
	@Override
    public void start(Stage primaryStage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #F4E1C1;");

        Label lblSearchBook = new Label("Search Book");
        lblSearchBook.setFont(new Font("Arial", 24));
        lblSearchBook.setTextFill(Color.web("#5C3D2E"));

        styleTextField(txtSearchBook, "Enter Book Title or Author");

        Button btnSearch = new Button("Search");
        Button btnBack = new Button("Back");
        styleButton(btnSearch);
        styleButton(btnBack);

        btnSearch.setOnAction(e -> searchBook());
        btnBack.setOnAction(e -> new UserDashboard(userid).start(primaryStage));

        lblMessage.setTextFill(Color.web("#5C3D2E"));
        
        layout.getChildren().addAll(lblSearchBook, txtSearchBook, btnSearch, searchResults, lblMessage, btnBack);

        Scene scene = new Scene(layout, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Search Book");
        primaryStage.show();
    }

    private void styleTextField(TextField textField, String promptText) {
        textField.setPromptText(promptText);
        textField.setStyle("-fx-background-color: #D4A76A; -fx-text-fill: black; -fx-font-size: 14px; -fx-padding: 10px; -fx-border-radius: 10px;");
    }

    private void styleButton(Button button) {
        button.setStyle("-fx-background-color: #A67B5B; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-border-radius: 10px;");
    }

    private void searchBook() {
        String keyword = txtSearchBook.getText().trim();

        if (keyword.isEmpty()) {
            lblMessage.setText("Please enter a book title or author.");
            return;
        }

        String query = "SELECT title, author, available FROM books WHERE title LIKE ? OR author LIKE ?";
        searchResults.getItems().clear();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");

            ResultSet rs = stmt.executeQuery();

            boolean found = false;
            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                boolean available = rs.getBoolean("available");
                String status = available ? "Available" : "Not Available";
                
                searchResults.getItems().add(title + " by " + author + " - " + status);
                found = true;
            }

            lblMessage.setText(found ? "Search completed." : "No matching books found.");

        } catch (SQLException e) {
            lblMessage.setText("Error searching for books.");
            e.printStackTrace();
        }
    }

    public void setUserId(int userid) {
        this.userid = userid;
    }
}
