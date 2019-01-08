package com.skylynx13.transpath.ui;

import com.skylynx13.transpath.store.StoreKeeper;
import com.skylynx13.transpath.task.NameReviser;
import com.skylynx13.transpath.task.PackageChecker;
import com.skylynx13.transpath.task.TaskKeeper;
import com.skylynx13.transpath.utils.TransConst;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassName: TransMenuBar
 * Description: Transpath menu bar
 * Date: 2017-10-14 16:00:27
 */
public class TranspathMenuBar extends JMenuBar {

    private static final long serialVersionUID = 1L;

    private TranspathFrame transpathFrame;
    private JDialog aboutDialog;

    private ExecutorService transpathMenuAction =
            Executors.newCachedThreadPool(r -> new Thread(r, "TranspathMenuAction"));

    public TranspathMenuBar(TranspathFrame transpathFrame) {
        this.transpathFrame = transpathFrame;

        setFont();

        this.add(createSysMenu());
        this.add(createTaskMenu());
        this.add(createStoreMenu());
        this.add(createHelpMenu());

    }

    private void setFont() {
        UIManager.put("Menu.font", TransConst.GLOBAL_FONT);
        UIManager.put("MenuItem.font", TransConst.GLOBAL_FONT);
    }

    private JMenu createSysMenu() {
        JMenu sysMenu = new JMenu("Sys");

        JMenuItem sysReloadItem = new JMenuItem("Reload");
        sysMenu.add(sysReloadItem);
        sysReloadItem.addActionListener(e -> transpathFrame.reloadStore());

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

        JMenuItem taskPackageItem = new JMenuItem("Package Check");
        taskMenu.add(taskPackageItem);
        taskPackageItem.addActionListener(e -> transpathMenuAction.submit(PackageChecker::check));
        taskPackageItem.setAccelerator(KeyStroke.getKeyStroke('K', InputEvent.CTRL_DOWN_MASK));

        JMenuItem taskNameItem = new JMenuItem("Name Revise");
        taskMenu.add(taskNameItem);
        taskNameItem.addActionListener(e -> transpathMenuAction.submit(NameReviser::rename));
        taskNameItem.setAccelerator(KeyStroke.getKeyStroke('R', InputEvent.CTRL_DOWN_MASK));

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
        storeSearchItem.addActionListener(e -> {
            String searchText = JOptionPane.showInputDialog("Search Text:");
            if (searchText != null) {
                transpathFrame.searchList(searchText);
            }
        });
        storeSearchItem.setAccelerator(KeyStroke.getKeyStroke('F', InputEvent.CTRL_DOWN_MASK));

        JMenuItem fetchSelectedItem = new JMenuItem("Fetch Selected");
        storeMenu.add(fetchSelectedItem);
        fetchSelectedItem.addActionListener(e -> transpathFrame.fetchSelectedStores());

        return storeMenu;
    }

    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu("Help");

        JMenuItem helpAboutItem = new JMenuItem("About");
        helpMenu.add(helpAboutItem);
        helpAboutItem.addActionListener(e -> showAboutDialog());
        return helpMenu;
    }

    private void showAboutDialog() {
        if (aboutDialog == null) {
            aboutDialog = new JDialog(this.transpathFrame, "About TransPath", true);
            aboutDialog.add(
                    new JLabel("<html><h2>TransPath is here anyway.</h2><hr>By Skylynx13</html>"),
                    BorderLayout.CENTER
            );
            JButton okButton = new JButton("OK");
            okButton.addActionListener(e -> aboutDialog.setVisible(false));
            JPanel okPanel = new JPanel();
            okPanel.add(okButton);
            aboutDialog.add(okPanel, BorderLayout.SOUTH);
            aboutDialog.setSize(250, 150);
        }
        aboutDialog.setVisible(true);
    }
}
