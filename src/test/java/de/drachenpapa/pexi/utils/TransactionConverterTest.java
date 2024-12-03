package de.drachenpapa.pexi.utils;

import de.drachenpapa.pexi.model.Transaction;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class TransactionConverterTest {

    ResultSet resultSet = mock(ResultSet.class);

    @Test
    public void shouldConvertCorrectly() throws SQLException {
        given(resultSet.next()).willReturn(true, false);
        given(resultSet.getInt("id")).willReturn(1);
        given(resultSet.getDate("date")).willReturn(Date.valueOf("2024-03-01"));
        given(resultSet.getDouble("amount")).willReturn(100.0);
        given(resultSet.getString("description")).willReturn("Salary");
        given(resultSet.getInt("account_id")).willReturn(1);
        given(resultSet.getInt("category_id")).willReturn(1);

        List<Transaction> transactions = TransactionConverter.convert(resultSet);

        assertThat("Transactions list should have one element", transactions, hasSize(1));

        Transaction transaction = transactions.get(0);
        assertAll("Transaction was not properly converted",
                () -> assertThat("ID should match", transaction.id(), is(1)),
                () -> assertThat("Date should match", transaction.date(), is(LocalDate.of(2024, 3, 1))),
                () -> assertThat("Amount should match", transaction.amount(), is(100.0)),
                () -> assertThat("Description should match", transaction.description(), is("Salary")),
                () -> assertThat("Account id should match", transaction.accountId(), is(1)),
                () -> assertThat("Category id should match", transaction.categoryId(), is(1)));
    }

    @Test
    public void shouldHandleEmptyResultSet() throws SQLException {
        given(resultSet.next()).willReturn(false);

        List<Transaction> transactions = TransactionConverter.convert(resultSet);

        assertThat("Transaction list should be empty", transactions, hasSize(0));
    }
}
