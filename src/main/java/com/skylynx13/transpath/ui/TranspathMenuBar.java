/**
 * Copyright (c) 2017,qxu.
 * All Rights Reserved.
 * <p>
 * Project Name:transpath
 * Package Name:com.libra42.transpath.ui
 * File Name:TransMenuBar.java
 * Date:2017年10月14日 下午4:00:27
 */
package com.skylynx13.transpath.ui;

import java.awt.event.InputEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.*;

import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.pub.PubKeeper;
import com.skylynx13.transpath.store.StoreKeeper;
import com.skylynx13.transpath.task.TaskChecker;
import com.skylynx13.transpath.task.TaskKeeper;
import com.skylynx13.transpath.utils.CompressUtils;
import com.skylynx13.transpath.utils.TransConst;

/**
 * ClassName: TransMenuBar <br/>
 * Description: TODO <br/>
 * Date: 2017年10月14日 下午4:00:27 <br/>
 * <br/>
 *
 * @author qxu@
 *
 *         Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 *
 */

public class TranspathMenuBar extends JMenuBar {
    /**
     * serialVersionUID:TODO.
     *
     */
    private static final long serialVersionUID = 1L;

    private TranspathFrame transpathFrame;

    private ExecutorService transpathMenuAction = Executors.newCachedThreadPool(r -> new Thread(r, "TranspathMenuAction"));

    private JMenu createSysMenu() {
        JMenu sysMenu = new JMenu("Sys");

        JMenuItem sysReloadItem = new JMenuItem("Reload");
        sysMenu.add(sysReloadItem);
        sysReloadItem.addActionListener(e -> transpathFrame.initPanel());

        sysMenu.add(new JSeparator());

        JMenuItem sysExitItem = new JMenuItem("Exit");
        sysMenu.add(sysExitItem);
        sysExitItem.addActionListener(e -> transpathFrame.exit());
        return sysMenu;
    }

    private JMenu createTaskMenu() {
        JMenu taskMenu = new JMenu("Task");

        JMenuItem taskWeekItem = new JMenuItem("Weekly Digging");
        taskMenu.add(taskWeekItem);
        taskWeekItem.addActionListener(e -> transpathMenuAction.submit(() -> {
            TaskKeeper.weekFresh();
            TaskKeeper.digNewFresh();
        }));

        JMenuItem taskSpecItem = new JMenuItem("Spec Digging");
        taskMenu.add(taskSpecItem);
        taskSpecItem.addActionListener(e -> transpathMenuAction.submit(TaskKeeper::digSpecFresh));

        JMenuItem taskNameItem = new JMenuItem("Name Revise");
        taskMenu.add(taskNameItem);
        taskNameItem.addActionListener(e -> transpathMenuAction.submit(TaskChecker::rename));
        taskNameItem.setAccelerator(KeyStroke.getKeyStroke('R', InputEvent.CTRL_DOWN_MASK));

        JMenuItem taskPackageItem = new JMenuItem("Package Check");
        taskMenu.add(taskPackageItem);
        taskPackageItem.addActionListener(e -> transpathMenuAction.submit(TaskChecker::checkPackages));
        taskPackageItem.setAccelerator(KeyStroke.getKeyStroke('K', InputEvent.CTRL_DOWN_MASK));
        return taskMenu;
    }

    private JMenu createStoreMenu() {
        JMenu storeMenu = new JMenu("Store");

        JMenuItem storeCombineItem = new JMenuItem("Combine");
        storeMenu.add(storeCombineItem);
        storeCombineItem.addActionListener(e -> transpathMenuAction.submit(StoreKeeper::buildCombinedList));

        JMenuItem storeCombineTestItem = new JMenuItem("CombineTest");
        storeMenu.add(storeCombineTestItem);
        storeCombineTestItem.addActionListener(e -> transpathMenuAction.submit(StoreKeeper::testCombinedList));

        storeMenu.add(new JSeparator());

        JMenuItem storeSearchItem = new JMenuItem("Search...");
        storeMenu.add(storeSearchItem);
        storeSearchItem.addActionListener(e -> transpathMenuAction.submit(() -> {
            String searchText = JOptionPane.showInputDialog("Search Text:");
            TranspathFrame.searchList(searchText);
        }));
        storeSearchItem.setAccelerator(KeyStroke.getKeyStroke('F', InputEvent.CTRL_DOWN_MASK));

        JMenuItem fetchSelectedItem = new JMenuItem("Fetch Selected");
        storeMenu.add(fetchSelectedItem);
        fetchSelectedItem.addActionListener(e -> transpathMenuAction.submit(TranspathFrame::fetchSelectedStores));

        return storeMenu;
    }

    private JMenu createPubMenu() {
        JMenu pubMenu = new JMenu("Pub");

        JMenuItem pubRefreshItem = new JMenuItem("Refresh");
        pubMenu.add(pubRefreshItem);
        pubRefreshItem.addActionListener(e -> transpathMenuAction.submit(PubKeeper::refreshPubList));
        return pubMenu;
    }

    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu("Help");

        JMenuItem helpAboutItem = new JMenuItem("About");
        helpMenu.add(helpAboutItem);
        helpAboutItem.addActionListener(e -> transpathMenuAction.submit(() -> {
            TransLog.getLogger().error("It's ok anyway.");
            TransLog.getLogger().info("Info can be seen.");
        }));
        return helpMenu;
    }

    private void setFont() {
        UIManager.put("Menu.font", TransConst.GLOBAL_FONT);
        UIManager.put("MenuItem.font", TransConst.GLOBAL_FONT);
    }

    public TranspathMenuBar(TranspathFrame transpathFrame) {
        this.transpathFrame = transpathFrame;

        setFont();

        this.add(createSysMenu());
        this.add(createTaskMenu());
        this.add(createStoreMenu());
        this.add(createPubMenu());
        this.add(createHelpMenu());

    }

}
