package com.skylynx13.transpath.store;

import com.skylynx13.transpath.error.StoreListException;
import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.ui.TranspathFrame;
import com.skylynx13.transpath.utils.FileUtils;
import com.skylynx13.transpath.utils.ProgressData;
import com.skylynx13.transpath.utils.TransConst;
import com.skylynx13.transpath.utils.TransProp;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StoreCombiner extends SwingWorker<StringBuilder, ProgressData> {
    private boolean updateList;
    private final static String REGEX_PATH_FULL = "^(A\\d{4})/B(\\d{4})(-(\\d{4}))?(,((A\\d{4})/)?B(\\d{4})(-(\\d{4}))?)*?$";
    private final static String REGEX_PATH_UNIT = ",?(A(\\d{4})/)?B(\\d{4})(-(\\d{4}))?";
    private final static Pattern PATTERN_PATH_UNIT = Pattern.compile(REGEX_PATH_UNIT);
    private final static int GROUP_A = 2;
    private final static int GROUP_B = 3;
    private final static int GROUP_S = 5;

    private long processedSize;
    private long processedCount;
    private ProgressData progressData = new ProgressData();
    private long totalSize;
    private long totalCount;

    public StoreCombiner(boolean updateList) {
        this.updateList = updateList;
    }

    @Override
    protected StringBuilder doInBackground() {
        try {
            TransLog.getLogger().info("Store combiner started...");
            StoreList oldStoreList = buildOldStoreList();
            StoreList newStoreList = buildNewStoreList();
            StoreList combinedList = combine(oldStoreList, newStoreList);
            if (updateList) {
                oldStoreList.backup();
                combinedList.keepFile();
            }
            return new StringBuilder("Store combiner done.")
                    .append(" Old:").append(oldStoreList.size())
                    .append(" New:").append(newStoreList.size())
                    .append(" Combined:").append(combinedList.getStoreSize());
        } catch (StoreListException e) {
            return new StringBuilder(e.getMessage());
        }
    }

    private StoreList buildOldStoreList() {
        TransLog.getLogger().info("Building old store list...");
        long t0 = System.currentTimeMillis();
        StoreList oldList = new StoreList().loadCurrent();
        TransLog.getLogger().info("Old store list built in " + (System.currentTimeMillis() - t0) + "ms.");
        return oldList;
    }

    private StoreList buildNewStoreList() throws StoreListException {
        TransLog.getLogger().info("Building new store list...");
        publishProgressNewList(0, 0);
        long t0 = System.currentTimeMillis();
        List<String> storePathList = buildStorePathList();
        totalSize = calcStoreFileSize(storePathList);
        totalCount = calcStoreFileCount(storePathList);

        StoreList newList = new StoreList();

        for (String storePathName : storePathList) {
            File storePath = new File(storePathName);
            if (!storePath.isDirectory() || storePath.listFiles() == null) {
                TransLog.getLogger().warn("Store path ignored: " + storePathName);
                continue;
            }

            processedSize = 0;
            processedCount = 0;
            newList.attachList(buildStoreListByPath(storePath));
        }
        TransLog.getLogger().info("New store list built.");
        logTimeElapsed(t0);
        return newList;
    }

    private void publishProgressNewList(long processedSize, long processedCount) {
        progressData.setProgress((int)(100 * processedSize / totalSize));
        progressData.setLine("Building new list: " + processedCount + " of " + totalCount + "files processed.");
        publish(progressData);
    }

    private StoreList buildStoreListByPath(File storePath) {
        final String STORE_ROOT = TransProp.get(TransConst.LOC_STORE);
        StoreList storeList = new StoreList();
        for (File aPath : Objects.requireNonNull(storePath.listFiles())) {
            if (aPath.isFile()) {
                StoreNode aNode = new StoreNode(STORE_ROOT, aPath);
                storeList.addNode(aNode);
                TransLog.getLogger().info(aNode.keepNode());
                processedSize += aNode.getLength();
                processedCount++;
                publishProgressNewList(processedSize, processedCount);
            }
            if (aPath.isDirectory() && aPath.listFiles() != null) {
                storeList.attachList(buildStoreListByPath(aPath));
            }
        }
        return storeList;
    }

    private long calcStoreFileSize(List<String> storePathList) {
        long totalSize = 0;
        for (String storePathName : storePathList) {
            totalSize += FileUtils.getFileSize(storePathName);
        }
        return totalSize;
    }

    private long calcStoreFileCount(List<String> storePathList) {
        long totalCount = 0;
        for (String storePathName : storePathList) {
            totalCount += FileUtils.getFileCount(storePathName);
        }
        return totalCount;
    }

    private List<String> buildStorePathList() throws StoreListException {
        if (updateList) {
            return parseStorePathList(TransProp.get(TransConst.PATH_BRANCH));
        } else {
            return parseStorePathList(TransProp.get(TransConst.PATH_DRILL));
        }
    }

    List<String> parseStorePathList(String branchPath) throws StoreListException {
        if (!branchPath.matches(REGEX_PATH_FULL)) {
            TransLog.getLogger().error("Error branch path: " + branchPath);
            throw new StoreListException("Error branch path.");
        }

        Matcher branchMatcher = PATTERN_PATH_UNIT.matcher(branchPath);
        int numberA = 0;
        List<String> storePathList = new ArrayList<>();
        while (branchMatcher.find()) {
            TransLog.getLogger()
                    .info("count: " + branchMatcher.groupCount() + ", group: " + branchMatcher.group());

            if (branchMatcher.group(GROUP_A) != null) {
                numberA = Integer.parseInt(branchMatcher.group(GROUP_A));
            }

            int numberB = Integer.parseInt(branchMatcher.group(GROUP_B));

            int numberS = numberB;
            if (branchMatcher.group(GROUP_S) != null) {
                numberS = Integer.parseInt(branchMatcher.group(GROUP_S));
            }

            if (numberB > numberS) {
                TransLog.getLogger().error("Error branch group: " + branchMatcher.group());
                throw new StoreListException("Error branch group.");
            }

            for (int numberI = numberB; numberI <= numberS; numberI++) {
                storePathList.add(String.format("A%04d/B%04d", numberA, numberI));
            }
        }
        TransLog.getLogger().info(storePathList.toString());
        return storePathList;
    }

    private long logTimeElapsed(long t0) {
        long t1 = System.currentTimeMillis();
        TransLog.getLogger().info("Time elapsed " + (t1-t0) + "ms.");
        return t1;
    }

    private StoreList combine(StoreList oldStoreList, StoreList newStoreList) {
        TransLog.getLogger().info("Combining...");
        publishCombinedList(0, 0);
        long t0 = System.currentTimeMillis();

        StoreList overallList = new StoreList();
        overallList.attachList(oldStoreList);
        overallList.attachList(newStoreList);

        TransLog.getLogger().info("Attach done.");
        t0 = logTimeElapsed(t0);

        overallList.orderByMd5();

        TransLog.getLogger().info("List ordered by MD5.");
        t0 = logTimeElapsed(t0);

        StoreNode dNode = null;
        StoreList reservedList = new StoreList();
        StoreList duplicatedList = new StoreList();
        StoreList removedList = new StoreList();

        totalSize = overallList.getStoreSize();
        totalCount = overallList.size();
        processedSize = 0;
        processedCount = 0;
        for (StoreNode aNode : overallList.storeList) {
            if (aNode == null) {
                continue;
            }
            if (aNode.checkDupStoreNode(dNode)) {
                if (!duplicatedList.hasNode(dNode)) {
                    duplicatedList.storeList.add(dNode);
                }
                duplicatedList.storeList.add(aNode);
                removedList.storeList.add(aNode);
            } else {
                dNode = aNode;
                reservedList.storeList.add(aNode);
            }
            processedSize += aNode.getLength();
            processedCount++;
            publishCombinedList(processedSize, processedCount);
        }
        TransLog.getLogger().info("Duplicated list.");
        TransLog.getLogger().info(duplicatedList.toString());
        TransLog.getLogger().info("Duplicated count: " + (duplicatedList.size() - removedList.size()));
        TransLog.getLogger().info("Removed count: " + removedList.size());
        TransLog.getLogger().info("Duplication checked.");
        t0 = logTimeElapsed(t0);

        removedList.orderByPathAndName();
        removedList.recap();
        keepDelList(removedList);
        TransLog.getLogger().info("Combined count: " + overallList.size());
        TransLog.getLogger().info("Reserved count: " + reservedList.size());
        TransLog.getLogger().info("Removed count: " + removedList.size());
        TransLog.getLogger().info("Removed size: " + removedList.getStoreSize());
        TransLog.getLogger().info("Removal scripted.");
        t0 = logTimeElapsed(t0);

        reservedList.orderByPathAndName();
        reservedList.recap();
        reservedList.reorgId();
        TransLog.getLogger().info("List ordered by name.");
        logTimeElapsed(t0);

        TransLog.getLogger().info("Store combined.");
        return reservedList;
    }

    private static void keepDelList(StoreList delList) {
        if (FileUtils.isWindows()) {
            keepDelBatch(delList, new File(TransProp.get(TransConst.LOC_LIST) + "ToDel.bat"));
        } else {
            keepDelShell(delList, new File(TransProp.get(TransConst.LOC_LIST) + "todel.sh"));
        }
    }

    private static void keepDelShell(StoreList delList, File delFile) {
        if (0 == delList.size()) {
            return;
        }
        try {
            PrintWriter out = new PrintWriter(delFile);
            for (StoreNode aNode : delList.storeList) {
                out.println("rm \"" + TransProp.get(TransConst.LOC_STORE)
                        + aNode.getPath().substring(1)
                        + aNode.getName() + "\"");
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void keepDelBatch(StoreList delList, File delFile) {
        if (0 == delList.size()) {
            return;
        }
        try {
            PrintWriter out = new PrintWriter(delFile);
            for (StoreNode aNode : delList.storeList) {
                out.println("del \"" + TransProp.get(TransConst.LOC_STORE)
                        + FileUtils.regulatePath(aNode.getPath().substring(1))
                        + aNode.getName() + "\"");
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void publishCombinedList(long processedSize, long processedCount) {
        progressData.setProgress((int)(100 * processedSize / totalSize));
        progressData.setLine("Combining list: " + processedCount + " of " + totalCount + "files processed.");
        publish(progressData);
    }

    @Override
    protected void process(List<ProgressData> progressData) {
        ProgressData lastProgressData = progressData.get(progressData.size()-1);
        TranspathFrame.getProgressBar().setValue(lastProgressData.getProgress());
        TranspathFrame.getStatusLabel().setText(lastProgressData.getLine());
    }

    @Override
    protected void done() {
        try {
            StringBuilder result = get();
            TransLog.getLogger().info(result.toString());
            TranspathFrame.getStatusLabel().setText(result.toString());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public boolean isUpdateList() {
        return updateList;
    }

    public void setUpdateList(boolean updateList) {
        this.updateList = updateList;
    }
}
