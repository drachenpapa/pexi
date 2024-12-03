package de.drachenpapa.pexi.database;

import de.drachenpapa.pexi.model.Account;
import de.drachenpapa.pexi.utils.AccountConverter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static de.drachenpapa.pexi.database.DatabaseManager.executeUpdate;

/**
 * Utility class for interacting with the database to manage {@link Account} records.
 * <p>
 * This class provides methods to create the accounts table, retrieve, insert, update, and remove
 * account data from the database.
 * </p>
 */
public class AccountsDB {

    /**
     * Creates the accounts table in the database if it does not exist.
     * <p>
     * This table includes columns for the account ID, name, IBAN, starting budget, and current budget.
     * </p>
     */
    static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS accounts (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "iban VARCHAR(22) NOT NULL, " +
                "startBudget DECIMAL(10, 2) NOT NULL, " +
                "budget DECIMAL(10, 2) NOT NULL)";
        executeUpdate(sql);
    }

    /**
     * Retrieves all accounts from the database.
     * <p>
     * This method executes a SQL query to fetch all records from the `accounts` table and returns them
     * as a list of {@link Account} objects.
     * </p>
     *
     * @return A list of {@link Account} objects.
     */
    public static List<Account> get() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts";

        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            accounts = AccountConverter.convert(resultSet);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return accounts;
    }

    /**
     * Inserts a new account into the database.
     * <p>
     * This method creates a new account record in the `accounts` table with the specified values for name,
     * IBAN, starting budget, and current budget.
     * </p>
     *
     * @param name The name of the account.
     * @param iban The IBAN of the account.
     * @param startBudget The starting budget of the account.
     * @param budget The current budget of the account.
     */
    public static void insert(String name, String iban, double startBudget, double budget) {
        String sql = "INSERT INTO accounts (name, iban, startBudget, budget) VALUES (?, ?, ?, ?)";
        DatabaseManager.executeUpdate(sql, name, iban, startBudget, budget);
    }

    /**
     * Updates an existing account in the database.
     * <p>
     * This method updates the record of an existing account identified by its ID with the new values for name,
     * IBAN, starting budget, and current budget.
     * </p>
     *
     * @param id The ID of the account to update.
     * @param name The new name of the account.
     * @param iban The new IBAN of the account.
     * @param startBudget The new starting budget of the account.
     * @param budget The new current budget of the account.
     */
    public static void update(String id, String name, String iban, double startBudget, double budget) {
        String sql = "UPDATE accounts SET name = ?, iban = ?, startBudget = ?, budget = ? WHERE id = ?";
        DatabaseManager.executeUpdate(sql, name, iban, startBudget, budget, id);
    }

    /**
     * Removes an account from the database.
     * <p>
     * This method deletes the account record identified by the given ID from the `accounts` table.
     * </p>
     *
     * @param id The ID of the account to remove.
     */
    public static void remove(String id) {
        String sql = "DELETE FROM accounts WHERE id = ?";
        DatabaseManager.executeUpdate(sql, id);
    }
}
