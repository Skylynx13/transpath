package com.skylynx13.transpath.ui;

import com.skylynx13.transpath.Transpath;
import com.skylynx13.transpath.store.StoreBrowser;
import com.skylynx13.transpath.store.StoreOldCombiner;
import com.skylynx13.transpath.store.StoreNameCombiner;
import com.skylynx13.transpath.store.StoreNewCombiner;
import com.skylynx13.transpath.task.NameReviser;
import com.skylynx13.transpath.task.PackageChecker;
import com.skylynx13.transpath.task.TaskKeeper;
import com.skylynx13.transpath.utils.TransConst;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: TransMenuBar
 * Description: Transpath menu bar
 * Date: 2017-10-14 16:00:27
 * @author skylynx
 */
public class TranspathMenuBar extends JMenuBar {

    private static final long serialVersionUID = 1L;

    private final ThreadPoolExecutor transpathTasks =
            new ThreadPoolExecutor(
                    0,
                    10,
                    0L,
                    TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(2),
                    new ThreadPoolExecutor.AbortPolicy());

    public TranspathMenuBar() {
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
        sysReloadItem.addActionListener(e -> Transpath.getTranspathFrame().reloadStore());

        JMenuItem sysPropertiesItem = new JMenuItem("Properties...");
        sysMenu.add(sysPropertiesItem);
        sysPropertiesItem.addActionListener(e -> new PropertiesDialog().setVisible(true));

        sysMenu.add(new JSeparator());

        JMenuItem sysExitItem = new JMenuItem("Exit");
        sysMenu.add(sysExitItem);
        sysExitItem.addActionListener(e -> Transpath.getTranspathFrame().exit());
        return sysMenu;
    }

    private JMenu createTaskMenu() {
        JMenu taskMenu = new JMenu("Task");

        JMenuItem taskWeekItem = new JMenuItem("Weekly Digging");
        taskMenu.add(taskWeekItem);
        taskWeekItem.addActionListener(e -> transpathTasks.submit(() -> {
            TaskKeeper.weekFresh();
            TaskKeeper.digNewFresh();
        }));

        JMenuItem taskSpecItem = new JMenuItem("Spec Digging");
        taskMenu.add(taskSpecItem);
        taskSpecItem.addActionListener(e -> transpathTasks.submit(TaskKeeper::digSpecFresh));

        JMenuItem taskPackageItem = new JMenuItem("Package Check");
        taskMenu.add(taskPackageItem);
        taskPackageItem.addActionListener(e -> new PackageChecker().execute());
        taskPackageItem.setAccelerator(KeyStroke.getKeyStroke('K', InputEvent.CTRL_DOWN_MASK));

        JMenuItem taskNameItem = new JMenuItem("Name Revise");
        taskMenu.add(taskNameItem);
        taskNameItem.addActionListener(e -> transpathTasks.submit(NameReviser::rename));
        taskNameItem.setAccelerator(KeyStroke.getKeyStroke('R', InputEvent.CTRL_DOWN_MASK));

        return taskMenu;
    }

    private JMenu createStoreMenu() {
        JMenu storeMenu = new JMenu("Store");

        JMenuItem storeOldCombineItem = new JMenuItem("OldCombine");
        storeMenu.add(storeOldCombineItem);
        storeOldCombineItem.addActionListener(e -> new StoreOldCombiner(true).execute());

        JMenuItem storeNewCombineItem = new JMenuItem("NewCombine");
        storeMenu.add(storeNewCombineItem);
        storeNewCombineItem.addActionListener(e -> new StoreNewCombiner(true).execute());

        JMenuItem storeNameCombineItem = new JMenuItem("NameCombine");
        storeMenu.add(storeNameCombineItem);
        storeNameCombineItem.addActionListener(e -> new StoreNameCombiner().execute());

        JMenuItem storeBrowseItem = new JMenuItem("Browse");
        storeMenu.add(storeBrowseItem);
        storeBrowseItem.addActionListener(e -> new StoreBrowser().execute());

        JMenuItem storeOldCombineTestItem = new JMenuItem("OldCombineTest");
        storeMenu.add(storeOldCombineTestItem);
        storeOldCombineTestItem.addActionListener(e -> new StoreOldCombiner(false).execute());
        storeOldCombineTestItem.setAccelerator(KeyStroke.getKeyStroke('T', InputEvent.CTRL_DOWN_MASK));

        JMenuItem storeNewCombineTestItem = new JMenuItem("NewCombineTest");
        storeMenu.add(storeNewCombineTestItem);
        storeNewCombineTestItem.addActionListener(e -> new StoreNewCombiner(false).execute());
        storeNewCombineTestItem.setAccelerator(KeyStroke.getKeyStroke('Y', InputEvent.CTRL_DOWN_MASK));

        JMenuItem storeExamineItem = new JMenuItem("Examine");
        storeMenu.add(storeExamineItem);
        storeExamineItem.addActionListener(e -> {
            new StoreNewCombiner(false).execute();
            new PackageChecker().execute();
        });
        storeExamineItem.setAccelerator(KeyStroke.getKeyStroke('E', InputEvent.CTRL_DOWN_MASK));

        storeMenu.add(new JSeparator());

        JMenuItem storeSearchItem = new JMenuItem("Search...");
        storeMenu.add(storeSearchItem);
        storeSearchItem.addActionListener(e -> {
            String searchText = JOptionPane.showInputDialog("Search Text:");
            if (searchText != null) {
                Transpath.getTranspathFrame().searchList(searchText);
            }
        });
        storeSearchItem.setAccelerator(KeyStroke.getKeyStroke('F', InputEvent.CTRL_DOWN_MASK));

        JMenuItem fetchSelectedItem = new JMenuItem("Fetch Selected");
        storeMenu.add(fetchSelectedItem);
        fetchSelectedItem.addActionListener(e -> Transpath.getTranspathFrame().fetchSelectedStores());

        return storeMenu;
    }

    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu("Help");

        JMenuItem helpAboutItem = new JMenuItem("About");
        helpMenu.add(helpAboutItem);
        helpAboutItem.addActionListener(e -> new AboutDialog().setVisible(true));
        return helpMenu;
    }
}
