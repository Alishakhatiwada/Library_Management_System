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

public class ManageFine extends Application {
    private TextField txtUserID = new TextField();
    private TextField txtBookID = new TextField();
    private TextField txtFineAmount = new TextField();
    private Label lblMessage = new Label();

    @SuppressWarnings("unused")
	@Override
    public void start(Stage primaryStage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #F4E1C1;");

        Label lblManageFine = new Label("Manage Fine");
        lblManageFine.setFont(new Font("Arial", 24));
        lblManageFine.setTextFill(Color.web("#5C3D2E"));

        styleTextField(txtUserID, "Enter User ID");
        styleTextField(txtBookID, "Enter Book ID");
        styleTextField(txtFineAmount, "Enter Fine Amount");

        Button btnApplyFine = new Button("Apply Fine");
        Button btnBack = new Button("Back");
        styleButton(btnApplyFine);
        styleButton(btnBack);

        btnApplyFine.setOnAction(e -> applyFine());
        btnBack.setOnAction(e -> new LibrarianDashboard().start(primaryStage));

        lblMessage.setTextFill(Color.web("#5C3D2E"));
        
        layout.getChildren().addAll(lblManageFine, txtUserID, txtBookID, txtFineAmount, btnApplyFine, lblMessage, btnBack);

        Scene scene = new Scene(layout, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Manage Fine");
        primaryStage.show();
    }

    private void styleTextField(TextField textField, String promptText) {
        textField.setPromptText(promptText);
        textField.setStyle("-fx-background-color: #D4A76A; -fx-text-fill: black; -fx-font-size: 14px; -fx-padding: 10px; -fx-border-radius: 10px;");
    }

    private void styleButton(Button button) {
        button.setStyle("-fx-background-color: #A67B5B; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-border-radius: 10px;");
    }

    private void applyFine() {
        String userID = txtUserID.getText().trim();
        String bookID = txtBookID.getText().trim();
        String fineAmount = txtFineAmount.getText().trim();

        if (userID.isEmpty() || bookID.isEmpty() || fineAmount.isEmpty()) {
            lblMessage.setText("Please enter valid User ID, Book ID, and Fine Amount.");
            return;
        }

        try {
            double fine = Double.parseDouble(fineAmount);
            if (fine < 0) {
                lblMessage.setText("Fine amount must be positive.");
                return;
            }

            String query = "UPDATE Borrowing SET Fines = Fines + ? WHERE User_ID = ? AND Book_ID = ?";

            try (Connection conn = DatabaseConnection.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setDouble(1, fine);
                stmt.setInt(2, Integer.parseInt(userID));
                stmt.setInt(3, Integer.parseInt(bookID));

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    lblMessage.setText("Fine of $" + fine + " applied to User ID: " + userID + " for Book ID: " + bookID);
                    txtUserID.clear();
                    txtBookID.clear();
                    txtFineAmount.clear();
                } else {
                    lblMessage.setText("User ID or Book ID not found, or no borrowing records.");
                }
            }
        } catch (NumberFormatException e) {
            lblMessage.setText("Please enter a valid numeric fine amount.");
        } catch (SQLException e) {
            lblMessage.setText("Error applying fine. Please try again.");
            e.printStackTrace();
        }
    }
}