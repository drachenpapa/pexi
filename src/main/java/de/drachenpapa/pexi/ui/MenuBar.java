package de.drachenpapa.pexi.ui;

import de.drachenpapa.pexi.utils.Messages;
import de.drachenpapa.pexi.utils.Settings;

import javax.swing.*;

public class MenuBar {

    private final JFrame frame;

    public MenuBar(JFrame frame) {
        this.frame = frame;
    }

    public JMenuBar create() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createEditMenu());
        menuBar.add(createHelpMenu());
        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu(Messages.get("file.menu"));
        fileMenu.add(createMenuItem(Messages.get("file.settings"), () -> SettingsDialog.showDialog(frame)));
        fileMenu.add(createMenuItem(Messages.get("file.exit"), this::exitApplication));
        return fileMenu;
    }

    private JMenu createEditMenu() {
        JMenu helpMenu = new JMenu(Messages.get("edit.menu"));
        helpMenu.add(createMenuItem(Messages.get("edit.accounts")));//, this::showAccountsDialog));
        helpMenu.add(createMenuItem(Messages.get("edit.categories")));//, this::showCategoriesDialog));
        return helpMenu;
    }

    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu(Messages.get("help.menu"));
        helpMenu.add(createMenuItem(Messages.get("help.about"), () -> AboutDialog.showDialog(frame)));
        return helpMenu;
    }

    private JMenuItem createMenuItem(String label) {
        return new JMenuItem(label);
    }

    private JMenuItem createMenuItem(String label, Runnable action) {
        JMenuItem menuItem = new JMenuItem(label);
        menuItem.addActionListener(e -> action.run());
        return menuItem;
    }

    private void exitApplication() {
        Settings settings = Settings.getInstance();
        settings.saveWindowSettings(frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight());
        System.exit(0);
    }
}
