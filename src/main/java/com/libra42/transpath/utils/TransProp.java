/**
 * Copyright (c) 2016,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.utils
 * File Name:TransProp.java
 * Date:2016-2-14 下午3:45:49
 * 
 */
package com.libra42.transpath.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

 /**
 * ClassName: TransProp <br/>
 * Description: Transpath properties interface.<br/>
 * Date: 2016-2-14 下午3:45:49 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
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
