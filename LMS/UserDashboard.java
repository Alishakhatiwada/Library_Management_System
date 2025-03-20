package LMS;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserDashboard extends Application {
    private Stage primaryStage;
    private int userId;

    public UserDashboard(int userId) {
        this.userId = userId;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("User Dashboard");

        BorderPane mainLayout = new BorderPane();
        mainLayout.setLeft(createSidebar());
        mainLayout.setCenter(createContent());
        mainLayout.setStyle("-fx-background-color: #F4E1C1; -fx-padding: 20;");

        Scene scene = new Scene(mainLayout, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(15));
        sidebar.setStyle("-fx-background-color: #A67B5B; -fx-padding: 15; -fx-border-radius: 10;");

        Button btnDashboard = createStyledButton("Dashboard", () -> new UserDashboard(userId).start(primaryStage));
        Button btnSearchBook = createStyledButton("Search Book", () -> new SearchBook().start(primaryStage));
        Button btnBorrowBook = createStyledButton("Borrow Book", () -> new BorrowBook(userId).start(primaryStage));
        Button btnViewBorrowHistory = createStyledButton("View Borrow History", () -> new BorrowHistory(userId).start(primaryStage));
        Button btnReturnBook = createStyledButton("Return Book", () -> new ReturnBook().start(primaryStage));
        Button btnLogout = createStyledButton("Logout", () -> new Login().start(primaryStage));

        sidebar.getChildren().addAll(btnDashboard, btnSearchBook, btnBorrowBook, btnViewBorrowHistory, btnReturnBook, btnLogout);
        return sidebar; 
    }

    private Label createContent() {
        Label welcomeLabel = new Label("Welcome to User Dashboard");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #5C3D2E; -fx-font-weight: bold; -fx-padding: 20;");
        return welcomeLabel;
    }

    @SuppressWarnings("unused")
	private Button createStyledButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #D4A76A; -fx-text-fill: black; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 5; -fx-padding: 8 20;");
        button.setOnAction(e -> action.run());
        return button;
    }
}
