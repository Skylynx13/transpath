/**
 * Copyright (c) 2015,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.ui
 * File Name:TranspathFrame.java
 * Date:2015-11-16 上午12:59:46
 * 
 */
package com.libra42.transpath.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

import com.libra42.transpath.log.TransLog;
import com.libra42.transpath.pub.PubKeeper;
import com.libra42.transpath.pub.PubList;
import com.libra42.transpath.store.StoreKeeper;
import com.libra42.transpath.store.StoreList;
import com.libra42.transpath.task.NameEditor;
import com.libra42.transpath.task.TaskKeeper;
import com.libra42.transpath.tree.NodeTree;
import com.libra42.transpath.utils.CompressUtils;
import com.libra42.transpath.utils.FileUtils;
import com.libra42.transpath.utils.TransConst;
import com.libra42.transpath.utils.TransProp;

/**
 * ClassName: TranspathFrame <br/>
 * Description: Transpath Main Frame <br/>
 * Date: 2015-11-16 上午12:59:46 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 *         Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class TranspathFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final Font GLOBAL_FONT = new Font("GLOBAL", Font.PLAIN, TransProp.getInt(TransConst.SIZE_TEXT));

    private static JTextArea logTextArea = new JTextArea();

    public static JTextArea getLogTextArea() {
        return logTextArea;
    }

    public TranspathFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) screenSize.getWidth() * 2 / 3, (int) screenSize.getHeight() * 2 / 3);
        this.setTitle("Storage Archivist");
        this.setIconImage(new ImageIcon(TransProp.get(TransConst.LOC_CONFIG) + "star16.png").getImage());
        initMenuBar();
        initJTree();
    }

    private ExecutorService service = Executors.newCachedThreadPool(new ThreadFactory() {

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "output");
        }
    });

    public void initMenuBar() {
        UIManager.put("Menu.font", GLOBAL_FONT);
        UIManager.put("MenuItem.font", GLOBAL_FONT);
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu sysMenu = new JMenu("Sys");
        menuBar.add(sysMenu);

        JMenuItem sysReloadItem = new JMenuItem("Reload");
        sysMenu.add(sysReloadItem);
        sysReloadItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initJTree();
            }
        });

        sysMenu.add(new JSeparator());

        JMenuItem sysExitItem = new JMenuItem("Exit");
        sysMenu.add(sysExitItem);
        sysExitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });

        JMenu taskMenu = new JMenu("Task");
        menuBar.add(taskMenu);

        JMenuItem taskWeekItem = new JMenuItem("Weekly Digging");
        taskMenu.add(taskWeekItem);
        taskWeekItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                service.submit(new Runnable() {
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
                service.submit(new Runnable() {
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
                service.submit(new Runnable() {
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
                service.submit(new Runnable() {
                    @Override
                    public void run() {
                        CompressUtils.unrar("D:/temp/test.rar", "d:/temp/test01/");
                    }
                });
            }
        });

        JMenu storeMenu = new JMenu("Store");
        menuBar.add(storeMenu);

        JMenuItem storeCombineItem = new JMenuItem("Combine");
        storeMenu.add(storeCombineItem);
        storeCombineItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                service.submit(new Runnable() {
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
                service.submit(new Runnable() {
                    @Override
                    public void run() {
                        StoreKeeper.testCombinedList();
                    }
                });
            }
        });

        JMenu pubMenu = new JMenu("Pub");
        menuBar.add(pubMenu);

        JMenuItem pubRefreshItem = new JMenuItem("Refresh");
        pubMenu.add(pubRefreshItem);
        pubRefreshItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                service.submit(new Runnable() {
                    @Override
                    public void run() {
                        PubKeeper.refreshPubList();
                    }
                });
            }
        });

        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        JMenuItem helpAboutItem = new JMenuItem("About");
        helpMenu.add(helpAboutItem);
        helpAboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                service.submit(new Runnable() {
                    @Override
                    public void run() {
                        TransLog.getLogger().error("It's ok anyway.");
                        TransLog.getLogger().info("Info can be seen.");
                    }
                });
            }
        });
    }

    public void initJTree() {
        JPanel contentPanel = (JPanel) this.getContentPane();
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        setLookAndFeel();

        String storeNameOfVersion = FileUtils.storeNameOfVersion(TransProp.get(TransConst.VER_CURR));
        StoreList storeList = new StoreList(storeNameOfVersion);
        TransLog.getLogger().info("List: " + storeNameOfVersion + " loaded.");
        NodeTree storeNodeTree = NodeTree.buildFromList(storeList);
        storeNodeTree.recursivelySort();
        JTree storeTree = new JTree(storeNodeTree);
        JScrollPane storeScrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        storeScrollPane.setViewportView(storeTree);
        storeTree.setFont(GLOBAL_FONT);
        storeTree.revalidate();

        PubList pubList = new PubList(FileUtils.pubNameOfVersion(TransProp.get(TransConst.VER_CURR)));
        NodeTree pubNodeTree = NodeTree.buildFromList(pubList);
        pubNodeTree.recursivelySort();
        JTree pubTree = new JTree(pubNodeTree);

        pubTree.setShowsRootHandles(true);
        pubTree.setRootVisible(true);
        pubTree.setEditable(true);

        JScrollPane pubScrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pubScrollPane.setViewportView(pubTree);
        pubTree.setFont(GLOBAL_FONT);
        pubTree.revalidate();

        JTabbedPane treeTabbedPane = new JTabbedPane();
        treeTabbedPane.setTabPlacement(JTabbedPane.TOP);
        treeTabbedPane.addTab("Store Tree", storeScrollPane);
        treeTabbedPane.addTab("Pub Tree", pubScrollPane);

        Object[][] tableData = storeList.toRows();
        String[] tableTitle = { "Id", "Length", "Update Time", "MD5", "SHA", "CRC32", "Store Path", "Name" };
        DefaultTableModel tableModel = new DefaultTableModel(tableData, tableTitle);

        JTable infoTable = new JTable(tableModel);
        JScrollPane infoTableScrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        infoTableScrollPane.setViewportView(infoTable);

        JTextArea infoText = new JTextArea();
        infoText.setText("information");
        JScrollPane infoTextScrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        infoTextScrollPane.setViewportView(infoText);

        JTabbedPane infoTabbedPane = new JTabbedPane();
        infoTabbedPane.setTabPlacement(JTabbedPane.TOP);
        infoTabbedPane.addTab("Info Table", infoTableScrollPane);
        infoTabbedPane.addTab("Info Page", infoTextScrollPane);

        JSplitPane upperSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeTabbedPane, infoTabbedPane);
        upperSplitPane.setContinuousLayout(true);
        upperSplitPane.setOneTouchExpandable(true);
        upperSplitPane.setSize(super.getSize());
        upperSplitPane.setDividerLocation(0.2);
        upperSplitPane.setDividerSize(8);

        JScrollPane logScrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        logScrollPane.setFont(GLOBAL_FONT);
        logScrollPane.setViewportView(logTextArea);

        JTabbedPane lowerTabbedPane = new JTabbedPane();
        lowerTabbedPane.setTabPlacement(JTabbedPane.TOP);
        lowerTabbedPane.addTab("TransLog", logScrollPane);
        lowerTabbedPane.addTab("test", new JTextArea());

        JSplitPane allSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperSplitPane, lowerTabbedPane);
        allSplitPane.setContinuousLayout(true);
        allSplitPane.setOneTouchExpandable(true);
        allSplitPane.setSize(super.getSize());
        allSplitPane.setDividerLocation(0.7);
        allSplitPane.setDividerSize(8);

        contentPanel.add(allSplitPane, BorderLayout.CENTER);

        this.setVisible(true);
    }

    private void setLookAndFeel() {
        UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            exit();
        }
    }

    private void exit() {
        TransLog.getLogger().info("Transpath Exit.");
        System.exit(0);
    }

    public static void main(String[] args) {
        new TranspathFrame();
    }

}
