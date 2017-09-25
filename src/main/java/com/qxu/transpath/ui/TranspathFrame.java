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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.DefaultMutableTreeNode;

import com.qxu.transpath.tree.NodeTree;
import com.qxu.transpath.tree.PubList;
import com.qxu.transpath.tree.SimpleNode;
import com.qxu.transpath.tree.StoreList;
import com.qxu.transpath.utils.FileUtils;
import com.qxu.transpath.utils.TransLog;
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

    public TranspathFrame() {
        this.setSize(1200, 600);
        this.setTitle("Storage Archivist");
        initMenuBar();
        initJTree();
    }

    @SuppressWarnings({ "serial", "unused" })
    public void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        
        JMenu sysMenu = new JMenu("Sys");
        menuBar.add(sysMenu);
        
        Action reloadAction = new AbstractAction("Reload") {
            @Override
            public void actionPerformed(ActionEvent e) {
                initJTree();
            }
        };
        JMenuItem sysReloadItem = sysMenu.add(reloadAction);
        
        Action exitAction = new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
        JMenuItem sysExitItem = sysMenu.add(exitAction);
        
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
        
        JSeparator taskSep = new JSeparator();
        taskMenu.add(taskSep);
        
        JMenuItem taskNameItem = new JMenuItem("Name Revise");
        taskMenu.add(taskNameItem);
        ActionListener listenerName = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NameEditor.renameNormalize();
                NameEditor.renameSpecialReplace();
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

        String storeNameOfVersion = FileUtils.storeNameOfVersion(TransProp.get("CURR_VER"));
        StoreList aList = new StoreList(storeNameOfVersion);
        TransLog.getLogger().info("List: " + storeNameOfVersion + " loaded.");
        NodeTree nodeTreeLeft = NodeTree.buildFromList(aList);
        nodeTreeLeft.recursivelySort();
        JTree jTreeLeft = new JTree(nodeTreeLeft);
        JScrollPane spaneLeft = new JScrollPane(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        spaneLeft.setViewportView(jTreeLeft);
        jTreeLeft.revalidate();
        
        PubList bList = new PubList(FileUtils.pubNameOfVersion(TransProp.get("CURR_VER")));
        NodeTree nodeTreeRight = NodeTree.buildFromList(bList);
        nodeTreeRight.recursivelySort();
        JTree jTreeRight = new JTree(nodeTreeRight);

        jTreeRight.setShowsRootHandles(true);
        jTreeRight.setRootVisible(true);
        jTreeRight.setEditable(true);
        
        JScrollPane spaneRight = new JScrollPane(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        spaneRight.setViewportView(jTreeRight);
        jTreeRight.revalidate();
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, spaneLeft, spaneRight);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setSize(super.getSize());
        splitPane.setDividerLocation(0.5);
        splitPane.setDividerSize(8);
        contentPanel.add(splitPane, BorderLayout.CENTER);
        
        this.setVisible(true);
    }

    private void setLookAndFeel() {
        UIManager.getSystemLookAndFeelClassName();
        try {
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
    
    @SuppressWarnings("unused")
    private NodeTree buildTestTree() {
        NodeTree tree1 = new NodeTree(new SimpleNode("tree1"));
        NodeTree node1 = new NodeTree(new SimpleNode("node1"));
        tree1.addChild(node1);
        NodeTree node2 = new NodeTree(new SimpleNode("node2"));
        tree1.addChild(node2);
        NodeTree node3 = new NodeTree(new SimpleNode("node3"));
        NodeTree tree2 = new NodeTree(new SimpleNode("tree2"));
        tree2.addChild(node3);
        tree1.addChild(tree2);
        node3.addChild(new NodeTree(new SimpleNode("node4")));
        return tree1;
    }

    @SuppressWarnings("unused")
    private void createTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("school");
        DefaultMutableTreeNode classroom = null;
        DefaultMutableTreeNode number = null;
        classroom = new DefaultMutableTreeNode("classroom");
        root.add(classroom);
        for (int i = 1; i <= 8; i++) {
            number = new DefaultMutableTreeNode("No." + String.valueOf(i));
            if (i == 4) {
                for (int j = 1; j <= 5; j++) {
                    number.add(new DefaultMutableTreeNode("seat"
                            + String.valueOf(j)));
                }
            }
            classroom.add(number);
        }
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        new TranspathFrame();
    }

}
