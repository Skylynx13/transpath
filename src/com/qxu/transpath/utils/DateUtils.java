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
    
    public static String formatDateTimeLongToday() {
        return formatDate(TransConst.FMT_DATE_TIME_LONG, new Date());
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
        TransLog.getLogger().info(DateUtils.formatDateToday());

        long aTime = System.currentTimeMillis();
        Date aDate = new Date();
        TransLog.getLogger().info(aTime);
        TransLog.getLogger().info(DateUtils.formatDateTimeLong(aTime));
        TransLog.getLogger().info(DateUtils.formatDateTimeLong(aDate));
    }
}
