package de.drachenpapa.pexi.database;

import java.sql.*;

public class DatabaseManager {

    private static final String JDBC_URL = "jdbc:h2:./data/pexi";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "nimda";

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

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

    private static void createTables() {
        AccountsDB.createTable();
        CategoriesDB.createTable();
        TransactionsDB.createTable();
    }

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

    public static void preloadExampleData() {
        if (isTableEmpty("transactions")) {
            insertExampleData();
        }
    }

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


    public static void startUp() {
        createTables();
        preloadExampleData();
    }
}
