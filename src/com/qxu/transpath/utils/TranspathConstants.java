package com.qxu.transpath.utils;

public class TranspathConstants {
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
        TranspathConstants.BRANCH_0MD, 
        TranspathConstants.BRANCH_1ST, 
        TranspathConstants.BRANCH_2ND, 
        TranspathConstants.BRANCH_3RD, 
        TranspathConstants.BRANCH_4TH
        };

    public static final int FIELDS_INFO   = 2;
    public static final int FIELDS_BRANCH = 3;
}
