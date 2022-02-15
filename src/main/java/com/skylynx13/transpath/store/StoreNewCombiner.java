package com.skylynx13.transpath.store;

import com.skylynx13.transpath.Transpath;
import com.skylynx13.transpath.error.StoreListException;
import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.utils.*;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author skylynx
 */
public class StoreNewCombiner extends SwingWorker<StringBuilder, ProgressReport> {
    private final boolean updateList;
    private final static String REGEX_PATH_FULL =
            "^(A\\d{4})/B(\\d{4})(-(\\d{4}))?(,((A\\d{4})/)?B(\\d{4})(-(\\d{4}))?)*?$";
    private final static String REGEX_PATH_UNIT = ",?(A(\\d{4})/)?B(\\d{4})(-(\\d{4}))?";
    private final static Pattern PATTERN_PATH_UNIT = Pattern.compile(REGEX_PATH_UNIT);
    private final static int GROUP_A = 2;
    private final static int GROUP_B = 3;
    private final static int GROUP_S = 5;
    private final ProgressTracer progressTracer = new ProgressTracer();

    public StoreNewCombiner(boolean updateList) {
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
                Transpath.getTranspathFrame().reloadStore();
            }

            return new StringBuilder("Store combiner done.")
                    .append(" Old: ").append(oldStoreList.size())
                    .append(" New: ").append(newStoreList.size())
                    .append(" Combined: ").append(combinedList.size())
                    .append(" Combined file size: ").append(combinedList.getStoreSize());
        } catch (StoreListException e) {
            return new StringBuilder(e.getMessage());
        }
    }

    private String buildRootPath() {
        return TransProp.get(TransConst.LOC_STORE);
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
        long t0 = System.currentTimeMillis();
        List<String> storePathList = buildStorePathList();
        resetProgress(calcStoreFileSize(storePathList), calcStoreFileCount(storePathList),
                "Building new list");
        StoreList newList = new StoreList();

        for (String storePathName : storePathList) {
            File storePath = new File(storePathName);
            newList.attachList(buildStoreListByPath(storePath));
        }
        TransLog.getLogger().info("New store list built.");
        logTimeElapsed(t0);
        return newList;
    }

    private StoreList buildStoreListByPath(File storePath) {
        StoreList storeList = new StoreList();

        File[] storeFiles = storePath.listFiles();

        if (!storePath.isDirectory() || storeFiles == null) {
            TransLog.getLogger().warn("Store path ignored: " + storePath.getPath());
            return storeList;
        }

        for (File aPath : storeFiles) {
            if (aPath.isFile()) {
                StoreNode aNode = new StoreNode(buildRootPath(), aPath);
                storeList.addNode(aNode);
                TransLog.getLogger().info(aNode.toNodeString());
                updateProgress(aNode.getLength());
            }
            if (aPath.isDirectory() && aPath.listFiles() != null) {
                storeList.attachList(buildStoreListByPath(aPath));
            }
        }
        return storeList;
    }

    private void resetProgress(long totalSize, long totalCount, String header) {
        progressTracer.reset(totalSize, totalCount, header);
        publish(progressTracer.report());
    }

    private void updateProgress(long nSize) {
        progressTracer.update(nSize);
        publish(progressTracer.report());
    }

    private long calcStoreFileSize(List<String> storePathList) throws StoreListException {
        long tSize = 0;
        for (String storePathName : storePathList) {
            tSize += FileUtils.getFileSize(storePathName);
        }
        if (tSize == 0) {
            throw new StoreListException("Total size is 0");
        }
        return tSize;
    }

    private long calcStoreFileCount(List<String> storePathList) throws StoreListException {
        long tCount = 0;
        for (String storePathName : storePathList) {
            tCount += FileUtils.getFileCount(storePathName);
        }
        if (tCount == 0) {
            throw new StoreListException("Total count is 0");
        }
        return tCount;
    }

    private List<String> buildStorePathList() throws StoreListException {
        if (updateList) {
            return buildList(TransProp.get(TransConst.PATH_BRANCH));
        } else {
            return buildList(TransProp.get(TransConst.PATH_DRILL));
        }
    }

    List<String> buildList(String branchPath) throws StoreListException {
        if (!branchPath.matches(REGEX_PATH_FULL)) {
            if (new File(branchPath).exists()) {
                List<String> singlePathList = new ArrayList<>();
                singlePathList.add(branchPath);
                TransLog.getLogger().info(singlePathList.toString());
                return singlePathList;
            }
            TransLog.getLogger().error("Error branch path: " + branchPath);
            throw new StoreListException("Error branch path.");
        }
        return checkExistList(parseList(branchPath));
    }

    List<String> parseList(String branchPath) throws StoreListException {
        List<String> parsedList = new ArrayList<>();
        Matcher branchMatcher = PATTERN_PATH_UNIT.matcher(branchPath);
        int numberA = 0;
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
                parsedList.add(String.format("A%04d/B%04d", numberA, numberI));
            }
        }
        TransLog.getLogger().info("Request list:" + parsedList);
        return parsedList;
    }

    private List<String> checkExistList(List<String> parsedList) throws StoreListException {
        ArrayList<String> existList = new ArrayList<>();
        for (String relativePath : parsedList) {
            String fullPath = FileUtils.regulateSysPath((buildRootPath() + relativePath));
            if (new File(fullPath).exists()) {
                existList.add(fullPath);
            }
        }
        if (existList.size() == 0) {
            throw new StoreListException("Respond list is empty.");
        }
        TransLog.getLogger().info("Respond list: " + existList);
        return existList;
    }

    private long logTimeElapsed(long t0) {
        long t1 = System.currentTimeMillis();
        TransLog.getLogger().info("Time elapsed " + (t1-t0) + "ms.");
        return t1;
    }

    private StoreList combine(StoreList oldStoreList, StoreList newStoreList) {
        TransLog.getLogger().info("Combining...");
        long t0 = System.currentTimeMillis();

        StoreList duplicateList = new StoreList();
        StoreList removeList = new StoreList();
        StoreList reservedNewList = getReservedNewList(newStoreList, duplicateList, removeList);
        TransLog.getLogger().info("Duplication in new list checked.");
        t0 = logTimeElapsed(t0);

        StoreList finalNewList = getFinalNewList(oldStoreList, duplicateList, removeList, reservedNewList);
        TransLog.getLogger().info("Duplication in old list checked.");
        t0 = logTimeElapsed(t0);

        duplicateList.orderByMd5();
        TransLog.getLogger().info("Duplicated list.");
        TransLog.getLogger().info(duplicateList.toString());
        TransLog.getLogger().info("=== Duplicated count: " + (duplicateList.size() - removeList.size()) + " ===");
        TransLog.getLogger().info("=== Removed count: " + removeList.size() + " ===");
        TransLog.getLogger().info("Duplication checked.");
        t0 = logTimeElapsed(t0);

        removeList.orderByPathAndName();
        removeList.recap();
        keepDelList(removeList);
        TransLog.getLogger().info("Removed count: " + removeList.size());
        TransLog.getLogger().info("Removed size: " + removeList.getStoreSize());
        TransLog.getLogger().info("Removal scripted.");
        t0 = logTimeElapsed(t0);

        // Keep old IDs
        StoreList combinedList = new StoreList(oldStoreList);
        // Assign new IDs
        combinedList.attachListWithId(finalNewList);
        TransLog.getLogger().info("Combined list attached.");
        logTimeElapsed(t0);

        return combinedList;
    }

    private StoreList getReservedNewList(StoreList newStoreList, StoreList duplicateList, StoreList removeList) {
        StoreList reservedNewList = new StoreList();
        HashMap<String, Integer> md5Map = new HashMap<>();
        for (StoreNode aNode : newStoreList.getStoreList()) {
            String md5 = aNode.getMd5();
            if (md5Map.get(md5) != null) {
                Integer counter = md5Map.get(md5);
                md5Map.put(md5, counter+1);
                removeList.addNode(aNode);
            } else {
                md5Map.put(md5, 1);
                reservedNewList.addNode(aNode);
            }
        }
        for (StoreNode aNode : newStoreList.getStoreList()) {
            String md5 = aNode.getMd5();
            if ((md5Map.get(md5) != null) && (md5Map.get(md5) > 1)) {
                duplicateList.addNode(aNode);
            }
        }
        reservedNewList.recap();
        return reservedNewList;
    }

    private StoreList getFinalNewList(StoreList reservedNewList, StoreList oldStoreList,
                                      StoreList duplicateList, StoreList removeList) {
        resetProgress(reservedNewList.getStoreSize(), reservedNewList.size(),
                "Checking duplication in old list");
        StoreList finalNewList = new StoreList();
        for (StoreNode newNode : reservedNewList.getStoreList()) {
            StoreNode oldNode = oldStoreList.queryNode(newNode);
            if (oldNode != null) {
                duplicateList.addNode(oldNode);
                duplicateList.addNode(newNode);
                removeList.addNode(newNode);
            } else {
                finalNewList.addNode(newNode);
            }
            updateProgress(newNode.getLength());
        }
        finalNewList.recap();
        return finalNewList;
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
                        + FileUtils.regulateSysPath(aNode.getPath().substring(1))
                        + aNode.getName() + "\"");
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
