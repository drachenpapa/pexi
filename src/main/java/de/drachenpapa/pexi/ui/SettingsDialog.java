package de.drachenpapa.pexi.ui;

import de.drachenpapa.pexi.PexiApplication;
import de.drachenpapa.pexi.utils.Messages;
import de.drachenpapa.pexi.utils.Settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class SettingsDialog extends JDialog {

    private JPanel contentPane;
    private JButton saveButton;
    private JButton cancelButton;
    private JComboBox<String> languageSelection;
    private JComboBox<String> currencySelection;
    private JLabel languageLabel;
    private JLabel currencyLabel;

    public SettingsDialog() {
        setTitle(Messages.get("settings.title"));
        setContentPane(contentPane);
        setIconImage(new ImageIcon(PexiApplication.class.getResource("/logo.png")).getImage());
        setModal(true);
        getRootPane().setDefaultButton(cancelButton);

        languageLabel.setText(Messages.get("settings.lang"));
        configureLanguageSelection();

        currencyLabel.setText(Messages.get("settings.currency"));
        configureCurrencySelection();

        saveButton.setText(Messages.get("settings.buttons.save"));
        configureSaveButton();

        cancelButton.setText(Messages.get("settings.buttons.cancel"));
        configureCancelButton();
    }

    private void configureCurrencySelection() {
        Set<Currency> currencies = Currency.getAvailableCurrencies();
        List<String> currencyCodes = currencies.stream()
                .map(Currency::getCurrencyCode)
                .sorted()
                .toList();

        for (String currency : currencyCodes) {
            currencySelection.addItem(currency);
        }

        Settings settings = Settings.getInstance();
        String savedCurrency = settings.getSavedCurrency();
        currencySelection.setSelectedItem(savedCurrency);
    }

    private void configureLanguageSelection() {
        languageSelection.addItem(Messages.get("settings.lang.en"));
        languageSelection.addItem(Messages.get("settings.lang.de"));

        Settings settings = Settings.getInstance();
        String currentLanguage = settings.getLocale().getLanguage();
        if (currentLanguage.equals("de")) {
            languageSelection.setSelectedIndex(1);
        } else {
            languageSelection.setSelectedIndex(0);
        }
    }

    private void configureSaveButton() {
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSave();
            }
        });
    }

    private void configureCancelButton() {
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
    }

    private void onSave() {
        String selectedLanguage = languageSelection.getSelectedIndex() == 0 ? "en" : "de";
        String selectedCurrency = (String) currencySelection.getSelectedItem();

        Settings settings = Settings.getInstance();
        settings.saveLocaleAndCurrency(Locale.forLanguageTag(selectedLanguage), selectedCurrency);

        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public static void showDialog(JFrame parent) {
        SettingsDialog dialog = new SettingsDialog();
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}
