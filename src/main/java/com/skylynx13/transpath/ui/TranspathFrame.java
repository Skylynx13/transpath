package com.skylynx13.transpath.ui;

import com.skylynx13.transpath.Transpath;
import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.store.StoreList;
import com.skylynx13.transpath.store.StoreNode;
import com.skylynx13.transpath.store.StoreTree;
import com.skylynx13.transpath.utils.FileUtils;
import com.skylynx13.transpath.utils.TransConst;
import com.skylynx13.transpath.utils.TransProp;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * ClassName: TranspathFrame
 * Description: Transpath Main Frame
 * Date: 2015-11-16 12:59:46
 * @author skylynx
 */
public class TranspathFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final int DIVIDER_SIZE = 8;
    private static final int ZOOM_PERCENT = 66;
    private static final double SPLIT_V_DEFAULT = 0.3;
    private static final double SPLIT_H_DEFAULT = 0.1;

    private final JTable infoTable = new JTable();
    private final JTextArea infoTextArea = new JTextArea();

    private StoreList storeList;
    private JScrollPane treePane;

    JTextArea getInfoTextArea() {
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

    public void reloadStore() {
        storeList = new StoreList(TransProp.get(TransConst.LOC_LIST) + "StoreList.txt");
        getTreePane().setViewportView(createTree(storeList));
        setTitle("Storage Archivist - " + storeList.getVersion());
        TransLog.getLogger().info("List of version {} loaded.", storeList.getVersion());
    }

    public StoreList getStoreList() {
        return storeList;
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
        setJMenuBar(new TranspathMenuBar());
    }

    /**
     * Other Look and feel candidates:
     * "javax.swing.plaf.metal.MetalLookAndFeel"
     * "javax.swing.plaf.nimbus.NimbusLookAndFeel"
     * "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
     * "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel";
     * "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
     * "com.sun.java.swing.plaf.mac.MacLookAndFeel";
     * "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
     * UIManager.getCrossPlatformLookAndFeelClassName();
     * UIManager.getSystemLookAndFeelClassName();
     */
    private void initLookAndFeel() {
        try {
            String lookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (ClassNotFoundException
                | InstantiationException
                | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            TransLog.getLogger().error("", e);
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
                createScrollPane(Transpath.getLogTextArea()),
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
        statusBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        statusBar.setFloatable(false);

        statusBar.add(getTimerLabel());
        statusBar.add(getProgressBar());
        statusBar.add(Transpath.getStatusLabel());

        return statusBar;
    }

    /**
     * Color change unavailable when using NimbusLookAndFeel;
     * progressBar.setBackground(Color.gray);
     * progressBar.setForeground(Color.green);
     */
    private JProgressBar getProgressBar() {
        Transpath.getProgressBar().setMaximum(100);
        Transpath.getProgressBar().setMinimum(0);
        Transpath.getProgressBar().setValue(0);
        Transpath.getProgressBar().setStringPainted(true);
        Transpath.getProgressBar().setIndeterminate(false);
        Transpath.getProgressBar().setPreferredSize(new Dimension(600, 20));
        return Transpath.getProgressBar();
    }

    private JLabel getTimerLabel() {
        JLabel timerLabel = new JLabel();
        Timer timer = new Timer(0,
                e -> timerLabel.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Date())));
        timer.start();
        return timerLabel;
    }

    private void updateInfoTable(StoreList storeList) {
        updateInfoTable(new DefaultTableModel(storeList.toRows(), TransConst.TABLE_TITLE_STORE));
    }

    void updateInfoTable(StoreTree selectTree) {
        updateInfoTable(new DefaultTableModel(selectTree.getChildrenAsRows(), selectTree.getChildrenTitle()));
    }

    private void updateInfoTable(DefaultTableModel tableModel) {
        String[] rightAlignedTitles = {"StoreId", "Length", "BranchId", "Count"};
        infoTable.setModel(tableModel);
        columnSizeFitContents(infoTable);

        for (String rightAlignedTitle : rightAlignedTitles) {
            columnAlignRight(rightAlignedTitle);
        }
    }

    private void columnAlignRight(String columnTitle) {
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
        TransLog.getLogger().info("Searching for \"{}\" ... ... ... ...", searchText);
        StoreList sList = storeList.searchName(searchText);
        updateInfoTable(sList);
        TransLog.getLogger().info(sList.toString());
    }

    void fetchSelectedStores() {
        int columnIndex = getIdColumnIndex();
        TransLog.getLogger().info("Column index = {}", columnIndex);
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
            String pathName = FileUtils.regulateSysPath(sNode.getPath().substring(1)) + sNode.getName();
            String cmd = TransConst.CMD_COPY_TO_TARGET + "\"" + sourceBase + pathName + "\" " + target;
            TransLog.getLogger().info("Command: {}", cmd);
        }
    }

    private ArrayList<Integer> getSelectedIdList(int columnIndex) {
        ArrayList<Integer> selectedIds = new ArrayList<>();
        for (int iSelectedRow : infoTable.getSelectedRows()) {
            int id = (Integer)infoTable.getValueAt(iSelectedRow, columnIndex);
            TransLog.getLogger().info("StoreId selected: {}", id);
            selectedIds.add(id);
        }
        return selectedIds;
    }

    private int getIdColumnIndex() {
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

    void exit() {
        TransLog.getLogger().info("Transpath Exit.");
        System.exit(0);
    }

    public void extractKeyword() {
        TransLog.getLogger().info("Extracting keywords...");
        storeList.extractKeyword(new File(TransProp.get(TransConst.LOC_LIST) + "StoreKeyword.txt"));
        TransLog.getLogger().info("Extracted");
    }
}
