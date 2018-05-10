/**
 * Copyright (c) 2015,qxu.
 * All Rights Reserved.
 * <p>
 * Project Name:transpath
 * Package Name:com.qxu.transpath.ui
 * File Name:TranspathFrame.java
 * Date:2015-11-16 上午12:59:46
 */
package com.skylynx13.transpath.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.dnd.DropTarget;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.pub.PubList;
import com.skylynx13.transpath.store.StoreList;
import com.skylynx13.transpath.tree.NodeList;
import com.skylynx13.transpath.tree.NodeTree;
import com.skylynx13.transpath.utils.FileUtils;
import com.skylynx13.transpath.utils.TransConst;
import com.skylynx13.transpath.utils.TransProp;

/**
 * ClassName: TranspathFrame <br/>
 * Description: Transpath Main Frame <br/>
 * Date: 2015-11-16 上午12:59:46 <br/>
 * <br/>
 *
 * @author qxu@
 * <p>
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 */

public class TranspathFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private static JTextArea logTextArea = new JTextArea();

    private static JTextArea infoTextArea = new JTextArea("information");

    private static JTable infoTable = new JTable();

    private static StoreList currStoreList = null;

    private static PubList currPubList = null;

    public static StoreList getCurrStoreList() {
        if (null == currStoreList) {
            currStoreList = new StoreList(FileUtils.storeNameOfVersion(TransProp.get(TransConst.VER_CURR)));
        }
        return currStoreList;
    }

    public static PubList getCurrPubList() {
        if (null == currPubList) {
            currPubList = new PubList(FileUtils.pubNameOfVersion(TransProp.get(TransConst.VER_CURR)));
        }
        return currPubList;
    }

    public static JTable getInfoTable() {
        return infoTable;
    }

    public static JTextArea getLogTextArea() {
        return logTextArea;
    }

    public static JTextArea getInfoTextArea() {
        return infoTextArea;
    }

    public TranspathFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) screenSize.getWidth() * 2 / 3, (int) screenSize.getHeight() * 2 / 3);
        this.setTitle("Storage Archivist");
        this.setIconImage(new ImageIcon(TransProp.get(TransConst.LOC_CONFIG) + "star16.png").getImage());
        this.setJMenuBar(new TranspathMenuBar(this));
        initPanel();
    }

    public static void searchList(String searchText) {
        TransLog.getLogger().info("Searching for \"" + searchText + "\" ... ... ... ...");
        StoreList aList = getCurrStoreList();
        StoreList sList = (StoreList) aList.searchName(searchText);
        setInfoTable(sList);
        TransLog.getLogger().info(sList.toString());
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
        return createTree(getCurrStoreList());
    }

    private JTree createCurrentPubTree() {
        return createTree(getCurrPubList());
    }

    private JTree createTree(NodeList nodeList) {
        NodeTree nodeTree = NodeTree.buildFromList(nodeList);
        nodeTree.recursivelySort();

        JTree jTree = new JTree(nodeTree);
        jTree.setShowsRootHandles(true);
        jTree.setRootVisible(true);
        jTree.setEditable(true);
        jTree.setDragEnabled(true);
        jTree.setDropTarget(new DropTarget());
        jTree.setFont(TransConst.GLOBAL_FONT);
        jTree.addMouseListener(new TreeMouseListener(jTree));

        return jTree;
    }

    public static void setInfoTable(NodeList nodeList) {
        DefaultTableModel tableModel = null;
        if (nodeList instanceof StoreList) {
            tableModel = new DefaultTableModel(((StoreList)nodeList).toRows(), TransConst.TABLE_TITLE_STORE);
        } else if (nodeList instanceof PubList) {
            tableModel = new DefaultTableModel(((PubList) nodeList).toRows(), TransConst.TABLE_TITLE_PUB);
        }
        setInfoTable(tableModel);
    }

    public static void setInfoTable(NodeTree selectTree) {
        DefaultTableModel tableModel = new DefaultTableModel(selectTree.getChildrenAsRows(), selectTree.getChildrenTitle());
        setInfoTable(tableModel);
    }

    public static void setInfoTable(DefaultTableModel tableModel) {
        if (null == tableModel) {
            return;
        }
        infoTable.setModel(tableModel);

        columnSizeFitContents(infoTable);

        try {
            DefaultTableCellRenderer alignRight = new DefaultTableCellRenderer();
            alignRight.setHorizontalAlignment(JLabel.RIGHT);
            infoTable.getColumn("Id").setCellRenderer(alignRight);
            infoTable.getColumn("Length").setCellRenderer(alignRight);
        } catch (IllegalArgumentException e) {
        }
    }

    private JTabbedPane createInfoTabbedPane() {
        JTabbedPane infoTabbedPane = createTabbedPane();
        infoTabbedPane.addTab("Info Table", createScrollPane(infoTable));
        infoTabbedPane.addTab("Info Page", createScrollPane(infoTextArea));
        return infoTabbedPane;
    }

    public static void columnSizeFitContents(JTable table) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int column = 0; column < table.getColumnCount(); column++) {
            int maxComponentWidth = 0;
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer rend = table.getCellRenderer(row, column);
                Object value = table.getValueAt(row, column);
                Component component = rend.getTableCellRendererComponent(table, value, false, false, row, column);
                maxComponentWidth = Math.max(component.getPreferredSize().width, maxComponentWidth);
            }
            TableCellRenderer headerRenderer = table.getColumnModel().getColumn(column).getHeaderRenderer();
            if (headerRenderer == null) {
                headerRenderer = table.getTableHeader().getDefaultRenderer();
            }
            Object headerValue = table.getColumnModel().getColumn(column).getHeaderValue();
            Component headerComp = headerRenderer.getTableCellRendererComponent(table, headerValue, false, false, 0, column);
            maxComponentWidth = Math.max(maxComponentWidth, headerComp.getPreferredSize().width);
            table.getColumnModel().getColumn(column).setPreferredWidth(maxComponentWidth + table.getIntercellSpacing().width);
        }
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
