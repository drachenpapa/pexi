package de.drachenpapa.pexi.utils;

import de.drachenpapa.pexi.model.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Converts a ResultSet to a list of {@link Transaction}.
 */
public class TransactionConverter {

    private static final String ID = "id";
    private static final String DATE = "date";
    private static final String AMOUNT = "amount";
    private static final String DESCRIPTION = "description";
    private static final String ACCOUNT_ID = "account_id";
    private static final String CATEGORY_ID = "category_id";


    /**
     * Converts a ResultSet to a list of {@link Transaction}.
     *
     * @param resultSet The ResultSet containing transactions data.
     * @return A list of {@link Transaction}.
     * @throws SQLException If an SQL exception occurs.
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
