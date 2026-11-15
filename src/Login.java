import javax.swing.*;
import java.awt.*;

/*
 * Represents the login page.
 */
public class Login extends JDialog {
    // input fields
    private final JTextField userInput; 
    private final JPasswordField passInput;
    private final JButton loginButton;

    // database to return on success
    private Database database; 

    public Login() {
        super((Frame) null, "Login", true);

        // initialze fields
        userInput = new JTextField(15);
        passInput = new JPasswordField(15);
        loginButton = new JButton("Login");
        database = null;

        // create panel for layout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // row for the username field
        JPanel userRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        userRow.add(new JLabel("Username:"));
        userRow.add(userInput);

        // row for the password field
        JPanel passRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passRow.add(new JLabel("Password:"));
        passRow.add(passInput);

        // row for the login button
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonRow.add(loginButton);

        // add rows to the panel
        panel.add(userRow);
        panel.add(passRow);
        panel.add(buttonRow);

        // add the panel to the frame
        add(panel);

        // allow pressing enter
        getRootPane().setDefaultButton(loginButton);

        // attempt to login on button press
        loginButton.addActionListener(e -> attemptLogin());

        // set size based on components
        pack();

        // center the window on the screen
        setLocationRelativeTo(null);

        // dispose when closed
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * Attempts to login using the information in the input fields.
     */
    private void attemptLogin() {
        // database information
        final String DB_URL = "jdbc:mysql://138.49.184.206:3306/";
        final String DB_NAME = "miller5945_sletten3563_Project";

        // get user credentials
        String username = userInput.getText().trim();
        String password = new String(passInput.getPassword());

        // try connection
        database = new Database(DB_URL, DB_NAME, username, password);

        // check connection
        if (database.connect()) {
            // login successful, resume execution
            dispose();
        } else {
            // login unsuccessful, show a message 
            JOptionPane.showMessageDialog(this,
                "Invalid username or password.",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);

            // delete failed database and connection
            database = null;
        }
    }

    /**
     * Gets user information and connects to the database.
     * @return the connected database
     */
    public Database login() {
        // holds the execution here until disposed
        setVisible(true);

        return database;
    }
}
