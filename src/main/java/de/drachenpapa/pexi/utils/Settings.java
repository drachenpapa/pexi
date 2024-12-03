package de.drachenpapa.pexi.utils;

import java.io.*;
import java.util.Locale;
import java.util.Properties;

public class Settings {

    private static final String SETTINGS_FILE = "pexi.properties";
    private static Settings instance;
    private final Properties properties;

    private Settings() {
        properties = new Properties();
        try (InputStream input = new FileInputStream(SETTINGS_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            // If the settings file doesn't exist, create a new one
        }
    }

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public void saveWindowSettings(int x, int y, int width, int height) {
        saveProperty("x", x);
        saveProperty("y", y);
        saveProperty("width", width);
        saveProperty("height", height);
        saveProperties();
    }

    public void saveLocaleAndCurrency(Locale locale, String currency) {
        saveProperty("locale", locale.toLanguageTag());
        saveProperty("currency", currency);
        saveProperties();
    }

    public int getX() {
        return Integer.parseInt(properties.getProperty("x", "100"));
    }

    public int getY() {
        return Integer.parseInt(properties.getProperty("y", "100"));
    }

    public int getWidth() {
        return Integer.parseInt(properties.getProperty("width", "400"));
    }

    public int getHeight() {
        return Integer.parseInt(properties.getProperty("height", "300"));
    }

    public Locale getLocale() {
        return Locale.forLanguageTag(properties.getProperty("locale", "en"));
    }

    public String getSavedCurrency() {
        return properties.getProperty("currency", "EUR");
    }

    private void saveProperty(String key, Object value) {
        properties.setProperty(key, String.valueOf(value));
    }

    private void saveProperties() {
        try (OutputStream output = new FileOutputStream(SETTINGS_FILE)) {
            properties.store(output, "PEXI Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
