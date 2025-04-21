package com.skylynx13.transpath.utils;

import com.skylynx13.transpath.log.TransLog;

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
    public static String getString(String propName) {
        Properties tProps = new Properties();
        try {
            tProps.load(Files.newInputStream(Paths.get(TransConst.TP_PROPS)));
        } catch (IOException e) {
            TransLog.getLogger().error("", e);
        }
        return tProps.getProperty(propName);
    }
    
    public static int getInt(String propName) {
        return Integer.parseInt(getString(propName));
    }

    public static String[] getStringArray(String propName) {
        return getString(propName).split(",");
    }

    /**
     * New properties supporting dialog.
     */
    private static LinkedHashMap<String, String> properties;

    public static LinkedHashMap<String, String> getProperties() {
        loadProperties();
        return properties;
    }

    private static void loadProperties() {
        properties = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(TransConst.TP_PROPS))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] keyValue = line.split("=", 2);
                if (keyValue.length == 2) {
                    properties.put(keyValue[0].trim(), keyValue[1].trim());
                }
            }
            TransLog.getLogger().info("Properties loaded.");
        } catch (IOException ex) {
            TransLog.getLogger().error("Error when loading: {}", ex.getMessage());
        }
    }

    public static void saveProperties(LinkedHashMap<String, String> updatedProperties) {
        // Write properties file
        try (OutputStream output = Files.newOutputStream(Paths.get(TransConst.TP_PROPS))) {
            for (Map.Entry<String, String> entry : updatedProperties.entrySet()) {
                output.write((entry.getKey() + "=" + entry.getValue() + "\n").getBytes());
            }
            TransLog.getLogger().info("Properties saved with order.");
        } catch (IOException ex) {
            TransLog.getLogger().error("Error when saving with order: {}", ex.getMessage());
        }

        properties = updatedProperties;
    }

}
