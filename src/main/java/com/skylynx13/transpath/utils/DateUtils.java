package com.skylynx13.transpath.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ClassName: DateUtils
 * Description: Date utils
 * Date: 2015-11-04 23:49:42
 * @author skylynx
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
    
    private static String formatDate(String pFormat, Date pDate) {
        return new SimpleDateFormat(pFormat).format(pDate);
    }
    
}
