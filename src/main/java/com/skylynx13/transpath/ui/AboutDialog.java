package com.skylynx13.transpath.ui;

import com.skylynx13.transpath.Transpath;

import javax.swing.*;
import java.awt.*;

class AboutDialog extends JDialog {
    AboutDialog() {
        super(Transpath.getTranspathFrame(), "About Transpath", true);
        add(createAboutLabel(), BorderLayout.CENTER);
        add(createOkPanel(), BorderLayout.SOUTH);
        setSize(250, 150);
    }

    private JLabel createAboutLabel() {
        return new JLabel("<html><h2>Transpath 2.0 waiting orders.</h2><hr>By Skylynx13</html>");
    }

    private JPanel createOkPanel() {
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> setVisible(false));
        JPanel okPanel = new JPanel();
        okPanel.add(okButton);
        return okPanel;
    }
}
