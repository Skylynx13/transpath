package com.skylynx13.transpath.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.skylynx13.transpath.log.TransLog;

 /**
 * ClassName: DateUtils
 * Description: Date utils
 * Date: 2015-11-04 23:49:42
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
    
    private static String formatDateTimeLong(Date pDate) {
        return formatDate(TransConst.FMT_DATE_TIME_LONG, pDate);
    }
    
    public static String formatDate(String pFormat, long pDateValue) {
        return formatDate(pFormat, new Date(pDateValue));
    }

    private static String formatDate(String pFormat, Date pDate) {
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
