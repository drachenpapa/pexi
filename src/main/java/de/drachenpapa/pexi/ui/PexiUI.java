package de.drachenpapa.pexi.ui;

import de.drachenpapa.pexi.utils.Messages;
import de.drachenpapa.pexi.utils.Settings;
import de.drachenpapa.pexi.utils.WindowSettingsSaver;

import javax.swing.*;

public class PexiUI {

    private final JFrame frame;

    public PexiUI() {
        this.frame = new JFrame(Messages.get("app.title"));
        initializeMainFrameContent();
    }

    private void initializeMainFrameContent() {
        MainWindow mainWindow = new MainWindow();
        MenuBar menuBar = new MenuBar(frame);
        Settings settings = Settings.getInstance();

        frame.setContentPane(mainWindow.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(new ImageIcon(PexiUI.class.getResource("/logo.png")).getImage());
        frame.setJMenuBar(menuBar.create());
        frame.setLocation(settings.getX(), settings.getY());
        frame.setSize(settings.getWidth(), settings.getHeight());
        frame.addWindowListener(new WindowSettingsSaver(frame));
    }

    private void show() {
        frame.setVisible(true);
    }

    public static void createAndDisplayUI() {
        SwingUtilities.invokeLater(() -> new PexiUI().show());
    }
}
