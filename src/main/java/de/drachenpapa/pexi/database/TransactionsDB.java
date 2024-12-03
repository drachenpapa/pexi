package de.drachenpapa.pexi.database;

import de.drachenpapa.pexi.model.Transaction;
import de.drachenpapa.pexi.utils.TransactionConverter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TransactionsDB {

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

    public static void insert(String date, String amount, String description, String account_id, String category_id) {
        String sql = "INSERT INTO transactions (date, amount, description, account_id, category_id) VALUES (?, ?, ?, ?, ?)";
        DatabaseManager.executeUpdate(sql, date, amount, description, account_id, category_id);
    }

    public static void update(String date, String amount, String description, String account_id, String category_id) {
        String sql = "UPDATE transactions SET date = ?, amount = ?, description = ?, account_id = ?, category_id = ? WHERE id = ?";
        DatabaseManager.executeUpdate(sql, date, amount, description, account_id, category_id);
    }

    public static void remove(String id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        DatabaseManager.executeUpdate(sql, id);
    }
}
