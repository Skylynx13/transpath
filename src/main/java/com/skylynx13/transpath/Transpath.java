package com.skylynx13.transpath;

import com.skylynx13.transpath.ui.TranspathFrame;

import javax.swing.*;
import java.awt.*;

public class Transpath {
    private static TranspathFrame tpFrame;
    private static JTextArea logTextArea = new JTextArea();

    public static TranspathFrame getTranspathFrame() {
        return tpFrame;
    }

    public static JTextArea getLogTextArea() {
        return logTextArea;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            tpFrame = new TranspathFrame();
            tpFrame.setVisible(true);
        });
    }
}
