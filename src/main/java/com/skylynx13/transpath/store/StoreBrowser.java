package com.skylynx13.transpath.store;

import com.skylynx13.transpath.Transpath;
import com.skylynx13.transpath.error.StoreListException;
import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.utils.*;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * @author skylynx
 */
public class StoreBrowser extends SwingWorker<StringBuilder, ProgressReport> {
    private final ProgressTracer progressTracer = new ProgressTracer();

    @Override
    protected StringBuilder doInBackground() {
        try {
            TransLog.getLogger().info("Store browser started...");
            StoreList storeList = browseStoreList();
            storeList.keepFile(new File(TransProp.get(TransConst.LOC_LIST) + "StoreList_Browse.txt"));
            return new StringBuilder("Store browser done.");
        } catch (StoreListException e) {
            return new StringBuilder(e.getMessage());
        }
    }

    private StoreList browseStoreList() throws StoreListException {
        TransLog.getLogger().info("Browse store list...");
        long t0 = System.currentTimeMillis();
        String browsePath = TransProp.get(TransConst.LOC_SOURCE);
        resetProgress(calcStoreFileSize(browsePath), calcStoreFileCount(browsePath)
        );

        StoreList storeList = new StoreList();
        File storePath = new File(browsePath);
        if (!storePath.isDirectory() || storePath.listFiles() == null) {
            TransLog.getLogger().warn("Store path ignored: " + browsePath);
            return storeList;
        }
        storeList.attachList(browseStoreListByPath(storePath));
        storeList.orderByPathAndName();
        storeList.recap();
        storeList.reorgId();

        TransLog.getLogger().info("Browse store list done.");
        logTimeElapsed(t0);
        return storeList;
    }

    private StoreList browseStoreListByPath(File storePath) {
        StoreList storeList = new StoreList();
        for (File aPath : Objects.requireNonNull(storePath.listFiles())) {
            if (aPath.isFile()) {
                StoreNode aNode = new StoreNode(TransProp.get(TransConst.LOC_SOURCE), aPath, true);
                storeList.addNodeWithId(aNode);
                TransLog.getLogger().info(aNode.toNodeString());
                updateProgress(aNode.getLength());
            }
            if (aPath.isDirectory() && aPath.listFiles() != null) {
                storeList.attachList(browseStoreListByPath(aPath));
            }
        }
        return storeList;
    }

    private void resetProgress(long totalSize, long totalCount) {
        progressTracer.reset(totalSize, totalCount, "Browsing store list");
        publish(progressTracer.report());
    }

    private void updateProgress(long nSize) {
        progressTracer.update(nSize);
        publish(progressTracer.report());
    }

    private long calcStoreFileSize(String browsePath) throws StoreListException {
        long tSize = FileUtils.getFileSize(browsePath);
        if (tSize == 0) {
            throw new StoreListException("Total size is 0");
        }
        return tSize;
    }

    private long calcStoreFileCount(String browsePath) throws StoreListException {
        long tCount = FileUtils.getFileCount(browsePath);
        if (tCount == 0) {
            throw new StoreListException("Total count is 0");
        }
        return tCount;
    }

    private void logTimeElapsed(long t0) {
        long t1 = System.currentTimeMillis();
        TransLog.getLogger().info("Time elapsed " + (t1-t0) + "ms.");
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
            e.printStackTrace();
        }
    }
}
