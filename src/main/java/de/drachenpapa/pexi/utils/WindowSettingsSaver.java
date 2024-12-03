package de.drachenpapa.pexi.utils;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A custom {@link WindowAdapter} that saves the window's settings (position and size)
 * when the window is closed.
 * <p>
 * This class listens for the window closing event and saves the current window
 * position (X and Y coordinates) and size (width and height) to the application's
 * settings using the {@link Settings} class.
 * </p>
 */
public class WindowSettingsSaver extends WindowAdapter {

    private final JFrame frame;

    /**
     * Constructs a {@link WindowSettingsSaver} for the given {@link JFrame}.
     *
     * @param frame the {@link JFrame} whose window settings (position and size)
     *              will be saved upon window close
     */
    public WindowSettingsSaver(JFrame frame) {
        this.frame = frame;
    }

    /**
     * Saves the window's position and size when the window is closed.
     * <p>
     * This method is triggered when the window is closed and stores the current
     * position (X, Y) and size (width, height) of the window in the application's
     * settings using the {@link Settings} class.
     * </p>
     *
     * @param e the {@link WindowEvent} triggered by closing the window
     */
    @Override
    public void windowClosing(WindowEvent e) {
        Settings settings = Settings.getInstance();
        settings.saveWindowSettings(frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight());
    }
}
