package com.skylynx13.transpath;

import com.skylynx13.transpath.ui.TranspathFrame;

import javax.swing.*;
import java.awt.*;

/**
 * @author skylynx
 */
public class Transpath {
    private static TranspathFrame tpFrame;
    private static final JTextArea LOG_TEXT_AREA = new JTextArea();
    private static final JProgressBar PROGRESS_BAR = new JProgressBar();
    private static final JLabel STATUS_LABEL = new JLabel("Status Normal.");
    private static final Dialog PROPERTIES_DIALOG = new Dialog(tpFrame);

    public static TranspathFrame getTranspathFrame() {
        return tpFrame;
    }
    public static JTextArea getLogTextArea() {
        return LOG_TEXT_AREA;
    }
    public static JProgressBar getProgressBar() {
        return PROGRESS_BAR;
    }
    public static JLabel getStatusLabel() {
        return STATUS_LABEL;
    }
    public static Dialog getPropertiesDialog() {
        return PROPERTIES_DIALOG;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            tpFrame = new TranspathFrame();
            tpFrame.setVisible(true);
        });
    }
}
