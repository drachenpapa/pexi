package de.drachenpapa.pexi.utils;

import de.drachenpapa.pexi.model.Category;
import de.drachenpapa.pexi.model.CategoryType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Converts a ResultSet into a list of {@link Category}.
 */
public class CategoryConverter {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String CATEGORY = "category";
    private static final String INCOME = "income";


    /**
     * Converts a ResultSet into a list of {@link Category}.
     *
     * @param resultSet the ResultSet to convert
     * @return A list of {@link Category}.
     * @throws SQLException if a database access error occurs
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
