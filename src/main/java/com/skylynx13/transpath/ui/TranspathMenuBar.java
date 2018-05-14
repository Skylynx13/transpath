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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.*;

import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.pub.PubKeeper;
import com.skylynx13.transpath.store.StoreKeeper;
import com.skylynx13.transpath.task.NameEditor;
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

    private ExecutorService transpathMenuAction = Executors.newCachedThreadPool(new ThreadFactory() {

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "TranspathMenuAction");
        }
    });

    private JMenu createSysMenu() {
        JMenu sysMenu = new JMenu("Sys");

        JMenuItem sysReloadItem = new JMenuItem("Reload");
        sysMenu.add(sysReloadItem);
        sysReloadItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transpathFrame.initPanel();
            }
        });

        sysMenu.add(new JSeparator());

        JMenuItem sysExitItem = new JMenuItem("Exit");
        sysMenu.add(sysExitItem);
        sysExitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transpathFrame.exit();
            }
        });
        return sysMenu;
    }

    private JMenu createTaskMenu() {
        JMenu taskMenu = new JMenu("Task");

        JMenuItem taskWeekItem = new JMenuItem("Weekly Digging");
        taskMenu.add(taskWeekItem);
        taskWeekItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transpathMenuAction.submit(new Runnable() {
                    @Override
                    public void run() {
                        TaskKeeper.weekFresh();
                        TaskKeeper.digNewFresh();
                    }
                });
            }
        });

        JMenuItem taskSpecItem = new JMenuItem("Spec Digging");
        taskMenu.add(taskSpecItem);
        taskSpecItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transpathMenuAction.submit(new Runnable() {
                    @Override
                    public void run() {
                        TaskKeeper.digSpecFresh();
                    }
                });
            }
        });

        JMenuItem taskNameItem = new JMenuItem("Name Revise");
        taskMenu.add(taskNameItem);
        taskNameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transpathMenuAction.submit(new Runnable() {
                    @Override
                    public void run() {
                        NameEditor.rename();
                    }
                });
            }
        });

        JMenuItem taskPackageItem = new JMenuItem("Package Check");
        taskMenu.add(taskPackageItem);
        taskPackageItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transpathMenuAction.submit(new Runnable() {
                    @Override
                    public void run() {
                        CompressUtils.unrar("D:/temp/test.rar", "d:/temp/test01/");
                    }
                });
            }
        });
        return taskMenu;
    }

    private JMenu createStoreMenu() {
        JMenu storeMenu = new JMenu("Store");

        JMenuItem storeCombineItem = new JMenuItem("Combine");
        storeMenu.add(storeCombineItem);
        storeCombineItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transpathMenuAction.submit(new Runnable() {
                    @Override
                    public void run() {
                        StoreKeeper.buildCombinedList();
                    }
                });
            }
        });

        JMenuItem storeCombineTestItem = new JMenuItem("CombineTest");
        storeMenu.add(storeCombineTestItem);
        storeCombineTestItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transpathMenuAction.submit(new Runnable() {
                    @Override
                    public void run() {
                        StoreKeeper.testCombinedList();
                    }
                });
            }
        });

        storeMenu.add(new JSeparator());

        JMenuItem storeSearchItem = new JMenuItem("Search...");
        storeMenu.add(storeSearchItem);
        storeSearchItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transpathMenuAction.submit(new Runnable() {
                    @Override
                    public void run() {
                        String searchText = JOptionPane.showInputDialog("Search Text:");
                        TranspathFrame.searchList(searchText);
                    }
                });
            }
        });
        storeSearchItem.setAccelerator(KeyStroke.getKeyStroke('F', InputEvent.CTRL_MASK));

        return storeMenu;
    }

    private JMenu createPubMenu() {
        JMenu pubMenu = new JMenu("Pub");

        JMenuItem pubRefreshItem = new JMenuItem("Refresh");
        pubMenu.add(pubRefreshItem);
        pubRefreshItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transpathMenuAction.submit(new Runnable() {
                    @Override
                    public void run() {
                        PubKeeper.refreshPubList();
                    }
                });
            }
        });
        return pubMenu;
    }

    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu("Help");

        JMenuItem helpAboutItem = new JMenuItem("About");
        helpMenu.add(helpAboutItem);
        helpAboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transpathMenuAction.submit(new Runnable() {
                    @Override
                    public void run() {
                        TransLog.getLogger().error("It's ok anyway.");
                        TransLog.getLogger().info("Info can be seen.");
                    }
                });
            }
        });
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
