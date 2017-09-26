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

import com.qxu.transpath.log.TransLog;
import com.qxu.transpath.utils.DateUtils;
import com.qxu.transpath.utils.FileUtils;
import com.qxu.transpath.utils.TransConst;
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
    
    public void keepRaw() {
        TransLog.getLogger().info("Keeping raw...");
        FileUtils.copyFileBytes(TransProp.get(TransConst.LOC_TASK) + "dump.txt", TransProp.get(TransConst.LOC_TASK) + "dump_" + archDate + ".txt");
        int n = new Arranger().trimFile(TransProp.get(TransConst.LOC_TASK) + "dump.txt", TransProp.get(TransConst.LOC_TASK) + "raw.txt");
        FileUtils.copyFileBytes(TransProp.get(TransConst.LOC_TASK) + "raw.txt", TransProp.get(TransConst.LOC_TASK) + "raw_" + archDate + ".txt");
        TransLog.getLogger().info("Raw Filtered to "+ n + " lines.");        
        TransLog.getLogger().info("Done.");
    }
    
    public void keepFresh() {
        TransLog.getLogger().info("Keeping fresh...");
        int n = new Arranger().readFromFile(TransProp.get(TransConst.LOC_TASK) + "raw.txt").sort().merge().writeToFile(TransProp.get(TransConst.LOC_TASK) + "fresh.txt");
        FileUtils.copyFileBytes(TransProp.get(TransConst.LOC_TASK) + "fresh.txt", TransProp.get(TransConst.LOC_TASK) + "fresh_" + archDate + ".txt");
        TransLog.getLogger().info("Totally " + n + " fresh entries processed.");
        TransLog.getLogger().info("Done.");
    }
    
    public static void weekFresh() {
        TaskKeeper keeper = new TaskKeeper();
        keeper.keepRaw();
        keeper.keepFresh();
    }
    
    public static void digNewFresh() {
        CodeDigger.digKeywordFileDefault(TransProp.get(TransConst.LOC_TASK) + "fresh.txt", 
                TransProp.get(TransConst.LOC_TASK) + "task_week.txt");
        TransLog.getLogger().info("Done.");
    }
    
    public static void digSpecFresh() {
        String[] keys = TransProp.get("DIG_SPEC").split("/");
        CodeDigger.digAllFreshSpecific(keys, 
                TransProp.get(TransConst.LOC_TASK) + "task_spec.txt");    
    }
}
