package de.drachenpapa.pexi;

import de.drachenpapa.pexi.ui.PexiUI;
import de.drachenpapa.pexi.utils.Messages;
import de.drachenpapa.pexi.utils.Settings;

public class PexiApplication {

    public static void main(String[] args) {
        Settings settings = Settings.getInstance();
        Messages.initialize(settings.getLocale());
        PexiUI.createAndDisplayUI();
    }
}
