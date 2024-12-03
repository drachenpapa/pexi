package de.drachenpapa.pexi.utils;

import de.drachenpapa.pexi.model.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for converting a {@link ResultSet} to a list of {@link Transaction} objects.
 * <p>
 * This class provides a method to map the result of a SQL query containing transaction data
 * into a list of {@link Transaction} objects.
 * </p>
 */
public class TransactionConverter {

    private static final String ID = "id";
    private static final String DATE = "date";
    private static final String AMOUNT = "amount";
    private static final String DESCRIPTION = "description";
    private static final String ACCOUNT_ID = "account_id";
    private static final String CATEGORY_ID = "category_id";

    /**
     * Converts a {@link ResultSet} containing transaction data into a list of {@link Transaction} objects.
     * <p>
     * This method reads the columns from the {@link ResultSet} and constructs {@link Transaction} objects
     * for each row in the result set. The columns are expected to be named "id", "date", "amount",
     * "description", "account_id", and "category_id".
     * </p>
     *
     * @param resultSet The {@link ResultSet} containing transaction data.
     * @return A list of {@link Transaction} objects created from the result set.
     * @throws SQLException If an SQL exception occurs while reading the {@link ResultSet}.
     */
    public static List<Transaction> convert(ResultSet resultSet) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        if (resultSet == null) {
            return transactions;
        }

        try (resultSet) {
            while (resultSet.next()) {
                int id = resultSet.getInt(ID);
                LocalDate date = resultSet.getDate(DATE).toLocalDate();
                double amount = resultSet.getDouble(AMOUNT);
                String description = resultSet.getString(DESCRIPTION);
                int accountId = resultSet.getInt(ACCOUNT_ID);
                int categoryId = resultSet.getInt(CATEGORY_ID);

                Transaction transaction = new Transaction(id, date, amount, description, accountId, categoryId);
                transactions.add(transaction);
            }
        }
        return transactions;
    }
}
