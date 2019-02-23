package com.skylynx13.transpath.task;

import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.utils.DateUtils;
import com.skylynx13.transpath.utils.FileUtils;
import com.skylynx13.transpath.utils.TransConst;
import com.skylynx13.transpath.utils.TransProp;

 /**
 * ClassName: TaskKeeper
 * Description: Task keeper
 * Date: 2015-11-04 23:23:27
 */
public class TaskKeeper {
    private String archDate;
    
    private TaskKeeper() {
        archDate = DateUtils.formatDateToday();
    }
    
    private void keepRaw() {
        TransLog.getLogger().info("Keeping raw...");
        FileUtils.copyFileBytes(TransProp.get(TransConst.LOC_TASK) + "dump.txt", TransProp.get(TransConst.LOC_TASK) + "dump_" + archDate + ".txt");
        int n = new TaskArranger().trimFile(TransProp.get(TransConst.LOC_TASK) + "dump.txt", TransProp.get(TransConst.LOC_TASK) + "raw.txt");
        FileUtils.copyFileBytes(TransProp.get(TransConst.LOC_TASK) + "raw.txt", TransProp.get(TransConst.LOC_TASK) + "raw_" + archDate + ".txt");
        TransLog.getLogger().info("Raw Filtered to "+ n + " lines.");        
        TransLog.getLogger().info("Done.");
    }
    
    private void keepFresh() {
        TransLog.getLogger().info("Keeping fresh...");
        int n = new TaskArranger().readFromFile(TransProp.get(TransConst.LOC_TASK) + "raw.txt").sort().merge().writeToFile(TransProp.get(TransConst.LOC_TASK) + "fresh.txt");
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
        TaskDigger.digKeywordFileDefault(TransProp.get(TransConst.LOC_TASK) + "fresh.txt", 
                TransProp.get(TransConst.LOC_TASK) + "task_week.txt");
        TransLog.getLogger().info("Done.");
    }
    
    public static void digSpecFresh() {
        String[] keys = TransProp.get("DIG_SPEC").split("/");
        TaskDigger.digAllFreshSpecific(keys, 
                TransProp.get(TransConst.LOC_TASK) + "task_spec.txt");    
    }
}
