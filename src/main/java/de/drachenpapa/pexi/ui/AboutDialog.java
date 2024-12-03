package de.drachenpapa.pexi.ui;

import de.drachenpapa.pexi.PexiApplication;
import de.drachenpapa.pexi.utils.Messages;

import javax.swing.*;
import java.awt.event.*;

public class AboutDialog extends JDialog {

    private JPanel contentPane;
    private JButton closeButton;
    private JEditorPane editorPane;

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

    private void onClose() {
        dispose();
    }

    public static void showDialog(JFrame parent) {
        AboutDialog dialog = new AboutDialog();
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}
