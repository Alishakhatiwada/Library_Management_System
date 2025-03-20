package LMS;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Abstract Base Class for Dashboard
abstract class Dashboard extends Application {
    protected Stage primaryStage;
    protected BorderPane mainLayout;
    
    public void initUI() {
        mainLayout = new BorderPane();
        mainLayout.setLeft(createSidebar());
        mainLayout.setCenter(createContent());
        mainLayout.setStyle("-fx-background-color: #F4E1C1; -fx-padding: 20;");
    }
    
    protected abstract VBox createSidebar();
    protected abstract Label createContent();
}

public class LibrarianDashboard extends Dashboard {
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Librarian Dashboard");
        
        initUI();
        Scene scene = new Scene(mainLayout, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    @Override
    protected VBox createSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(10));
        sidebar.setStyle("-fx-background-color: #A67B5B; -fx-padding: 15; -fx-border-radius: 10;");
        
        Button btnDashboard = createButton("Dashboard", () -> new LibrarianDashboard().start(primaryStage));
        Button btnUserRegistration = createButton("User Registration", () -> new UserRegistration().start(primaryStage));
        Button btnManageBooks = createButton("Manage Books", () -> new ManageBook().start(primaryStage));
        Button btnAddBook = createButton("Add Book", () -> new AddBook().start(primaryStage));
        Button btnUpdateBook = createButton("Update Book", () -> new UpdateBook().start(primaryStage));
        Button btnDeleteBook = createButton("Delete Book", () -> new DeleteBook().start(primaryStage));
        Button btnManageFine = createButton("Manage Fine", () -> new ManageFine().start(primaryStage));
        Button btnLogout = createButton("Logout", () -> new Login().start(primaryStage));
        
        sidebar.getChildren().addAll(
            btnDashboard, btnUserRegistration, btnManageBooks,
            btnAddBook, btnUpdateBook, btnDeleteBook,
            btnManageFine, btnLogout
        );
        
        return sidebar;
    }
    
    @Override
    protected Label createContent() {
        Label contentLabel = new Label("Welcome to Librarian Dashboard");
        contentLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #5C3D2E; -fx-padding: 20;");
        return contentLabel;
    }
    
    @SuppressWarnings("unused")
    private Button createButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #D4A76A; -fx-text-fill: black; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 5; -fx-padding: 8 20;");
        button.setOnAction(e -> action.run());
        return button;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
