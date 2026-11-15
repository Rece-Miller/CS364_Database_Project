import javax.swing.*;
import java.awt.*;

public class App extends JFrame {
    public App() {
        setTitle("Main Application");
        setSize(800, 800);

        JLabel welcome = new JLabel("Welcome!", JLabel.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 64));
        add(welcome, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
