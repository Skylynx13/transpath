package com.skylynx13.transpath.store;

import com.skylynx13.transpath.Transpath;
import com.skylynx13.transpath.error.StoreListException;
import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.utils.*;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author skylynx
 */
public class StoreNameCombiner extends SwingWorker<StringBuilder, ProgressReport> {
    private final ProgressTracer progressTracer = new ProgressTracer();

    @Override
    protected StringBuilder doInBackground() {
        try {
            TransLog.getLogger().info("Store name combiner started...");
            StoreList storeList = buildStoreList();
            StoreList diskStoreList = buildDiskStoreList();
            StoreList nameCombinedList = nameCombine(storeList, diskStoreList);
            nameCombinedList.keepFile(new File(TransProp.get(TransConst.LOC_LIST) + "StoreList_All.txt"));

            return new StringBuilder("Store name combiner done.")
                    .append(" Store: ").append(storeList.size())
                    .append(" Disk: ").append(diskStoreList.size())
                    .append(" Name Combined: ").append(nameCombinedList.size())
                    .append(" Name Combined file size: ").append(nameCombinedList.getStoreSize());
        } catch (StoreListException e) {
            return new StringBuilder(e.getMessage());
        }
    }

    private StoreList buildStoreList() {
        TransLog.getLogger().info("Building store list...");
        long t0 = System.currentTimeMillis();
        StoreList storeList = new StoreList(TransProp.get(TransConst.LOC_LIST) + "StoreList_Store.txt");
        TransLog.getLogger().info("Store list built in " + (System.currentTimeMillis() - t0) + "ms.");
        return storeList;
    }

    private StoreList buildDiskStoreList() throws StoreListException {
        TransLog.getLogger().info("Building disk store list...");
        long t0 = System.currentTimeMillis();
        StoreList diskList = new StoreList(TransProp.get(TransConst.LOC_LIST) + "StoreList_Browse.txt");
        TransLog.getLogger().info("Disk store list built in " + (System.currentTimeMillis() - t0) + "ms.");
        return diskList;
    }

    private void resetProgress(long totalSize, long totalCount) {
        progressTracer.reset(totalSize, totalCount, "Name combining list");
        publish(progressTracer.report());
    }

    private void updateProgress(long nSize) {
        progressTracer.update(nSize);
        publish(progressTracer.report());
    }

    private void logTimeElapsed(long t0) {
        long t1 = System.currentTimeMillis();
        TransLog.getLogger().info("Time elapsed " + (t1-t0) + "ms.");
    }

    private StoreList nameCombine(StoreList storeList, StoreList diskStoreList) {
        TransLog.getLogger().info("Combining...");
        long t0 = System.currentTimeMillis();

        resetProgress(storeList.getStoreSize(), storeList.size());
        for (StoreNode storeNode : storeList.storeList) {
            for (StoreNode diskNode : diskStoreList.storeList) {
                if ((storeNode.getPath().equals(diskNode.getPath())) &&
                        (storeNode.getLength() == diskNode.getLength())) {
                    storeNode.setRawName(diskNode.getName());
                    storeNode.setLastModified(diskNode.getLastModified());
                    diskNode.setRawName("MMM");
                    break;
                }
            }
            updateProgress(storeNode.getLength());
        }

        StoreList noDiskList = new StoreList();
        StoreList noStoreList = new StoreList();

        for (StoreNode storeNode : storeList.storeList) {
            if ((storeNode.getRawName() == null) || (storeNode.getRawName().isEmpty())) {
                noDiskList.storeList.add(storeNode);
            }
        }

        for (StoreNode diskNode : diskStoreList.storeList) {
            if ((diskNode.getRawName() == null) || (diskNode.getRawName().isEmpty())) {
                noStoreList.storeList.add(diskNode);
            }
        }

        TransLog.getLogger().info("No disk list.");
        TransLog.getLogger().info(noDiskList.toString());
        TransLog.getLogger().info("=== No disk count: " + noDiskList.size() + " ===");

        TransLog.getLogger().info("No store list.");
        TransLog.getLogger().info(noStoreList.toString());
        TransLog.getLogger().info("=== No store count: " + noStoreList.size() + " ===");

        TransLog.getLogger().info("Store list mismatch checked.");
        logTimeElapsed(t0);
        return storeList;
    }

    @Override
    protected void process(List<ProgressReport> progressReportList) {
        ProgressReport lastProgressReport = progressReportList.get(progressReportList.size()-1);
        Transpath.getProgressBar().setValue(lastProgressReport.getProgress());
        Transpath.getStatusLabel().setText(lastProgressReport.getReportLine());
    }

    @Override
    protected void done() {
        try {
            StringBuilder result = get();
            TransLog.getLogger().info(result.toString());
            Transpath.getStatusLabel().setText(result.toString());
        } catch (InterruptedException | ExecutionException e) {
            TransLog.getLogger().error("", e);
        }
    }
}
