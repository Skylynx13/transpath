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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class PropertiesDialog extends JDialog {
    PropertiesDialog() {
        super(Transpath.getTranspathFrame(), "Properties Settings", true);
        //add(new JLabel(), BorderLayout.CENTER);
        this.setSize(800,600);
        this.setLayout(new BorderLayout());

        // 创建表格模型
        DefaultTableModel tableModel = TransProp.createTableModel();

        // 创建表格并启用拖拽排序
        JTable table = new JTable(tableModel);
        table.setDragEnabled(true);
        table.setDropMode(DropMode.INSERT_ROWS);
        table.setTransferHandler(new TableRowTransferHandler(table));

        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // 确保表格编辑器提交所有更改
            if (table.isEditing()) {
                table.getCellEditor().stopCellEditing();
            }
            // 更新 orderedProperties 并保存顺序
            TransProp.savePropertiesWithOrder(tableModel);
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
    // 表格行拖拽处理类
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
