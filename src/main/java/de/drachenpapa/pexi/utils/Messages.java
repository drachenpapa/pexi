package de.drachenpapa.pexi.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Utility class for retrieving localized messages.
 * <p>
 * This class provides methods to initialize the localization of messages based on a given {@link Locale}
 * and to retrieve localized strings. It uses the {@link ResourceBundle} to load and manage messages from
 * a properties file.
 * </p>
 */
public class Messages {

    private static ResourceBundle messages;

    /**
     * Initializes the {@link ResourceBundle} for retrieving localized messages.
     * This method loads the appropriate messages bundle based on the specified {@link Locale}.
     *
     * @param locale The {@link Locale} for which the messages should be loaded (e.g., "en", "de").
     *               This determines the language and country-specific messages.
     */
    public static void initialize(Locale locale) {
        messages = ResourceBundle.getBundle("messages", locale);
    }

    /**
     * Retrieves a localized message based on the provided key.
     *
     * @param key The key corresponding to the message in the properties file.
     * @return The localized message associated with the given key.
     */
    public static String get(String key) {
        return messages.getString(key);
    }

    /**
     * Retrieves a localized message and formats it with the provided arguments.
     * The message corresponding to the key is formatted using {@link String#format(String, Object...)}
     * with the supplied arguments.
     *
     * @param key  The key corresponding to the message in the properties file.
     * @param args The arguments to format the message string with.
     * @return The formatted localized message.
     */
    public static String get(String key, Object... args) {
        return String.format(messages.getString(key), args);
    }
}
