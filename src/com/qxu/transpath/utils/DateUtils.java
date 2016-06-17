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
    
    public static String formatDateToday() {
        return formatDate(TransConst.FMT_DATE, new Date());
    }
    
    public static String formatDateTimeLong(long pDateValue) {
        return formatDateTimeLong(new Date(pDateValue));
    }
    
    public static String formatDateTimeLong(Date pDate) {
        return formatDate(TransConst.FMT_DATE_TIME_LONG, pDate);
    }
    
    public static String formatDate(String pFormat, long pDateValue) {
        return formatDate(pFormat, new Date(pDateValue));
    }

    public static String formatDate(String pFormat, Date pDate) {
        return new SimpleDateFormat(pFormat).format(pDate);
    }
    
    public static void main (String[] args) {
        System.out.println(DateUtils.formatDateToday());

        long aTime = System.currentTimeMillis();
        Date aDate = new Date();
        System.out.println(aTime);
        System.out.println(DateUtils.formatDateTimeLong(aTime));
        System.out.println(DateUtils.formatDateTimeLong(aDate));
    }
}
