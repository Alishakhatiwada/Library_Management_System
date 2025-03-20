package LMS;

import LMS.GlobalVar;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class Login extends Application {
    private TextField userField;
    private PasswordField passField;
    private Label messageLabel;

    @SuppressWarnings("unused")
	@Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Library Management System - Login");

        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #F4E1C1; -fx-border-radius: 15; -fx-padding: 30;");

        Label titleLabel = new Label("Library Login");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #5C3D2E;");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        
        Label userLabel = new Label("Username:");
        userLabel.setStyle("-fx-text-fill: #5C3D2E; -fx-font-size: 14px;");
        userField = new TextField();
        userField.setStyle("-fx-background-color: #D4A76A; -fx-text-fill: black; -fx-border-color: #5C3D2E; -fx-border-radius: 5; -fx-padding: 5;");

        Label passLabel = new Label("Password:");
        passLabel.setStyle("-fx-text-fill: #5C3D2E; -fx-font-size: 14px;");
        passField = new PasswordField();
        passField.setStyle("-fx-background-color: #D4A76A; -fx-text-fill: black; -fx-border-color: #5C3D2E; -fx-border-radius: 5; -fx-padding: 5;");

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #A67B5B; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 5; -fx-padding: 8 20;");
        loginButton.setOnAction(e -> login(primaryStage));

        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        grid.add(userLabel, 0, 0);
        grid.add(userField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passField, 1, 1);
        
        root.getChildren().addAll(titleLabel, grid, loginButton, messageLabel);
        
        Scene scene = new Scene(root, 380, 280);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void login(Stage primaryStage) {
        String username = userField.getText();
        String password = passField.getText();

        if (username.equals("admin") && password.equals("admin")) {
            messageLabel.setText("Login Successful!");
            new LibrarianDashboard().start(primaryStage);
            return;
        }

        if (authenticateUser(username, password)) {
            messageLabel.setText("Login Successful!");
            new UserDashboard(1).start(primaryStage);
        } else {
            messageLabel.setText("Invalid Username or Password");
        }
    }

    public static boolean authenticateUser(String username, String password) {
        String query = "SELECT id FROM users WHERE username = ? and password = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                GlobalVar.id = userId;
                System.out.println(GlobalVar.id);
                return true;
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;  // Authentication failed
    }
}
