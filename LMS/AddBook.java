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

public class AddBook extends Application {
    private String bookTitle;
    private String bookAuthor;
    private String isbn;
    private boolean available;

    private TextField txtBookTitle = new TextField();
    private TextField txtBookAuthor = new TextField();
    private TextField txtISBN = new TextField();
    private CheckBox chkAvailable = new CheckBox("Available"); // Checkbox for availability
    private Label lblMessage = new Label();

    @SuppressWarnings("unused")
    @Override
    public void start(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #F4E1C1;");

        Label lblAddBook = new Label("Add Book");
        lblAddBook.setStyle("-fx-font-size: 20px; -fx-text-fill: #5C3D2E; -fx-font-weight: bold;");

        txtBookTitle.setPromptText("Enter Book Title");
        txtBookTitle.setStyle("-fx-background-color: #D4A76A; -fx-text-fill: black; -fx-border-radius: 5;");
        
        txtBookAuthor.setPromptText("Enter Author");
        txtBookAuthor.setStyle("-fx-background-color: #D4A76A; -fx-text-fill: black; -fx-border-radius: 5;");
        
        txtISBN.setPromptText("Enter ISBN");
        txtISBN.setStyle("-fx-background-color: #D4A76A; -fx-text-fill: black; -fx-border-radius: 5;");
        
        chkAvailable.setStyle("-fx-text-fill: #5C3D2E;");
        
        Button btnAdd = new Button("Add Book");
        btnAdd.setStyle("-fx-background-color: #A67B5B; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5; -fx-padding: 8 20;");
        
        Button btnBack = new Button("Back");
        btnBack.setStyle("-fx-background-color: #A67B5B; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5; -fx-padding: 8 20;");
        
        btnAdd.setOnAction(e -> {
            setBookTitle(txtBookTitle.getText().trim());
            setBookAuthor(txtBookAuthor.getText().trim());
            setIsbn(txtISBN.getText().trim());
            setAvailable(chkAvailable.isSelected());

            addBook();
        });

        btnBack.setOnAction(e -> new LibrarianDashboard().start(primaryStage));
        
        lblMessage.setStyle("-fx-text-fill: red;");
        
        layout.getChildren().addAll(lblAddBook, txtBookTitle, txtBookAuthor, txtISBN, chkAvailable, btnAdd, lblMessage, btnBack);

        Scene scene = new Scene(layout, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Getter and Setter Methods
    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    private void addBook() {
        if (getBookTitle().isEmpty() || getBookAuthor().isEmpty() || getIsbn().isEmpty()) {
            lblMessage.setText("Please fill in all fields.");
            return;
        }

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            if (conn == null) {
                lblMessage.setText("Database connection failed.");
                System.out.println("Connection is null.");
                return;
            }

            // Check if book with the same ISBN already exists
            if (isBookExists(conn, getIsbn())) {
                lblMessage.setText("Book with this ISBN already exists.");
                return;
            }

            // Insert book into the database
            String insertQuery = "INSERT INTO books (title, author, isbn, available) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                stmt.setString(1, getBookTitle());
                stmt.setString(2, getBookAuthor());
                stmt.setString(3, getIsbn());
                stmt.setInt(4, isAvailable() ? 1 : 0);
                int rowsAffected = stmt.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);  
                if (rowsAffected > 0) {
                    lblMessage.setText("Book added successfully!");
                } else {
                    lblMessage.setText("Failed to add book.");
                }
            }
        } catch (SQLException e) {
            lblMessage.setText("Error adding book: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Check if book already exists based on ISBN
    private boolean isBookExists(Connection conn, String isbn) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM books WHERE isbn = ?";
        try (PreparedStatement stmt = conn.prepareStatement(checkQuery)) {
            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}
