/**
 * Copyright (c) 2015,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.ui
 * File Name:TransMainFrame.java
 * Date:2015-11-16 上午12:59:46
 * 
 */
package com.qxu.transpath.ui;

import java.awt.event.WindowEvent;

import javax.swing.JFrame;

 /**
 * ClassName: TransMainFrame <br/>
 * Description: TODO <br/>
 * Date: 2015-11-16 上午12:59:46 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class TransMainFrame extends JFrame {
    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            System.exit(0);
        }
    }
    public static void main(String[] args) {
        TransMainFrame tmFrame = new TransMainFrame();
        tmFrame.setVisible(true);
    }

}
