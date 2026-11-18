import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * A menu used for creating tuples in the database.
 */
public class CreateMenu extends JPanel {
    Database database;

    // all columns for the current table
    List<ColumnField> columnFields;

    Map<String, Database.Table> nameToTable;

    // GUI components
    JComboBox<String> tableComboBox;
    JButton createButton;

    /**
     * Constructions the creation menu.
     */
    public CreateMenu(Database database) {
        this.database = database;
        columnFields = new ArrayList<ColumnField>();

        // set style
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // all tables
        List<Database.Table> tables = database.getTables();
        // map to get a table from its name
        nameToTable = new HashMap<>();
        // all table names
        String[] tableOptions = new String[tables.size() + 1];
        tableOptions[0] = "Select Table";

        // populate the nameToTableMap and tableNames
        for (int i = 0; i < tables.size(); i++) {
            Database.Table table = tables.get(i);
            String name = table.getName();
            tableOptions[i + 1] = name;
            nameToTable.put(name, table);
        }

        // table selector
        tableComboBox = new JComboBox<>(tableOptions);
        add(tableComboBox);

        // button to submit creation
        createButton = new JButton("Create");

        // on selected table changed
        tableComboBox.addActionListener(e -> changeTable());

        // on create button pressed
        createButton.addActionListener(e -> create());
    }

    /**
     * Changes the menu for the newly selected table.
     */
    private void changeTable() {
        // remove all old column fields
        columnFields.clear();
        removeAll();
        add(tableComboBox);

        String tableName = (String) tableComboBox.getSelectedItem();
        // skip prompt selection
        if (tableName.equals("Select Table")) {
            revalidate();
            repaint();
            return;
        }

        // the newly selected table
        Database.Table table = nameToTable.get(tableName);

        // for each column
        for (Database.Column column : table.getColumns()) {
            // create a GUI field
            ColumnField columnField = new ColumnField(column);
            columnFields.add(columnField);

            // create the field's row
            JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER));
            row.add(columnField.label);
            row.add(columnField.inputField);
            add(row);
        }

        // update the panel
        add(createButton);
        revalidate();
        repaint();
    }

    /**
     * Takes the entered values and attempts to create the tuple.
     */
    private void create() {
        // get selected table
        String tableName = (String) tableComboBox.getSelectedItem();
        Database.Table table = nameToTable.get(tableName);

        // variable map
        HashMap<String, String> vars = new HashMap<String, String>();

        // populate variable map
        for (ColumnField columnField : columnFields) {
            vars.put(columnField.column.getName(), 
                     columnField.inputField.getText().trim());
        }

        // create the tuple
        database.createTuple(table, vars);
    }

    /**
     * The GUI components for a database column.
     */
    private class ColumnField {
        public Database.Column column;
        public JLabel label;
        public JTextField inputField;

        /**
         * Constructs a ColumnField.
         * @parm column the database column to create GUI components for
         */
        public ColumnField(Database.Column column) {
            this.column = column;

            // create GUI components
            label = new JLabel(
                column.getName() + " (" + column.getType() + ")");
            inputField = new JTextField(15);
        }
    }
}
