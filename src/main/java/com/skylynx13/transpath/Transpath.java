package com.skylynx13.transpath;

import com.skylynx13.transpath.ui.TranspathFrame;

import javax.swing.*;
import java.awt.*;

public class Transpath {
    private static TranspathFrame tpFrame;
    private static JTextArea logTextArea = new JTextArea();
    private static JProgressBar progressBar = new JProgressBar();
    private static JLabel statusLabel = new JLabel();
    private static Dialog propertiesDialog = new Dialog(tpFrame);

    public static TranspathFrame getTranspathFrame() {
        return tpFrame;
    }
    public static JTextArea getLogTextArea() {
        return logTextArea;
    }
    public static JProgressBar getProgressBar() {
        return progressBar;
    }
    public static JLabel getStatusLabel() {
        return statusLabel;
    }
    public static Dialog getPropertiesDialog() {
        return propertiesDialog;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            tpFrame = new TranspathFrame();
            tpFrame.setVisible(true);
        });
    }

}
