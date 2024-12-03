package de.drachenpapa.pexi.ui;

import de.drachenpapa.pexi.utils.Messages;
import de.drachenpapa.pexi.utils.Settings;
import de.drachenpapa.pexi.utils.WindowSettingsSaver;

import javax.swing.*;

/**
 * Represents the main user interface of the Pexi application.
 * It initializes the main frame, menu bar, and other components to display the application's content.
 */
public class PexiUI {

    private final JFrame frame;

    /**
     * Constructs a new {@code PexiUI} instance, initializing the main frame with a title and content.
     */
    public PexiUI() {
        this.frame = new JFrame(Messages.get("app.title"));
        initializeMainFrameContent();
    }

    /**
     * Creates and displays the user interface on the Swing event dispatch thread.
     * This method should be invoked to start the application's UI.
     */
    public static void createAndDisplayUI() {
        SwingUtilities.invokeLater(() -> new PexiUI().show());
    }

    /**
     * Initializes the main content of the frame, including the main window, menu bar, and window settings.
     * It sets the frame's content pane, size, location, and behavior (such as window closing and icon).
     */
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

    /**
     * Displays the main window by setting the frame visible.
     */
    private void show() {
        frame.setVisible(true);
    }
}
