import javax.swing.*;
import java.awt.*;

/**
 * The main GUI application.
 */
public class App extends JFrame {
    private Database database;

    // menus
    CreateMenu createMenu;
    JPanel selectedMenu;
    JPanel menuPanel;

    /**
     * Constructs the main GUI.
     */
    public App(Database database) {
        this.database = database;
        // menus
        createMenu = new CreateMenu(database);
        selectedMenu = null;

        // set style
        setTitle("Main Application");
        setSize(800, 800);
        setLayout(new BorderLayout());

        // parent panel

        // select panel that contains menus
        JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // create menu
        JButton createMenuButton = new JButton("Create");
        createMenuButton.addActionListener(e -> changeMenu(createMenu));
        selectPanel.add(createMenuButton);

        // read menu
        JButton readMenuButton = new JButton("Read");
        readMenuButton.addActionListener(e -> changeMenu(null));
        selectPanel.add(readMenuButton);

        // update menu
        JButton updateMenuButton = new JButton("Update");
        updateMenuButton.addActionListener(e -> changeMenu(null));
        selectPanel.add(updateMenuButton);

        // delete menu
        JButton deleteMenuButton = new JButton("Delete");
        deleteMenuButton.addActionListener(e -> changeMenu(null));
        selectPanel.add(deleteMenuButton);

        // panel for the current menu
        menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // add panels
        add(selectPanel, BorderLayout.NORTH);
        add(menuPanel, BorderLayout.CENTER);

        // set options
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void changeMenu(JPanel menu) {
        if (selectedMenu != null) menuPanel.remove(selectedMenu);
        selectedMenu = menu;
        if (selectedMenu != null) menuPanel.add(selectedMenu);
        menuPanel.revalidate();
        menuPanel.repaint();
    }
}
