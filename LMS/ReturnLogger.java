package LMS;

public class ReturnLogger implements ReturnBookObserver {
    @Override
    public void onBookReturned(String bookID) {
        System.out.println("Log: Book ID " + bookID + " returned and recorded in history.");
    }
}
