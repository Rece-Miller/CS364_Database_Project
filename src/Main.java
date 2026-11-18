import javax.swing.SwingUtilities;

public class Main {
    // development mode
    private static Boolean dev = false;

    public static void main(String[] args) {
        // parse run flags
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-dev")) dev = true;
        }

        SwingUtilities.invokeLater(() -> {
            Database database;
            if (dev) {
                // use .env file for login information
                database = new Database();
                if (database.connect()) {
                    System.out.println("Success");
                } else {
                    System.out.println("Fail");
                    return;
                }
            } else {
                // get login information from the user
                Login login = new Login();
                database = login.login();
                if (database == null) return;
            }

            // main gui application
            App app = new App(database);
        });
    }
}
