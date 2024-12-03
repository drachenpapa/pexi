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

/**
 * Represents a dialog where users can modify application settings such as language and currency.
 * This dialog allows the user to save or cancel changes.
 */
public class SettingsDialog extends JDialog {

    private JPanel contentPane;
    private JButton saveButton;
    private JButton cancelButton;
    private JComboBox<String> languageSelection;
    private JComboBox<String> currencySelection;
    private JLabel languageLabel;
    private JLabel currencyLabel;

    /**
     * Constructs a {@code SettingsDialog} with the necessary UI components and initial settings.
     * Sets up the labels, language selection, currency selection, and buttons.
     */
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

    /**
     * Displays the {@code SettingsDialog} as a modal dialog.
     *
     * @param parent the parent frame for centering the dialog
     */
    public static void showDialog(JFrame parent) {
        SettingsDialog dialog = new SettingsDialog();
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    /**
     * Configures the currency selection dropdown with available currencies.
     * The currently saved currency is selected by default.
     */
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

    /**
     * Configures the language selection dropdown with available languages.
     * The currently selected language (English or German) is selected by default.
     */
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

    /**
     * Configures the action listener for the save button. When clicked, it saves the selected language and currency.
     */
    private void configureSaveButton() {
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSave();
            }
        });
    }

    /**
     * Configures the action listener for the cancel button. When clicked, it closes the dialog without saving changes.
     */
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

    /**
     * Saves the selected language and currency settings to the application preferences.
     * Then, closes the dialog.
     */
    private void onSave() {
        String selectedLanguage = languageSelection.getSelectedIndex() == 0 ? "en" : "de";
        String selectedCurrency = (String) currencySelection.getSelectedItem();

        Settings settings = Settings.getInstance();
        settings.saveLocaleAndCurrency(Locale.forLanguageTag(selectedLanguage), selectedCurrency);

        dispose();
    }

    /**
     * Closes the dialog without saving any changes.
     */
    private void onCancel() {
        dispose();
    }
}
