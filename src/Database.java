import java.sql.*;

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
    
    /**
     * Initializes the database information from .env file.
     */
    public Database() {
        url = Env.get("DB_URL");
        dbName = Env.get("DB_NAME");
        username = Env.get("DB_USERNAME");
        password = Env.get("DB_PASSWORD");
    }
    
    /**
     * Connects to the database.
     * @return whether or not the connection was successful
     */
    public boolean connect() {
        try {
            url = url + dbName + "?";
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.err.println("Error: Connection failed");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    /**
     * Disconnects from the databse.
     * @return whether or not the disconnect was successful
     */
    public boolean disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("Error: Disconnect failed");
            e.printStackTrace();
            return false;
        }
        return true;
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
     * TEMPLATE
     * Looks up an Object based on its primary key.
     * Assumes at most one tuple is returned.
     * @param key the ssn of the employee tuple you are interested in
     * @return    an employee object containing the data from the database,
     *            or null if no results were returned.
     * @throws SQLException
     */
    /*
    public Object objectLookup(String key) throws SQLException {
        String query = 
            """
            SELECT *
            FROM Table
            WHERE Key = ?
            """;
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, key);
        ResultSet results = stmt.executeQuery();
        Object o = null;

        // unpack the data into an object for use in Java
        if(results.next()) {
            // unpack in order the data came in
            String attr1 = results.getString("Attr1");
            String attr2 = results.getString("Attr2");
            // ...
            
            o = new Object(attr1, attr2, ...);
        }
        return o;
    }
    */
}
