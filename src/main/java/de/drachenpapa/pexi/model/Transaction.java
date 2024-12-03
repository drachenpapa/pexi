package de.drachenpapa.pexi.model;

import java.time.LocalDate;

/**
 * Represents a transaction in the database.
 */
public record Transaction(
        /**
         * The unique identifier of the transaction.
         */
        int id,
        /**
         * The date of the transaction.
         */
        LocalDate date,
        /**
         * The amount of the transaction.
         */
        double amount,
        /**
         * The description of the transaction.
         */
        String description,
        /**
         * The ID of the account associated with the transaction.
         */
        int accountId,
        /**
         * The ID of the category associated with the transaction.
         */
        int categoryId
) {
}
