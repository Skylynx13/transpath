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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
import com.libra42.transpath.pub.PubList;
import com.libra42.transpath.store.StoreList;
import com.libra42.transpath.tree.NodeList;
import com.libra42.transpath.tree.NodeTree;
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

    private static JTextArea logTextArea = new JTextArea();

    public static JTextArea getLogTextArea() {
        return logTextArea;
    }

    public TranspathFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) screenSize.getWidth() * 2 / 3, (int) screenSize.getHeight() * 2 / 3);
        this.setTitle("Storage Archivist");
        this.setIconImage(new ImageIcon(TransProp.get(TransConst.LOC_CONFIG) + "star16.png").getImage());
        this.setJMenuBar(new TranspathMenuBar(this));
        initPanel();
    }

    protected void initPanel() {
        JPanel contentPanel = (JPanel) this.getContentPane();
        contentPanel.removeAll();

        contentPanel.setLayout(new BorderLayout());
        setLookAndFeel();

        contentPanel.add(createAllSplitPane(), BorderLayout.CENTER);
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

    private JSplitPane createAllSplitPane() {
        return createSplitPane(JSplitPane.VERTICAL_SPLIT, createUpperSplitPane(), createLowerTabbedPane(), 0.7);
    }

    private JSplitPane createUpperSplitPane() {
        return createSplitPane(JSplitPane.HORIZONTAL_SPLIT, createTreeTabbedPane(), createInfoTabbedPane(), 0.2);
    }

    private JSplitPane createSplitPane(int newOrientation, Component newLeftComponent, Component newRightComponent,
            double proportionalLocation) {
        JSplitPane splitPane = new JSplitPane(newOrientation, newLeftComponent, newRightComponent);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setSize(super.getSize());
        splitPane.setDividerLocation(proportionalLocation);
        splitPane.setDividerSize(8);

        return splitPane;
    }

    private JTabbedPane createTreeTabbedPane() {
        JTabbedPane treeTabbedPane = createTabbedPane();
        treeTabbedPane.addTab("Store Tree", createScrollPane(createCurrentStoreTree()));
        treeTabbedPane.addTab("Pub Tree", createScrollPane(createCurrentPubTree()));

        TransLog.getLogger()
                .info("List: " + FileUtils.storeNameOfVersion(TransProp.get(TransConst.VER_CURR)) + " loaded.");

        return treeTabbedPane;
    }

    private JTree createCurrentStoreTree() {
        return createTree(new StoreList(FileUtils.storeNameOfVersion(TransProp.get(TransConst.VER_CURR))));
    }

    private JTree createCurrentPubTree() {
        return createTree(new PubList(FileUtils.pubNameOfVersion(TransProp.get(TransConst.VER_CURR))));
    }

    private JTree createTree(NodeList nodeList) {
        NodeTree nodeTree = NodeTree.buildFromList(nodeList);
        nodeTree.recursivelySort();

        JTree jTree = new JTree(nodeTree);
        jTree.setShowsRootHandles(true);
        jTree.setRootVisible(false);
        jTree.setEditable(true);
        jTree.setFont(TransConst.GLOBAL_FONT);

        return jTree;
    }

    private JTabbedPane createInfoTabbedPane() {
        // Object[][] tableData = new
        // StoreList(FileUtils.storeNameOfVersion(TransProp.get(TransConst.VER_CURR))).toRows();
        // String[] tableTitle = { "Id", "Length", "Update Time", "MD5", "SHA", "CRC32",
        // "Store Path", "Name" };
        // DefaultTableModel tableModel = new DefaultTableModel(tableData, tableTitle);
        // JTable infoTable = new JTable(tableModel);
        JTabbedPane infoTabbedPane = createTabbedPane();
        infoTabbedPane.addTab("Info Table", createScrollPane(null));
        infoTabbedPane.addTab("Info Page", createScrollPane(new JTextArea("information")));

        return infoTabbedPane;
    }

    private JTabbedPane createLowerTabbedPane() {
        JTabbedPane lowerTabbedPane = createTabbedPane();
        lowerTabbedPane.addTab("Console", createScrollPane(logTextArea));

        return lowerTabbedPane;
    }
    
    private JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        tabbedPane.setFont(TransConst.GLOBAL_FONT);
        return tabbedPane;
    }

    private JScrollPane createScrollPane(Component comp) {
        JScrollPane scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setFont(TransConst.GLOBAL_FONT);
        if (comp != null) {
            scrollPane.setViewportView(comp);
        }
        return scrollPane;
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            exit();
        }
    }

    protected void exit() {
        TransLog.getLogger().info("Transpath Exit.");
        System.exit(0);
    }

    public static void main(String[] args) {
        new TranspathFrame();
    }

}
