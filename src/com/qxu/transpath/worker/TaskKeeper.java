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
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;

import com.qxu.transpath.utils.DateUtils;
import com.qxu.transpath.utils.FileUtils;
import com.qxu.transpath.utils.TranspathConstants;

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
        archDate = DateUtils.dateStringToday();
    }
    
    public TaskKeeper(String dateString) {
        archDate = dateString;
    }

    public void countMatchString() {
        int n = FileUtils.countMatch("resource/rawDump.txt", 
                "^\\s*Last edited by.*$");
        System.out.println("Matched Lines: "+ n);        
    }

    public void keepFresh() {
        System.out.println("Keeping fresh...");
        int n = new Arranger().readFromFile(TranspathConstants.TP_HOME + "raw.txt").sort().merge().writeToFile(TranspathConstants.TP_HOME + "fresh.txt");
        FileUtils.copyFileBytes(TranspathConstants.TP_HOME + "fresh.txt", TranspathConstants.TP_HOME + "fresh_" + archDate + ".txt");
        System.out.println("Totally " + n + " fresh entries processed.");
        System.out.println("Done.");
    }
    
    public void keepTask() {
        System.out.println("Keeping task...");
        FileUtils.copyFileBytes(TranspathConstants.TP_HOME + "task.txt", TranspathConstants.TP_HOME + "task_" + archDate + "_old.txt");
        int n = new Arranger().readFromFile(TranspathConstants.TP_HOME + "task.txt").merge(new Arranger().readFromFile(TranspathConstants.TP_HOME + "fresh.txt")).writeToFile(TranspathConstants.TP_HOME + "task.txt");
        FileUtils.copyFileBytes(TranspathConstants.TP_HOME + "task.txt", TranspathConstants.TP_HOME + "task_" + archDate + ".txt");
        System.out.println("Totally " + n + " task entries processed.");
        System.out.println("Done.");
    }
    
    public void keepReadyIndex() {
        System.out.println("Keeping iReady...");
        int n = new Arranger().readFromFile(TranspathConstants.TP_HOME + "ready.txt").writeIndexToFile(TranspathConstants.TP_HOME + "iready.txt");
        System.out.println("Totally " + n + " ready index processed.");
        System.out.println("Done.");
    }
    
    public void checkFresh() {
        System.out.println("Checking fresh...");
        Arranger arrFresh = new Arranger().readFromFile(TranspathConstants.TP_HOME + "fresh.txt");
        String result = "";
        result += "==================================================================";
        result += "Fresh vs Store: \n";
        result += arrFresh.compare(new Arranger().readFromFile(TranspathConstants.TP_HOME + "istore.txt"));
        result += "==================================================================";
        result += "Fresh vs Ready: \n";
        result += arrFresh.compare(new Arranger().readFromFile(TranspathConstants.TP_HOME + "iready.txt"));
        System.out.println(result);
        try {
            PrintWriter out = new PrintWriter(TranspathConstants.TP_HOME + "compare.txt");
            out.println(result);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Done.");
    }
    
    public void checkTask() {
        System.out.println("Checking task...");
        Arranger arrTask = new Arranger().readFromFile(TranspathConstants.TP_HOME + "task.txt");
        String result = "";
        result += "==================================================================\n";
        result += "Task vs Store: \n";
        result += arrTask.compare(new Arranger().readFromFile(TranspathConstants.TP_HOME + "istore.txt"));
        result += "==================================================================\n";
        result += "Task vs Ready: \n";
        result += arrTask.compare(new Arranger().readFromFile(TranspathConstants.TP_HOME + "iready.txt"));
        System.out.println(result);
        try {
            PrintWriter out = new PrintWriter(TranspathConstants.TP_HOME + "compare.txt");
            out.println(result);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Done.");
    }
    
    public void keepRaw() {
        System.out.println("Keeping raw...");
        FileUtils.copyFileBytes(TranspathConstants.TP_HOME + "dump.txt", TranspathConstants.TP_HOME + "dump_" + archDate + ".txt");
        int n = new Arranger().trimFile(TranspathConstants.TP_HOME + "dump.txt", TranspathConstants.TP_HOME + "raw.txt");
        FileUtils.copyFileBytes(TranspathConstants.TP_HOME + "raw.txt", TranspathConstants.TP_HOME + "raw_" + archDate + ".txt");
        System.out.println("Raw Filtered to "+ n + " lines.");        
        System.out.println("Done.");
    }
    
    /**
     * main:<br/>
     * bin>java com.qxu.transpath.worker.TaskKeeper fresh check<br/>
     * bin>java com.qxu.transpath.worker.TaskKeeper task<br/>
     * 
     * @param args
     */
    public static void main (String[] args) {
        TaskKeeper keeper = new TaskKeeper();
        System.out.println("TaskKeeper on job...");
        for (String arg : args){
            if (arg.equals("fresh")) {
                keeper.keepRaw();
                keeper.keepFresh();
            } else if (arg.equals("task")) {
                keeper.keepRaw();
                keeper.keepFresh();
                keeper.keepTask();
            } else if (arg.equals("iready")) {
                keeper.keepReadyIndex();
            } else if (arg.equals("checkfresh")) {
                keeper.checkFresh();
            } else if (arg.equals("checktask")) {
                keeper.checkTask();
            }
        }
        System.out.println("Keeper job done");
        //countMatchString();
    }
}
