package com.skylynx13.transpath.utils;

import java.text.DecimalFormat;

/**
 * ClassName: StringUtils
 * Description: String utils
 * Date: 2015-08-06 17:08:23
 */
public class StringUtils {
    public static final String EMPTY_STRING = "";

    public static boolean isBlankString(String pString) {
        int strLength;
        if (pString == null || (strLength = pString.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLength; i++) {
            if (!Character.isWhitespace(pString.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isBlank(String pString) {
        return pString == null || pString.trim().length() == 0;
    }

    public static boolean isEmpty(String pString) {
        return pString == null || pString.length() == 0;
    }

    public static boolean isNumeric(String pString) {
        return pString.matches("\\d*");
    }

    public static String parseContent(String pString, String startAfter, String endBefore, int startIndex) {
        if (isEmpty(pString) || isEmpty(startAfter) || isEmpty(endBefore) || startIndex > pString.length()) {
            return null;
        }

        String result;

        int startIdx = pString.indexOf(startAfter, startIndex);
        int endIdx = pString.indexOf(endBefore, startIdx);
        if ((startIdx < 0) || (endIdx < 0)) {
            return null;
        }
        try {
            result = pString.substring(startIdx + startAfter.length(), endIdx);
        } catch (StringIndexOutOfBoundsException e) {
            return null;
        }

        return result;
    }

    public static int getNextIndex(String pString, String startAfter, String endBefore, int startIndex) {
        if (isEmpty(pString) || isEmpty(startAfter) || isEmpty(endBefore) || startIndex > pString.length()) {
            return -1;
        }

        int startIdx = pString.indexOf(startAfter, startIndex);
        int endIdx = pString.indexOf(endBefore, startIdx);
        if ((startIdx < 0) || (endIdx < 0)) {
            return -1;
        }

        return pString.indexOf(endBefore, startIndex) + endBefore.length() - 1;
    }

    public static String getSimpleName(String name) {
        int iEnd = name.length();
        if (name.lastIndexOf('.') > 0) {
            iEnd = name.lastIndexOf('.');
        }
        if (name.indexOf('(') > 0) {
            iEnd = name.indexOf('(');
        }
        return name.substring(0, iEnd);
    }

    public static String formatLongInt(Long longInt) {
        return new DecimalFormat("#,###").format(longInt);
    }

}
