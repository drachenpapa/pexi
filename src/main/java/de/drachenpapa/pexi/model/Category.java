package de.drachenpapa.pexi.model;

/**
 * Represents a category record in the database.
 */
public record Category(
        /**
         * The unique identifier of the category.
         */
        int id,
        /**
         * The name of the category.
         */
        String name,
        /**
         * The top-level category to which this category belongs.
         */
        String category,
        /**
         * The type of category (income or expense).
         */
        CategoryType categoryType
) {
}
