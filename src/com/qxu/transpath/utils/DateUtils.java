/**
 * Copyright (c) 2015,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.utils
 * File Name:DateUtils.java
 * Date:2015-11-4 下午11:49:42
 * 
 */
package com.qxu.transpath.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.text.DateFormatter;

 /**
 * ClassName: DateUtils <br/>
 * Description: TODO <br/>
 * Date: 2015-11-4 下午11:49:42 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class DateUtils {
    
    public static String dateStringToday() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }
    
    public static void main (String[] args) {
        System.out.println(DateUtils.dateStringToday());
    }
}
