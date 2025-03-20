package LMS;

public class UserSession {
    private static volatile UserSession instance;
    private final int userID;  // Immutable userID

    private UserSession(int userID) {
        this.userID = userID;
    }

    // Thread-safe singleton creation using double-checked locking
    public static UserSession getInstance(int userID) {
        if (instance == null) { 
            synchronized (UserSession.class) {
                if (instance == null) { 
                    instance = new UserSession(userID);
                }
            }
        }
        return instance;
    }

    public static UserSession getInstance() {
        return instance;
    }

    public int getUserID() {
        return (instance != null) ? instance.userID : -1;
    }

    public static boolean isActive() {
        return instance != null;
    }

    public static void clearSession() {
        instance = null;
    }
}
