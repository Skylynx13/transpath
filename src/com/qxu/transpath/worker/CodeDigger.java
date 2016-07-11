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

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

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
                                  TransProp.get("TP_HOME") + "fresh.txt", 
                                  TransProp.get("TP_HOME") + "track001.txt");
    }

    public static void digKeywordFile(String key, String src, String target) {
        String[] keywords = CodeDigger.readKeywordList(key);
        CodeDigger.digKeyword(keywords, src, target);
    }

    public static void digKeyword(String[] keywords, String src, String target) {
        System.out.println("Digging keyword...");
        System.out.println(Arrays.toString(keywords));
        int n = new Arranger().readFromFile(src).applyFilter(keywords).writeToFile(target);
        System.out.println("Totally " + n + " entries extracted.");
        System.out.println("Done.");
    }

    public static void pickoutKeyword(String[] keys, String src, String target) {
        System.out.println("Digging keyword...");
        int n = new Arranger().readFromFile(src).applyFilterReversely(keys).writeToFile(target);
        System.out.println("Totally " + n + " entries extracted.");
        System.out.println("Done.");
    }

    public static void digBigEntries(String src, String target) {
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

    public static void main(String[] args) {
        CodeDigger.digKeywordFileDefault(TransProp.get("TP_HOME") + "fresh.txt", 
                                         TransProp.get("TP_HOME") + "track001.txt");
        // CodeDigger.pickoutBigEntries(TransProp.get("TP_HOME") + "task20160331_stone.txt", 
        //                              TransProp.get("TP_HOME") + "track001.txt");
    }
}
