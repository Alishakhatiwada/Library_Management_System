package LMS;

public class ReturnNotification implements ReturnBookObserver {
    @Override
    public void onBookReturned(String bookID) {
        System.out.println("Notification: Book with ID " + bookID + " has been returned.");
    }
}
