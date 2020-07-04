package com.skylynx13.transpath.ui;

import com.skylynx13.transpath.Transpath;

import javax.swing.*;

class PropertiesDialog extends JDialog {
    PropertiesDialog() {
        super(Transpath.getTranspathFrame(), "Properties Settings", true);
        //add(new JLabel(), BorderLayout.CENTER);
        setSize(800,600);
    }
}
