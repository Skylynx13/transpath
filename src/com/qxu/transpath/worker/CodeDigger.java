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

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import com.qxu.transpath.utils.FileUtils;
import com.qxu.transpath.utils.TransConst;
import com.qxu.transpath.utils.TransProp;

/**
 * ClassName: CodeDigger <br/>
 * Description: To dig a given code from a source file, get all of the entries
 * containing the code, then write them to a track file.<br/>
 * Date: 2016-4-7 下午7:20:44 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 */

public class CodeDigger {

    private static final String[] KEYS_BIG_ENTRY = { "(\\d{3}(\\.\\d{1,2}){0,1} MB|\\d{1,2}(\\.\\d{1,2}){0,1} GB)" };

    public static void digKeywordFileDefault(String src, String target) {
        CodeDigger.digKeywordFile(TransProp.get("TP_HOME") + TransConst.TP_KEYWORDS, 
                                  src, target);
    }

    public static void digKeywordFile(String key, String src, String target) {
        String[] keywords = CodeDigger.readKeywordList(key);
        System.out.println("Digging keywords " + Arrays.toString(keywords));
        CodeDigger.digKeyword(keywords, src, target);
    }

    public static int digKeyword(String[] keywords, String src, String target) {
        int n = new Arranger().readFromFile(src).applyFilter(keywords).appendToFile(target);
        System.out.println("" + n + " <- " + src);
        return n;
    }

    public static void pickoutKeyword(String[] keys, String src, String target) {
        System.out.println("Digging keyword...");
        int n = new Arranger().readFromFile(src).applyFilterReversely(keys).appendToFile(target);
        System.out.println("Totally " + n + " entries extracted.");
        System.out.println("Done.");
    }

    public static void digBigEntries(String src, String target) {
        System.out.println("Digging keywords " + Arrays.toString(KEYS_BIG_ENTRY));
        CodeDigger.digKeyword(KEYS_BIG_ENTRY, src, target);
    }

    public static void pickoutBigEntries(String src, String target) {
        CodeDigger.pickoutKeyword(KEYS_BIG_ENTRY, src, target);
    }

    public static String[] readKeywordList(String key) {
        Scanner in = null;
        ArrayList<String> keywords = new ArrayList<String>();
        try {
            in = new Scanner(new FileReader(key));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        while (in.hasNext()) {
            keywords.add(in.nextLine());
        }
        in.close();
        return (String[]) keywords.toArray(new String[keywords.size()]);
    }
    
    public static void digAllFreshSpecific(String[] keys, String target) {
        long t0 = System.currentTimeMillis();
        FileUtils.clearFile(target);
        File path = new File(TransProp.get("TP_HOME"));
        int total = 0;
        System.out.println("Digging keywords " + Arrays.toString(keys));
        for (File file : path.listFiles(new FilenameFilter() {
            public boolean accept(File file, String name) {
                return name.matches("fresh_.*.txt");
            }
        })) {
            total += CodeDigger.digKeyword(keys, file.getAbsolutePath(), target);
            //System.out.println(file.getAbsolutePath());
        }
        System.out.println("Totoally " + total + " entries extracted.");
        System.out.println("Time: " + (System.currentTimeMillis() - t0));
    }

//    public static void clearFile(String fileName) {
//        try {
//            PrintWriter out = new PrintWriter(fileName);
//            out.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
//        CodeDigger.digKeywordFileDefault(TransProp.get("TP_HOME") + "fresh.txt", 
//                                         TransProp.get("TP_HOME") + "track001.txt");
        // CodeDigger.pickoutBigEntries(TransProp.get("TP_HOME") + "task20160331_stone.txt", 
        //                              TransProp.get("TP_HOME") + "track001.txt");
        //FileUtils.clearFile(TransProp.get("TP_HOME") + "track002.txt");
    }
}
