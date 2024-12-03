package de.drachenpapa.pexi;

import de.drachenpapa.pexi.ui.PexiUI;
import de.drachenpapa.pexi.utils.Messages;
import de.drachenpapa.pexi.utils.Settings;

/**
 * The main entry point for the Pexi application.
 * <p>
 * This class initializes application settings, localizes the user interface,
 * and displays the main UI window.
 * </p>
 */
public class PexiApplication {

    /**
     * The main method that launches the Pexi application.
     * <p>
     * It initializes application settings using the {@link Settings} singleton,
     * sets the locale for localization via the {@link Messages} utility,
     * and then creates and displays the main user interface using {@link PexiUI}.
     * </p>
     *
     * @param args the command line arguments (not used)
     */
    public static void main(String[] args) {
        Settings settings = Settings.getInstance();
        Messages.initialize(settings.getLocale());
        PexiUI.createAndDisplayUI();
    }
}
