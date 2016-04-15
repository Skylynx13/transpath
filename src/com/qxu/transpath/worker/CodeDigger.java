/**
 * Copyright (c) 2016,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.worker
 * File Name:CodeDigger.java
 * Date:2016-4-7 下午7:20:44
 * 
 */
package com.qxu.transpath.worker;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import com.qxu.transpath.utils.FileUtils;
import com.qxu.transpath.utils.TransConst;
import com.qxu.transpath.utils.TransProp;

 /**
 * ClassName: CodeDigger <br/>
 * Description: To dig a given code from a source file,
 * get all of the entries containing the code,
 * then write them to a track file.<br/>
 * Date: 2016-4-7 下午7:20:44 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 */

public class CodeDigger {
    public static void digKeyword(String[] keys, String filename) {
        System.out.println("Digging keyword...");
        int n = new Arranger().readFromFile(filename).applyFilter(keys).writeToFile(TransProp.get("TP_HOME") + "track001.txt");
        System.out.println("Totally " + n + " entries extracted.");
        System.out.println("Done.");
    }
    
    public static void pickoutKeyword(String[] keys, String filename) {
        System.out.println("Digging keyword...");
        int n = new Arranger().readFromFile(filename).applyFilterReversely(keys).writeToFile(TransProp.get("TP_HOME") + "track001.txt");
        System.out.println("Totally " + n + " entries extracted.");
        System.out.println("Done.");
    }
    
    public static void digBigEntries(String fileName) {
        String[] keys = {"(\\d{3}(\\.\\d{1,2}){0,1} MB|\\d{1,2}(\\.\\d{1,2}){0,1} GB)"};
        CodeDigger.digKeyword(keys, fileName);
    }
    
    public static void pickoutBigEntries(String fileName) {
        String[] keys = {"(\\d{3}(\\.\\d{1,2}){0,1} MB|\\d{1,2}(\\.\\d{1,2}){0,1} GB)"};
        CodeDigger.pickoutKeyword(keys, fileName);
    }
    
    public static String[] readKeywordList(String fileName) {
        Scanner in = null;
        ArrayList<String> keywords = new ArrayList<String>();
        try {
            in = new Scanner(new FileReader(fileName));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        while (in.hasNext()) {
            keywords.add(in.nextLine());
        }
        in.close();
        return (String[])keywords.toArray(new String[keywords.size()]);
    }
    
    public static void main (String[] args) {
        String[] keywords = CodeDigger.readKeywordList(TransProp.get("TP_HOME") + TransConst.TP_KEYWORDS);
        System.out.println(Arrays.toString(keywords));
        CodeDigger.digKeyword(keywords, TransProp.get("TP_HOME") + "fresh.txt");
        //CodeDigger.pickoutBigEntries(TransProp.get("TP_HOME") + "task20160331_stone.txt");
    }
}
