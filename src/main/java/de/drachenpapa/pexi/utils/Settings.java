package de.drachenpapa.pexi.utils;

import java.io.*;
import java.util.Locale;
import java.util.Properties;

/**
 * This class provides functionality for reading and writing application settings.
 * It is designed to load settings from a properties file and store window and locale-related
 * settings, such as window size, position, and the user's locale and currency preferences.
 */
public class Settings {

    private static final String SETTINGS_FILE = "pexi.properties";
    private static Settings instance;
    private final Properties properties;

    /**
     * Private constructor to initialize the properties object and load settings
     * from the properties file. If the file does not exist or an error occurs,
     * default settings are created.
     */
    private Settings() {
        properties = new Properties();
        try (InputStream input = new FileInputStream(SETTINGS_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            createDefaultSettings();
        }
    }

    /**
     * Retrieves the singleton instance of the Settings class.
     *
     * @return The single instance of the Settings class.
     */
    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    /**
     * Saves the window settings (position and size) to the properties file.
     *
     * @param x      The x-coordinate of the window's position.
     * @param y      The y-coordinate of the window's position.
     * @param width  The width of the window.
     * @param height The height of the window.
     */
    public void saveWindowSettings(int x, int y, int width, int height) {
        setProperty("x", x);
        setProperty("y", y);
        setProperty("width", width);
        setProperty("height", height);
        saveProperties();
    }

    /**
     * Saves the locale and currency settings to the properties file.
     *
     * @param locale  The locale to be saved.
     * @param currency The currency to be saved.
     */
    public void saveLocaleAndCurrency(Locale locale, String currency) {
        setProperty("locale", locale.toLanguageTag());
        setProperty("currency", currency);
        saveProperties();
    }

    /**
     * Retrieves the x-coordinate of the window's position from the properties file.
     * If not set, it returns a default value of 100.
     *
     * @return The x-coordinate of the window's position.
     */
    public int getX() {
        return Integer.parseInt(properties.getProperty("x", "100"));
    }

    /**
     * Retrieves the y-coordinate of the window's position from the properties file.
     * If not set, it returns a default value of 100.
     *
     * @return The y-coordinate of the window's position.
     */
    public int getY() {
        return Integer.parseInt(properties.getProperty("y", "100"));
    }

    /**
     * Retrieves the width of the window from the properties file.
     * If not set, it returns a default value of 400.
     *
     * @return The width of the window.
     */
    public int getWidth() {
        return Integer.parseInt(properties.getProperty("width", "400"));
    }

    /**
     * Retrieves the height of the window from the properties file.
     * If not set, it returns a default value of 300.
     *
     * @return The height of the window.
     */
    public int getHeight() {
        return Integer.parseInt(properties.getProperty("height", "300"));
    }

    /**
     * Retrieves the locale from the properties file.
     * If not set, it returns a default locale of "en" (English).
     *
     * @return The saved locale.
     */
    public Locale getLocale() {
        return Locale.forLanguageTag(properties.getProperty("locale", "en"));
    }

    /**
     * Retrieves the saved currency from the properties file.
     * If not set, it returns a default currency of "EUR" (Euro).
     *
     * @return The saved currency.
     */
    public String getSavedCurrency() {
        return properties.getProperty("currency", "EUR");
    }

    /**
     * Creates and sets default settings, including window size, position, locale, and currency.
     * This method is invoked if the properties file cannot be loaded or is missing.
     */
    private void createDefaultSettings() {
        setProperty("x", 100);
        setProperty("y", 100);
        setProperty("width", 400);
        setProperty("height", 300);
        setProperty("locale", "en");
        setProperty("currency", "EUR");
        saveProperties();
    }

    /**
     * Sets a property in the properties object with the specified key and value.
     *
     * @param key   The key for the property.
     * @param value The value of the property.
     */
    private void setProperty(String key, Object value) {
        properties.setProperty(key, String.valueOf(value));
    }

    /**
     * Saves the current properties to the properties file.
     */
    private void saveProperties() {
        try (OutputStream output = new FileOutputStream(SETTINGS_FILE)) {
            properties.store(output, "PEXI Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
