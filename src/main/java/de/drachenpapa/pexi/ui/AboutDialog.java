package de.drachenpapa.pexi.ui;

import de.drachenpapa.pexi.PexiApplication;
import de.drachenpapa.pexi.utils.Messages;

import javax.swing.*;
import java.awt.event.*;

/**
 * Represents the "About" dialog in the Pexi application.
 * This dialog displays information about the application, including its title and description.
 */
public class AboutDialog extends JDialog {

    private JPanel contentPane;
    private JButton closeButton;
    private JEditorPane editorPane;

    /**
     * Constructs a new {@code AboutDialog} instance, initializing the dialog with content, title, and buttons.
     */
    public AboutDialog() {
        setTitle(Messages.get("about.title"));
        setContentPane(contentPane);
        setIconImage(new ImageIcon(PexiApplication.class.getResource("/logo.png")).getImage());
        setModal(true);
        getRootPane().setDefaultButton(closeButton);

        editorPane.setText(Messages.get("about.content"));

        closeButton.setText(Messages.get("about.buttons.close"));
        configureCloseButton();
    }

    /**
     * Displays the "About" dialog centered relative to the given parent frame.
     *
     * @param parent the parent {@code JFrame} used to center the dialog.
     */
    public static void showDialog(JFrame parent) {
        AboutDialog dialog = new AboutDialog();
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    /**
     * Configures the behavior of the close button and the window's closing actions.
     * This includes adding an action listener to the close button and registering the ESC key
     * as a shortcut to close the dialog.
     */
    private void configureCloseButton() {
        closeButton.addActionListener(e -> onClose());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onClose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * Closes the dialog and disposes of its resources.
     */
    private void onClose() {
        dispose();
    }
}
