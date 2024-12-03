package de.drachenpapa.pexi.utils;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowSettingsSaver extends WindowAdapter {

    private final JFrame frame;

    public WindowSettingsSaver(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        Settings settings = Settings.getInstance();
        settings.saveWindowSettings(frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight());
    }
}
