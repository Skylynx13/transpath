package com.skylynx13.transpath.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

 /**
 * ClassName: TransProp
 * Description: Transpath properties interface.
 * Date: 2016-02-14 15:45:49
 */
public class TransProp {
    // Retrieve file on every get so that props can change dynamically.
    public static String get(String propName) {
        Properties tProps = new Properties();
        try {
            tProps.load(new FileInputStream(TransConst.TP_PROPS));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tProps.getProperty(propName);
    }
    
    public static int getInt(String propName) {
        return Integer.parseInt(get(propName));
    }
}
