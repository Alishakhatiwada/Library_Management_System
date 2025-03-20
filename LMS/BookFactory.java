package LMS;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookFactory {
    public static Book createBook(ResultSet rs) throws SQLException {
        return new Book(rs.getString("title"), rs.getString("author"));
    }
}
