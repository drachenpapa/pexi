package de.drachenpapa.pexi.database;

import de.drachenpapa.pexi.model.Category;
import de.drachenpapa.pexi.model.CategoryType;
import de.drachenpapa.pexi.utils.CategoryConverter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for interacting with the database to manage {@link Category} records.
 * <p>
 * This class provides methods to create the categories table, retrieve, insert, update, and remove
 * category data from the database.
 * </p>
 */
public class CategoriesDB {

    /**
     * Creates the categories table in the database if it does not exist.
     * <p>
     * This table includes columns for the category ID, name, category type (income/expense), and category.
     * </p>
     */
    static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS categories (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "category VARCHAR(255) NOT NULL, " +
                "income BOOLEAN NOT NULL)";
        DatabaseManager.executeUpdate(sql);
    }

    /**
     * Retrieves all categories from the database.
     * <p>
     * This method executes a SQL query to fetch all records from the `categories` table and returns them
     * as a list of {@link Category} objects.
     * </p>
     *
     * @return A list of {@link Category} objects.
     */
    public static List<Category> get() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";

        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            categories = CategoryConverter.convert(resultSet);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return categories;
    }

    /**
     * Inserts a new category into the database.
     * <p>
     * This method creates a new category record in the `categories` table with the specified values for name,
     * category, and category type (income or expense).
     * </p>
     *
     * @param name The name of the category.
     * @param category The type or description of the category.
     * @param categoryType The type of the category, which can be either {@link CategoryType#INCOME} or {@link CategoryType#EXPENSE}.
     */
    public static void insert(String name, String category, CategoryType categoryType) {
        String sql = "INSERT INTO categories (name, category, categoryType) VALUES (?, ?, ?, ?)";
        DatabaseManager.executeUpdate(sql, name, category, categoryType);
    }


    /**
     * Updates an existing category in the database.
     * <p>
     * This method updates the record of an existing category identified by its ID with the new values for name,
     * category, and category type.
     * </p>
     *
     * @param id The ID of the category to update.
     * @param name The new name of the category.
     * @param category The new description of the category.
     * @param categoryType The new category type, which can be either {@link CategoryType#INCOME} or {@link CategoryType#EXPENSE}.
     */
    public static void update(String id, String name, String category, CategoryType categoryType) {
        String sql = "UPDATE categories SET name = ?, category = ?, categoryType = ? WHERE id = ?";
        DatabaseManager.executeUpdate(sql, name, category, categoryType, id);
    }

    /**
     * Removes a category from the database.
     * <p>
     * This method deletes the category record identified by the given ID from the `categories` table.
     * </p>
     *
     * @param id The ID of the category to remove.
     */
    public static void remove(String id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        DatabaseManager.executeUpdate(sql, id);
    }
}
