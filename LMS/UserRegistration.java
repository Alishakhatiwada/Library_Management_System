package LMS;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserRegistration extends Application {
    private String name;
    private String email;
    private String password;

    @SuppressWarnings("unused")
    @Override
    public void start(Stage primaryStage) {
        VBox layout = new VBox(15); // Increased spacing for better alignment
        layout.setPadding(new Insets(25));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #F4E1C1; -fx-border-radius: 10; -fx-padding: 25;");

        Label lblTitle = new Label("User Registration");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #5C3D2E;");

        TextField txtName = new TextField();
        txtName.setPromptText("Enter Name");
        txtName.setStyle("-fx-background-color: #D4A76A; -fx-text-fill: black; -fx-border-radius: 8; -fx-padding: 10; -fx-font-size: 14px;");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Enter Email");
        txtEmail.setStyle("-fx-background-color: #D4A76A; -fx-text-fill: black; -fx-border-radius: 8; -fx-padding: 10; -fx-font-size: 14px;");

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Enter Password");
        txtPassword.setStyle("-fx-background-color: #D4A76A; -fx-text-fill: black; -fx-border-radius: 8; -fx-padding: 10; -fx-font-size: 14px;");

        Button btnRegister = new Button("Register");
        btnRegister.setStyle("-fx-background-color: #A67B5B; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 10; -fx-padding: 12 25; -fx-font-size: 14px;");

        Button btnBack = new Button("Back");
        btnBack.setStyle("-fx-background-color: #A67B5B; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 10; -fx-padding: 12 25; -fx-font-size: 14px;");

        btnBack.setOnAction(e -> new LibrarianDashboard().start(primaryStage));

        btnRegister.setOnAction(e -> {
            setName(txtName.getText());
            setEmail(txtEmail.getText());
            setPassword(txtPassword.getText());

            if (registerUser()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "User Registered Successfully!", ButtonType.OK);
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Registration Failed!", ButtonType.OK);
                alert.show();
            }
        });

        layout.getChildren().addAll(lblTitle, txtName, txtEmail, txtPassword, btnRegister, btnBack);

        Scene scene = new Scene(layout, 400, 400); // Adjusted size for better proportion
        primaryStage.setScene(scene);
        primaryStage.setTitle("User Registration");
        primaryStage.show();
    }

    // Getter and Setter Methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private boolean registerUser() {
        String query = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, 'user')";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, getName());
            stmt.setString(2, getEmail());
            stmt.setString(3, getPassword());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
