package com.qxu.transpath.utils;

public class TransConst {
    public static final String PREFIX_ARCHIVE = "A";
    public static final String PREFIX_BLOCK   = "B";    
    public static final String BRANCH_LINKER = "|";
    public static final String LEVEL_LINKER = "/";
    public static final String LINE_LINKER = "\n";
    public static final String PATH_LINKER = "/";
	public static final String TREENAME_BRANCH = ":";  
    public static final String TREE_LOCATION = "LOC";
    public static final String TREE_FULLLIST = "TFL";
	
	public static final String EMPTY = "";
    public static final String PERIOD = ".";
    public static final String COLON = ":";   
    public static final String SLASH = "/";   
    public static final String BACK_SLASH_4 = "\\\\";
    public static final String CRLN = "\r\n";    
    public static final String CR = "\r";    
    public static final String LN = "\n";
    
    public static final String ROOT = "TFLib";   

    public static final String NODE_ID = "ID";
    public static final String BRANCH_0MD = "0MD"; //Metadata
    public static final String BRANCH_1ST = "1ST"; //Storage
    public static final String BRANCH_2ND = "2ND"; //Index
    public static final String BRANCH_3RD = "3RD";
    public static final String BRANCH_4TH = "4TH";
    public static final String[] SUPPORTED_BRANCH_TYPES = {
        TransConst.BRANCH_0MD, 
        TransConst.BRANCH_1ST, 
        TransConst.BRANCH_2ND, 
        TransConst.BRANCH_3RD, 
        TransConst.BRANCH_4TH
        };

    public static final int FIELDS_INFO   = 2;
    public static final int FIELDS_BRANCH = 3;
    
    public static final String INDEX_PRE_CATALOG = "/PreCatalog/";

// Path info be moved to properties
//    public static final String TP_HOME = "D:\\_TF\\_Update\\transpath\\";
//    public static final String TP_HOME = "D:\\Qdata\\update\\transpath\\";
    public static final String TP_PROPS = "transpath.properties";
    public static final String TP_KEYWORDS = "keywords.list";
//    public static final String TP_HOME = "TP_HOME";
    public static final String MD5 = "MD5";
    public static final String SHA = "SHA";
    public static final String SHA1 = "SHA-1";
    public static final String SHA256 = "SHA-256";
    public static final String SHA384 = "SHA-384";
    public static final String SHA512 = "SHA-512";
    public static final String CRC32 = "CRC32";
    
    public static final String FORMAT_INT_04 = "%04d";
    public static final String FORMAT_INT_08 = "%08d";
    public static final String FORMAT_INT_13 = "%013d";
    public static final String FORMAT_HEX_08 = "%08x";
    
    public static final String FMT_DATE_TIME_SHORT = "yyyyMMddHHmm";
    public static final String FMT_DATE_TIME = "yyyyMMddHHmmss";
    public static final String FMT_DATE_TIME_LONG = "yyyyMMddHHmmssSSS";
    public static final String FMT_DATE = "yyyyMMdd";
}
