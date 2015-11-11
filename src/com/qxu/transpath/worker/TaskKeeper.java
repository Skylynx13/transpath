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
        int n = new Arranger().readFromFile("resource/raw.txt").sort().merge().writeToFile("resource/fresh.txt");
        FileUtils.copyFileBytes("resource/fresh.txt", "resource/fresh_" + archDate + ".txt");
        System.out.println("Totally " + n + " fresh entries processed.");
    }
    
    public void keepTask() {
        FileUtils.copyFileBytes("resource/task.txt", "resource/task_" + archDate + "_old.txt");
        int n = new Arranger().readFromFile("resource/task.txt").merge(new Arranger().readFromFile("resource/fresh.txt")).writeToFile("resource/task.txt");
        FileUtils.copyFileBytes("resource/task.txt", "resource/task_" + archDate + ".txt");
        System.out.println("Totally " + n + " task entries processed.");
    }
    
    public void keepReadyIndex() {
        int n = new Arranger().readFromFile("resource/ready.txt").writeIndexToFile("resource/iready.txt");
        System.out.println("Totally " + n + " task index processed.");
    }
    
    public void checkFresh() {
        Arranger arrFresh = new Arranger().readFromFile("resource/fresh.txt");
        String result = "";
        result += "==================================================================";
        result += "Fresh vs Store: \n";
        result += arrFresh.compare(new Arranger().readFromFile("resource/istore.txt"));
        result += "==================================================================";
        result += "Fresh vs Ready: \n";
        result += arrFresh.compare(new Arranger().readFromFile("resource/iready.txt"));
        System.out.println(result);
        try {
            PrintWriter out = new PrintWriter("resource/compare.txt");
            out.println(result);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
    }
    
    public void keepRaw() {
        FileUtils.copyFileBytes("resource/dump.txt", "resource/dump_" + archDate + ".txt");
        int n = new Arranger().trimFile("resource/dump.txt", "resource/raw.txt");
        FileUtils.copyFileBytes("resource/raw.txt", "resource/raw_" + archDate + ".txt");
        System.out.println("Raw Filtered to "+ n + " lines.");        
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
                System.out.println("Keeping fresh...");
                keeper.keepRaw();
                keeper.keepFresh();
                keeper.keepReadyIndex();
                System.out.println("Done.");
            } else if (arg.equals("check")) {
                System.out.println("Checking fresh...");
                keeper.checkFresh();
                System.out.println("Done.");
            } else if (arg.equals("task")) {
                System.out.println("Keeping task...");
                keeper.keepTask();
                System.out.println("Done.");
            }
        }
        System.out.println("Keeper job done");
        //countMatchString();
    }
}
