import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/*
 * Class that represents a database.
 */
public class Database {
    // server connection information
    private String url;
    private String dbName;
    private String username;
    private String password;

    // connection object for running queries
    private Connection connection;

    private List<Table> tables;

    /**
     * Initializes the database from given information.
     * @param url      The url of the database server
     * @param dbName   The name of the database on the server
     * @param username The username of the user on the server
     * @param password The password of the user on the server
     */
    public Database(String url, String dbName, String username, String password) {
        this.url = url;
        this.dbName = dbName;
        this.username = username;
        this.password = password;
        tables = null;
    }
    
    /**
     * Initializes the database information from .env file.
     */
    public Database() {
        this(Env.get("DB_URL"),
             Env.get("DB_NAME"),
             Env.get("DB_USERNAME"),
             Env.get("DB_PASSWORD"));
    }
 
    /**
     * Connects to the database.
     * @return whether or not the connection was successful
     */
    public boolean connect() {
        try {
            connection = DriverManager.getConnection(
                url + dbName + "?", username, password);
            return populateTables();
        } catch (SQLException e) {
            System.err.println("Error: Connection failed");
            return false;
        }
    }
    
    /**
     * Disconnects from the databse.
     * @return whether or not the disconnect was successful
     */
    public boolean disconnect() {
        try {
            connection.close();
            tables = null;
            return true;
        } catch (SQLException e) {
            System.err.println("Error: Disconnect failed");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets all tables and their attributes from the database.
     * Converts them into a Java represenation.
     */
    private Boolean populateTables() {
        try {
            tables = new ArrayList<Table>();

            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultTables = metaData.getTables(
                dbName, null, "%", new String[] {"TABLE"});
            while (resultTables.next()) {
                String tableName = resultTables.getString("TABLE_NAME");
                ArrayList<Column> tableColumns = new ArrayList<Column>();

                ResultSet resultColumns =
                    metaData.getColumns(dbName, null, tableName, "%");
                while (resultColumns.next()) {
                    tableColumns.add(new Column(
                        resultColumns.getString("COLUMN_NAME"),
                        resultColumns.getString("TYPE_NAME"),
                        resultColumns.getInt("COLUMN_SIZE")));
                }

                tables.add(new Table(tableName, tableColumns));
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error: Failed to get tables"); 
            tables = null;
            return false;
        }
    }

    /**
     * @return a list of all tables in the database
     */
    public List<Table> getTables() {
        if (tables == null) return null;
        return new ArrayList<Table>(tables);
    }

    /**
     * Runs a query with no parameters using prepared statements.
     * @param query the query to run
     * @return      the results set from the database
     * @throws SQLException
     */
    public ResultSet runQuery(String query) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet results = stmt.executeQuery();
        return results;
    }
    
    /**
     * Creates a tuple.
     * @param table the table to insert the tuple into 
     * @param vars  a map that maps column names to their values
     */
    public void createTuple(Table table, Map<String, String> vars) {
        // get column names
        List<Column> columns = table.getColumns();
        String[] columnNames = new String[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            columnNames[i] = columns.get(i).getName();
        }

        // build the SQL statement
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(table.getName()).append(" (");
        sql.append(String.join(", ", columnNames));
        sql.append(") VALUES (");
        for (int i = 0; i < columns.size(); i++) {
            sql.append("?");
            if (i < columns.size() - 1) sql.append(", ");
        }
        sql.append(")");

        try {
            PreparedStatement stmt =
                connection.prepareStatement(sql.toString());

            for (int i = 0; i < columns.size(); i++) {
                Database.Column column = columns.get(i);
                // value of the column as a string
                String stringValue = vars.get(column.getName());
                // query index (1 indexed)
                int index = i + 1;

                // Handle empty inputs (set as NULL if allowed, or skip)
                if (stringValue == null || stringValue.isEmpty()) {
                    stmt.setNull(index, java.sql.Types.NULL);
                    continue;
                }

                // set based on data type
                String type = column.getType().toUpperCase();
                if (type.contains("INT")) {
                    stmt.setInt(index, Integer.parseInt(stringValue));
                } else if (type.contains("DOUBLE") ||
                           type.contains("FLOAT") ||
                           type.contains("DECIMAL")) {
                    stmt.setDouble(index, Double.parseDouble(stringValue));
                } else {
                    stmt.setString(index, stringValue);
                }
            }
            // execute the insertion
            stmt.executeUpdate();
            System.out.println("Tuple created");
        } catch(Exception e) {
            System.err.println("Error: Creation failed");
            System.err.println("Msg: " + e.getMessage());
        }
    }

    /**
     * Represents a table in the database.
     */
    public class Table {
        // table name
        private String name;
        // all attributes in the table
        private List<Column> columns;

        /**
         * Constructs a Table.
         * @param name    the name of the table
         * @param columns a list of all columns in the table
         */
        public Table(String name, List<Column> columns) {
            this.name = name;
            this.columns = columns;
        }

        /** @return the name of the table */
        public String getName() { return name; }
        
        /** @return a list of all columns in the table */
        public List<Column> getColumns() {
            return new ArrayList<Column>(columns);
        }
    }

    /**
     * Represents an attribute in a database table.
     */
    public class Column {
        // name of the attribute
        private String name;
        // data type of the attribute
        private String type;
        // column size of the attribute
        private int size;

        /**
         * Constructs a Column.
         * @param name the name of the column
         * @param type the data type of the Column
         * @param size the size of the column
         */
        public Column(String name, String type, int size) {
            this.name = name;
            this.type = type;
            this.size = size;
        }

        /** @return the name of the column */
        public String getName() { return name; }

        /** @return the data type of the column */
        public String getType() { return type; }

        /** @return the size of the column */
        public int getSize() { return size; }
    }
}
