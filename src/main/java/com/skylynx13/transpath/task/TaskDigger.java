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
package com.skylynx13.transpath.task;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.utils.FileUtils;
import com.skylynx13.transpath.utils.TransConst;
import com.skylynx13.transpath.utils.TransProp;

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

public class TaskDigger {

    private static final String[] KEYS_BIG_ENTRY = { "(\\d{3}(\\.\\d{1,2}){0,1} MB|\\d{1,2}(\\.\\d{1,2}){0,1} GB)" };

    public static void digKeywordFileDefault(String src, String target) {
        TaskDigger.digKeywordFile(TransProp.get(TransConst.LOC_CONFIG) + TransConst.LIST_KEYWORDS, 
                                  src, target);
    }

    public static void digKeywordFile(String key, String src, String target) {
        String[] keywords = TaskDigger.readKeywordList(key);
        TransLog.getLogger().info("Digging keywords " + Arrays.toString(keywords));
        TaskDigger.digKeyword(keywords, src, target);
    }

    public static int digKeyword(String[] keywords, String src, String target) {
        int n = new TaskArranger().readFromFile(src).applyFilter(keywords).appendToFile(target);
        TransLog.getLogger().info("" + n + " entries digged from " + src + " are saved to " + target);
        return n;
    }

    public static void pickoutKeyword(String[] keys, String src, String target) {
        TransLog.getLogger().info("Digging keyword...");
        int n = new TaskArranger().readFromFile(src).applyFilterReversely(keys).appendToFile(target);
        TransLog.getLogger().info("Totally " + n + " entries extracted.");
        TransLog.getLogger().info("Done.");
    }

    public static void digBigEntries(String src, String target) {
        TransLog.getLogger().info("Digging keywords " + Arrays.toString(KEYS_BIG_ENTRY));
        TaskDigger.digKeyword(KEYS_BIG_ENTRY, src, target);
    }

    public static void pickoutBigEntries(String src, String target) {
        TaskDigger.pickoutKeyword(KEYS_BIG_ENTRY, src, target);
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
        File path = new File(TransProp.get(TransConst.LOC_TASK));
        int total = 0;
        TransLog.getLogger().info("Digging keywords " + Arrays.toString(keys));
        for (File file : path.listFiles(new FilenameFilter() {
            public boolean accept(File file, String name) {
                return name.matches("fresh_.*.txt");
            }
        })) {
            total += TaskDigger.digKeyword(keys, file.getAbsolutePath(), target);
            //TransLog.getLogger().info(file.getAbsolutePath());
        }
        TransLog.getLogger().info("Total: " + total);
        TransLog.getLogger().info("Time : " + (System.currentTimeMillis() - t0) + "ms");
    }
}