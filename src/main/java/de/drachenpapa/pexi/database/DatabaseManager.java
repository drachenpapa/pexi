package de.drachenpapa.pexi.database;

import java.sql.*;

/**
 * Utility class for managing database connections and operations related to the database schema.
 * <p>
 * This class provides methods for establishing database connections, executing SQL updates, and managing tables.
 * It also handles preloading example data into the database for initialization purposes.
 * </p>
 */
public class DatabaseManager {

    private static final String JDBC_URL = "jdbc:h2:./data/pexi";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "nimda";

    /**
     * Establishes and returns a connection to the database.
     * <p>
     * This method uses the JDBC URL, username, and password to create and return a database connection.
     * </p>
     *
     * @return A {@link Connection} object representing the connection to the database.
     * @throws SQLException If an error occurs while attempting to establish the connection.
     */
    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    /**
     * Executes an update query (INSERT, UPDATE, DELETE) on the database.
     * <p>
     * This method prepares and executes an SQL statement with the provided parameters.
     * </p>
     *
     * @param sql The SQL query string to execute.
     * @param params The parameters to set in the SQL query.
     */
    static void executeUpdate(String sql, Object... params) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Creates the necessary tables in the database if they do not exist.
     * <p>
     * This method ensures that the required tables for accounts, categories, and transactions are created.
     * </p>
     */
    private static void createTables() {
        AccountsDB.createTable();
        CategoriesDB.createTable();
        TransactionsDB.createTable();
    }

    /**
     * Checks if the specified table is empty.
     * <p>
     * This method executes a SQL query to count the number of rows in the given table. It returns true if the
     * table is empty (i.e., the row count is 0), and false otherwise.
     * </p>
     *
     * @param tableName The name of the table to check.
     * @return true if the table is empty, false otherwise.
     */
    private static boolean isTableEmpty(String tableName) {
        String sql = "SELECT COUNT(*) AS count FROM " + tableName;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            resultSet.next();
            int rowCount = resultSet.getInt("count");
            return rowCount == 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Preloads example data into the database if the `transactions` table is empty.
     * <p>
     * This method checks if the `transactions` table is empty, and if so, it populates the database with
     * example data for accounts, categories, and transactions.
     * </p>
     */
    public static void preloadExampleData() {
        if (isTableEmpty("transactions")) {
            insertExampleData();
        }
    }

    /**
     * Inserts example data into the accounts, categories, and transactions tables.
     * <p>
     * This method adds predefined example entries into the `accounts`, `categories`, and `transactions` tables.
     * </p>
     */
    private static void insertExampleData() {
        String[] accountsData = {
                "('Main Account', 'DE12345678901234567890', '0', '0')"
        };

        for (String data : accountsData) {
            String sql = "INSERT INTO accounts (name, iban, startBudget, budget) VALUES " + data;
            executeUpdate(sql);
        }

        String[] categoriesData = {
                "('Income', 'Salary', true)",
                "('Expense', 'Groceries', false)"
        };

        for (String data : categoriesData) {
            String sql = "INSERT INTO categories (name, category, income) VALUES " + data;
            executeUpdate(sql);
        }

        String[] transactionsData = {
                "('2024-03-01', 100.0, 'Salary', 1, 1)",
                "('2024-03-02', 50.0, 'Bonus', 1, 1)",
                "('2024-03-03', -20.0, 'Groceries', 1, 2)"
        };

        for (String data : transactionsData) {
            String sql = "INSERT INTO transactions (date, amount, description, account_id, category_id) VALUES " + data;
            executeUpdate(sql);
        }
    }

    /**
     * Initializes the database by creating the necessary tables and preloading example data.
     * <p>
     * This method is typically called during the application startup to ensure that the database schema
     * is in place and contains sample data if needed.
     * </p>
     */
    public static void startUp() {
        createTables();
        preloadExampleData();
    }
}
