package LMS;

import java.util.ArrayList;
import java.util.List;

public class ReturnBookSubject {
    private List<ReturnBookObserver> observers = new ArrayList<>();

    public void addObserver(ReturnBookObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ReturnBookObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String bookID) {
        for (ReturnBookObserver observer : observers) {
            observer.onBookReturned(bookID);
        }
    }
}
