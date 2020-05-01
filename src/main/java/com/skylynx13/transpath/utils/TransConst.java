package com.skylynx13.transpath.utils;

import java.awt.*;

public class TransConst {
    public static final String LOC_CONFIG = "LOC_CONFIG";
    public static final String LOC_LIST   = "LOC_LIST";
    public static final String LOC_TASK   = "LOC_TASK";
    public static final String LOC_STORE  = "LOC_STORE";
    public static final String LOC_TRANS  = "LOC_TRANS";
    public static final String LOC_SOURCE  = "LOC_SOURCE";
    public static final String LOC_TARGET  = "LOC_TARGET";

    private static final String FONT_TYPE = "FONT_TYPE";
    private static final String FONT_SIZE = "FONT_SIZE";

    public static final String PATH_BRANCH = "PATH_BRANCH";
    public static final String PATH_DRILL = "PATH_DRILL";

    static final String SYS_TYPE = "SYS_TYPE";
    static final String SYS_WINDOWS = "Windows";

    public static final String EMPTY  = "";
    public static final String COLON  = ":";
    public static final String SLASH  = "/";   
    public static final String CRLN   = "\r\n";    
    public static final String CR     = "\r";    
    public static final String LN     = "\n";
    static final String BACK_SLASH_4 = "\\\\";
    static final String BACK_SLASH_8 = "\\\\\\\\";

    public static final String ROOT = "TFLib";

    public static final String TP_PROPS = System.getProperty("transpath.properties");
    
    public static final String LIST_KEYWORDS = "keywords.list";
    public static final String LIST_RENAME   = "rename.list";

    static final String MD5    = "MD5";
    static final String SHA    = "SHA";
    static final String CRC32  = "CRC32";
    
    public static final String FORMAT_INT_08 = "%08d";
    public static final String FORMAT_INT_13 = "%013d";
    public static final String FORMAT_INT_20 = "%020d";
    static final String FORMAT_HEX_08 = "%08x";
    
    public static final String FMT_DATE_TIME_LONG  = "yyyyMMddHHmmssSSS";
    static final String FMT_DATE            = "yyyyMMdd";
    
    public static final String NAME_CHARS = "[\\-\\+!@#$%^&½=,.'A-Za-z0-9 \\(\\)]+";

    public static final Font GLOBAL_FONT =
            new Font(TransProp.get(FONT_TYPE), Font.PLAIN, TransProp.getInt(FONT_SIZE));

    public static String[] TABLE_TITLE_BRANCH =
            {"BranchId", "BranchPath", "BranchName", "Length", "Count"};

    public static String[] TABLE_TITLE_STORE =
            {"StoreId", "StorePath", "StoreName", "Length", "Update Time", "MD5", "SHA", "CRC32"};

    public static final String CMD_COPY_TO_TARGET = "cmd /c chcp 437|copy ";

    public static final String PKG_ALL_FAILED = "All checkers failed.";
    public static final String PKG_TYPE_MISMATCH = "Type mismatch.";
    public static final String PKG_TYPE_NOT_RECOGNIZED = "File type not recognized.";
    public static final String PKG_IGNORE = "File type ignorable.";
    }
