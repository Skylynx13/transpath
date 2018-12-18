package com.skylynx13.transpath;

import com.skylynx13.transpath.ui.TranspathFrame;

import javax.swing.*;
import java.awt.*;

public class TransPath {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                TranspathFrame tpFrame = new TranspathFrame();
                tpFrame.setVisible(true);
            }
        });
    }
}
