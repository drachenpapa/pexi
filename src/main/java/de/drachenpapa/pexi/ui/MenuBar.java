package de.drachenpapa.pexi.ui;

import de.drachenpapa.pexi.utils.Messages;
import de.drachenpapa.pexi.utils.Settings;

import javax.swing.*;

/**
 * Represents a menu bar for the application, allowing users to interact with various menu options.
 * It includes menus for file operations, editing, and help.
 */
public class MenuBar {

    private final JFrame frame;

    /**
     * Constructs a {@code MenuBar} for the given frame.
     *
     * @param frame the {@code JFrame} where the menu bar will be added
     */
    public MenuBar(JFrame frame) {
        this.frame = frame;
    }

    /**
     * Creates and returns the main menu bar with all necessary menus.
     *
     * @return the created {@code JMenuBar} with all menus and actions
     */
    public JMenuBar create() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createEditMenu());
        menuBar.add(createHelpMenu());
        return menuBar;
    }

    /**
     * Creates the 'File' menu with actions for settings and exiting the application.
     *
     * @return the created 'File' {@code JMenu} with corresponding menu items
     */
    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu(Messages.get("file.menu"));
        fileMenu.add(createMenuItem(Messages.get("file.settings"), () -> SettingsDialog.showDialog(frame)));
        fileMenu.add(createMenuItem(Messages.get("file.exit"), this::exitApplication));
        return fileMenu;
    }

    /**
     * Creates the 'Edit' menu with actions for managing accounts and categories.
     *
     * @return the created 'Edit' {@code JMenu} with corresponding menu items
     */
    private JMenu createEditMenu() {
        JMenu helpMenu = new JMenu(Messages.get("edit.menu"));
        helpMenu.add(createMenuItem(Messages.get("edit.accounts")));//, this::showAccountsDialog));
        helpMenu.add(createMenuItem(Messages.get("edit.categories")));//, this::showCategoriesDialog));
        return helpMenu;
    }

    /**
     * Creates the 'Help' menu with an action for displaying about information.
     *
     * @return the created 'Help' {@code JMenu} with the about menu item
     */
    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu(Messages.get("help.menu"));
        helpMenu.add(createMenuItem(Messages.get("help.about"), () -> AboutDialog.showDialog(frame)));
        return helpMenu;
    }


    /**
     * Creates a simple {@code JMenuItem} with the specified label.
     *
     * @param label the label for the menu item
     * @return the created {@code JMenuItem} with the specified label
     */
    private JMenuItem createMenuItem(String label) {
        return new JMenuItem(label);
    }

    /**
     * Creates a {@code JMenuItem} with the specified label and action.
     *
     * @param label  the label for the menu item
     * @param action the action to perform when the menu item is selected
     * @return the created {@code JMenuItem} with the specified label and action
     */
    private JMenuItem createMenuItem(String label, Runnable action) {
        JMenuItem menuItem = new JMenuItem(label);
        menuItem.addActionListener(e -> action.run());
        return menuItem;
    }

    /**
     * Exits the application, saving the current window settings (position and size).
     */
    private void exitApplication() {
        Settings settings = Settings.getInstance();
        settings.saveWindowSettings(frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight());
        System.exit(0);
    }
}
