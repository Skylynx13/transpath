package com.skylynx13.transpath.utils;

import com.skylynx13.transpath.log.TransLog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

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
}
