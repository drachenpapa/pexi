package de.drachenpapa.pexi.utils;

import de.drachenpapa.pexi.model.Account;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class AccountConverterTest {

    ResultSet resultSet = mock(ResultSet.class);

    @Test
    void shouldConvertCorrectly() throws SQLException {
        // Given
        given(resultSet.next()).willReturn(true, true, false);
        given(resultSet.getInt("id")).willReturn(1, 2);
        given(resultSet.getString("name")).willReturn("Account 1", "Account 2");
        given(resultSet.getString("iban")).willReturn("DE123456789", "DE987654321");

        // When
        List<Account> accountList = AccountConverter.convert(resultSet);

        // Then
        assertThat("Account list should contain two accounts", accountList, hasSize(2));

        Account firstAccount = accountList.get(0);
        assertAll("First account was not properly converted",
                () -> assertThat("ID of first account should match", firstAccount.id(), is(1)),
                () -> assertThat("Name of first account should match", firstAccount.name(), is("Account 1")),
                () -> assertThat("IBAN of first account should match", firstAccount.iban(), is("DE123456789")));

        Account secondAccount = accountList.get(1);
        assertAll("Second account was not properly converted",
                () -> assertThat("ID of second account should match", secondAccount.id(), is(2)),
                () -> assertThat("Name of second account should match", secondAccount.name(), is("Account 2")),
                () -> assertThat("IBAN of second account should match", secondAccount.iban(), is("DE987654321")));
    }

    @Test
    void shouldHandleEmptyResultSet() throws SQLException {
        // Given
        given(resultSet.next()).willReturn(false);

        // When
        List<Account> accountList = AccountConverter.convert(resultSet);

        // Then
        assertThat("Account list should be empty when ResultSet is null", accountList, hasSize(0));
    }
}
