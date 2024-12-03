package de.drachenpapa.pexi.ui;

import de.drachenpapa.pexi.utils.Messages;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Represents the main window of the Pexi application.
 * This window contains a table for displaying transactions and buttons for adding,
 * removing, and displaying statistics of the transactions.
 */
public class MainWindow {

    private JTable table;
    private JPanel panel;
    private JButton addButton;
    private JButton removeButton;
    private JButton statisticsButton;

    /**
     * Constructs a new {@code MainWindow} instance and initializes the table and buttons.
     * The buttons are labeled using the appropriate messages from the {@link Messages} utility.
     * The table is populated with a default model based on predefined columns.
     */
    public MainWindow() {
        addButton.setText(Messages.get("toolbar.add"));
        removeButton.setText(Messages.get("toolbar.remove"));
        statisticsButton.setText(Messages.get("toolbar.statistics"));

        table.setModel(createTableModel());
    }

    /**
     * Creates and returns a {@link DefaultTableModel} for the transactions table,
     * with predefined columns such as ID, date, amount, description, account ID, and category ID.
     *
     * @return a {@code DefaultTableModel} instance for the table.
     */
    private DefaultTableModel createTableModel() {
        String[] columns = {
                Messages.get("table.column.id"),
                Messages.get("table.column.date"),
                Messages.get("table.column.amount"),
                Messages.get("table.column.description"),
                Messages.get("table.column.account_id"),
                Messages.get("table.column.category_id")
        };

        return new DefaultTableModel(columns, 0);
    }

    /**
     * Returns the main panel that contains the table and the toolbar buttons.
     *
     * @return the {@code JPanel} instance containing the main window's UI components.
     */
    public JPanel getMainPanel() {
        return panel;
    }
}
