/**
 * Copyright (c) 2015,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.worker
 * File Name:FreshKeeper.java
 * Date:2015-11-4 下午11:23:27
 * 
 */
package com.qxu.transpath.worker;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import com.qxu.transpath.utils.DateUtils;
import com.qxu.transpath.utils.FileUtils;
import com.qxu.transpath.utils.TransLog;
import com.qxu.transpath.utils.TransProp;

 /**
 * ClassName: FreshKeeper <br/>
 * Description: TODO <br/>
 * Date: 2015-11-4 下午11:23:27 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class TaskKeeper {
    private String archDate;
    
    public TaskKeeper() {
        archDate = DateUtils.formatDateToday();
    }
    
    @Deprecated
    public TaskKeeper(String dateString) {
        archDate = dateString;
    }

    @Deprecated
    public void countMatchString() {
        int n = FileUtils.countMatch("resource/rawDump.txt", 
                "^\\s*Last edited by.*$");
        TransLog.getLogger().info("Matched Lines: "+ n);        
    }

    @Deprecated
    public void keepTask() {
        TransLog.getLogger().info("Keeping task...");
        FileUtils.copyFileBytes(TransProp.get("TP_HOME") + "task.txt", TransProp.get("TP_HOME") + "task_" + archDate + "_old.txt");
        int n = new Arranger().readFromFile(TransProp.get("TP_HOME") + "task.txt").merge(new Arranger().readFromFile(TransProp.get("TP_HOME") + "fresh.txt")).writeToFile(TransProp.get("TP_HOME") + "task.txt");
        FileUtils.copyFileBytes(TransProp.get("TP_HOME") + "task.txt", TransProp.get("TP_HOME") + "task_" + archDate + ".txt");
        TransLog.getLogger().info("Totally " + n + " task entries processed.");
        TransLog.getLogger().info("Done.");
    }
    
    @Deprecated
    public void keepReadyIndex() {
        TransLog.getLogger().info("Keeping iReady...");
        int n = new Arranger().readFromFile(TransProp.get("TP_HOME") + "ready.txt").writeIndexToFile(TransProp.get("TP_HOME") + "iready.txt");
        TransLog.getLogger().info("Totally " + n + " ready index processed.");
        TransLog.getLogger().info("Done.");
    }
    
    @Deprecated
    public void checkFresh() {
        TransLog.getLogger().info("Checking fresh...");
        Arranger arrFresh = new Arranger().readFromFile(TransProp.get("TP_HOME") + "fresh.txt");
        String result = "";
        result += "==================================================================";
        result += "Fresh vs Store: \n";
        result += arrFresh.findSameEntries(new Arranger().readFromFile(TransProp.get("TP_HOME") + "istore.txt"));
        result += "==================================================================";
        result += "Fresh vs Ready: \n";
        result += arrFresh.findSameEntries(new Arranger().readFromFile(TransProp.get("TP_HOME") + "iready.txt"));
        TransLog.getLogger().info(result);
        try {
            PrintWriter out = new PrintWriter(TransProp.get("TP_HOME") + "compare.txt");
            out.println(result);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        TransLog.getLogger().info("Done.");
    }
    
    @Deprecated
    public void checkTask() {
        TransLog.getLogger().info("Checking task...");
        Arranger arrTask = new Arranger().readFromFile(TransProp.get("TP_HOME") + "task.txt");
        String result = "";
        result += "==================================================================\n";
        result += "Task vs Store: \n";
        result += arrTask.findSameEntries(new Arranger().readFromFile(TransProp.get("TP_HOME") + "istore.txt"));
        result += "==================================================================\n";
        result += "Task vs Ready: \n";
        result += arrTask.findSameEntries(new Arranger().readFromFile(TransProp.get("TP_HOME") + "iready.txt"));
        TransLog.getLogger().info(result);
        try {
            PrintWriter out = new PrintWriter(TransProp.get("TP_HOME") + "compare.txt");
            out.println(result);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        TransLog.getLogger().info("Done.");
    }
    
    public void keepRaw() {
        TransLog.getLogger().info("Keeping raw...");
        FileUtils.copyFileBytes(TransProp.get("TP_HOME") + "dump.txt", TransProp.get("TP_HOME") + "dump_" + archDate + ".txt");
        int n = new Arranger().trimFile(TransProp.get("TP_HOME") + "dump.txt", TransProp.get("TP_HOME") + "raw.txt");
        FileUtils.copyFileBytes(TransProp.get("TP_HOME") + "raw.txt", TransProp.get("TP_HOME") + "raw_" + archDate + ".txt");
        TransLog.getLogger().info("Raw Filtered to "+ n + " lines.");        
        TransLog.getLogger().info("Done.");
    }
    
    public void keepFresh() {
        TransLog.getLogger().info("Keeping fresh...");
        int n = new Arranger().readFromFile(TransProp.get("TP_HOME") + "raw.txt").sort().merge().writeToFile(TransProp.get("TP_HOME") + "fresh.txt");
        FileUtils.copyFileBytes(TransProp.get("TP_HOME") + "fresh.txt", TransProp.get("TP_HOME") + "fresh_" + archDate + ".txt");
        TransLog.getLogger().info("Totally " + n + " fresh entries processed.");
        TransLog.getLogger().info("Done.");
    }
    
    public static void weekFresh() {
        TaskKeeper keeper = new TaskKeeper();
        keeper.keepRaw();
        keeper.keepFresh();
    }
    
    public static void digNewFresh() {
        CodeDigger.digKeywordFileDefault(TransProp.get("TP_HOME") + "fresh.txt", 
                TransProp.get("TP_HOME") + "task_week.txt");
    }
    
    public static void digSpecFresh() {
        String[] keys = TransProp.get("DIG_SPEC").split("/");
        CodeDigger.digAllFreshSpecific(keys, 
                TransProp.get("TP_HOME") + "task_spec.txt");    
    }
    
    /**
     * main:<br/>
     * 
     * @param args
     */
    public static void main (String[] args) {
//        TransLog.getLogger().info("TaskKeeper on job...");
//        weekFresh();
//        digNewFresh();
//        //digSpecFresh();
//        TransLog.getLogger().info("Keeper job done");
    }
}
