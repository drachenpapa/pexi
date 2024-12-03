package de.drachenpapa.pexi.database;

import de.drachenpapa.pexi.model.Transaction;
import de.drachenpapa.pexi.utils.TransactionConverter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for interacting with the database to manage {@link Transaction} records.
 * <p>
 * This class provides methods to create the transactions table, retrieve, insert, update, and remove
 * transaction data from the database.
 * </p>
 */
public class TransactionsDB {

    /**
     * Creates the transactions table in the database if it does not exist.
     * <p>
     * This table includes columns for the transaction ID, date, amount, description, account ID, and category ID.
     * It also defines foreign key relationships to the `accounts` and `categories` tables.
     * </p>
     */
    static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS transactions (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "date DATE NOT NULL, " +
                "amount DECIMAL(10, 2) NOT NULL, " +
                "description VARCHAR(255) NOT NULL, " +
                "account_id INT NOT NULL, " +
                "category_id INT NOT NULL, " +
                "FOREIGN KEY (account_id) REFERENCES accounts(id), " +
                "FOREIGN KEY (category_id) REFERENCES categories(id))";
        DatabaseManager.executeUpdate(sql);
    }

    /**
     * Retrieves all transactions from the database.
     * <p>
     * This method executes a SQL query to fetch all records from the `transactions` table and returns them
     * as a list of {@link Transaction} objects.
     * </p>
     *
     * @return A list of {@link Transaction} objects.
     */
    public static List<Transaction> get() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";

        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            transactions = TransactionConverter.convert(resultSet);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return transactions;
    }

    /**
     * Inserts a new transaction into the database.
     * <p>
     * This method creates a new transaction record in the `transactions` table with the specified values for date,
     * amount, description, account ID, and category ID.
     * </p>
     *
     * @param date The date of the transaction.
     * @param amount The amount of the transaction.
     * @param description The description of the transaction.
     * @param account_id The ID of the associated account.
     * @param category_id The ID of the associated category.
     */
    public static void insert(String date, String amount, String description, String account_id, String category_id) {
        String sql = "INSERT INTO transactions (date, amount, description, account_id, category_id) VALUES (?, ?, ?, ?, ?)";
        DatabaseManager.executeUpdate(sql, date, amount, description, account_id, category_id);
    }

    /**
     * Updates an existing transaction in the database.
     * <p>
     * This method updates the record of an existing transaction identified by its ID with the new values for date,
     * amount, description, account ID, and category ID.
     * </p>
     *
     * @param date The new date of the transaction.
     * @param amount The new amount of the transaction.
     * @param description The new description of the transaction.
     * @param account_id The new account ID associated with the transaction.
     * @param category_id The new category ID associated with the transaction.
     */
    public static void update(String date, String amount, String description, String account_id, String category_id) {
        String sql = "UPDATE transactions SET date = ?, amount = ?, description = ?, account_id = ?, category_id = ? WHERE id = ?";
        DatabaseManager.executeUpdate(sql, date, amount, description, account_id, category_id);
    }

    /**
     * Removes a transaction from the database.
     * <p>
     * This method deletes the transaction record identified by the given ID from the `transactions` table.
     * </p>
     *
     * @param id The ID of the transaction to remove.
     */
    public static void remove(String id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        DatabaseManager.executeUpdate(sql, id);
    }
}
