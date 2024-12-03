package de.drachenpapa.pexi.utils;

import de.drachenpapa.pexi.model.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Converts a ResultSet to a list of {@link Account}.
 */
public class AccountConverter {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String IBAN = "iban";
    private static final String START_BUDGET = "startBudget";
    private static final String BUDGET = "budget";


    /**
     * Converts a ResultSet to a list of {@link Account}.
     *
     * @param resultSet The ResultSet containing account data.
     * @return A list of {@link Account}.
     * @throws SQLException If a database access error occurs.
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
