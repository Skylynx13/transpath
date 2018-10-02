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

import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.pub.LinkList;
import com.skylynx13.transpath.pub.PubList;
import com.skylynx13.transpath.store.StoreList;
import com.skylynx13.transpath.store.StoreNode;
import com.skylynx13.transpath.tree.Node;
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
    private static LinkList currLinkList = null;

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

    public static LinkList getCurrLinkList() {
        if (null == currLinkList) {
            currLinkList = new LinkList(FileUtils.linkNameOfVersion(TransProp.get(TransConst.VER_CURR)));
        }
        return currLinkList;
    }

    private void refreshLists() {
        currStoreList = new StoreList(FileUtils.storeNameOfVersion(TransProp.get(TransConst.VER_CURR)));
        currPubList = new PubList(FileUtils.pubNameOfVersion(TransProp.get(TransConst.VER_CURR)));
        currLinkList = new LinkList(FileUtils.linkNameOfVersion(TransProp.get(TransConst.VER_CURR)));
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
        this.setIconImage(new ImageIcon(TransProp.get(TransConst.LOC_CONFIG) + "star16.png").getImage());
        this.setJMenuBar(new TranspathMenuBar(this));
        initPanel();
    }

    public static void searchList(String searchText) {
        TransLog.getLogger().info("Searching for \"" + searchText + "\" ... ... ... ...");
        StoreList sList = getCurrStoreList().searchName(searchText);
        setInfoTable(sList);
        TransLog.getLogger().info(sList.toString());
    }

    protected void initPanel() {
        JPanel contentPanel = (JPanel) this.getContentPane();
        contentPanel.removeAll();

        contentPanel.setLayout(new BorderLayout());
        setLookAndFeel();

        refreshLists();

        contentPanel.add(createAllSplitPane(), BorderLayout.CENTER);

        contentPanel.add(createStatusBar(), BorderLayout.SOUTH);

        this.setTitle("Storage Archivist - " + TransProp.get(TransConst.VER_CURR));
        this.setVisible(true);
    }

    private JToolBar createStatusBar() {
        JToolBar statusBar = new JToolBar();
        statusBar.setFloatable(false);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setMaximum(100);
        progressBar.setMinimum(0);
        progressBar.setValue(88);
        progressBar.setStringPainted(true);
        progressBar.setIndeterminate(false);
        //Color change unavailable when using NimbusLookAndFeel;
        //progressBar.setBackground(Color.gray);
        //progressBar.setForeground(Color.green);
        statusBar.add(progressBar);

        statusBar.add(new JLabel("8,800 of 10,000 bytes processed. Status Normal."));

        return statusBar;
    }

    private void setLookAndFeel() {
        UIManager.getSystemLookAndFeelClassName();
        try {
            //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            //UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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

    private JTabbedPane createInfoTabbedPane() {
        JTabbedPane infoTabbedPane = createTabbedPane();
        infoTabbedPane.addTab("Info Table", createScrollPane(infoTable));
        infoTabbedPane.addTab("Info Page", createScrollPane(infoTextArea));
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

        columnAlignRight("StoreId");
        columnAlignRight("Length");
        columnAlignRight("PubId");
        columnAlignRight("Order");
        columnAlignRight("BranchId");
    }

    private static void columnAlignRight(String columnTitle) {
        DefaultTableCellRenderer alignRight = new DefaultTableCellRenderer();
        alignRight.setHorizontalAlignment(JLabel.RIGHT);
        try {
            infoTable.getColumn(columnTitle).setCellRenderer(alignRight);
        } catch (IllegalArgumentException e) {
        }
    }

    public static void columnSizeFitContents(JTable table) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setFont(TransConst.GLOBAL_FONT);
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

    public static void fetchSelectedStores() {
        int columnIndex = -1;
        columnIndex = getIdColumnIndex("StoreId");
        TransLog.getLogger().info("Column index = " + columnIndex);
        if (columnIndex != -1) {
            ArrayList<Integer> storeIdList = getSelectedIdList(columnIndex);
            fetchStore(storeIdList);
            return;
        }
        columnIndex = getIdColumnIndex("PubId");
        TransLog.getLogger().info("Column index = " + columnIndex);
        if (columnIndex != -1) {
            ArrayList<Integer> pubIdList = getSelectedIdList(columnIndex);
            ArrayList<Integer> storeIdList = TranspathFrame.getCurrLinkList().getStoreIdList(pubIdList);
            fetchStore(storeIdList);
            return;
        }
        TransLog.getLogger().info("No fetch performed.");
        return;
    }

    private static void fetchStore(ArrayList<Integer> storeIdList) {
        //get source path-name list
        NodeList sList = TranspathFrame.getCurrStoreList().getListByIds(storeIdList);
        //get target
        String sourceBase = TransProp.get(TransConst.LOC_SOURCE);
        String target = TransProp.get(TransConst.LOC_TARGET);
        //exec and feedback
        for (Node sNode : sList.nodeList) {
            String pathName = sNode.path.substring(1).replaceAll(TransConst.SLASH, TransConst.BACK_SLASH_4) + sNode.name;
            String cmd = TransConst.CMD_COPY_TO_TARGET + "\"" + sourceBase + pathName + "\" " + target;
            TransLog.getLogger().info("Command: " + cmd);
            //execCmd(cmd);
        }
    }

    private static void execCmd(String cmd) {
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            BufferedReader iReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(proc.getInputStream())));
            BufferedReader eReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(proc.getErrorStream())));
            String inLine;
            while ((inLine = iReader.readLine()) != null) {
                TransLog.getLogger().info("Return: " + inLine);
            }
            while ((inLine = eReader.readLine()) != null) {
                TransLog.getLogger().info("Error: " + inLine);
            }
            if (proc.waitFor() != 0) {
                if (proc.exitValue() == 1) {
                    TransLog.getLogger().info("Error executing.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Integer> getSelectedIdList(int columnIndex) {
        ArrayList<Integer> selectedIds = new ArrayList<>();
        for (int iSelectedRow : infoTable.getSelectedRows()) {
            int id = (Integer)infoTable.getValueAt(iSelectedRow, columnIndex);
            TransLog.getLogger().info("id is: " + id);
            selectedIds.add(id);
        }
        return selectedIds;
    }

    private static int getIdColumnIndex(String columnTitle) {
        try {
            return infoTable.getColumnModel().getColumnIndex(columnTitle);
        } catch (IllegalArgumentException e) {
            TransLog.getLogger().info(columnTitle + " not found.");
            return -1;
        }
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
