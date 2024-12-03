package de.drachenpapa.pexi.utils;

import de.drachenpapa.pexi.model.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Converts a {@link ResultSet} into a list of {@link Account} objects.
 * <p>
 * This class provides functionality to map the result of a SQL query into a list of {@link Account}
 * objects. It extracts data from the {@link ResultSet}, where each row represents an account.
 * </p>
 */
public class AccountConverter {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String IBAN = "iban";
    private static final String START_BUDGET = "startBudget";
    private static final String BUDGET = "budget";


    /**
     * Converts a {@link ResultSet} into a list of {@link Account} objects.
     * <p>
     * This method reads each row of the provided {@link ResultSet} and creates a corresponding
     * {@link Account} object. It maps the columns in the result set to the {@link Account} fields.
     * </p>
     *
     * @param resultSet the {@link ResultSet} containing account data. It is expected to contain
     *                  columns for account ID, name, IBAN, starting budget, and current budget.
     * @return A list of {@link Account} objects created from the result set.
     *         If the result set is null or empty, an empty list will be returned.
     * @throws SQLException if a database access error occurs or the result set is not properly formed.
     */
    public static List<Account> convert(ResultSet resultSet) throws SQLException {
        List<Account> accountList = new ArrayList<>();
        if (resultSet == null) {
            return accountList;
        }

        try (resultSet) {
            while (resultSet.next()) {
                int id = resultSet.getInt(ID);
                String name = resultSet.getString(NAME);
                String iban = resultSet.getString(IBAN);
                double startBudget = resultSet.getDouble(START_BUDGET);
                double budget = resultSet.getDouble(BUDGET);

                Account account = new Account(id, name, iban, startBudget, budget);
                accountList.add(account);
            }
        }

        return accountList;
    }
}
