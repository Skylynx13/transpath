package com.skylynx13.transpath.utils;

import com.skylynx13.transpath.log.TransLog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * ClassName: TransProp
 * Description: Transpath properties interface.
 * Date: 2016-02-14 15:45:49
 * @author skylynx
 */
public class TransProp {
    /** Retrieve file on every get so that props can change dynamically. */
    public static String get(String propName) {
        Properties tProps = new Properties();
        try {
            tProps.load(Files.newInputStream(Paths.get(TransConst.TP_PROPS)));
        } catch (IOException e) {
            TransLog.getLogger().error("", e);
        }
        return tProps.getProperty(propName);
    }
    
    public static int getInt(String propName) {
        return Integer.parseInt(get(propName));
    }

    public static List<String> getList(String propName) {
        return Arrays.asList(get(propName).split(","));
    }

    /**
     * New properties supporting dialog.
     */
    private static LinkedHashMap<String, String> orderedProperties;
    private static LinkedHashMap<String, String> orderedComments;

    public static DefaultTableModel createTableModel() {
        loadProperties();
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Key", "Value"}, 0);
        for (Map.Entry<String, String> entry : orderedProperties.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
        return tableModel;
    }

    private static void loadProperties() {
        orderedProperties = new LinkedHashMap<>();
        orderedComments = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(TransConst.TP_PROPS))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    orderedComments.put(String.valueOf(lineNumber), line);
                } else {
                    String[] keyValue = line.split("=", 2);
                    if (keyValue.length == 2) {
                        orderedProperties.put(keyValue[0].trim(), keyValue[1].trim());
                    }
                }
            }
            TransLog.getLogger().info("Properties loaded.");
        } catch (IOException ex) {
            TransLog.getLogger().error("Error when loading: {}", ex.getMessage());
        }
    }

    private void saveProperties() {
        try (OutputStream output = Files.newOutputStream(Paths.get(TransConst.TP_PROPS))) {
            for (Map.Entry<String, String> entry : orderedComments.entrySet()) {
                output.write((entry.getValue() + "\n").getBytes());
            }
            for (Map.Entry<String, String> entry : orderedProperties.entrySet()) {
                output.write((entry.getKey() + "=" + entry.getValue() + "\n").getBytes());
            }
            TransLog.getLogger().info("Properties saved.");
        } catch (IOException ex) {
            TransLog.getLogger().error("Error when saving: {}", ex.getMessage());
        }
    }

    public static void savePropertiesWithOrder(DefaultTableModel tableModel) {
        // 创建一个临时的 LinkedHashMap 来保持顺序
        LinkedHashMap<String, String> tempOrderedProperties = new LinkedHashMap<>();

        // 按照表格的顺序添加键值对
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String key = (String) tableModel.getValueAt(i, 0);
            String value = (String) tableModel.getValueAt(i, 1);
            tempOrderedProperties.put(key, value);
        }

        // 将临时的 LinkedHashMap 写入文件
        try (OutputStream output = Files.newOutputStream(Paths.get(TransConst.TP_PROPS))) {
            for (Map.Entry<String, String> entry : orderedComments.entrySet()) {
                output.write((entry.getValue() + "\n").getBytes());
            }
            for (Map.Entry<String, String> entry : tempOrderedProperties.entrySet()) {
                output.write((entry.getKey() + "=" + entry.getValue() + "\n").getBytes());
            }
            TransLog.getLogger().info("Properties saved with order.");
        } catch (IOException ex) {
            TransLog.getLogger().error("Error when saving with order: {}", ex.getMessage());
        }

        // 更新主 LinkedHashMap
        orderedProperties = tempOrderedProperties;
    }

}
