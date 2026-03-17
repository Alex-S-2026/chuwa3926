/**
 * Thread-safe Singleton using Bill Pugh pattern (static inner class).
 */
public class DatabaseConnection {

    private DatabaseConnection() {
        System.out.println("Database connection created");
    }

    private static class Holder {
        private static final DatabaseConnection INSTANCE = new DatabaseConnection();
    }

    public static DatabaseConnection getInstance() {
        return Holder.INSTANCE;
    }

    public void executeQuery(String sql) {
        System.out.println("Executing: " + sql);
    }

    public static void main(String[] args) {
        DatabaseConnection conn1 = DatabaseConnection.getInstance();
        DatabaseConnection conn2 = DatabaseConnection.getInstance();

        System.out.println("Same instance? " + (conn1 == conn2));

        conn1.executeQuery("SELECT * FROM users");
    }
}
