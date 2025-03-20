package LMS;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.sql.*;

public class BorrowHistory extends Application {
    private ListView<String> historyListView = new ListView<>(); 
    private Label lblMessage = new Label();
    private int userId;

    public BorrowHistory(int userId) {
        this.userId = userId;
    }

    @SuppressWarnings("unused")
	@Override
    public void start(Stage primaryStage) {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #F4E1C1;");

        Label lblBorrowHistory = new Label("Borrow History");
        lblBorrowHistory.setStyle("-fx-font-size: 22px; -fx-text-fill: #5C3D2E; -fx-font-weight: bold;");

        historyListView.setPrefHeight(350);
        historyListView.setStyle("-fx-background-color: #D4A76A; -fx-border-color: #5C3D2E; -fx-border-radius: 10px;");
        
        lblMessage.setTextFill(Color.web("#5C3D2E"));
        lblMessage.setStyle("-fx-font-size: 14px;");
        
        Button btnRefresh = createStyledButton("Refresh List");
        Button btnBack = createStyledButton("Back");

        btnRefresh.setOnAction(e -> loadBorrowHistory());
        btnBack.setOnAction(e -> new UserDashboard(userId).start(primaryStage));
        
        HBox buttonBox = new HBox(15, btnRefresh, btnBack);
        buttonBox.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(lblBorrowHistory, historyListView, lblMessage, buttonBox);

        Scene scene = new Scene(layout, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Borrow History");
        primaryStage.show();

        loadBorrowHistory();
    }

    private void loadBorrowHistory() {
        historyListView.getItems().clear();
        String query = "SELECT Book_ID, Borrow_Date FROM Borrowing WHERE User_ID = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String record = rs.getString("Book_ID") + " - Borrowed on: " + rs.getString("Borrow_Date");
                historyListView.getItems().add(record);
            }
            lblMessage.setText("History loaded successfully.");
        } catch (SQLException e) {
            lblMessage.setText("Error loading history.");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
	private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #A67B5B; -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-border-radius: 10px; -fx-padding: 12 24; -fx-font-size: 14px; -fx-cursor: hand;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #8D8D6E; -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-border-radius: 10px; -fx-padding: 12 24; -fx-font-size: 14px; -fx-cursor: hand;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #A67B5B; -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-border-radius: 10px; -fx-padding: 12 24; -fx-font-size: 14px; -fx-cursor: hand;"));
        return button;
    }
}