package de.drachenpapa.pexi.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Utility class for retrieving localized messages.
 */
public class Messages {

    private static ResourceBundle messages;

    public static void initialize(Locale locale) {
        messages = ResourceBundle.getBundle("messages", locale);
    }

    public static String get(String key) {
        return messages.getString(key);
    }

    public static String get(String key, Object... args) {
        return String.format(messages.getString(key), args);
    }
}
