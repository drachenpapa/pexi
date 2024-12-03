package de.drachenpapa.pexi.utils;

import de.drachenpapa.pexi.model.Category;
import de.drachenpapa.pexi.model.CategoryType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Converts a {@link ResultSet} into a list of {@link Category}.
 * <p>
 * This class provides functionality to map the result of a SQL query into a list of {@link Category}
 * objects. It extracts data from the {@link ResultSet}, where each row is used to create a new {@link Category}.
 * </p>
 */
public class CategoryConverter {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String CATEGORY = "category";
    private static final String INCOME = "income";

    /**
     * Converts a {@link ResultSet} into a list of {@link Category} objects.
     * <p>
     * This method reads each row of the provided {@link ResultSet} and creates a corresponding
     * {@link Category} object. It maps the columns in the result set to the {@link Category} fields.
     * </p>
     *
     * @param resultSet the {@link ResultSet} to convert. It is expected to contain columns for
     *                  category ID, name, type, and whether the category is for income or expense.
     * @return A list of {@link Category} objects created from the result set.
     *         If the result set is null or empty, an empty list will be returned.
     * @throws SQLException if a database access error occurs or the result set is not properly formed.
     */
    public static List<Category> convert(ResultSet resultSet) throws SQLException {
        List<Category> categories = new ArrayList<>();

        if (resultSet == null) {
            return categories;
        }

        try (resultSet) {
            while (resultSet.next()) {
                int id = resultSet.getInt(ID);
                String name = resultSet.getString(NAME);
                String category = resultSet.getString(CATEGORY);
                CategoryType type = resultSet.getBoolean(INCOME) ? CategoryType.INCOME : CategoryType.EXPENSE;

                Category newCategory = new Category(id, name, category, type);
                categories.add(newCategory);
            }
        }

        return categories;
    }
}
