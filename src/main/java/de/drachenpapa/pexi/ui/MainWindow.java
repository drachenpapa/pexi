package de.drachenpapa.pexi.ui;

import de.drachenpapa.pexi.utils.Messages;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MainWindow {

    private JTable table;
    private JPanel panel;
    private JButton addButton;
    private JButton removeButton;
    private JButton statisticsButton;

    public MainWindow() {
        addButton.setText(Messages.get("toolbar.add"));
        removeButton.setText(Messages.get("toolbar.remove"));
        statisticsButton.setText(Messages.get("toolbar.statistics"));

        table.setModel(createTableModel());
    }

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

    public JPanel getMainPanel() {
        return panel;
    }
}
