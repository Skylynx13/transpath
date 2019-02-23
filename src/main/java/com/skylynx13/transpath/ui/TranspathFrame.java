package com.skylynx13.transpath.ui;

import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.store.*;
import com.skylynx13.transpath.utils.FileUtils;
import com.skylynx13.transpath.utils.TransConst;
import com.skylynx13.transpath.utils.TransProp;

/**
 * ClassName: TranspathFrame
 * Description: Transpath Main Frame
 * Date: 2015-11-16 12:59:46
 */
public class TranspathFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final int DIVIDER_SIZE = 8;
    private static final int ZOOM_PERCENT = 66;
    private static final double SPLIT_V_DEFAULT = 0.7;
    private static final double SPLIT_H_DEFAULT = 0.2;

    private static JTable infoTable = new JTable();
    private static JTextArea logTextArea = new JTextArea();
    private static JTextArea infoTextArea = new JTextArea();
    private static JProgressBar progressBar = new JProgressBar();
    private static JLabel statusLabel = new JLabel();

    private StoreList storeList;
    private JScrollPane treePane;

    public static JTextArea getLogTextArea() {
        return logTextArea;
    }
    public static JProgressBar getProgressBar() {
        return progressBar;
    }
    public static JLabel getStatusLabel() {
        return statusLabel;
    }
    static JTextArea getInfoTextArea() {
        return infoTextArea;
    }

    private JScrollPane getTreePane() {
        if (treePane == null) {
            treePane = createScrollPane();
        }
        return treePane;
    }

    public TranspathFrame() {
        initSize();
        initIcon();
        initMenuBar();
        initLookAndFeel();
        initPanel();

        setTitle("Storage Archivist - " + storeList.getVersion());
        setVisible(true);
    }

    void reloadStore() {
        storeList = new StoreList(TransProp.get(TransConst.LOC_LIST) + "StoreList.txt");
        getTreePane().setViewportView(createTree(storeList));
        setTitle("Storage Archivist - " + storeList.getVersion());
        TransLog.getLogger().info("List of version " + storeList.getVersion() + " loaded.");
    }

    private JTree createTree(StoreList storeList) {
        StoreTree storeTree = StoreTree.buildFromList(storeList);
        storeTree.recursivelySort();

        JTree jTree = new JTree(storeTree);
        jTree.setShowsRootHandles(true);
        jTree.setRootVisible(true);
        jTree.setEditable(true);
        jTree.setDragEnabled(true);
        jTree.setDropTarget(new DropTarget());
        jTree.setFont(TransConst.GLOBAL_FONT);
        jTree.addMouseListener(new TreeMouseListener(jTree));

        return jTree;
    }

    private void initSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        super.setSize(
                (int) screenSize.getWidth() * ZOOM_PERCENT / 100,
                (int) screenSize.getHeight() * ZOOM_PERCENT / 100
        );
    }

    private void initIcon() {
        setIconImage(new ImageIcon(TransProp.get(TransConst.LOC_CONFIG) + "star16.png").getImage());
    }

    private void initMenuBar() {
        setJMenuBar(new TranspathMenuBar(this));
    }

    private void initLookAndFeel() {
        UIManager.getSystemLookAndFeelClassName();
        try {
            // Look and feel candidates:
            // "javax.swing.plaf.metal.MetalLookAndFeel"
            // "javax.swing.plaf.nimbus.NimbusLookAndFeel"
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException
                | InstantiationException
                | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private void initPanel() {
        JPanel mainPanel = (JPanel) getContentPane();
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());
        reloadStore();
        mainPanel.add(createAllSplitPane(), BorderLayout.CENTER);
        mainPanel.add(setupStatusBar(), BorderLayout.SOUTH);
    }

    private JSplitPane createAllSplitPane() {
        return createSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                createUpperSplitPane(),
                createScrollPane(logTextArea),
                SPLIT_V_DEFAULT
        );
    }

    private JSplitPane createUpperSplitPane() {
        return createSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                getTreePane(),
                createInfoTabbedPane(),
                SPLIT_H_DEFAULT
        );
    }

    private JTabbedPane createInfoTabbedPane() {
        JTabbedPane infoTabbedPane = createTabbedPane();
        infoTabbedPane.addTab("Content List", createScrollPane(infoTable));
        infoTabbedPane.addTab("Selected Details", createScrollPane(infoTextArea));
        return infoTabbedPane;
    }

    private JSplitPane createSplitPane(
            int newOrientation,
            Component newLeftComponent,
            Component newRightComponent,
            double proportionalLocation
    ) {
        JSplitPane splitPane = new JSplitPane(newOrientation, newLeftComponent, newRightComponent);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setSize(super.getSize());
        splitPane.setDividerLocation(proportionalLocation);
        splitPane.setDividerSize(DIVIDER_SIZE);

        return splitPane;
    }

    private JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        tabbedPane.setFont(TransConst.GLOBAL_FONT);
        return tabbedPane;
    }

    private JScrollPane createScrollPane(Component comp) {
        JScrollPane scrollPane = createScrollPane();
        scrollPane.setViewportView(comp);
        return scrollPane;
    }

    private JScrollPane createScrollPane() {
        JScrollPane scrollPane = new JScrollPane(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        scrollPane.setFont(TransConst.GLOBAL_FONT);
        return scrollPane;
    }

    private JToolBar setupStatusBar() {
        JToolBar statusBar = new JToolBar();
        statusBar.setLayout(new GridLayout());
        statusBar.setFloatable(false);

        progressBar.setMaximum(100);
        progressBar.setMinimum(0);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setIndeterminate(false);
        // Color change unavailable when using NimbusLookAndFeel;
        // progressBar.setBackground(Color.gray);
        // progressBar.setForeground(Color.green);
        statusBar.add(progressBar);

        statusLabel.setText("Status Normal.");
        statusBar.add(statusLabel);

        return statusBar;
    }

    private static void updateInfoTable(StoreList storeList) {
        updateInfoTable(new DefaultTableModel(storeList.toRows(), TransConst.TABLE_TITLE_STORE));
    }

    static void updateInfoTable(StoreTree selectTree) {
        updateInfoTable(new DefaultTableModel(selectTree.getChildrenAsRows(), selectTree.getChildrenTitle()));
    }

    private static void updateInfoTable(DefaultTableModel tableModel) {
        String[] rightAlignedTitles = {"StoreId", "Length", "BranchId"};
        infoTable.setModel(tableModel);
        columnSizeFitContents(infoTable);

        for (String rightAlignedTitle : rightAlignedTitles) {
            columnAlignRight(rightAlignedTitle);
        }
    }

    private static void columnAlignRight(String columnTitle) {
        DefaultTableCellRenderer alignRight = new DefaultTableCellRenderer();
        alignRight.setHorizontalAlignment(JLabel.RIGHT);
        try {
            infoTable.getColumn(columnTitle).setCellRenderer(alignRight);
        } catch (IllegalArgumentException ignore) {
        }
    }

    private static void columnSizeFitContents(JTable table) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setFont(TransConst.GLOBAL_FONT);
        for (int column = 0; column < table.getColumnCount(); column++) {
            int maxComponentWidth = 0;
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer rend = table.getCellRenderer(row, column);
                Object value = table.getValueAt(row, column);
                Component component = rend.getTableCellRendererComponent(
                        table,
                        value,
                        false,
                        false,
                        row,
                        column
                );
                maxComponentWidth = Math.max(component.getPreferredSize().width, maxComponentWidth);
            }
            TableCellRenderer headerRenderer = table.getColumnModel().getColumn(column).getHeaderRenderer();
            if (headerRenderer == null) {
                headerRenderer = table.getTableHeader().getDefaultRenderer();
            }
            Object headerValue = table.getColumnModel().getColumn(column).getHeaderValue();
            Component headerComp = headerRenderer.getTableCellRendererComponent(
                    table,
                    headerValue,
                    false,
                    false,
                    0,
                    column
            );
            maxComponentWidth = Math.max(maxComponentWidth, headerComp.getPreferredSize().width);
            table.getColumnModel()
                    .getColumn(column)
                    .setPreferredWidth(maxComponentWidth + table.getIntercellSpacing().width);
        }
    }

    void searchList(String searchText) {
        TransLog.getLogger().info("Searching for \"" + searchText + "\" ... ... ... ...");
        StoreList sList = storeList.searchName(searchText);
        updateInfoTable(sList);
        TransLog.getLogger().info(sList.toString());
    }

    void fetchSelectedStores() {
        int columnIndex = getIdColumnIndex();
        TransLog.getLogger().info("Column index = " + columnIndex);
        if (columnIndex != -1) {
            ArrayList<Integer> storeIdList = getSelectedIdList(columnIndex);
            fetchStore(storeIdList);
            return;
        }
        TransLog.getLogger().info("No fetch performed.");
    }

    private void fetchStore(ArrayList<Integer> storeIdList) {
        //get source path-name list
        StoreList sList = storeList.getListByIds(storeIdList);
        //get target
        String sourceBase = TransProp.get(TransConst.LOC_SOURCE);
        String target = TransProp.get(TransConst.LOC_TARGET);
        //exec and feedback
        for (StoreNode sNode : sList.getStoreList()) {
            String pathName = FileUtils.regulatePath(sNode.getPath().substring(1)) + sNode.getName();
            String cmd = TransConst.CMD_COPY_TO_TARGET + "\"" + sourceBase + pathName + "\" " + target;
            TransLog.getLogger().info("Command: " + cmd);
        }
    }

    private static ArrayList<Integer> getSelectedIdList(int columnIndex) {
        ArrayList<Integer> selectedIds = new ArrayList<>();
        for (int iSelectedRow : infoTable.getSelectedRows()) {
            int id = (Integer)infoTable.getValueAt(iSelectedRow, columnIndex);
            TransLog.getLogger().info("StoreId selected: " + id);
            selectedIds.add(id);
        }
        return selectedIds;
    }

    private static int getIdColumnIndex() {
        try {
            return infoTable.getColumnModel().getColumnIndex("StoreId");
        } catch (IllegalArgumentException e) {
            TransLog.getLogger().info("Column \"StoreId\" not found.");
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
}
