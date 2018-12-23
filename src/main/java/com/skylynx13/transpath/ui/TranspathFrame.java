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
import com.skylynx13.transpath.store.StoreList;
import com.skylynx13.transpath.tree.Node;
import com.skylynx13.transpath.tree.NodeList;
import com.skylynx13.transpath.tree.NodeTree;
import com.skylynx13.transpath.utils.TransConst;
import com.skylynx13.transpath.utils.TransProp;
import org.jetbrains.annotations.NotNull;

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

    private static JTable infoTable = new JTable();
    private static JTextArea logTextArea = new JTextArea();
    private static JTextArea infoTextArea = new JTextArea("information");

    private StoreList storeList;
    private JScrollPane treePane;

    public static JTextArea getLogTextArea() {
        return logTextArea;
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
    }

    private void initSize() {
        int zoomPercent = 66;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        super.setSize(
                (int) screenSize.getWidth() * zoomPercent / 100,
                (int) screenSize.getHeight() * zoomPercent / 100
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
        JPanel mainPanel = (JPanel) this.getContentPane();
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());
        reloadStore();
        mainPanel.add(createAllSplitPane(), BorderLayout.CENTER);
        mainPanel.add(createStatusBar(), BorderLayout.SOUTH);
        this.setTitle("Storage Archivist - " + storeList.version);
        this.setVisible(true);
    }

    void reloadStore() {
        storeList = new StoreList(TransProp.get(TransConst.LOC_LIST) + "StoreList.txt");
        getTreePane().setViewportView(createTree(storeList));
        TransLog.getLogger().info("List of version " + storeList.version + " loaded.");
    }

    private JTree createTree(StoreList storeList) {
        NodeTree storeTree = NodeTree.buildFromList(storeList);
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

    private JSplitPane createAllSplitPane() {
        return createSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                createUpperSplitPane(),
                createLowerTabbedPane(),
                0.7
        );
    }

    private JSplitPane createUpperSplitPane() {
        return createSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                getTreePane(),
                createInfoTabbedPane(),
                0.2);
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
        splitPane.setDividerSize(8);

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

    private JToolBar createStatusBar() {
        JToolBar statusBar = new JToolBar();
        statusBar.setFloatable(false);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setMaximum(100);
        progressBar.setMinimum(0);
        progressBar.setValue(88);
        progressBar.setStringPainted(true);
        progressBar.setIndeterminate(false);
        // Color change unavailable when using NimbusLookAndFeel;
        // progressBar.setBackground(Color.gray);
        // progressBar.setForeground(Color.green);
        statusBar.add(progressBar);

        statusBar.add(new JLabel("8,800 of 10,000 bytes processed. Status Normal."));

        return statusBar;
    }

    private static void setInfoTable(@NotNull StoreList storeList) {
        setInfoTable(new DefaultTableModel(storeList.toRows(), TransConst.TABLE_TITLE_STORE));
    }

    static void setInfoTable(NodeTree selectTree) {
        setInfoTable(new DefaultTableModel(selectTree.getChildrenAsRows(), selectTree.getChildrenTitle()));
    }

    private static void setInfoTable(@NotNull DefaultTableModel tableModel) {
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
        } catch (IllegalArgumentException e) {
            // Ignore
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
        setInfoTable(sList);
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
        NodeList sList = storeList.getListByIds(storeIdList);
        //get target
        String sourceBase = TransProp.get(TransConst.LOC_SOURCE);
        String target = TransProp.get(TransConst.LOC_TARGET);
        //exec and feedback
        for (Node sNode : sList.nodeList) {
            String pathName = sNode.path.substring(1).replaceAll(TransConst.SLASH, TransConst.BACK_SLASH_4)
                    + sNode.name;
            String cmd = TransConst.CMD_COPY_TO_TARGET + "\"" + sourceBase + pathName + "\" " + target;
            TransLog.getLogger().info("Command: " + cmd);
            //execCmd(cmd);
        }
    }

    private static void execCmd(String cmd) {
        try {
            Process cmdProcessor = Runtime.getRuntime().exec(cmd);
            BufferedReader iReader =
                    new BufferedReader(new InputStreamReader(new BufferedInputStream(cmdProcessor.getInputStream())));
            BufferedReader eReader =
                    new BufferedReader(new InputStreamReader(new BufferedInputStream(cmdProcessor.getErrorStream())));
            String inLine;
            while ((inLine = iReader.readLine()) != null) {
                TransLog.getLogger().info("Return: " + inLine);
            }
            while ((inLine = eReader.readLine()) != null) {
                TransLog.getLogger().info("Error: " + inLine);
            }
            if (cmdProcessor.waitFor() != 0) {
                if (cmdProcessor.exitValue() == 1) {
                    TransLog.getLogger().info("Error executing.");
                }
            }
        } catch (IOException | InterruptedException e) {
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

    private static int getIdColumnIndex() {
        try {
            return infoTable.getColumnModel().getColumnIndex("StoreId");
        } catch (IllegalArgumentException e) {
            TransLog.getLogger().info("StoreId" + " not found.");
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
