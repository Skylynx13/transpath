package com.skylynx13.transpath.utils;

import java.text.DecimalFormat;

/**
 * ClassName: StringUtils
 * Description: String utils
 * Date: 2015-08-06 17:08:23
 * @author skylynx
 */
public class StringUtils {
    public static boolean isEmpty(String pString) {
        return pString == null || pString.isEmpty();
    }

    public static String formatLongInt(Long longInt) {
        return new DecimalFormat("#,###").format(longInt);
    }
}
