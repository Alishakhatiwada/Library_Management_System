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

public class ManageBook extends Application {
    private ListView<String> bookListView = new ListView<>();

    @SuppressWarnings("unused")
    @Override
    public void start(Stage primaryStage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(25));
        layout.setStyle("-fx-background-color: #F4E1C1; -fx-border-radius: 10; -fx-padding: 20;");

        Label lblManageBooks = new Label("Manage Books");
        applyLabelStyle(lblManageBooks);

        applyListViewStyle(bookListView);

        Button btnRefreshBooks = new Button("Refresh List");
        applyButtonStyle(btnRefreshBooks);
        btnRefreshBooks.setOnAction(e -> loadBooks());

        Button btnBack = new Button("Back");
        applyButtonStyle(btnBack);
        btnBack.setOnAction(e -> new LibrarianDashboard().start(primaryStage));

        layout.getChildren().addAll(lblManageBooks, bookListView, btnRefreshBooks, btnBack);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Manage Books");
        primaryStage.show();
        loadBooks();
    }

    private void loadBooks() {
        bookListView.getItems().clear();
        String query = "SELECT title, author FROM books";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Book book = BookFactory.createBook(rs); // Using Factory Pattern
                bookListView.getItems().add(book.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void applyLabelStyle(Label label) {
        label.setStyle("-fx-font-size: 18px; -fx-text-fill: #5C3D2E; -fx-font-weight: bold;");
    }

    private void applyListViewStyle(ListView<String> listView) {
        listView.setStyle("-fx-background-color: #D4A76A; -fx-border-radius: 8; -fx-padding: 8; -fx-font-size: 14px;");
        listView.setPrefHeight(250);
    }

    private void applyButtonStyle(Button button) {
        button.setStyle("-fx-background-color: #A67B5B; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 10; -fx-padding: 10 25; -fx-font-size: 14px;");
    }
}
