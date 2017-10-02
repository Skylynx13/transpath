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
package com.qxu.transpath.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
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
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import com.qxu.transpath.log.TransLog;
import com.qxu.transpath.tree.NodeTree;
import com.qxu.transpath.tree.PubList;
import com.qxu.transpath.tree.StoreList;
import com.qxu.transpath.utils.FileUtils;
import com.qxu.transpath.utils.TransConst;
import com.qxu.transpath.utils.TransProp;
import com.qxu.transpath.worker.NameEditor;
import com.qxu.transpath.worker.PubKeeper;
import com.qxu.transpath.worker.StoreKeeper;
import com.qxu.transpath.worker.TaskKeeper;

 /**
 * ClassName: TranspathFrame <br/>
 * Description: Transpath Main Frame <br/>
 * Date: 2015-11-16 上午12:59:46 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
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
        this.setSize((int)screenSize.getWidth()*2/3, (int)screenSize.getHeight()*2/3);
        this.setTitle("Storage Archivist");
        this.setIconImage(new ImageIcon(TransProp.get(TransConst.LOC_CONFIG)+"star16.png").getImage());
        initMenuBar();
        initJTree();
    }

    public void initMenuBar() {
        UIManager.put("Menu.font", GLOBAL_FONT);
        UIManager.put("MenuItem.font", GLOBAL_FONT);
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        
        JMenu sysMenu = new JMenu("Sys");
        menuBar.add(sysMenu);
        
        JMenuItem sysReloadItem = new JMenuItem("Reload");
        sysMenu.add(sysReloadItem);
        ActionListener reloadListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initJTree();
            }
        };
        sysReloadItem.addActionListener(reloadListener);
        
        JSeparator sysSep = new JSeparator();
        sysMenu.add(sysSep);
        
        JMenuItem sysExitItem = new JMenuItem("Exit");
        sysMenu.add(sysExitItem);
        ActionListener exitListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        };
        sysExitItem.addActionListener(exitListener);
        
        JMenu taskMenu = new JMenu("Task");
        menuBar.add(taskMenu);      
        
        JMenuItem taskWeekItem = new JMenuItem("Weekly Digging");
        taskMenu.add(taskWeekItem);
        ActionListener listenerWeek = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TaskKeeper.weekFresh();
                TaskKeeper.digNewFresh();
            }
        };
        taskWeekItem.addActionListener(listenerWeek);
        
        JMenuItem taskSpecItem = new JMenuItem("Spec Digging");
        taskMenu.add(taskSpecItem);
        ActionListener listenerSpec = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TaskKeeper.digSpecFresh();
            }
        };
        taskSpecItem.addActionListener(listenerSpec);
        
        JMenuItem taskNameItem = new JMenuItem("Name Revise");
        taskMenu.add(taskNameItem);
        ActionListener listenerName = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NameEditor.rename();
            }
        };
        taskNameItem.addActionListener(listenerName);

        JMenu storeMenu = new JMenu("Store");
        menuBar.add(storeMenu);
        
        JMenuItem storeCombineItem = new JMenuItem("Combine");
        storeMenu.add(storeCombineItem);
        storeCombineItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StoreKeeper.buildCombinedList();
            }
        });
        
        JMenuItem storeCombineTestItem = new JMenuItem("CombineTest");
        storeMenu.add(storeCombineTestItem);
        storeCombineTestItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StoreKeeper.testCombinedList();
            }
        });
        
        JMenu pubMenu = new JMenu("Pub");
        menuBar.add(pubMenu);
        
        JMenuItem pubRefreshItem = new JMenuItem("Refresh");
        pubMenu.add(pubRefreshItem);
        pubRefreshItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PubKeeper.refreshPubList();
            }
        });
        
        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        JMenuItem helpAboutItem = new JMenuItem("About");
        helpMenu.add(helpAboutItem);
        helpAboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TransLog.getLogger().error("It's ok anyway.");
                TransLog.getLogger().info("Info can be seen.");
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
        JScrollPane storeScrollPane = new JScrollPane(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
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
        
        JScrollPane pubScrollPane = new JScrollPane(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pubScrollPane.setViewportView(pubTree);
        pubTree.setFont(GLOBAL_FONT);
        pubTree.revalidate();
        
        JTabbedPane treeTabbedPane = new JTabbedPane();
        treeTabbedPane.setTabPlacement(JTabbedPane.TOP);
        treeTabbedPane.addTab("Store Tree", storeScrollPane);
        treeTabbedPane.addTab("Pub Tree", pubScrollPane);

        JScrollPane infoScrollPane = new JScrollPane(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        infoScrollPane.setViewportView(new JTextArea());
       
        JSplitPane upperSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeTabbedPane, infoScrollPane);
        upperSplitPane.setContinuousLayout(true);
        upperSplitPane.setOneTouchExpandable(true);
        upperSplitPane.setSize(super.getSize());
        upperSplitPane.setDividerLocation(0.5);
        upperSplitPane.setDividerSize(8);
        
        JScrollPane logScrollPane = new JScrollPane(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
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
