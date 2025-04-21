package com.skylynx13.transpath.ui;

import com.skylynx13.transpath.Transpath;
import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.utils.TransProp;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.*;
import java.util.List;

class PropertiesDialog extends JDialog {
    PropertiesDialog() {
        super(Transpath.getTranspathFrame(), "Properties Settings", true);
        //add(new JLabel(), BorderLayout.CENTER);
        this.setSize(800,600);
        this.setLayout(new BorderLayout());

        DefaultTableModel tableModel = createTableModel();

        // Create table that can be dragged
        JTable table = new JTable(tableModel);
        table.setDragEnabled(true);
        table.setDropMode(DropMode.INSERT_ROWS);
        table.setTransferHandler(new TableRowTransferHandler(table));

        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // Make sure all changes can be committed
            if (table.isEditing()) {
                table.getCellEditor().stopCellEditing();
            }
            // Save properties in given order
            TransProp.saveProperties(getPropertiesFromTableModel(tableModel));
            this.dispose();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            if (table.isEditing()) {
                table.getCellEditor().cancelCellEditing();
            }
            this.dispose();
        });

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            // 弹出对话框，获取新的键值对
            String key = JOptionPane.showInputDialog(this, "Input Key:");
            if (key != null && !key.isEmpty()) {
                String value = JOptionPane.showInputDialog(this, "Input Value:");
                if (value != null) {
                    tableModel.addRow(new Object[]{key, value});
                }
            }
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                tableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(
                        this, "Please select line to delete", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private static LinkedHashMap<String, String> getPropertiesFromTableModel(DefaultTableModel tableModel) {
        LinkedHashMap<String, String> properties = new LinkedHashMap<>();

        // add keys and values in given order
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String key = (String) tableModel.getValueAt(i, 0);
            String value = (String) tableModel.getValueAt(i, 1);
            properties.put(key, value);
        }

        return properties;
    }

    private static DefaultTableModel createTableModel() {
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Key", "Value"}, 0);
        for (Map.Entry<String, String> entry : TransProp.getProperties().entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
        return tableModel;
    }

    // Table with lines that can be dragged
    private static class TableRowTransferHandler extends TransferHandler {
        private final JTable table;

        public TableRowTransferHandler(JTable table) {
            this.table = table;
        }

        @Override
        public boolean canImport(TransferSupport support) {
            return support.isDataFlavorSupported(DataFlavor.stringFlavor);
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            int[] selectedRows = table.getSelectedRows();
            List<String> rows = new ArrayList<>();
            for (int row : selectedRows) {
                rows.add(table.getValueAt(row, 0).toString());
            }
            return new StringSelection(String.join(",", rows));
        }

        @Override
        public int getSourceActions(JComponent c) {
            return COPY_OR_MOVE;
        }

        @Override
        public boolean importData(TransferSupport support) {
            if (!canImport(support)) {
                return false;
            }

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int targetIndex = table.rowAtPoint(support.getDropLocation().getDropPoint());

            try {
                String data = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                String[] rowKeys = data.split(",");
                List<Integer> selectedIndices = new ArrayList<>();
                for (String key : rowKeys) {
                    for (int i = 0; i < model.getRowCount(); i++) {
                        if (model.getValueAt(i, 0).equals(key)) {
                            selectedIndices.add(i);
                        }
                    }
                }

                // 按照选中的顺序移动行
                Collections.sort(selectedIndices);
                List<Object[]> rows = new ArrayList<>();
                for (int index : selectedIndices) {
                    rows.add(new Object[]{model.getValueAt(index, 0), model.getValueAt(index, 1)});
                }

                // 删除选中的行
                for (int i = selectedIndices.size() - 1; i >= 0; i--) {
                    model.removeRow(selectedIndices.get(i));
                }

                // 在目标位置插入行
                for (Object[] row : rows) {
                    model.insertRow(targetIndex++, row);
                }

                return true;
            } catch (UnsupportedFlavorException | IOException e) {
                TransLog.getLogger().error("Error processing properties log {}", e.getMessage());
                return false;
            }
        }
    }
}
